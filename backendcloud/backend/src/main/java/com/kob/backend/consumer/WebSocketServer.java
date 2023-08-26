package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.User;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthorizationUtil;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    /**
     * 用来存储当前websocket与game的对应关系
     */
    private Game game = null;

    /**
     * 由于websocket是多实例的，只能通过以下的方式进行注入
     */
    private static UserMapper userMapper;
    /**
     * 注入recordMapper
     */
    public static RestTemplate restTemplate;
    private static RecordMapper recordMapper;
    private static BotMapper botMapper;

    private static final String ADD_PLAYER_URL = "http://127.0.0.1:3001/player/add/";
    private static final String REMOVE_PLAYER_URL = "http://127.0.0.1:3001/player/remove/";

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    public static RecordMapper getRecordMapper() {
        return recordMapper;
    }

    public Game getGame() {
        return game;
    }

    /**
     * 建立连接
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose() {
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    /**
     * 开启游戏
     * @param aId
     * @param bId
     */
    public static void startGame(int aId, int aBotId, int bId, int bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB
        );
        game.createMap();
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;

        // 启动线程
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    /**
     * 开始匹配
     */
    private void startMatch(String bot_id) {
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());
        data.add("rating", user.getRating().toString());
        data.add("bot_id", bot_id);
        restTemplate.postForObject(ADD_PLAYER_URL, data, String.class);
    }

    /**
     * 取消匹配
     */
    private void stopMatch() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());
        restTemplate.postForObject(REMOVE_PLAYER_URL, data, String.class);
    }

    /**
     * 设置两个玩家的下一步操作
     * @param direction
     */
    private void move(int direction) {
        if (this.game.getPlayerA().getId().equals(this.user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) // 人工操作时才接收前端的操作
                game.setNextStepA(direction);
        } else if (this.game.getPlayerB().getId().equals(this.user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) // 人工操作时才接收前端的操作
                game.setNextStepB(direction);
        }
    }

    /**
     * 从Client接收消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatch(data.getString("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatch();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
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