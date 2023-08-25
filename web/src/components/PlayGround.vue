<template>
    <div class="playground">
        <GameMap v-if="$store.state.pk.status === 'playing'" />
        <MatchMap v-if="$store.state.pk.status === 'matching'" />
        <RecordBoard v-if="$store.state.pk.loser !== 'none'"/>
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
    }
}
</script>

<style scoped>
div.playground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
}
</style>