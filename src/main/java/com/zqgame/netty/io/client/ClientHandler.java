package com.zqgame.netty.io.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description
 * @auther peng.chen
 * @create 2018/3/15 13:41
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		ByteBuf byteBuf = ctx.alloc().buffer();

		StringBuilder output = new StringBuilder("你好aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

		for(int i = 0 ; i < 1000 ; i++){
			output.append("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}




		ChannelPromise channelPromise = ctx.newPromise();

		channelPromise.addListener(

				future -> {


					logger.debug("1");
					System.out.println(future.isSuccess());
				}
		);


		ctx.write(output.toString(),channelPromise);
		logger.debug("2");


//		ctx.write();
//		byteBuf.writeBytes();

//		ctx.write(byteBuf);

//		byteBuf = ctx.alloc().buffer();
//		byteBuf.writeBytes("c".getBytes(CharsetUtil.UTF_8));

//		ctx.writeAndFlush("c");

		ctx.write("111");

		ctx.write("12312332");

//		ctx.writeAndFlush(byteBuf);

//		byteBuf = ctx.alloc().buffer();
//		byteBuf.writeBytes("c".getBytes(CharsetUtil.UTF_8));


		ctx.writeAndFlush("c");
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String m = (String) msg;


//		long time = m.readUnsignedInt() * 1000L;

		logger.debug(m);


//		ByteBuf byteBuf = ctx.alloc().buffer();



//		ctx.writeAndFlush("c".getBytes(CharsetUtil.UTF_8));

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("异常:{}", cause);

		ctx.close();
	}
}
