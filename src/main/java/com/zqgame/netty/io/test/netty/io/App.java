package com.zqgame.netty.io.test.netty.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description
 * @auther peng.chen
 * @create 2018/3/14 13:28
 */
public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	private int port;

	public App(int port){
		this.port = port;
	}

	public void run() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();

		ServerBootstrap serverBootstrap = b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
				new ChannelInitializer <SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel socketChannel) {
						socketChannel.pipeline().addLast(new DiscardServerHandler());
					}
				}
		).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);

		ChannelFuture f = b.bind(port).sync();

		f.channel().closeFuture().sync();

		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();

	}

	public static void main(String[] args) throws InterruptedException {

		new App(8000).run();


	}

}
