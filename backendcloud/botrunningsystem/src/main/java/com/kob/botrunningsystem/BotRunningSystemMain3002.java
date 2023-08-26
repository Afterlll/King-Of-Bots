package com.kob.botrunningsystem;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotRunningSystemMain3002 {
    public static void main(String[] args) {
        BotRunningServiceImpl.botPool.start(); // 在启动服务之前开启消费者线程
        SpringApplication.run(BotRunningSystemMain3002.class, args);
    }
}
