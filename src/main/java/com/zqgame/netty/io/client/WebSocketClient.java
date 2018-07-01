package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.handle.*;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * webSocket客户端
 * peng.chen 2018/06/29 16:24:47
 */
public class WebSocketClient implements  Client {
    private static Logger logger = LoggerFactory.getLogger(TCPClient.class);

    /**
     * 实际连接的channel
     */
    private Channel channel;

    /**
     * 是否操作去关掉连接的
     */
    private boolean isActionClose = false;

    private URI uri;

    /**
     * 用于连接的线程池
     * peng.chen 2018/06/27 10:35:19
     */
    private EventLoopGroup workerGroup;

    /**
     * 连接启动器
     */
    private Bootstrap bootstrap;

    public WebSocketClient(String url){
        this(url,(ChannelHandler[]) null);

    }

    public WebSocketClient(String url,ChannelHandler channelHandler){
        this(url,new ChannelHandler[]{channelHandler});
    }


    /**
     *
     * @param url
     */
    public WebSocketClient(String url, ChannelHandler [] channelHandlers) {

        if (StringUtil.isNullOrEmpty(url)) {
            throw new IllegalArgumentException();
        }

        uri = URI.create(url);

        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new HttpClientCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(65536));
                ch.pipeline().addLast(new ChunkedWriteHandler());

                ch.pipeline().addLast(new WebSocketClientProtocolHandler(uri, WebSocketVersion.V13, "", true, null, 65536));
                ch.pipeline().addLast(new WebSocketFrameHandler());

                ch.pipeline().addLast(new WebSocketBinaryFrameEncode());

                ch.pipeline().addLast(new ProtobufEncoder());
                ch.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

                ch.pipeline().addLast(new BaseServerProtoMessageDecode());
                ch.pipeline().addLast(new BaseServerProtoMessageEncode());
//
                ch.pipeline().addLast(new BaseServerMap2ProtoEncode());
                ch.pipeline().addLast(new BaseServerProto2MapDecode());
//
//                //30秒没有输出数据就发送心跳包
//                ch.pipeline().addLast(new IdleStateHandler(0, SystemProperty.HEARTBEAT_TIME, 0, TimeUnit.SECONDS));
//                ch.pipeline().addLast(new BaseClientHeartbeatHandle());

                if (channelHandlers != null) {
                    for (var item : channelHandlers) {
                        ch.pipeline().addLast(item);
                    }
                }

            }
        });

    }

    /**
     * @return 成功连接时可以通过这个获取Channel
     */
    public Channel getChannel() {
        return channel;
    }

    ;


    /**
     * 尝试连接
     */
    @Override
    public void connect() {

        var channelFuture = bootstrap.connect(uri.getHost(),uri.getPort());

        this.channel = channelFuture.channel();

        channelFuture.addListener(future -> {

            if(future.isSuccess()){
                logger.debug("channel:{} connect success",channel);
            }else{
                logger.debug("channel:{} connect fail",channel);
            }

        });


        channelFuture.addListener(future -> {

            Runnable connectRun = () -> {
                logger.debug("尝试重连 uri:{}", uri);
                connect();
            };


            //重新连接
            Runnable reconnect = () -> {

                if (!isActionClose) {
                    //定时一段时间后去重连
                    workerGroup.schedule(connectRun, SystemProperty.RECOUNNECT_DELAY_TIME, TimeUnit.MILLISECONDS);
                }
            };

            if (future.isSuccess()) {
                logger.info("成功连接 uri:{}",uri);


                //注册掉线重连
                //掉线重连
                channelFuture.channel().closeFuture().addListener(closeFuture -> {
                    logger.debug("channel:{}掉线了!", channel);
                    reconnect.run();
                });

            } else {
                logger.debug("连接 uri:{} ", uri);
                //连接失败重连
                reconnect.run();
            }

        });

    }

    /**
     * 尝试关掉
     */
    @Override
    public void close() {
        isActionClose = true;

        Runnable runClose = () ->{
            channel.close().addListener(future -> {
                if (future.isSuccess()){
                    logger.debug("channel:{} 成功关闭");
                }else {
                    logger.debug("channel:{} 成功失败了");
                }


            });

            workerGroup.shutdownGracefully();

        };

        if(channel.isOpen()){

            channel.writeAndFlush(new CloseWebSocketFrame()).addListener(future -> {
                runClose.run();

            });

        }else{
            runClose.run();
        }

    }


}
