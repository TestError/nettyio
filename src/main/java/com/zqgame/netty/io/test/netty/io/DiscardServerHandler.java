package com.zqgame.netty.io.test.netty.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description
 * @auther peng.chen
 * @create 2018/3/14 14:14
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("链接不活动");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		logger.debug("消息读完了");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		logger.debug("用户事件触发了");
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		logger.debug("可写触发了");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("新的连接了");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("连接释放了:{}");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		logger.debug("新链接活动,ip:{}", ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		try {
			ByteBuf in = (ByteBuf) msg;

			logger.debug("接收到信息:{}", in.toString(CharsetUtil.UTF_8));
			ctx.writeAndFlush(msg);
//		} finally {
//			ReferenceCountUtil.release(msg);
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		logger.warn("连接异常", cause);
		ctx.close();

	}
}
