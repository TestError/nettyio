package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

		Map<String,Object> data = new HashMap<String, Object>();
		data.put("item",1);

		List<String> values = new ArrayList<String>();
		values.add("12231321");

		data.put("value",new ArrayList<String>().add("11111"));

		message.put(Constant.MESSAGE,data);

		ctx.writeAndFlush(message);
		logger.debug("发消息了哦");

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
