import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

/**
 * 蛇基类
 */
export class Snake extends AcGameObject {
    /**
     * 构造器
     * @param {*} info 创建蛇的一些信息，有id、行坐标、列坐标、蛇的颜色...
     * @param {*} gamemap 创建的蛇处于的地图位置
     */
    constructor(info, gamemap) {
        super() 

        this.id = info.id // 成员变量：蛇的id
        this.color = info.color // 成员变量：蛇的颜色
        this.gamemap = gamemap // 成员变量：蛇所处的地图

        // 蛇的所有节点，cells[0]存放蛇头
        this.cells = [new Cell(info.r, info.c)]

        this.next_cell = null // 下一步的目标位置

        this.speed = 5 // 蛇每秒钟移动5个格子

        this.status = "idle" // 蛇的状态："idle"表示静止，"move"表示正在移动，"die"表示死亡
        this.direction = -1 // 0,1,2,3分别表示上右下左

        this.dr = [-1, 0, 1, 0] // 四个方向的行偏移量
        this.dc = [0, 1, 0, -1] // 四个方向的列偏移量

        this.step = 0 // 表示蛇当前走了几步

        this.eps = 1e-2 // 蛇两个节点是否重合的误差判断值

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;  // 左下角的蛇初始朝上，右上角的蛇朝下

        this.eye_dx = [  // 蛇眼睛不同方向的x的偏移量
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [  // 蛇眼睛不同方向的y的偏移量
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]
    }

    start() {

    }

    set_direction(d) {
        this.direction = d
    }

    /**
     * 检测当前回合，蛇的长度是否增加
     * 前十回合每一步增加一个单位长度，十步之后每三步增加一个单位长度
     * @returns 
     */
    check_tail_increasing() { 
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }

    /**
     * 将蛇的状态变为走下一步
     */
    next_step () {
        const d = this.direction
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d])
        this.eye_direction = d
        this.direction = -1 // 确定好下一步走的位置之后要清空方向的操作
        this.status = "move" // 蛇正在移动
        this.step ++ // 步数加一

        // 将蛇的身体节点都复制一份
        const k = this.cells.length
        for (let i = k; i > 0; i -- ) {
            // 注意节点之间不能直接复制，否则就是节点的引用，就导致所有节点都是第k个节点了，可以通过JSON作为中间转换
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

        if (!this.gamemap.check_valid(this.next_cell)) {  // 下一步操作撞了，蛇瞬间去世
            this.status = "die";
        }

    }

    /**
     * 蛇的移动 -------> 只移动蛇头
     * 如何让蛇动起来呢？
     * timedelta维护的是每两桢之间的时间间隔，时间是毫秒，需要转换为秒
     * 横坐标 this.cells[0].x += this.speed * this.timedelta / 1000 
     */
    update_move() {
        // 以斜线走为一般情况就可以退出走一步的规律，就是通过下一步的坐标与当前的头节点坐标的横纵坐标之差的距离
        const dx = this.next_cell.x - this.cells[0].x
        const dy = this.next_cell.y - this.cells[0].y
        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance < this.eps) { // 两个点重合了，也就是蛇此时已经从移动状态变为了静止状态
            this.cells[0] = this.next_cell // 蛇头变为下一步节点
            this.next_cell = null 
            this.status = "idle" // 走完了，停下来

            if (!this.check_tail_increasing()) {  // 蛇不变长
                this.cells.pop();
            }
        } else { // 此时蛇需要移动
            // 蛇移动的距离
            const move_distance = this.speed * this.timedelta / 1000
            this.cells[0].x += move_distance * dx / distance
            this.cells[0].y += move_distance * dy / distance

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update () {
        if (this.status === "move") {
            this.update_move()
        }
        this.render()
    }
    
    render () {
        const L = this.gamemap.L
        const ctx = this.gamemap.ctx

        ctx.fillStyle = this.color
        if (this.status === "die") {
            ctx.fillStyle = "white";
        }


        for (const cell of this.cells) {
            ctx.beginPath() // 开启路径
            /** 准备出圆弧
             * arc(x, y, radius, startAngle, endAngle, anticlockwise)
             * 画一个以（x,y）为圆心的以 radius 为半径的圆弧（圆），从 startAngle 开始到 endAngle 结束，按照 anticlockwise 给定的方向（默认为顺时针）来生成
             */
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill() // 绘制出圆弧来
        }

        // 相邻两个节点之后用长方形覆盖以下（为了蛇的美观）
        for (let i = 1; i < this.cells.length; i ++ ) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) { // 水平方向
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }


        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i ++ ) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }

    }
}