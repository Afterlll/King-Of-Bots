import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RankListIndexView from '../views/ranklist/RankListIndexView.vue'
import RecodeIndexView from '../views/record/RecodeIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import NotFound from '../views/error/NotFound.vue'

const routes = [
  // 根路径默认跳转到/pk界面
  {
    path : '/',
    name : 'home',
    redirect : '/pk'
  },
  {
    path: '/pk',
    name: 'pk_index',
    component: PkIndexView
  },
  {
    path : '/ranklist',
    name : 'ranklist_index',
    component : RankListIndexView
  },
  {
    path : '/recode',
    name : 'recode_index',
    component : RecodeIndexView
  },
  {
    path : '/user/bot',
    name : 'user_bot_index',
    component : UserBotIndexView
  },
  {
    path : '/404',
    name : 'not_found_index',
    component : NotFound
  },
  // 其他没有匹配的界面跳转到404界面
  {
    path : '/:catchAll(.*)',
    redirect : '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
