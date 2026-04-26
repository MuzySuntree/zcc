<template>
  <div class="home-page page-enter">
    <section class="hero-section">
      <div class="hero-text">
        <div class="hero-badge">Campus Swap</div>
        <h1>发现校园里的优质闲置好物</h1>
        <p>
          在这里快速浏览教材、数码、宿舍用品与生活好物，
          让交换更轻松，也让闲置继续发挥价值。
        </p>

        <div class="hero-tags">
          <span>校园交换</span>
          <span>绿色循环</span>
          <span>发布便捷</span>
          <span>安全清晰</span>
        </div>
      </div>

      <div class="hero-card">
        <div class="hero-card-title">平台推荐</div>
        <div class="hero-card-list">
          <div class="hero-mini-card">
            <strong>教材书籍</strong>
            <span>期末之后，旧教材也能继续流转</span>
          </div>
          <div class="hero-mini-card">
            <strong>数码配件</strong>
            <span>耳机、键盘、显示器等闲置更容易被看见</span>
          </div>
          <div class="hero-mini-card">
            <strong>宿舍好物</strong>
            <span>台灯、收纳、风扇等物品更适合校内交换</span>
          </div>
        </div>
      </div>
    </section>

    <el-card class="soft-card filter-card" shadow="never">
      <div class="card-title-row">
        <div>
          <h2 class="section-title">物品广场</h2>
          <div class="muted">按关键词与分类快速筛选你想看的闲置物品</div>
        </div>
      </div>

      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="请输入物品标题或关键词" clearable />
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="query.categoryId" clearable style="width: 180px" placeholder="请选择分类">
            <el-option
                v-for="c in categories"
                :key="c.id"
                :label="c.categoryName"
                :value="c.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-space>
            <el-button type="primary" @click="load">查询</el-button>
            <el-button plain @click="resetQuery">重置</el-button>
          </el-space>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="soft-card table-card" shadow="never">
      <div class="table-header">
        <div class="table-header-left">
          <h3>最新物品列表</h3>
          <span>共 {{ list.records?.length || 0 }} 条结果</span>
        </div>
      </div>

      <el-table :data="list.records" class="item-table" empty-text="暂时还没有找到符合条件的物品">
        <el-table-column label="封面" width="110">
          <template #default="{ row }">
            <div class="cover-wrap">
              <el-image
                  :src="row.coverImage"
                  fit="cover"
                  class="cover-image"
              >
                <template #error>
                  <div class="cover-fallback">暂无图片</div>
                </template>
              </el-image>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="物品信息" min-width="280">
          <template #default="{ row }">
            <div class="item-info">
              <div class="item-title">{{ row.title }}</div>
              <div class="item-sub muted">发布者：{{ row.ownerNickname || '未知用户' }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="categoryName" label="分类" width="140">
          <template #default="{ row }">
            <span class="category-pill">{{ row.categoryName || '未分类' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="新旧程度" width="140">
          <template #default="{ row }">
            <span class="condition-pill">{{ row.conditionLevel || '-' }}/10</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="140">
          <template #default="{ row }">
    <span :class="getStatusClass(row.status)">
      {{ getStatusText(row.status) }}
    </span>
          </template>
        </el-table-column>

        <el-table-column prop="ownerNickname" label="发布者" width="140" />

        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button
                link
                class="detail-link-btn"
                @click="router.push(`/item/${row.id}`)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { categoryListApi } from '../../api/category'
import { itemPageApi } from '../../api/item'

const router = useRouter()
const categories = ref([])
const list = ref({ records: [] })
const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  categoryId: undefined
})

const load = async () => {
  list.value = await itemPageApi(query)
}

const resetQuery = async () => {
  query.pageNum = 1
  query.pageSize = 10
  query.keyword = ''
  query.categoryId = undefined
  await load()
}

onMounted(async () => {
  categories.value = await categoryListApi()
  await load()
})
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
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.hero-section {
  display: grid;
  grid-template-columns: 1.2fr 0.9fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background:
      linear-gradient(135deg, rgba(237, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.hero-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
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
  margin-bottom: 18px;
}

.hero-text h1 {
  margin: 0 0 16px;
  font-size: 36px;
  line-height: 1.3;
  color: #2f5c52;
}

.hero-text p {
  margin: 0;
  max-width: 680px;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 24px;
}

.hero-tags span {
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: #537a71;
  font-size: 13px;
  box-shadow: 0 10px 24px rgba(137, 174, 163, 0.08);
}

.hero-card {
  border-radius: 24px;
  padding: 22px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: inset 0 0 0 1px rgba(219, 240, 233, 0.9);
}

.hero-card-title {
  font-size: 18px;
  font-weight: 700;
  color: #3f685f;
  margin-bottom: 18px;
}

.hero-card-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.hero-mini-card {
  padding: 16px 16px;
  border-radius: 18px;
  background: rgba(244, 255, 250, 0.9);
  border: 1px solid rgba(220, 240, 234, 0.95);
}

.hero-mini-card strong {
  display: block;
  color: #446b62;
  margin-bottom: 8px;
  font-size: 15px;
}

.hero-mini-card span {
  color: #75928a;
  font-size: 13px;
  line-height: 1.7;
}

.filter-card,
.table-card {
  border-radius: 24px;
}

.card-title-row {
  margin-bottom: 18px;
}

.filter-form :deep(.el-form-item__label) {
  color: #5f7e76;
  font-weight: 600;
}

.filter-form :deep(.el-input__wrapper),
.filter-form :deep(.el-select__wrapper) {
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(199, 232, 223, 0.95) inset;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.table-header-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.table-header-left h3 {
  margin: 0;
  font-size: 20px;
  color: #385f56;
}

.table-header-left span {
  font-size: 13px;
  color: #839c95;
}

.cover-wrap {
  display: flex;
  justify-content: center;
}

.cover-image {
  width: 68px;
  height: 68px;
  border-radius: 14px;
  overflow: hidden;
  background: #f4fbf8;
  box-shadow: 0 8px 18px rgba(125, 177, 163, 0.1);
}

.cover-fallback {
  width: 68px;
  height: 68px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3faf7;
  color: #91a8a1;
  font-size: 12px;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.item-title {
  font-size: 16px;
  font-weight: 700;
  color: #365d54;
}

.item-sub {
  font-size: 13px;
}

.category-pill {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  background: rgba(122, 202, 176, 0.12);
  color: #52927f;
  font-size: 13px;
  font-weight: 600;
}

.condition-pill {
  display: inline-flex;
  align-items: center;
  padding: 8px 12px;
  border-radius: 999px;
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
  font-size: 13px;
  font-weight: 700;
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

.item-table :deep(.el-table__cell) {
  padding: 16px 0;
}

@media (max-width: 1100px) {
  .hero-section {
    grid-template-columns: 1fr;
  }

  .hero-text h1 {
    font-size: 30px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 22px;
  }

  .hero-text h1 {
    font-size: 26px;
  }

  .hero-text p {
    font-size: 14px;
  }

  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}

.detail-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.detail-link-btn:hover {
  color: #1f5448 !important;
}
</style>