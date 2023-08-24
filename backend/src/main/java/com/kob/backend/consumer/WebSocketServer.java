package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.bean.User;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthorizationUtil;
import com.kob.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 前端请求匹配时就是发送一个websocket链接过来，后端就是新建一个WebSocketServer类的实例
 */
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    /**
     * 注意这个session是websocket中的session
     * 用来维护此次链接的所有信息
     */
    private Session session;
    /**
     * 存储此次链接的用户信息
     */
    private User user;
    /**
     * 维护所有websocket链接和用户之间的关系
     * userId --- WebSocketServer
     * 涉及到多线程，需要使用线程安全的ConcurrentHashMap哈希表
     * 由于是多用户共享的信息，需要使用static
     */
    private final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    /**
     * 匹配池
     */
    private final static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    /**
     * 由于websocket是多实例的，只能通过以下的方式进行注入
     */
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }


    /**
     * 建立连接
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            System.out.println("connected!");

            // 维护此次session
            this.session = session;
            // 解析出jwt-token中的userId（先直接使用userId）
            int userId = JwtAuthorizationUtil.getUserId(token);
            // 绑定用户
            this.user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
            if (this.user != null) {
                // 增加映射
                users.put(userId, this);
            } else {
                this.session.close();
            }

            System.out.println(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose() {
        System.out.println("disconnected!");
        if (this.user != null) {
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    /**
     * 开始匹配
     */
    private void startMatch() {
        System.out.println("start match");
        matchPool.add(this.user);

        while (matchPool.size() >= 2) {
            Iterator<User> it = matchPool.iterator();
            User a = it.next();
            User b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13, 14, 20);
            game.createMap();

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap", game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap", game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    /**
     * 取消匹配
     */
    private void stopMatch() {
        System.out.println("stop match");
        matchPool.remove(this.user);
    }

    /**
     * 从Client接收消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatch();
        } else if ("stop-matching".equals(event)) {
            stopMatch();
        }
    }

    /**
     * 后端向前端发送数据
     * @param message
     */
    public void sendMessage(String message) {
        // session对象涉及到多线程，需要锁住
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}