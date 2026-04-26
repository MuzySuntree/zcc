<template>
  <div class="admin-exchange page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Exchange Records</div>
        <h1>交换记录管理</h1>
        <p>
          在这里查看平台中的交换记录信息，包括物品、交换双方、确认情况和交换地点。
          方便管理员查看当前交换进度。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">查看重点</div>
        <ul>
          <li>查看记录当前是否还在交换中</li>
          <li>核对双方是否都已经确认完成</li>
          <li>查看实际填写的交换地点</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card table-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">交换记录列表</h3>
          <div class="muted">当前展示 {{ list.records?.length || 0 }} 条记录</div>
        </div>
      </div>

      <el-table :data="list.records" empty-text="暂无交换记录">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="itemTitle" label="物品" min-width="180" />
        <el-table-column prop="ownerNickname" label="拥有者" width="120" />
        <el-table-column prop="requesterNickname" label="申请人" width="120" />

        <el-table-column label="交换地点" min-width="220">
          <template #default="{ row }">
            <div class="location-text">
              {{ formatLocation(row.exchangeLocation) }}
            </div>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <span :class="getStatusClass(row.status)">
              {{ getStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="物主确认" width="110">
          <template #default="{ row }">
            <span :class="getConfirmClass(row.ownerConfirmed)">
              {{ row.ownerConfirmed === 1 ? '已确认' : '未确认' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="申请人确认" width="120">
          <template #default="{ row }">
            <span :class="getConfirmClass(row.requesterConfirmed)">
              {{ row.requesterConfirmed === 1 ? '已确认' : '未确认' }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { exchangeMyRecordsApi } from '../../api/exchange'

const list = ref({ records: [] })

const load = async () => {
  list.value = await exchangeMyRecordsApi({ pageNum: 1, pageSize: 20 })
}

const getStatusText = (status) => {
  if (status === 1) return '交换中'
  if (status === 2) return '已完成'
  if (status === 3) return '已取消'
  return '未知状态'
}

const getStatusClass = (status) => {
  if (status === 1) return 'status-pill doing'
  if (status === 2) return 'status-pill done'
  if (status === 3) return 'status-pill cancel'
  return 'status-pill'
}

const getConfirmClass = (status) => {
  return status === 1 ? 'confirm-pill yes' : 'confirm-pill no'
}

const formatLocation = (location) => {
  if (!location || !location.trim()) return '未填写具体地点'
  return location
}

onMounted(load)
</script>

<style scoped>
.admin-exchange {
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

.location-text {
  color: #4d6b63;
  line-height: 1.7;
}

.status-pill,
.confirm-pill {
  display: inline-flex;
  align-items: center;
  padding: 7px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 700;
}

.status-pill.doing {
  background: #fff3e6;
  color: #b26a00;
  border: 1px solid #ffd8a8;
}

.status-pill.done {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.status-pill.cancel {
  background: #fef0f0;
  color: #d84b4b;
  border: 1px solid #f6c4c4;
}

.confirm-pill.yes {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.confirm-pill.no {
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #d1d5db;
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