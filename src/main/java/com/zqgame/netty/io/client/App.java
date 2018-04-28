package com.zqgame.netty.io.client;

import com.zqgame.netty.io.handle.BaseServerMap2ProtoEncode;
import com.zqgame.netty.io.handle.BaseServerProto2MapDecode;
import com.zqgame.netty.io.handle.BaseServerProtoMessageDecode;
import com.zqgame.netty.io.handle.BaseServerProtoMessageEncode;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

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

					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());

					ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

					ch.pipeline().addLast( new BaseServerProtoMessageDecode() );
					ch.pipeline().addLast( new BaseServerProtoMessageEncode() );

					ch.pipeline().addLast(new BaseServerMap2ProtoEncode());
					ch.pipeline().addLast(new BaseServerProto2MapDecode());

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

		new App("127.0.0.1",8000).run();


//		NettyIoProto.test test = NettyIoProto.test.newBuilder().setValue("111").build();





	}
}
