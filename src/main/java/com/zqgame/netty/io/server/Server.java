package com.zqgame.netty.io.server;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.handle.*;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 基本的Server
 * peng.chen 2018/06/27 17:40:15
 */
public class Server {

    private static Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * 绑定的端口
     */
    private int port;

    /**
     * 服务端启动引导
     */
    private ServerBootstrap serverBootstrap;

    /**
     * 主线程组   用于接入访问请求
     */
    private EventLoopGroup bossGroup;

    /**
     * 从线程组 用于处理消息在Handler链中工作
     */
    private EventLoopGroup workerGroup;

    /**
     * 绑定后的Channel
     */
    private Channel channel;

    public Server(int port){
        this(port,(ChannelHandler[])null);
    }


    public Server(int port,ChannelHandler channelHandler){
        this(port,new ChannelHandler[]{channelHandler});
    }


    public Server(int port, ChannelHandler [] channelHandlers){

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException();
        }

        this.port = port;

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class).childHandler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());

                        ch.pipeline().addLast(new ProtobufEncoder());
                        ch.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

                        ch.pipeline().addLast(new BaseServerProtoMessageDecode());
                        ch.pipeline().addLast(new BaseServerProtoMessageEncode());

                        ch.pipeline().addLast(new BaseServerMap2ProtoEncode());
                        ch.pipeline().addLast(new BaseServerProto2MapDecode());

                        //九十秒没有收到消息设置为断线
                        ch.pipeline().addLast(new IdleStateHandler(SystemProperty.HEARTBEAT_TIME_OUT_TIME,0,0,TimeUnit.SECONDS));
                        ch.pipeline().addLast(new BaseServerHeartbeatHandle());

                        if (channelHandlers != null){
                            for(var item : channelHandlers){
                                ch.pipeline().addLast(item);
                            }
                        }

                    }
                }

        );

        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

    }

    /**
     * 绑定的方法
     */
    public void bind(){
        var channelFuture = serverBootstrap.bind(port);

        channel = channelFuture.channel();

        channelFuture.addListener(future -> {

            if (future.isSuccess()){
                logger.debug("端口监听成功 port:{}",port);
            }else{
                logger.debug("端口监听失败 port:{}",port);
            }
        });

    }

    public void close(){
        channel.close().addListener(
            future ->{
                if(future.isSuccess()){
                    logger.debug("channel:{} 已经关闭",channel);
                }else{
                    logger.debug("channel:{} 关闭失败",channel);
                }
            }
        );

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }


}
