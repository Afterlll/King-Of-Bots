package com.kob.botrunningsystem.service;

public interface BotRunningService {
    /**
     * 添加bot
     * @param userId 该bot的主人
     * @param botCode bot的执行代码
     * @param input 执行的bot的环境信息（地图，障碍物....）
     * @return
     */
    String addBot(Integer userId, String botCode, String input);
}
