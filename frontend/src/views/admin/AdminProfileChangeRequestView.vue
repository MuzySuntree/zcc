<template>
  <div class="admin-profile-change page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Profile Change Review</div>
        <h1>资料审核</h1>
        <p>
          在这里查看用户提交的资料修改申请，
          审核通过后才会正式更新到用户资料中。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">审核说明</div>
        <ul>
          <li>优先核对手机号和邮箱格式</li>
          <li>确认新资料是否符合平台要求</li>
          <li>审核通过后会直接更新正式信息</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card table-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">申请列表</h3>
          <div class="muted">当前展示 {{ list.records?.length || 0 }} 条申请</div>
        </div>
      </div>

      <el-table :data="list.records" empty-text="暂无申请记录">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />

        <el-table-column prop="newNickname" label="昵称" width="120" />
        <el-table-column prop="newRealName" label="真实姓名" width="120" />
        <el-table-column prop="newPhone" label="手机号" width="140" />
        <el-table-column prop="newEmail" label="邮箱" min-width="180" />

        <el-table-column prop="reason" label="申请说明" min-width="180" />

        <el-table-column label="状态" width="120">
          <template #default="{ row }">
      <span :class="getStatusClass(row.status)">
        {{ getStatusText(row.status) }}
      </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 1">
              <el-button link class="approve-link-btn" @click="approve(row.id)">同意修改</el-button>
              <el-button link class="reject-link-btn" @click="reject(row.id)">驳回</el-button>
            </template>
            <template v-else>
              <span class="muted">已处理</span>
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
import {
  adminProfileChangeRequestListApi,
  approveProfileChangeRequestApi,
  rejectProfileChangeRequestApi
} from '../../api/profileChangeRequest'

const list = ref({ records: [] })

const load = async () => {
  list.value = await adminProfileChangeRequestListApi({ pageNum: 1, pageSize: 20 })
}

const approve = async (id) => {
  await approveProfileChangeRequestApi(id)
  ElMessage.success('已通过修改申请')
  await load()
}

const reject = async (id) => {
  await rejectProfileChangeRequestApi(id)
  ElMessage.success('已驳回修改申请')
  await load()
}

const getStatusText = (status) => {
  if (status === 1) return '待审核'
  if (status === 2) return '已通过'
  if (status === 3) return '已驳回'
  return '未知状态'
}

const getStatusClass = (status) => {
  if (status === 1) return 'status-pill waiting'
  if (status === 2) return 'status-pill done'
  if (status === 3) return 'status-pill reject'
  return 'status-pill'
}

onMounted(load)
</script>

<style scoped>
.admin-profile-change {
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

.status-pill.reject {
  background: #fef0f0;
  color: #d84b4b;
  border: 1px solid #f6c4c4;
}

.approve-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.reject-link-btn {
  color: #d84b4b !important;
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