<template>
    <div class="MatchMap">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="自己的头像">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4 user-select-bot">
                <select v-model="select_bot" class="form-select" aria-label="Default select example">
                    <option selected value="-1">亲自上马</option>
                    <option v-for="bot of bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                </select>
            </div>
            <div class="col-4">
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
import $ from 'jquery'
export default {
    setup() {
        const store = useStore();
        let match_info = ref('开始匹配')
        let bots = ref([])
        let select_bot = ref("-1") // 选择的bot

        const click_match_btn = () => {
            if (match_info.value === '开始匹配') {
                match_info.value = '取消匹配'
                // 通过websocket协议通道发送给后端一个开启匹配的信号
                store.state.pk.socket.send(JSON.stringify({
                    event : 'start-matching',
                    bot_id : select_bot.value
                }))
            } else if (match_info.value === '取消匹配') {
                match_info.value = '开始匹配'
                // 通过websocket协议通道发送给后端一个取消匹配的信号
                store.state.pk.socket.send(JSON.stringify({
                    event : 'stop-matching'
                }))
            }
        }

        const refresh_bots = () => {
            $.ajax({
                url : 'https://app5765.acapp.acwing.com.cn/api/user/bot/getlist/',
                type : 'get',
                headers : {
                    Authorization : 'Bearer ' + store.state.user.token
                },
                success(resp) {
                    bots.value = resp.data
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }
        
        // 从云端拉取bot列表
        refresh_bots();

        return {
            match_info,
            bots,
            select_bot,
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
div.user-select-bot {
    padding-top: 20vh;
}
div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}

</style>