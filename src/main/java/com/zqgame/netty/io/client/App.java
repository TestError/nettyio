package com.zqgame.netty.io.client;

/**
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException{


	    var TCPClient = new UDPClient("127.0.0.1",8000, new ClientHandler());
        TCPClient.connect();
//	    Thread.sleep(10000);
//	    TCPClient.close();


	    Object o = new Object();
//        synchronized (o) {
//            o.wait();
//        }



	}
}
