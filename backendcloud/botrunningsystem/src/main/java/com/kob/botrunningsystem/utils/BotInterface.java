package com.kob.botrunningsystem.utils;

/**
 * 实现前端用户编写的代码的接口
 * 也就是说前端用户提供的代码需要实现这个接口
 */
public interface BotInterface {
    /**
     * 执行完bot代码之后返回的蛇的下一步操作
     * @param input
     * @return
     */
    Integer nextMove(String input);
}
