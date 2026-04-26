<template>
  <div class="admin-category page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Category Manage</div>
        <h1>分类管理</h1>
        <p>
          在这里维护平台物品分类，用于前台发布与筛选展示。
          建议按常用程度设置合理排序，方便用户快速浏览物品。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">维护建议</div>
        <ul>
          <li>分类名称尽量简洁清楚</li>
          <li>排序值越小通常越靠前</li>
          <li>不需要的分类可及时删除</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card form-card" shadow="never">
      <div class="card-head">
        <h3 class="section-title">新增分类</h3>
        <div class="muted">填写分类名称与排序号后即可新增</div>
      </div>

      <div class="form-row">
        <el-input v-model="form.categoryName" placeholder="请输入分类名" class="input-item" />
        <el-input-number v-model="form.sortNo" :min="0" />
        <el-button type="primary" @click="create">新增</el-button>
      </div>
    </el-card>

    <el-card class="soft-card table-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">分类列表</h3>
          <div class="muted">当前共有 {{ list.length || 0 }} 个分类</div>
        </div>
      </div>

      <el-table :data="list" empty-text="暂无分类数据">
        <el-table-column prop="id" label="ID" width="90" />
        <el-table-column prop="categoryName" label="名称" min-width="220" />
        <el-table-column prop="sortNo" label="排序" width="120" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryCreateApi, categoryDeleteApi, categoryListApi } from '../../api/category'

const list = ref([])
const form = reactive({
  categoryName: '',
  sortNo: 0,
  icon: '',
  status: 1
})

const load = async () => {
  list.value = await categoryListApi()
}

const create = async () => {
  await categoryCreateApi(form)
  ElMessage.success('新增成功')
  form.categoryName = ''
  form.sortNo = 0
  await load()
}

const del = async (id) => {
  await categoryDeleteApi(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.admin-category {
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

.form-card,
.table-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 18px;
}

.form-row {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
  align-items: center;
}

.input-item {
  width: 240px;
}

.form-card :deep(.el-input__wrapper),
.form-card :deep(.el-input-number) {
  border-radius: 14px;
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

  .form-row {
    align-items: stretch;
  }

  .input-item {
    width: 100%;
  }
}
</style>