<!-- 游戏地图背景，通过嵌套关系保证地图的响应式 -->
<template>
    <div class="playground">
        <GameMap />
    </div>
</template>

<script>
import GameMap from './GameMap.vue';
export default {
    components : {
        GameMap
    },
    setup() {
    }
}
</script>

<style scoped>
    div.playground {
        /* background-color: lightblue; */
        width: 60vw;
        height: 70vh;
        margin: 60px auto;
    }
</style>