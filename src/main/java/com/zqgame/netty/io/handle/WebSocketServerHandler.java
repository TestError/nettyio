package com.zqgame.netty.io.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理websocekt的Handler
 * peng.chen 2018/06/29 17:24:02
 */
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof TextWebSocketFrame){
            var textMsg = (TextWebSocketFrame)msg;

            logger.debug("接收到Text消息:{}",textMsg.text());

            ctx.writeAndFlush(textMsg);

        }else if(msg instanceof PingWebSocketFrame){
            ctx.writeAndFlush(new PongWebSocketFrame());
        }
        else if (msg instanceof BinaryWebSocketFrame){
            var binaryMsg = (BinaryWebSocketFrame)msg;
            ctx.fireChannelRead(binaryMsg.content());
        }


    }


}
