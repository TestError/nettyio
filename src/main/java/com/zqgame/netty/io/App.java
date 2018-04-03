package com.zqgame.netty.io;

import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description
 * @auther peng.chen
 * @create 2018/3/14 13:28
 */
public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);

	private int port;

	public App(int port) {
		this.port = port;
	}




	public void run() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();




		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
				new ChannelInitializer <SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel socketChannel) {

						socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
						socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());

						socketChannel.pipeline().addLast(new ProtobufEncoder());
						socketChannel.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));
//						socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
//						socketChannel.pipeline().addLast(new LengthFieldPrepender(8));

//						socketChannel.pipeline().addLast(new StringEncoder());
//						socketChannel.pipeline().addLast(new StringDecoder());

						socketChannel.pipeline().addLast(new DiscardServerHandler());

					}
				}
		).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture f = (ChannelFuture) b.bind(port).sync();
//		var

//		GlobalChannelTrafficShapingHandler

		logger.debug(f.toString());

//		ChannelPromise fp = f.



		f.channel().closeFuture().sync();

//		f.channel().close();

		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();

	}


//	public static volatile boolean callStop = false;

	public static void main(String[] args) throws InterruptedException {



		new App(8000).run();
//		new App(8001).run();

	/*	new Thread(() -> {


			do{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(callStop);
				System.out.println("111");
			}while (!callStop);

		}).run();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			callStop = true;

			System.out.println("准备退出");

		}));
*/
	}

}
