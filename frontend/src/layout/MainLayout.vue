<template>
  <el-container class="main-shell">
    <el-aside width="240px" class="side-panel">
      <div class="logo">🎒 校园闲置交换</div>
      <el-menu :default-active="$route.path" router class="menu">
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
      <el-header class="top-bar soft-card">
        <div class="muted">欢迎回来，{{ store.userInfo?.nickname || '同学' }}</div>
        <el-button type="danger" plain @click="logout">退出登录</el-button>
      </el-header>
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const store = useUserStore()
const router = useRouter()
const logout = () => { store.logout(); router.push('/login') }
</script>

<style scoped>
.main-shell { height: 100vh; padding: 12px; gap: 12px; }
.side-panel { border-radius: 18px; background: rgba(255,255,255,.86); box-shadow: var(--shadow-main); overflow: hidden; }
.logo { padding: 18px; font-weight: 700; }
.menu { border-right: none; }
.top-bar { margin: 0 0 12px; border-radius: 16px; display: flex; justify-content: space-between; align-items: center; }
.main-content { padding: 4px; }
.fade-slide-enter-active, .fade-slide-leave-active { transition: all .35s ease; }
.fade-slide-enter-from, .fade-slide-leave-to { opacity: 0; transform: translateY(8px); }
</style>
