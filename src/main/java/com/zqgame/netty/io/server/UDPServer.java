package com.zqgame.netty.io.server;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.handle.*;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.DatagramPacketEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UDPServer implements Server{

    private static Logger logger = LoggerFactory.getLogger(UDPServer.class);

    /**
     * 绑定的端口
     */
    private int port;

    /**
     * 服务端启动引导
     */
    private Bootstrap bootstrap;

    /**
     * 主线程组   用于接入访问请求
     */
    private EventLoopGroup bossGroup;

    /**
     * 从线程组 用于处理消息在Handler链中工作
     */
//    private EventLoopGroup workerGroup;

    /**
     * 绑定后的Channel
     */
    private Channel channel;

    public UDPServer(int port){
        this(port,(ChannelHandler[])null);
    }


    public UDPServer(int port, ChannelHandler channelHandler){
        this(port,new ChannelHandler[]{channelHandler});
    }


    public UDPServer(int port, ChannelHandler [] channelHandlers){

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException();
        }

        this.port = port;

        bossGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(bossGroup);
        bootstrap.channel(NioDatagramChannel.class);

        bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {

            @Override
            protected void initChannel(NioDatagramChannel ch) {


                ch.pipeline().addLast(new DatagramPacketEncoder(new ProtobufEncoder()));
                ch.pipeline().addLast(new DatagramPacketDecoder((new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance())) ));

                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

                ch.pipeline().addLast(new BaseServerProtoMessageDecode());
                ch.pipeline().addLast(new BaseServerProtoMessageEncode());

                ch.pipeline().addLast(new BaseServerMap2ProtoEncode());
                ch.pipeline().addLast(new BaseServerProto2MapDecode());

                //UDP不使用这个心跳包
                //九十秒没有收到消息设置为断线
//                ch.pipeline().addLast(new IdleStateHandler(SystemProperty.HEARTBEAT_TIME_OUT_TIME,0,0,TimeUnit.SECONDS));
//                ch.pipeline().addLast(new BaseServerHeartbeatHandle());

                if (channelHandlers != null){
                    for(var item : channelHandlers){
                        ch.pipeline().addLast(item);
                    }
                }
            }
        });
    }

    /**
     * 绑定的方法
     */
    @Override
    public void bind(){
        var channelFuture = bootstrap.bind(port);

        channel = channelFuture.channel();

        channelFuture.addListener(future -> {

            if (future.isSuccess()){
                logger.debug("端口监听成功 port:{}",port);
            }else{
                logger.debug("端口监听失败 port:{}",port);
            }
        });

    }

    @Override
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

//        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }

}
