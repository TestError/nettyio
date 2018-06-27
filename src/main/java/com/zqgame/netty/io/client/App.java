package com.zqgame.netty.io.client;

/**
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException{


	    Client client = new Client("127.0.0.1",8000, new ClientHandler());
        client.connect();
//	    Thread.sleep(10000);
//	    client.close();


	    Object o = new Object();
//        synchronized (o) {
//            o.wait();
//        }



	}
}
