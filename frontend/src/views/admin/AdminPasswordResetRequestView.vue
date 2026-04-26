<template>
  <div class="admin-password-reset page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Password Reset Review</div>
        <h1>密码重置申请</h1>
        <p>
          在这里查看未登录用户提交的密码重置申请，
          管理员处理后，系统会把对应用户密码重置为默认值 12345678。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">处理说明</div>
        <ul>
          <li>先核对用户名、手机号和邮箱信息</li>
          <li>确认申请合理后再执行密码重置</li>
          <li>重置后用户可用默认密码重新登录</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card table-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">申请列表</h3>
          <div class="muted">当前展示 {{ list.length }} 条申请</div>
        </div>
      </div>

      <el-table :data="list" empty-text="暂无密码重置申请">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="reason" label="申请说明" min-width="220" />

        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <span :class="row.status === 1 ? 'status-pill waiting' : 'status-pill done'">
              {{ row.status === 1 ? '待处理' : '已处理' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 1">
              <el-button link class="approve-link-btn" @click="reset(row.id)">重置密码</el-button>
            </template>
            <template v-else>
              <span class="muted">已完成</span>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminHandlePasswordResetApi, adminPasswordResetListApi } from '../../api/passwordReset'

const list = ref([])

const load = async () => {
  list.value = await adminPasswordResetListApi()
}

const reset = async (id) => {
  await adminHandlePasswordResetApi(id)
  ElMessage.success('密码已重置为 12345678')
  await load()
}

onMounted(load)
</script>

<style scoped>
.admin-password-reset {
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

.table-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 18px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 700;
}

.status-pill.waiting {
  background: #fff3e6;
  color: #b26a00;
  border: 1px solid #ffd8a8;
}

.status-pill.done {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.approve-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
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