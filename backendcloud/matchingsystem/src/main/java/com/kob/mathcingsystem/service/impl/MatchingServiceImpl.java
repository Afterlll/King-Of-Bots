package com.kob.mathcingsystem.service.impl;

import com.kob.mathcingsystem.service.MatchingServer;
import com.kob.mathcingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingServer {

    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer botId, Integer rating) {
        System.out.println("add player: " + userId + " " + botId + " " + rating);
        matchingPool.addPlayer(userId, botId, rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
