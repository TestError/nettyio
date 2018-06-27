package com.zqgame.netty.io.client;

/**
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException{


	    AppClient appClient = new AppClient("127.0.0.1",8000, new ClientHandler());
        appClient.connect();
//	    Thread.sleep(10000);
//	    appClient.close();


	    Object o = new Object();
//        synchronized (o) {
//            o.wait();
//        }



	}
}
