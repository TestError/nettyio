package com.zqgame.netty.io.client;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent){
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

			//发送心跳包HeartBeat
			if (idleStateEvent.state() == IdleState.WRITER_IDLE){
				Map<String,Object> message = new HashMap<String, Object>();

				message.put(Constant.PROTO,Constant.HEART_BEAT_PROTO);
				ctx.writeAndFlush(message);
			}
		}else {
			ctx.fireUserEventTriggered(evt);
		}


	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {

		Map<String,Object> message = new HashMap<String, Object>();

		message.put(Constant.PROTO,"com.zqgame.netty.io.proto.NettyIoProto.Test");

		Map<String,Object> data = new HashMap<String, Object>();
		data.put("item",1);

		List<String> values = new ArrayList<String>();
		values.add("12231321");

		data.put("value",values);

		message.put(Constant.MESSAGE,data);

		ctx.writeAndFlush(message);
		logger.debug("发消息了哦");

	}

	//掉线后尝试重连
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("连接断掉了:{}",ctx);
		super.channelInactive(ctx);
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
