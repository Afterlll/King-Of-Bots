package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id; // 玩家id
    private Integer botId; // -1表示亲自出马，否则AI
    private String botCode;
    private Integer sx; // 玩家起点的横坐标
    private Integer sy; // 玩家七点的纵坐标
    private List<Integer> steps; // 玩家蛇的移动方向

    // 检测当前回合，蛇的长度是否增加
    // 前十步每一步蛇的长度加1，超过十步每三步蛇的长度加1
    private boolean check_tail_increasing(int step) {
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    // 获取蛇的轨迹
    // 注意：list集合的第一位是蛇尾，最后一位是蛇头
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy; // 蛇的起点坐标
        int step = 0; // 记录蛇的操作步数
        res.add(new Cell(x, y)); // 添加蛇的起点
        for (int d : steps) { // 枚举蛇的下一步操作
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing(++ step)) { // 蛇不增加长度时移除蛇尾
                res.remove(0);
            }
        }

        return res;
    }

    /**
     * 将每一步的操作转换为字符串
     * @return
     */
    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }

}