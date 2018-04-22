package com.zqgame.netty.io.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {


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
