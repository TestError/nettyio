package com.zqgame.netty.io.server;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.handle.*;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * websocket 服务端
 * peng.chen 2018/06/29 16:25:49
 */
public class WebSocketServer implements Server {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private Channel channel;

    /**
     * 路径
     */
    private String path;

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

    public WebSocketServer(int port,String path){
        this(port,path,(ChannelHandler[]) null);
    }

    public WebSocketServer(int port,String path,ChannelHandler channelHandler){
        this(port,path,new ChannelHandler[]{channelHandler});
    }



    public WebSocketServer(int port ,String path, ChannelHandler[] channelHandlers){

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException();
        }

        this.port = port;

        if(StringUtil.isNullOrEmpty(path)){
            throw new IllegalArgumentException();
        }
        this.path = path;

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel ch) {

                ch.pipeline().addLast(new HttpServerCodec());

                ch.pipeline().addLast(new HttpObjectAggregator(65536));
                ch.pipeline().addLast(new ChunkedWriteHandler());
                ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                ch.pipeline().addLast(new WebSocketServerProtocolHandler(path, null, true));
                ch.pipeline().addLast(new WebSocketFrameHandler());

                ch.pipeline().addLast(new WebSocketBinaryFrameEncode());

                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

                ch.pipeline().addLast(new BaseServerProtoMessageDecode());
                ch.pipeline().addLast(new BaseServerProtoMessageEncode());
//
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
        });

    }


    @Override
    public void bind() {

        ChannelFuture channelFuture = serverBootstrap.bind(port);

        channel = channelFuture.channel();

        channelFuture.addListener(future -> {

            if (future.isSuccess()){
                logger.debug("端口监听成功 port:{} path:{}",port,path);
            }else{
                logger.debug("端口监听失败 port:{} path:{}",port,path);
            }

        });



    }

    @Override
    public void close() {

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
