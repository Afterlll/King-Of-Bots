<template>
    <div class="playground">
        <GameMap v-if="$store.state.pk.status === 'playing'" />
        <MatchMap v-if="$store.state.pk.status === 'matching'" />
        <RecordBoard v-if="$store.state.pk.loser !== 'none'"/>

        <div class="position" v-if="$store.state.user.position !== 'none' && $store.state.record.is_record === false">
            <div>您的位置 {{ $store.state.user.position }}</div>
        </div>

        <div class="position-record" v-if="$store.state.record.is_record === true">
            玩家A: 左下角<br>
            玩家B: 右上角<br>
        </div>
    </div>
</template>

<script>
import GameMap from './GameMap.vue'
import MatchMap from './MatchMap.vue'
import RecordBoard from './RecordBoard.vue'

export default {
    components: {
        GameMap,
        MatchMap,
        RecordBoard,
    },
    setup() {
    }
}
</script>

<style scoped>
div.playground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
}
div.position {
    position: absolute;
    background-color: rgba(50, 50, 50, 0.5);
    width: 150px;
    height: 100px;
    top: 95px;
    left: 50px;
    z-index: 999;
    color: white;
    font-weight: 600;
    font-size: 32px;
    font-style: italic;
    text-align: center;
    display: flex;
}
div.position-record {
    position: absolute;
    background-color: rgba(50, 50, 50, 0.5);
    width: 110px;
    height: 50px;
    top: 95px;
    left: 50px;
    z-index: 999;
    color: white;
    font-weight: 600;
    font-style: italic;
    text-align: center;
    display: flex;
}
</style>