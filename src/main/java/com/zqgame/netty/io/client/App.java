package com.zqgame.netty.io.client;

import com.zqgame.netty.io.server.TCPServer;

/**
 *
 */
public class App {

	public static void main(String[] args) throws InterruptedException{


//	    var TCPClient = new TCPClient("192.168.53.16",8000, new ClientHandler());
//        TCPClient.connect();
//	    Thread.sleep(10000);
//	    TCPClient.close();

		var webSocketClient = new WebSocketClient("ws://127.0.0.1:8000/ws",new WebSocketClientHandler());

		webSocketClient.connect();
//	    Object o = new Object();
//        synchronized (o) {
//            o.wait();
//        }



	}
}
