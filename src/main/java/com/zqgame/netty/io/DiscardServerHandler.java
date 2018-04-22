package com.zqgame.netty.io;

import com.zqgame.netty.io.common.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * peng.chen
 *  2018/3/14 14:14
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);


	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.debug("链接不活动");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
		logger.debug("消息读完了");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		logger.debug("用户事件触发了");
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) {
		logger.debug("可写触发了");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		logger.debug("新的连接了");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) {
		logger.debug("连接释放了:{}");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {

		logger.debug("新链接活动,ip:{}", ctx.channel().remoteAddress().toString());

		Map<String,Object> message = new HashMap<String,Object>();

		message.put(Constant.PROTO,"com.zqgame.netty.io.proto.NettyIoProto.IOTest");
		Map<String,Object> body = new HashMap<String,Object>();
		body.put("message","测试消息哦！！！！！！！！！！！！");

		message.put(Constant.MESSAGE,body);

		ctx.writeAndFlush(message);


		/*ByteBuf time = ctx.alloc().buffer(4);
		time.writeInt((int) (System.currentTimeMillis() / 1000L));

		ChannelFuture f = ctx.writeAndFlush(time.toString());
		logger.debug(ctx.channel().id().asShortText());
		logger.debug(ctx.channel().toString());

		f.channel().write("123456");*/

//		logger.debug(f.toString());
//		f.
//		f.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture channelFuture) {
//				channelFuture.channel().close();
////				logger.debug(channelFuture.);
////				assert f = channelFuture;
//
////				ctx.close();
//			}
//		});

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
//		try {

		Map<String,Object> message = (Map<String,Object>)msg;

//		String in = (String) msg;

//		String rev = in.toString();
		logger.debug("接收到信息:{}", message);
		ctx.writeAndFlush(message);




//		if (rev.equals("c")) {
//			ctx.channel().close();
//		}
//			ctx.close();

//		} finally {
//			ReferenceCountUtil.release(msg);
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		logger.warn("连接异常", cause);
		ctx.close();

	}
}
