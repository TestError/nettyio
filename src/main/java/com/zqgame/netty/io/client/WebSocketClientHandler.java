package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChannelHandler.Sharable
public class WebSocketClientHandler  extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt.equals(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) )
        {

//            ctx.writeAndFlush(new TextWebSocketFrame("1111"));

            Map<String,Object> message = new HashMap<String, Object>();

            message.put(Constant.PROTO,"com.zqgame.netty.io.proto.NettyIoProto.Test");

            Map<String,Object> data = new HashMap<String, Object>();
            data.put("item",1);

            List<String> values = new ArrayList<String>();
            values.add("12231ssssssssssssssssssssssssssssssssssssssssss321");

            data.put("value",values);

            message.put(Constant.MESSAGE,data);


            logger.debug("channel:{}",ctx.channel());

            ctx.writeAndFlush(message);
            logger.debug("发消息了哦");

        }


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Map<String,Object> message = (Map<String,Object>) msg;

        logger.debug("消息:{}",message);
    }
}
