<template>
  <el-container style="height: 100vh">
    <el-aside width="220px">
      <div class="logo">校园闲置交换</div>
      <el-menu :default-active="$route.path" router>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/publish">发布物品</el-menu-item>
        <el-menu-item index="/my-items">我的物品</el-menu-item>
        <el-menu-item index="/exchange/sent">我的申请</el-menu-item>
        <el-menu-item index="/exchange/received">收到的申请</el-menu-item>
        <el-menu-item index="/profile">个人中心</el-menu-item>
        <el-sub-menu index="/admin" v-if="store.isAdmin">
          <template #title>管理员后台</template>
          <el-menu-item index="/admin">后台首页</el-menu-item>
          <el-menu-item index="/admin/categories">分类管理</el-menu-item>
          <el-menu-item index="/admin/items">物品管理</el-menu-item>
          <el-menu-item index="/admin/exchange-records">交换记录</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display:flex;justify-content:space-between;align-items:center">
        <div>{{ store.userInfo?.nickname || '未登录' }}</div>
        <el-button type="danger" link @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const store = useUserStore()
const router = useRouter()

const logout = () => {
  store.logout()
  router.push('/login')
}
</script>

<style scoped>
.logo { padding: 16px; font-weight: bold; }
</style>
