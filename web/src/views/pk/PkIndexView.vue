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
                if (data.event === 'start-matching') {
                    store.commit('updateOpponent', {
                        username : data.opponent_username,
                        photo : data.opponent_photo
                    })
                }
                // 匹配成功之后延迟两秒钟跳转到对战页面
                setTimeout(() => {
                    store.commit('updateStatus', 'playing')
                }, 2000);
                // 匹配成功之后更新地图
                store.commit('updateGameMap', data.gamemap)
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