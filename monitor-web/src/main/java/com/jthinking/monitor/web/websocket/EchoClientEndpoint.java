package com.jthinking.monitor.web.websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * WebSocket Java客户端
 * @author Bochao
 * @version 2017-11-17 10:29:08
 */
@ClientEndpoint
public class EchoClientEndpoint {
	
	/**
	 * 建立连接时调用
	 * @param session
	 */
	@OnOpen
    public void onOpen(Session session) {
		System.out.println("Client OnOpen!");
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

	/**
	 * 收到服务端发送的消息时调用
	 * @param message
	 */
    @OnMessage
    public void receiveServerMessage(String message) {
    	System.out.println("Client OnMessage!");
        System.out.println(message);
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose() {
    	
    }
}
