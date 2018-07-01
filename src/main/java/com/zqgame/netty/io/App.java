package com.zqgame.netty.io;

import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.context.ContextGetter;
import com.zqgame.netty.io.handle.MessageProcessHandle;
import com.zqgame.netty.io.server.TCPServer;
import com.zqgame.netty.io.server.UDPServer;
import com.zqgame.netty.io.server.WebSocketServer;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * peng.chen
 * 2018/3/14 13:28
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);



    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, IllegalAccessException {


        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(SystemProperty.APPLICATION_XML_PATH);

        applicationContext.start();
        ContextGetter.setApplicationContext(applicationContext);


        var server = new WebSocketServer(8000,"/ws",(ChannelHandler) applicationContext.getBean(MessageProcessHandle.class));

        server.bind();

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

//        scheduledExecutorService.schedule(() -> {
//            server.close();
//        },10,TimeUnit.SECONDS);

        scheduledExecutorService.schedule(oneRun,1,TimeUnit.SECONDS);

        ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(moreRun,1,5,TimeUnit.SECONDS);

        scheduledExecutorService.schedule(() -> {scheduledFuture.cancel(true);},20,TimeUnit.SECONDS);

//        scheduledExecutorService.schedule(() -> {
//
//            scheduledExecutorService.shutdown();
//
//        },30,TimeUnit.SECONDS);


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
//		new App(8000).run();

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
