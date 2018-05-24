package com.zqgame.netty.io;

import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.context.ContextGetter;
import com.zqgame.netty.io.handle.*;
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
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


/**
 * peng.chen
 * 2018/3/14 13:28
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
                new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) {

                        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());

                        socketChannel.pipeline().addLast(new ProtobufEncoder());
                        socketChannel.pipeline().addLast(new ProtobufDecoder(NettyIoProto.Base.getDefaultInstance()));

                        socketChannel.pipeline().addLast(new BaseServerProtoMessageDecode());
                        socketChannel.pipeline().addLast(new BaseServerProtoMessageEncode());

                        socketChannel.pipeline().addLast(new BaseServerMap2ProtoEncode());
                        socketChannel.pipeline().addLast(new BaseServerProto2MapDecode());

                        //九十秒没有收到消息设置为断线
                        socketChannel.pipeline().addLast(new IdleStateHandler(90,0,0,TimeUnit.SECONDS));

                        socketChannel.pipeline().addLast(new MesssageProcessHandle());

                    }
                }
        ).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f = (ChannelFuture) b.bind(port).sync();

        logger.debug(f.toString());

        f.channel().closeFuture().sync();


        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }


    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {


        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(SystemProperty.APPLICATION_XML_PATH);

        applicationContext.start();
        ContextGetter.setApplicationContext(applicationContext);



//        applicationContext.registerShutdownHook();

//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            countDownLatch.countDown();
//            logger.debug("system exit");
//        }));

//        Callable

        logger.debug("bean:{}", applicationContext.getBean("testMessage"));

        Map messageMap = (Map)applicationContext.getBean(Constant.MESSAGE_MAP);

        Method method = (Method)messageMap.get("com.zqgame.netty.io.proto.NettyIoProto.Test");


        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService)applicationContext.getBean("scheduleThreadPool");


        Runnable oneRun = () -> {

            logger.debug("运行了一次");

        };

        Runnable moreRun = () -> {
            logger.debug("运行了多次");
        };

        scheduledExecutorService.schedule(oneRun,1,TimeUnit.SECONDS);

        ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(moreRun,1,5,TimeUnit.SECONDS);

        scheduledExecutorService.schedule(() -> {scheduledFuture.cancel(true);},20,TimeUnit.SECONDS);




//        scheduledExecutorService



//        scheduledExecutorService.scheduleWithFixedDelay()

//        try {
//            method.invoke(applicationContext.getBean(method.getDeclaringClass()),new HashMap<>());
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

//        countDownLatch.await();
//        ExecutorService executorService = new ThreadPoolExecutor(1,2);
//        ExecutorService executorService = new ExecutorService() {
//        }

//		logger.info( "开始跑了" );
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
