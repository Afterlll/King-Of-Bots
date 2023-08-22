/**
 * 障碍物类
 */

import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
    /**
     * 三个参数 r, c, gamemap
     * 分别对应：障碍物所在的行，障碍物所在的列，障碍物所处的地图对象
     */
    constructor (r, c, gamemap) {
        super() // 调用父类的构造方法

        this.r = r
        this.c = c
        this.gamemap = gamemap

        // 障碍物的背景颜色
        this.color = "#B37226"
    }

    start () {

    }

    update () {
        this.render()
    }

    // 渲染出单个障碍物
    render () {
        const L = this.gamemap.L
        const ctx = this.gamemap.ctx

        ctx.fillStyle = this.color
        ctx.fillRect(this.c * L, this.r * L, L, L)
    }
}