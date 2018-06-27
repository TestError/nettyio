package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.handle.BaseServerMap2ProtoEncode;
import com.zqgame.netty.io.handle.BaseServerProto2MapDecode;
import com.zqgame.netty.io.handle.BaseServerProtoMessageDecode;
import com.zqgame.netty.io.handle.BaseServerProtoMessageEncode;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 客户端 peng.chen 2018/06/26 17:00:24
 */
public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    /**
     * 实际连接的channel
     */
    private Channel channel;

    /**
     * 是否操作去关掉连接的
     */
    private boolean isActionClose = false;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 用于连接的线程池
     * peng.chen 2018/06/27 10:35:19
     */
    private EventLoopGroup workerGroup;

    /**
     * 连接启动器
     */
    private Bootstrap bootstrap;


    /**
     * 没有对应的客户端Hadnlers 没有啥实际意义
     * peng.chen 2018/06/27 11:47:38
     *
     * @param host 主机Ip地址
     * @param port 对应的端口
     */
    public Client(String host, int port) {
        this(host, port, (ChannelHandler[]) null);
    }

    /**
     * 单一Handler的情况
     *
     * @param host
     * @param port
     * @param channelHandler 需要注意   这里的都需要加上注解@ChannelHandler.Sharable 否则在重连时会报错
     */
    public Client(String host, int port, ChannelHandler channelHandler) {
        this(host, port, new ChannelHandler[]{channelHandler});
    }


    /**
     * 初始化客户端连接
     *
     * @param host            主机Ip地址
     * @param port            对应的端口
     * @param channelHandlers 需要注意   这里的都需要加上注解@ChannelHandler.Sharable 否则在重连时会报错
     */
    public Client(String host, int port, ChannelHandler[] channelHandlers) {

        if (StringUtil.isNullOrEmpty(host)) {
            throw new IllegalArgumentException();
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException();
        }

        this.host = host;
        this.port = port;

        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
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

                //30秒没有输出数据就发送心跳包
                ch.pipeline().addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));

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
    public void connect() {
        ChannelFuture channelFuture = bootstrap.connect(this.host, this.port);

        //注册Channel
        channel = channelFuture.channel();

        channelFuture.addListener(future -> {

            Runnable connectRun = () -> {
                logger.debug("尝试重连 host:{},port:{}", host, port);
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
                logger.info("成功连接 host:{} port:{}", host, port);


                //注册掉线重连
                //掉线重连
                channelFuture.channel().closeFuture().addListener(closeFuture -> {
                    logger.debug("channel:{}掉线了!", channel);
                    reconnect.run();
                });

            } else {
                logger.debug("连接 host:{} port:{} 失败", host, port);
                //连接失败重连
                reconnect.run();
            }

        });

    }

    /**
     * 尝试关掉
     */
    public void close() {
        isActionClose = true;

        channel.close().addListener(future -> {
            if (future.isSuccess()) {
                logger.debug("channel:{} 关掉了", channel);
            } else {
                logger.debug("channel:{} 无法正常关闭", channel);
            }
        });

        workerGroup.shutdownGracefully();
    }


}
