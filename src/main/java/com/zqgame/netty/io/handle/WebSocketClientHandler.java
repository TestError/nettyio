package com.zqgame.netty.io.handle;

import com.zqgame.netty.io.client.WebSocketClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.ClosedWatchServiceException;

public class WebSocketClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt.equals(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE)) {
            logger.debug("event:{}", evt);
            var textWebSocketFrame = new TextWebSocketFrame("测试的");

            ctx.writeAndFlush(textWebSocketFrame);
        }


    }

    //    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        var textWebSocketFrame  = new TextWebSocketFrame("测试的");
//
//        ctx.writeAndFlush(textWebSocketFrame);
//
//
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof TextWebSocketFrame) {
            var textMsg = (TextWebSocketFrame) msg;

            logger.debug("接收到Text消息:{}", textMsg.text());


//            ctx.writeAndFlush(new CloseWebSocketFrame());

        } else if (msg instanceof PingWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame());
        } else if (msg instanceof BinaryWebSocketFrame) {
            var binaryMsg = (BinaryWebSocketFrame) msg;
            ctx.fireChannelRead(binaryMsg.content());
        }

    }
}
