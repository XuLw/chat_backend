package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.Record;
import com.example.chat.entity.User;
import com.example.chat.service.RecordService;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{id}")
@Component
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static Map<String, List<Record>> unSendMsg = new ConcurrentHashMap<>(); // 存放未发送消息
    private String userId;

    @Autowired
    RecordService recordService;  // 将消息存放到数据库中

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String userId) {
        log.info("打开连接： [ id " + userId + ", session id :" + session.getId() + " ]");
        this.userId = userId;

        User user = new User();

        //将新用户存入在线的组
        clients.put(userId, session);
        log.info("当前在线人数：" + clients.size());
        //检查离线消息
        checkUnSend(userId);
    }

    private void showUnSendMesage() {

    }

    // 发送离线消息
    private void checkUnSend(String userId) {
        if (StringUtils.isNullOrEmpty(userId)) {
            return;
        }
        List<Record> list = unSendMsg.get(userId);
        //            有未发送消息
        if (list != null) {
            send(userId, list);
        }
//        清除未读消息
        list.clear();
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
        Record record = JSON.parseObject(message, Record.class);
        Session session = clients.get(record.getId2());
        if (session == null) {
//            为空
            List<Record> records = unSendMsg.getOrDefault(record.getId2(), new ArrayList<Record>());
            records.add(record);
            unSendMsg.put(record.getId2(), records);
        } else {
            send(session, record);
        }
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
    private void send(String userId, List<Record> msgs) {
        Session session = clients.get(userId);
        for (Record msg : msgs) {
            session.getAsyncRemote().sendText(JSON.toJSONString(msg));
        }
    }


    /**
     * 单发消息
     */
    private void send(Session session, Record message) {
        log.info("发送消息 " + message);
        session.getAsyncRemote().sendText(JSON.toJSONString(message));
    }

}
