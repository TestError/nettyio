package com.zqgame.netty.io.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class WebSocketBinaryFrameEncode extends MessageToMessageEncoder<ByteBuf> {

    private static Logger logger = LoggerFactory.getLogger(WebSocketBinaryFrameEncode.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {

        msg.retain();
        out.add(new BinaryWebSocketFrame(msg) );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        logger.error("ex:{}",cause);

//        super.exceptionCaught(ctx, cause);
    }
}
