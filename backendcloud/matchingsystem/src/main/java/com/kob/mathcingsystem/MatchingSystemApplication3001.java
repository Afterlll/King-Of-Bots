package com.kob.mathcingsystem;

import com.kob.mathcingsystem.service.impl.MatchingServiceImpl;
import com.kob.mathcingsystem.service.impl.utils.MatchingPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplication3001 {
    public static void main(String[] args) {
        // 在启动服务之前开启匹配池线程
        MatchingServiceImpl.matchingPool.start();
        SpringApplication.run(MatchingSystemApplication3001.class, args);
    }
}
