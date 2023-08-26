<template>
    <PlayGround />
</template>

<script>
import { onMounted, onUnmounted } from 'vue'
import PlayGround from '../../components/PlayGround.vue'
import store from '@/store'

export default {
    components: {
        PlayGround
    },
    setup() {
        const socketUrl = `ws:localhost:3000/websocket/${store.state.user.token}`

        store.commit("updateStatus", "matching");
        store.commit("updateLoser", "none")
        store.commit("updateIsRecord", false) 
        store.commit('updatePosition', 'none')

        let socket = null
        onMounted(() => {

            // 设置默认的对手信息
            store.commit('updateOpponent', {
                username : '我的对手',
                photo : 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
            })
            socket = new WebSocket(socketUrl)

            // 开启连接
            socket.onopen = () =>  {
                console.log("connected!");
                store.commit("updateSocket", socket);
            }

            // 关闭连接
            socket.onclose = () => {
                console.log("disconnected!");
            }

            // 接收后端传递过来的数据
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data)
                if (data.event === 'start-matching') { // 匹配成功
                    store.commit('updateOpponent', {
                        username : data.opponent_username,
                        photo : data.opponent_photo
                    })
                    // 匹配成功之后延迟两秒钟跳转到对战页面
                    setTimeout(() => {
                        store.commit('updateStatus', 'playing')
                    }, 200);
                    // 匹配成功之后更新地图
                    store.commit('updateGame', data.game)
                    if (store.state.pk.a_id === store.state.user.id) {
                        store.commit('updatePosition', '左下角')
                    } else {
                        store.commit('updatePosition', '右上角')
                    }
                } else if (data.event === 'move') { // 蛇移动
                    const [snake0, snake1] = store.state.pk.gameObject.snakes // 获取到当前对战的两条蛇对象
                    snake0.set_direction(data.a_direction) // 设置从后端获取到的蛇下一步方向
                    snake1.set_direction(data.b_direction)
                } else if (data.event === 'result') { // 分出结果了
                    const [snake0, snake1] = store.state.pk.gameObject.snakes // 获取到当前对战的两条蛇对象
                    console.log(data.loser);
                    if (data.loser === 'all') {
                        snake0.status = 'die'
                        snake1.status = 'die'
                    } else if (data.loser === 'A') {
                        snake0.status = 'die'
                    } else if (data.loser === 'B') {
                        snake1.status = 'die'
                    }
                    store.commit("updateLoser", data.loser)
                    store.commit("updateEyeDirectionA", data.eye_direction_a)
                    store.commit("updateEyeDirectionB", data.eye_direction_b)
                } 
            }

        })

        onUnmounted(() => {
            socket.close();
            store.commit('updateStatus', 'matching')
        })

        return {

        }

    }
}
</script>

<style scoped>

</style>