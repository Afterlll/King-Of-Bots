package com.kob.backend.service.pk;

public interface ReceiveBotMoveService {
    /**
     * 接收bot的下一步执行
     * @param userId
     * @param direction
     * @return
     */
    String receiveBotMove(Integer userId, Integer direction);
}
