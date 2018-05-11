package com.zqgame.netty.io.test;

import org.junit.Test;

import java.util.concurrent.*;

public class ScheduledExecutorTest {

    @Test
    public void test(){



        Executors.newFixedThreadPool(10);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        scheduledExecutorService.schedule(()->{ System.out.println("计划调度");},10,TimeUnit.SECONDS);
        scheduledExecutorService.execute(()->{System.out.println("立即执行");});

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
