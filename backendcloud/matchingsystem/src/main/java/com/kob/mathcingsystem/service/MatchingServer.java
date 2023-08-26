package com.kob.mathcingsystem.service;

public interface MatchingServer {
    /**
     * 根据玩家id和分数进行匹配对手
     * @param userId
     * @param rating
     * @return
     */
    String addPlayer(Integer userId, Integer botId, Integer rating);

    /**
     * 删除对手
     * @param userId
     * @return
     */
    String removePlayer(Integer userId);
}
