<template>
    <div class="container" style="margin-top: 20px;">
        <div class="row">
            <div class="col-3">
                <img :src="$store.state.user.photo" alt="头像">
            </div>
            <div class="col-9">
                <div class="card">
                    <div class="card-header">
                        <span style="font-weight : 800; font-size: 24px;">我的bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add_bot">创建bot</button>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>标题</th>
                                    <!-- <th>描述</th> -->
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot of bots" :key="bot.id">
                                    <th>{{ bot.title }}</th>
                                    <th>{{ bot.createTime }}</th>
                                    <!-- <th>{{ bot.description }}</th> -->
                                    <th>
                                        <button style="margin-right: 10px;" type="button" class="btn btn-secondary" data-bs-toggle="modal" :data-bs-target="'#update_bot_btn' + bot.id">修改</button>
                                        <!-- 修改bot模态框 -->
                                        <div class="modal fade" :id="'update_bot_btn' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">修改bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">标题</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot的标题">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">简介</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot的描述"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-code" class="form-label">代码</label>
                                                        <VAceEditor v-model:value="bot.content" @init="editorInit" lang="c_cpp"
                                                            theme="textmate" style="height: 300px" :options="{
                                                                enableBasicAutocompletion: true, //启用基本自动完成
                                                                enableSnippets: true, // 启用代码段
                                                                enableLiveAutocompletion: true, // 启用实时自动完成
                                                                fontSize: 18, //设置字号
                                                                tabSize: 4, // 标签大小
                                                                showPrintMargin: false, //去除编辑器里的竖线
                                                                highlightActiveLine: true,
                                                            }" />
                                                    </div>
                                                </div>
                                                <div class="alert alert-danger" role="alert" v-if="message">
                                                    {{ message }}
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-primary" @click="update_a_bot(bot)">保存修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>

                                        <button style="margin-right: 10px;" type="button" class="btn btn-danger" @click="delete_a_bot(bot.id)">删除</button>
                                    </th>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 创建bot模态框 -->
    <div class="modal fade" id="add_bot" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">创建bot</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="add-bot-title" class="form-label">标题</label>
                    <input v-model="addbot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot的标题">
                </div>
                <div class="mb-3">
                    <label for="add-bot-description" class="form-label">简介</label>
                    <textarea v-model="addbot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot的描述"></textarea>
                </div>
                <div class="mb-3">
                    <label for="add-bot-code" class="form-label">代码</label>
                    <VAceEditor v-model:value="addbot.content" @init="editorInit" lang="c_cpp"
                        theme="textmate" style="height: 300px" :options="{
                            enableBasicAutocompletion: true, //启用基本自动完成
                            enableSnippets: true, // 启用代码段
                            enableLiveAutocompletion: true, // 启用实时自动完成
                            fontSize: 18, //设置字号
                            tabSize: 4, // 标签大小
                            showPrintMargin: false, //去除编辑器里的竖线
                            highlightActiveLine: true,
                        }" />
                </div>
            </div>
            <div class="alert alert-danger" role="alert" v-if="addbot.message">
                {{ addbot.message }}
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" @click="add_a_bot">创建</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            </div>
            </div>
        </div>
    </div>
</template>

<script>
import $ from 'jquery'
import store from '@/store'
import { reactive, ref } from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';
export default {
    components : {
        VAceEditor
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" +
            require("ace-builds").version +
            "/src-noconflict/")

        let message = ref('')
        let bots = ref([])
        let addbot = reactive({
            title : '',
            description : '',
            content : '',
            message : ''
        })

        const refresh_bots = () => {
            $.ajax({
                url : 'http://localhost:3000/user/bot/getlist/',
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
        
        // 拉取bot列表
        refresh_bots();

        // 增加bot
        const add_a_bot = () => {
            $.ajax({
                url : 'http://localhost:3000/user/bot/add/',
                type : 'post',
                headers : {
                    Authorization : 'Bearer ' + store.state.user.token
                },
                data : {
                    title : addbot.title,
                    description : addbot.description,
                    content : addbot.content
                },
                success(resp) {
                    if (resp.message === 'bot新增成功') {
                        addbot.title = ''
                        addbot.description = ''
                        addbot.content = ''
                        addbot.message = ''
                        Modal.getInstance("#add_bot").hide()
                        refresh_bots();
                    } else {
                        addbot.message = resp.message
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            })   
        }

        // 删除bot
        const delete_a_bot = (botId) => {
            if (window.confirm("您确认删除吗？")) {
                $.ajax({
                    url : 'http://localhost:3000/user/bot/remove/',
                    type : 'post',
                    headers : {
                        Authorization : 'Bearer ' + store.state.user.token
                    },
                    data : {
                        bot_id : botId
                    },
                    success(resp) {
                        if (resp.message === 'bot删除成功') {
                            refresh_bots();
                        }
                    },
                    error(resp) {
                        console.log(resp);
                    }
                })
            }
        }

        // 修改bot
        const update_a_bot = (bot) => {
            $.ajax({
                url : 'http://localhost:3000/user/bot/update/',
                type : 'post',
                headers : {
                    Authorization : 'Bearer ' + store.state.user.token
                },
                data : {
                    bot_id : bot.id,
                    title : bot.title,
                    description : bot.description,
                    content : bot.content
                },
                success(resp) {
                    if (resp.message === 'bot修改成功') {
                        Modal.getInstance("#update_bot_btn" + bot.id).hide()
                        refresh_bots();
                    } else {
                        message = resp.message
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        return {
            bots,
            addbot,
            message,
            refresh_bots,
            add_a_bot,
            delete_a_bot,
            update_a_bot
        }
    }
}
</script>

<style scoped>

</style>