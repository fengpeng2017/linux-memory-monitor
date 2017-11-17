package com.jthinking.monitor.service;


import com.jthinking.monitor.web.websocket.EchoClientEndpoint;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * WebSocket工具类，实时消息发送
 * @author JiaBochao
 * @version 2017-11-17 10:09:18
 */
public class WebSocketManager {

    private WebSocketContainer container;
    private URI uri;
    private Session session;

    public WebSocketManager(String url) {
        try {
            container = ContainerProvider.getWebSocketContainer();
            //"ws://localhost:8420/websocket/echo"
            uri = URI.create(url);
            session = container.connectToServer(EchoClientEndpoint.class, uri);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
