<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>A</th>
                    <th>B</th>
                    <th>对战结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id">
                    <td>
                        <img :src="record.a_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.a_username }}</span>
                    </td>
                    <td>
                        <img :src="record.b_photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ record.b_username }}</span>
                    </td>
                    <td>{{ record.result }}</td>
                    <td>{{ record.record.createTime }}</td>
                    <td>
                        <button @click="open_record_content(record.record.id)" type="button" class="btn btn-secondary">查看录像</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2)">
                <a class="page-link" href="#">前一页</a>
            </li>
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item" @click="click_page(-1)">
                <a class="page-link" href="#">后一页</a>
            </li>
        </ul>
        </nav>
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import $ from 'jquery'
import store from '@/store'
import { ref } from 'vue'
import router from '@/router'
export default {
    components: {
        ContentField
    },
    setup() {

        store.commit("updateLoser", "none")

        let current_page = 1
        let records = ref([])
        let pages = ref([])
        let max_pages = 0

        // 跳转页码
        const click_page = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;

            if (page >= 1 && page <= max_pages) {
                pull_records(page);
            }
        }


        const udpate_pages = () => {
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }


        const pull_records = page => {
            current_page = page
            $.ajax({
                url: "https://app5765.acapp.acwing.com.cn/api/record/getList/",
                type: "get",
                data : {
                    page
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.message === "对战记录返回成功") {
                        console.log(resp);
                        records.value = resp.data.records
                        max_pages = resp.data.page
                        udpate_pages()
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        pull_records(current_page)

        // 将后端传过来的地图（字符串格式）转换为二维数组形式
        const stringTo2D = map => {
            let g = [];
            for (let i = 0, k = 0; i < 13; i ++ ) {
                let line = [];
                for (let j = 0; j < 14; j ++, k ++ ) {
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }

        // 点击录像按钮
        const open_record_content = recordId => {
            for (const record of records.value) { // 遍历当前界面的十条记录，找到我们点击的那个录像
                if (record.record.id === recordId) {
                    store.commit("updateIsRecord", true);
                    store.commit("updateStatus", "playing");
                    store.commit("updateGame", {
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,
                    });
                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    });
                    store.commit("updatePosition", {
                        a_position: record.record.aid,
                        b_position: record.record.bid,
                    });
                    store.commit("updateRecordLoser", record.record.loser);
                    router.push({
                        name: "record_content",
                        params: {
                            recordId
                        }
                    })
                    break;
                }
            }
        }

        return {
            records,
            pages,
            open_record_content,
            click_page,
        }
    }
}
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}

</style>