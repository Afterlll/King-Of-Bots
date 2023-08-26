package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * 消费者线程
 */
@Component
public class Consumer extends Thread{

    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String RECEIVE_BOT_MOVE = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    /**
     * 限制bot代码的最长执行时间
     * @param timeout
     * @param bot
     */
    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start(); // 开启线程

        try {
            // 最多等待timeout秒，当这个线程执行完毕（就是执行编译的逻辑）之后就执行join后面的代码，如果超过timeout该线程的逻辑还没有执行完毕，就中断该线程
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 中断该线程
        }
    }

    // 在code中的Bot类名后添加uid
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    /**
     * 消费线程就是在不断的编译前端传过来的代码
     * 这里目前只支持java
     */
    @Override
    public void run() {
        String uid = UUID.randomUUID().toString().substring(0, 8);

        // 使用joor进行代码的编译，同一个类名只会编译一次，因此需要不断变化类名
        // 参数：类名，进行编译的代码
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot" + uid, // 不断变化类名
                addUid(bot.getBotCode(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());

        restTemplate.postForObject(RECEIVE_BOT_MOVE, data, String.class);
    }
}
