<!-- 游戏地图 -->
<template>
    <div ref="parent" class="gamemap">
        <!-- tabindex="0" : 让canvas可以接收用户操作 -->
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { onMounted, ref } from 'vue'
// 类是直接export出来的，需要通过 "{}" 引用
import { GameMap } from '../assets/scripts/GameMap.js';
export default {
    setup () {
        let canvas = ref(null)
        let parent = ref(null)

        onMounted(() => {
            // 挂载完毕之后创建地图
            new GameMap(canvas.value.getContext('2d'), parent.value)
        })

        return {
            canvas,
            parent
        }
    }
}
</script>

<style scoped>
    div.gamemap {
        /* 注意：在没有规定子元素的长宽时，默认并没有占据父元素的全部，知识一部分 */
        width: 100%;
        height: 100%;
        /* div居中，竖直居中+水平居中 */
        display: flex;
        justify-content: center;
        align-content: center;
    }
</style>