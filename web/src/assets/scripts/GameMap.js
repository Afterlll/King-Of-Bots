/**
 * 游戏地图类
 * 继承基类（实现动画）
 */

import { AcGameObject } from "./AcGameObject"
import { Snake } from "./Snake"
import { Wall } from "./Wall"

export class GameMap extends AcGameObject {
    // 构造器，每次新建地图时需要传入两个参数
    // ctx    ---- 图像稍后将在此被渲染
    // parent ---- 地图对象的父元素，也就是canvas的父元素，作用：
    constructor (ctx, parent) {
        super() // 语法，子类必须先调用父类的构造器

        this.ctx = ctx // 画布对象
        this.parent = parent // 父元素

        this.L = 0 // 一个正方形格子的边长
        /**
         * 行和列相同时，会存在两个蛇的头结点重合的情况，使得先手必赢成为输了，本来是赢了的，但是成为了平局
         * 为了解决这种方式，更改行数
         */
        this.rows = 13 // 行数
        this.cols = 14 // 列数

        // 生成障碍物的数量（不包括四周）
        this.inner_walls_count = 20
        // 存放生成的障碍物
        this.walls = []

        // 参与对战的两条蛇
        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ]
    }

    // 保证连通(Flood Fill算法)
    check_connectivity(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i ++ ) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }

        return false;
    }

    create_walls () {
        const g = [] // 用来验证当前位置是否生成了障碍物，true表示当前位置是障碍物

        for (let r = 0; r < this.rows; r ++ ) {
            g[r] = [] // 将g变为二维数组
            for (let c = 0; c < this.cols; c ++ ) {
                // 给四周加上障碍物
                if (r === 0 || c === 0 || r === this.rows - 1 || c === this.cols - 1) {
                    g[r][c] = true
                } else {
                    g[r][c] = false
                }
            }
        }

        // 创建随机障碍物（中心对称）
        for (let i = 0; i < this.inner_walls_count / 2; i ++ ) {
            // 每个格子个1000次随机的机会，防止死循环
            for (let j = 0; j < 1000; j ++ ) {
                let r = parseInt(Math.random() * this.rows)
                let c = parseInt(Math.random() * this.cols)
                if (g[r][c]) continue

                // 给初始时的蛇预留一个位置，左下角和右上角
                if (r === this.rows - 2 && c === 1 || r === 1 && c === this.cols - 2) continue

                // g[r][c] = g[c][r] = true // 轴对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true // 中心对称
                break
            }
        }

        // 验证连通性
        const copy_g = JSON.parse(JSON.stringify(g))
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2))
            return false

        // 根据标志数组生成障碍物，并将其放进walls数组里
        for (let r = 0; r < this.rows; r ++ ) 
            for (let c = 0; c < this.cols; c ++ )
                if (g[r][c]) 
                    this.walls.push(new Wall(r, c, this))


        return true
    }


    /**
     * 给canvas增加监听事件
     */
    add_listening_events() {
        this.ctx.canvas.focus() // 聚焦

        const [snake0, snake1] = this.snakes
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') snake0.set_direction(0)
            else if (e.key === 'd') snake0.set_direction(1)
            else if (e.key === 's') snake0.set_direction(2)
            else if (e.key === 'a') snake0.set_direction(3)
            else if (e.key === 'ArrowUp') snake1.set_direction(0)
            else if (e.key === 'ArrowRight') snake1.set_direction(1)
            else if (e.key === 'ArrowDown') snake1.set_direction(2)
            else if (e.key === 'ArrowLeft') snake1.set_direction(3)
        })
    }

    start () {
        // 生成障碍物就是只生成一次了
        for (let i = 0; i < 1000; i ++ ) 
            if (this.create_walls())
                break;

        this.add_listening_events()
    }

    /**
     * 获取画布的边长比例情况
     */
    update_size() {
        // 根据当前地图背景的响应式长宽获取到游戏地图的一个正方形小格子的边长
        // 也就是取到父元素的长和宽的最小值
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows))
        // 取得画布的长和宽
        this.ctx.canvas.width = this.L * this.cols
        this.ctx.canvas.height = this.L * this.rows        
    }
    
    /**
     * 回合制游戏
     * 判断两条蛇是否都准备好走下一步了
     */
    check_ready() {
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false
            if (snake.direction === -1) return false
        }

        return true
    }
    
    /**
     * 让两条蛇进行下一回合
     */
    next_step () {
        for (const snake of this.snakes) {
            snake.next_step()
        }
    }

    check_valid(cell) {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k -- ;
            }
            for (let i = 0; i < k; i ++ ) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }

    /**
     * 每一帧都渲染一遍的地图，目前想不到更好的解释，感觉可能是为了防止用户在游戏过程中改变了网页的布局，导致地图没有进行响应式处理
     */
    update () {
        this.update_size()
        if (this.check_ready()) {
            this.next_step()
        }
        this.render()
    }


    /**
     * 画布游戏地图的背景，两种绿色的格子
     * canvas的坐标系：所有元素的位置都相对于原点定位。所以图中蓝色方形左上角的坐标为距离左边（X 轴）x 像素，距离上边（Y 轴）y 像素（坐标为（x,y））。
     */
    render() {
        const color_even = "#AAD751", color_odd = "#A2D149"
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even
                } else {
                    this.ctx.fillStyle = color_odd
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
            }
        }
    }
}