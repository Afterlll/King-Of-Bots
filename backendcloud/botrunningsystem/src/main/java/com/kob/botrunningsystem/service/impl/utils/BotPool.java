package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * bot池
 */
public class BotPool extends Thread{

    private final ReentrantLock lock = new ReentrantLock(); // 锁
    private final Condition condition = lock.newCondition(); // 条件
    private final Queue<Bot> bots = new LinkedList<>(); // 所有将要执行的bot队列

    public void addBot(Bot bot) {
        lock.lock();
        try {
            bots.add(bot);
            condition.signalAll(); // 队列中有元素了就唤醒所有线程
        } finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot); // 五秒内需要接收到下一步操作，每个AI给出两秒钟的时间
    }

    /**
     * 当bot队列中有元素时，进行消费
     * 没有元素时，阻塞线程
     */
    @Override
    public void run() {
        while (true) {
            lock.lock(); // 一进线程就锁住
            if (bots.isEmpty()) { // 队列为空
                try {
                    condition.await(); // 阻塞线程，同时会自动释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove(); // 取得队头元素并移除
                lock.unlock(); // 在真正的消费之前解锁，确定队中元素之后就不涉及读写冲突了
                consume(bot); // 比较耗时，可能会执行几秒钟
            }
        }
    }
}