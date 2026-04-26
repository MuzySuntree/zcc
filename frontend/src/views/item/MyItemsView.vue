<template>
  <div class="my-items-page page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">我的物品</div>
        <h1>管理我发布的闲置物品</h1>
        <p>
          在这里查看、编辑和删除自己发布的物品信息，
          让你的闲置展示始终保持清晰、完整。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">使用建议</div>
        <ul>
          <li>及时更新标题和描述，提升展示效果</li>
          <li>不再交换的物品建议及时删除</li>
          <li>信息越完整，越容易收到交换申请</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card list-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">我的物品列表</h3>
          <div class="muted">共 {{ list.records?.length || 0 }} 件物品</div>
        </div>
      </div>

      <el-table :data="list.records" empty-text="你还没有发布任何物品">
        <el-table-column label="封面" width="110">
          <template #default="{ row }">
            <div class="cover-wrap">
              <el-image
                  v-if="row.coverImage"
                  :src="row.coverImage"
                  fit="cover"
                  class="cover-image"
              />
              <div v-else class="cover-empty">暂无图片</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="标题" min-width="260">
          <template #default="{ row }">
            <div class="item-title">{{ row.title }}</div>
          </template>
        </el-table-column>

        <el-table-column label="分类" width="150">
          <template #default="{ row }">
            <span class="category-pill">{{ row.categoryName || '未分类' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="140">
          <template #default="{ row }">
    <span :class="getStatusClass(row.status)">
      {{ getStatusText(row.status) }}
    </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link class="edit-link-btn" @click="edit(row)">编辑</el-button>
            <el-button link class="delete-link-btn" @click="remove(row.id, row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { itemDeleteApi, itemMyPageApi } from '../../api/item'

const router = useRouter()
const list = ref({ records: [] })

const load = async () => {
  list.value = await itemMyPageApi({ pageNum: 1, pageSize: 50 })
}

const remove = async (id, row) => {
  if (row.status === 3) {
    ElMessage.warning('已同意交换，无法删除！')
    return
  }

  await ElMessageBox.confirm('确认删除该物品?')
  await itemDeleteApi(id)
  ElMessage.success('删除成功')
  load()
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
  return 'status-pill waiting'
}

const edit = (row) => {
  if (row.status === 3) {
    ElMessage.warning('已同意交换，无法进行编辑！')
    return
  }
  router.push(`/item/edit/${row.id}`)
}

onMounted(load)
</script>

<style scoped>
.my-items-page {
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

.list-card {
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
.cover-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-image {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  object-fit: cover;
  border: 1px solid #d8ece4;
  box-shadow: 0 6px 14px rgba(125, 180, 164, 0.10);
}

.cover-empty {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  background: #f3faf7;
  border: 1px dashed #c9e4da;
  color: #88a39b;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.category-pill {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  background: #dff4ec;
  color: #2f6b5c;
  font-size: 13px;
  font-weight: 700;
  border: 1px solid #bfe3d7;
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
  padding: 6px 12px;
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
.edit-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.edit-link-btn:hover {
  color: #1f5448 !important;
}

.delete-link-btn {
  color: #e35b5b !important;
  font-weight: 700;
}

.delete-link-btn:hover {
  color: #c94040 !important;
}
</style>