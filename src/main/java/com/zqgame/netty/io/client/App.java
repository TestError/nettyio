package com.zqgame.netty.io.client;

import com.zqgame.netty.io.server.TCPServer;

/**
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException{


	    var TCPClient = new TCPClient("192.168.53.16",8000, new ClientHandler());
        TCPClient.connect();
//	    Thread.sleep(10000);
//	    TCPClient.close();


	    Object o = new Object();
//        synchronized (o) {
//            o.wait();
//        }



	}
}
