package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {

		Map<String,Object> message = new HashMap<String, Object>();

		message.put(Constant.PROTO,"com.zqgame.netty.io.proto.NettyIoProto.Test");

		ctx.writeAndFlush(message);


	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Map<String,Object> message = (Map<String,Object>) msg;

		logger.debug("消息:{}",message);


	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("异常:{}", cause);

		ctx.close();
	}
}
