package com.zqgame.netty.io.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 *
 */
public class App {

	private String host;

	private int port;

	public App(String host,int port){
		this.host = host;
		this.port = port;
	}

	public void run() throws InterruptedException{

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try{

			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE,true);

			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {


					ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
					ch.pipeline().addLast(new LengthFieldPrepender(2));


					ch.pipeline().addLast(new StringEncoder());
					ch.pipeline().addLast(new StringDecoder());


					ch.pipeline().addLast(new ClientHandler());

				}
			});

			ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
			channelFuture.channel().closeFuture().sync();


		}finally {
			workerGroup.shutdownGracefully();
		}


	}

	public static void main(String[] args) throws InterruptedException{

//		new App("127.0.0.1",8000).run();


//		NettyIoProto.test test = NettyIoProto.test.newBuilder().setValue("111").build();





	}
}
