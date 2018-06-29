package com.zqgame.netty.io.test;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitTest {

    @Test
    public void testAwait() throws InterruptedException {

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        Runnable runnable = () -> {
            reentrantLock.lock();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll();
            reentrantLock.unlock();
        };

        new Thread(runnable).start();

        System.out.println("等待唤醒");
        reentrantLock.lock();
        condition.await();
        System.out.println("被唤醒了");
        reentrantLock.unlock();


    }

}
