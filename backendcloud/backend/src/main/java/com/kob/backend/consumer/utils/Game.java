package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Record;
import com.kob.backend.consumer.WebSocketServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 地图类
 * 每两个玩家的game都要新开一个新线程
 */
public class Game extends Thread {
    private final Integer rows; // 地图的行数
    private final Integer cols; // 地图的列数
    private final Integer inner_walls_count; // 生成的障碍物数量
    private final int[][] g; // 存储地图
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1}; // 四个方位数组
    private final Player playerA, playerB; // 玩家A、玩家B
    private Integer nextStepA = null; // 玩家A的下一步操作
    private Integer nextStepB = null; // 玩家B的下一步操作
    private ReentrantLock lock = new ReentrantLock(); // 锁
    private String status = "playing"; // 记录整个游戏的状态 playing -> finished
    private String loser = ""; // all: 都输 A: A输 B: B输
    private static final String ADD_BOT_URL = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols]; // 每次构造的时候新开一个空间存储地图

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    /**
     * 下一步的写操作需要加锁
     * @param nextStepA
     */
    public void setNextStepA(Integer nextStepA) {
        lock.lock(); // 加锁
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock(); // 解锁
        }
    }

    /**
     * 下一步的写操作需要加锁
     * @param nextStepB
     */
    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    // 返回地图
    public int[][] getG() {
        return g;
    }

    // 检查地图的对称连通性
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    // 画地图
    private boolean draw() {
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    // 创建出一个地图
    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

    /**
     * 将当前的局面信息转换为字符串
     * 格式：地图#我的起点横坐标#我的起点纵坐标#我的操作#对手的起点横坐标#对手的起点纵坐标#对手的操作
     * @param player
     * @return
     */
    private String getInput(Player player) {
        // 分清敌我
        Player me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }

        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }

    /**
     * 此时是AI进行游戏，发送给Bot代码执行的微服务
     * @param player
     */
    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return; // 人工操作，return
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(ADD_BOT_URL, data, String.class);
    }

    // 等待两名玩家的下一步操作，当后端接收到两个client传过来的下一步操作之后才返回结果给前端
    private boolean nextStep() {
        // 由于前端每秒钟走五个格子，为了防止后端读取到下一步的操作过快而覆盖前面的操作，因此需要让后端至少睡眠一个格子的时间
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 下一步操作是人还是AI
        sendBotCode(playerA);
        sendBotCode(playerB);

        // 五秒钟内双方（一方）没有输入时，就是双方（一方）输了
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    // 两名玩家都读取到了下一步操作
                    if (nextStepA != null && nextStepB != null) {
                        // 记录下两名玩家下一步的操作
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 判断cellA的当前操作是否合法
     * @param cellA 判断是否合法的对象
     * @param cellB 判断依据其一
     * @return
     */
    private boolean check_valid(List<Cell> cellA, List<Cell> cellB) {
        int n = cellA.size();
        Cell cell = cellA.get(n - 1); // A蛇的蛇头

        if (g[cell.getX()][cell.getY()] == 1) return false; // A蛇头与墙重合，A操作非法

        // A蛇头是否撞到A蛇身，A操作非法
        for (int i = 0; i < n - 1; i ++ ) {
            if (cellA.get(i).getX().equals(cell.getX()) && cellA.get(i).getY().equals(cell.getY())) {
                return false;
            }
        }

        // A蛇头是否撞到B蛇身，B操作非法
        for (int i = 0; i < n - 1; i ++ ) {
            if (cellB.get(i).getX().equals(cell.getX()) && cellB.get(i).getY().equals(cell.getY())) {
                return false;
            }
        }

        return true;
    }

    // 判断两名玩家下一步操作是否合法
    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB); // A操作是否合法
        boolean validB = check_valid(cellsB, cellsA); // B操作是否合法

        if (!validA || !validB) {
            this.status = "finished";

            if (!validA && !validB) {
                this.loser = "all";
            } else if (!validA) {
                this.loser = "A";
            } else {
                this.loser = "B";
            }
        }
    }

    // 向两个玩家发送信息
    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    // 向两个玩家发送移动信息
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = null; // 读取之后重置下一步操作
            nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    // 将本局的对战记录保存到数据库中
    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.getRecordMapper().insert(record);
    }

    // 向两名玩家广播结果
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", this.loser);
        List<Integer> cellsA = playerA.getSteps();
        List<Integer> cellsB = playerB.getSteps();
        if (cellsA.size() > 0 && cellsB.size() > 0) {
            resp.put("eye_direction_a", cellsA.get(cellsA.size() - 1));
            resp.put("eye_direction_b", cellsB.get(cellsB.size() - 1));
        }
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++ ) {
            for (int j = 0; j < cols; j ++ ) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    @Override
    public void run() {
        // 整个游戏最多走600步，循环一千次已经足够了
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) { // 获取到下一步操作了
                this.judge(); // 获取到两个玩家的下一步操作之后先判断是否合法
                if ("playing".equals(this.status)) { // 两个玩家的下一步操作都合法
                    sendMove();
                } else { // 有至少一个玩家死了
                    sendResult();
                    break;
                }
            } else { // 没有获取到下一步操作
                this.status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        this.loser = "all";
                    } else if (nextStepA == null) {
                        this.loser = "A";
                    } else {
                        this.loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
