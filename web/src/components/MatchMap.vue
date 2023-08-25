<template>
    <div class="MatchMap">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="自己的头像">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="对手的头像">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12 match-btn">
                <button type="button" class="btn btn-warning" @click="click_match_btn" v-if="match_info === '开始匹配'">{{ match_info }}</button>
                <button type="button" class="btn btn-danger" @click="click_match_btn" v-if="match_info === '取消匹配'">{{ match_info }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'

export default {
    setup() {
        const store = useStore();
        let match_info = ref('开始匹配')

        const click_match_btn = () => {
            if (match_info.value === '开始匹配') {
                match_info.value = '取消匹配'
                // 通过websocket协议通道发送给后端一个开启匹配的信号
                store.state.pk.socket.send(JSON.stringify({
                    event : 'start-matching'
                }))
            } else if (match_info.value === '取消匹配') {
                match_info.value = '开始匹配'
                // 通过websocket协议通道发送给后端一个取消匹配的信号
                store.state.pk.socket.send(JSON.stringify({
                    event : 'stop-matching'
                }))
            }
        }

        return {
            match_info,
            click_match_btn,
        }
    }
}
</script>

<style scoped>
div.MatchMap {
    width: 100%;
    height: 100%;
    background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
    padding-top: 60px;
    text-align: center;
}
div.user-photo>img {
    border-radius: 50%;
    width: 50%;
    height: 50%;
    
}
div.user-username {
    color: white;
    font-size: 32px;
    text-align: center;
}
div.match-btn {
    text-align: center;
    padding-top: 50px;
}
</style>