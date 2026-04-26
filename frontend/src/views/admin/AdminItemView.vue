<template>
  <div class="admin-item page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Item Manage</div>
        <h1>物品管理</h1>
        <p>
          管理员可在这里查看平台已发布物品，并对不合适或无效内容进行删除处理。
          当前页面主要用于基础巡检与清理。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">管理说明</div>
        <ul>
          <li>可统一查看平台物品发布情况</li>
          <li>状态展示更便于巡检</li>
          <li>发现异常内容可直接删除</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card table-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">物品列表</h3>
          <div class="muted">当前展示 {{ list.records?.length || 0 }} 条记录</div>
        </div>
      </div>

      <el-table :data="list.records" empty-text="暂无物品数据">
        <el-table-column prop="id" label="ID" width="90" />

        <el-table-column label="标题" min-width="260">
          <template #default="{ row }">
            <div class="item-title">{{ row.title }}</div>
          </template>
        </el-table-column>

        <el-table-column prop="ownerNickname" label="发布者" width="140" />

        <el-table-column label="状态" width="140">
          <template #default="{ row }">
    <span :class="getStatusClass(row.status)">
      {{ getStatusText(row.status) }}
    </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="remove(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { itemDeleteApi, itemPageApi } from '../../api/item'

const list = ref({ records: [] })

const load = async () => {
  list.value = await itemPageApi({
    pageNum: 1,
    pageSize: 30
  })
}

const remove = async (id) => {
  await itemDeleteApi(id)
  ElMessage.success('已删除')
  await load()
}

const statusText = (status) => {
  if (status === 1) return '正常'
  if (status === 0) return '禁用'
  return `状态${status}`
}

const statusTagType = (status) => {
  if (status === 1) return 'success'
  if (status === 0) return 'info'
  return 'warning'
}
const getStatusText = (status) => {
  if (status === 1) return '待交换'
  if (status === 3) return '交换中'
  if (status === 2) return '已下架'
  return '未知状态'
}

const getStatusClass = (status) => {
  if (status === 1) return 'status-pill waiting'
  if (status === 3) return 'status-pill doing'
  if (status === 2) return 'status-pill off'
  return 'status-pill'
}

onMounted(load)
</script>

<style scoped>
.admin-item {
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

.item-title {
  font-size: 15px;
  font-weight: 700;
  color: #365d54;
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
.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 700;
}

.status-pill.waiting {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.status-pill.doing {
  background: #fff3e6;
  color: #b26a00;
  border: 1px solid #ffd8a8;
}

.status-pill.off {
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #d1d5db;
}
</style>