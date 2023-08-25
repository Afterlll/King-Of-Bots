package com.kob.backend.service.pk;

public interface StartGameService {
    /**
     * 玩家a与玩家b开始游戏
     * @param aId
     * @param bId
     * @return
     */
    String startGame(Integer aId, Integer bId);
}
