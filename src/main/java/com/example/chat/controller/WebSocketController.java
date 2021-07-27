package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String userId) {
        log.info("打开连接： [ id " + userId + ", session id :" + session.getId() + " ]");
        this.userId = userId;

        User user = new User();
//        user.setUserMsg(new Msg(false,"有新人加入聊天",true));
//        sendAll(user.toString());

        //将新用户存入在线的组
        clients.put(userId, session);
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session, @PathParam("id") String userId) {

        log.info("断开连接： [ id " + userId + ", session id :" + session.getId() + " ]");

//        User user = new User();
//        user.setUserMsg(new Msg(false,"有用户断开聊天",true));
//        sendAll(user.toString());

        //将掉线的用户移除在线的组里
        clients.remove(userId);
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        log.info("发生错误 : " + throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("服务端收到客户端发来的消息" + message);
        User user = JSON.parseObject(message, User.class);

        this.sendAll(user.toString());
    }

    /**
     * 群发消息
     *
     * @param message 消息内容
     */
    private void sendAll(String message) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            if (!sessionEntry.getKey().equals(userId)) {
                sessionEntry.getValue().getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 单发消息
     */
    private void send(String message) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            if (sessionEntry.getKey().equals(userId)) {
                sessionEntry.getValue().getAsyncRemote().sendText(message);
            }
        }
    }
}
