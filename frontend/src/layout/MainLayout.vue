<template>
  <div class="layout-page">
    <div class="layout-bg layout-bg-left"></div>
    <div class="layout-bg layout-bg-right"></div>

    <el-container class="main-shell">
      <el-aside width="248px" class="side-panel">
        <div class="logo-wrap">
          <div class="logo-icon">🎒</div>
          <div>
            <div class="logo-title">校园闲置交换</div>
            <div class="logo-sub">Campus Swap Platform</div>
          </div>
        </div>

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
            <el-menu-item index="/admin/users">用户管理</el-menu-item>
            <el-menu-item index="/admin/profile-change-requests">资料审核</el-menu-item>
            <el-menu-item index="/admin/password-reset">密码重置</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container class="content-shell">
        <el-header class="top-bar soft-card">
          <div class="top-left">
            <div class="welcome-title">
              欢迎回来，{{ store.userInfo?.nickname || '同学' }}
            </div>
            <div class="muted">
              愿你的每一件闲置，都在这里找到新的价值
            </div>
          </div>

          <div class="top-actions">
            <el-button type="danger" plain @click="logout">退出登录</el-button>
          </div>
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
  </div>
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
.layout-page {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background:
      linear-gradient(135deg, #f6fff9 0%, #eefaf6 35%, #eef7ff 72%, #fbfffd 100%);
}

.layout-bg {
  position: absolute;
  border-radius: 50%;
  opacity: 0.42;
  pointer-events: none;
}

.layout-bg-left {
  width: 320px;
  height: 320px;
  background: rgba(182, 235, 214, 0.46);
  top: -100px;
  left: -80px;
}

.layout-bg-right {
  width: 360px;
  height: 360px;
  background: rgba(193, 226, 255, 0.42);
  right: -100px;
  bottom: -100px;
}

.main-shell {
  position: relative;
  z-index: 2;
  height: 100vh;
  padding: 14px;
  gap: 14px;
}

.side-panel {
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 18px 36px rgba(126, 174, 160, 0.12);
  overflow: hidden;
  border: 1px solid rgba(222, 240, 234, 0.88);
  backdrop-filter: blur(8px);
}

.logo-wrap {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 22px 18px 18px;
}

.logo-icon {
  width: 46px;
  height: 46px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #dff7ee, #e8f3ff);
  font-size: 22px;
}

.logo-title {
  font-size: 18px;
  font-weight: 800;
  color: #365f55;
}

.logo-sub {
  margin-top: 4px;
  font-size: 12px;
  color: #85a099;
}

.menu {
  border-right: none;
  background: transparent;
  padding: 8px 10px 18px;
}

.menu :deep(.el-menu-item),
.menu :deep(.el-sub-menu__title) {
  height: 46px;
  line-height: 46px;
  border-radius: 14px;
  margin-bottom: 6px;
  color: #53776e;
}

.menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, rgba(119, 207, 177, 0.16), rgba(115, 185, 220, 0.12));
  color: #3d6b61;
  font-weight: 700;
}

.content-shell {
  min-width: 0;
}

.top-bar {
  margin: 0 0 14px;
  min-height: 78px;
  border-radius: 22px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 22px;
}

.top-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.welcome-title {
  font-size: 22px;
  font-weight: 800;
  color: #355d53;
}

.top-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  padding: 2px;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.35s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

@media (max-width: 992px) {
  .main-shell {
    padding: 10px;
    gap: 10px;
  }

  .top-bar {
    padding: 14px 16px;
    min-height: auto;
    align-items: flex-start;
    flex-direction: column;
    gap: 12px;
  }

  .welcome-title {
    font-size: 18px;
  }
}
</style>