/**
 * 游戏动画全局基类，用来实现每一帧不断的刷新
 */

const AC_GAME_OBJECT = [] // 全局数组，存放所有的游戏对象

/**
 *  一秒刷新60桢（基本都是这样）
    为了让我们每一秒都将游戏对象刷新一遍，我们需要把所有游戏对象都存下来，
    存到一个全局变量数组里面，class定义基类，export出去，export出去，实
    现每秒刷新六十次，这个函数他可以传一个回调函数，会在浏览器下一祯渲
    染之前，执行一遍
 */
export class AcGameObject {
    constructor() {
        // 每次调用构造器就将当前游戏对象存放起来
        AC_GAME_OBJECT.push(this)
        // 定义成员变量timedelta，用来记录每一帧的时间间隔
        this.timedelta = 0
        // 定义成员变量has_called_start，用来判断该游戏对象是否已经执行过start函数
        this.has_called_start = false
    }

    // 只执行一次，也就是在第一帧的时候执行
    start () {

    }

    // 每一帧执行一次，除了第一帧之外
    update () {

    }

    // 删除游戏对象之前执行
    on_destroy () {

    }

    // 删除游戏对象
    destroy () {
        // 删除之前执行前置操作
        this.on_destroy()

        // 遍历全局游戏对象数组，找到当前的对象并进行删除
        // in ---- 遍历下标 
        // of ---- 遍历对应下标的值
        for (let i in AC_GAME_OBJECT) {
            let obj = AC_GAME_OBJECT[i]
            if (obj === this) {
                // 找到对应的下标将其删除
                AC_GAME_OBJECT.splice(i)
                break
            }
        }
    }
}

// 上一次执行的时间
let last_timestamp

// 传入参数：执行step方法的当前时间
const step = timestamp => {
    // 执行该动画时需要判断当前是否是第一帧
    // 如果是，执行start
    // 如果否，执行update
    for (let obj of AC_GAME_OBJECT) {
        if (obj.has_called_start) { // 该游戏对象已经执行过了start
            // 更新当前游戏对象的时间间隔
            obj.timedelta = timestamp - last_timestamp
            obj.update()
        } else { // 该游戏对象不是第一帧，执行update
            obj.has_called_start = true
            obj.start()
        }
    }

    // 更新上一次执行的时刻
    last_timestamp = timestamp

    /**
     * 告诉浏览器 —— 你希望执行一个动画，并且要求浏览器在下次重绘之前调用指定的回调函数更新动画。
     * 该方法需要传入一个回调函数作为参数，该回调函数会在浏览器下一次重绘之前执行。
     */
    requestAnimationFrame(step)
}

// 递归实现每一帧都重复执行该动画
requestAnimationFrame(step)