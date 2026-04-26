<template>
  <div class="admin-dashboard page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Admin Dashboard</div>
        <h1>管理员后台首页</h1>
        <p>
          这里主要用于处理平台里的分类、物品、交换记录和用户信息。
          进入对应模块后，可以直接进行查看和维护。
        </p>

        <div class="hero-tags">
          <span>分类维护</span>
          <span>物品查看</span>
          <span>交换记录</span>
          <span>用户管理</span>
        </div>
      </div>

      <div class="hero-side-card">
        <div class="side-title">后台说明</div>
        <ul>
          <li>分类管理里可以维护前台物品分类</li>
          <li>物品管理里可以查看平台发布内容</li>
          <li>交换记录里可以查看平台流转情况</li>
          <li>用户管理里可以维护账号信息</li>
        </ul>
      </div>
    </section>



    <section class="shortcut-grid">
      <el-card class="soft-card shortcut-card" shadow="never" @click="go('/admin/categories')">
        <div class="shortcut-icon">🗂️</div>
        <div class="shortcut-title">分类管理</div>
        <div class="shortcut-desc">维护前台发布时使用的分类信息。</div>
        <div class="shortcut-link">进入模块</div>
      </el-card>

      <el-card class="soft-card shortcut-card" shadow="never" @click="go('/admin/items')">
        <div class="shortcut-icon">📦</div>
        <div class="shortcut-title">物品管理</div>
        <div class="shortcut-desc">查看平台里的物品，并处理异常内容。</div>
        <div class="shortcut-link">进入模块</div>
      </el-card>

      <el-card class="soft-card shortcut-card" shadow="never" @click="go('/admin/exchange-records')">
        <div class="shortcut-icon">🔄</div>
        <div class="shortcut-title">交换记录</div>
        <div class="shortcut-desc">查看申请处理结果和交换流转记录。</div>
        <div class="shortcut-link">进入模块</div>
      </el-card>

      <el-card class="soft-card shortcut-card" shadow="never" @click="go('/admin/users')">
        <div class="shortcut-icon">👤</div>
        <div class="shortcut-title">用户管理</div>
        <div class="shortcut-desc">查看用户列表，后面可继续补充新增和编辑功能。</div>
        <div class="shortcut-link">进入模块</div>
      </el-card>
    </section>

    <section class="bottom-grid">
      <el-card class="soft-card info-card" shadow="never">
        <h3 class="section-title">页面说明</h3>
        <div class="info-paragraph">
          后台首页主要起到集中入口的作用，方便管理员直接进入各个管理页面。
        </div>
        <div class="info-paragraph">
          如果后面还要增加审核、公告或统计页面，也可以继续从这里进入。
        </div>
      </el-card>

      <el-card class="soft-card notice-card" shadow="never">
        <h3 class="section-title">使用建议</h3>
        <ul class="notice-list">
          <li>先处理用户和分类，再处理物品内容</li>
          <li>查看交换记录时注意前后状态是否一致</li>
          <li>删除或修改数据前先确认对应信息</li>
          <li>后台操作尽量保持简洁，便于后续维护</li>
        </ul>
      </el-card>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const go = (path) => {
  router.push(path)
}
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.hero-section {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.hero-badge {
  display: inline-flex;
  width: fit-content;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(122, 202, 176, 0.14);
  color: #55a08c;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 16px;
}

.hero-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.hero-main p {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 22px;
}

.hero-tags span {
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: #537a71;
  font-size: 13px;
  box-shadow: 0 10px 24px rgba(137, 174, 163, 0.08);
}

.hero-side-card {
  padding: 22px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: inset 0 0 0 1px rgba(219, 240, 233, 0.9);
}

.side-title {
  font-size: 18px;
  font-weight: 700;
  color: #41695f;
  margin-bottom: 16px;
}

.hero-side-card ul {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}


.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
}

.shortcut-card {
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.25s ease;
}

.shortcut-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 16px 30px rgba(121, 176, 160, 0.14);
}

.shortcut-icon {
  width: 52px;
  height: 52px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #dff7ee, #e8f3ff);
  font-size: 24px;
  margin-bottom: 16px;
}

.shortcut-title {
  font-size: 20px;
  font-weight: 800;
  color: #355d53;
  margin-bottom: 10px;
}

.shortcut-desc {
  color: #6f8d85;
  font-size: 14px;
  line-height: 1.9;
  min-height: 52px;
}

.shortcut-link {
  margin-top: 16px;
  color: #4d8f7d;
  font-size: 14px;
  font-weight: 700;
}

.bottom-grid {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 18px;
}

.info-card,
.notice-card {
  border-radius: 24px;
}

.section-title {
  margin: 0 0 16px;
  font-size: 20px;
  color: #375e55;
}

.info-paragraph {
  color: #6f8d85;
  font-size: 14px;
  line-height: 2;
  margin-bottom: 10px;
}

.notice-list {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}

@media (max-width: 1200px) {


  .shortcut-grid {
    grid-template-columns: 1fr;
  }

  .bottom-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 1100px) {
  .hero-section {
    grid-template-columns: 1fr;
  }

  .hero-main h1 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 22px;
  }

  .hero-main h1 {
    font-size: 24px;
  }

  .hero-main p {
    font-size: 14px;
  }

}
</style>