<template>
  <div class="home-page page-enter">
    <el-card class="soft-card hero-card">
      <el-carousel height="220px" indicator-position="outside" arrow="always" autoplay :interval="4200">
        <el-carousel-item v-for="slide in bannerSlides" :key="slide.title">
          <div class="hero-slide">
            <p class="slide-tag">{{ slide.tag }}</p>
            <h2>{{ slide.title }}</h2>
            <p class="slide-desc">{{ slide.desc }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <el-card class="soft-card market-card">
      <div class="market-header">
        <h3 class="section-title">校园二手交换广场</h3>
        <span class="muted">发现你需要的物品，也让闲置发挥价值</span>
      </div>

      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="关键词"><el-input v-model="query.keyword" placeholder="例如：单车 / 书籍 / 耳机" /></el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.categoryId" clearable style="width: 190px" placeholder="全部分类">
            <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>

      <el-table :data="list.records" class="market-table">
        <el-table-column label="封面" width="90">
          <template #default="{ row }"><el-image :src="row.coverImage" style="width: 60px; height: 60px" /></template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="ownerNickname" label="发布者" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }"><el-button link @click="router.push(`/item/${row.id}`)">详情</el-button></template>
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
const query = reactive({ pageNum: 1, pageSize: 10, keyword: '', categoryId: undefined })
const bannerSlides = [
  {
    tag: '换物专区',
    title: '好物循环，校园生活更轻盈',
    desc: '支持以物换物与闲置出售，宿舍清理、毕业转让一站搞定。'
  },
  {
    tag: '安心交易',
    title: '同校交易更放心',
    desc: '优先面对面交换，沟通透明，交易记录可追踪。'
  },
  {
    tag: '绿色消费',
    title: '让每件闲置都有新故事',
    desc: '书籍、数码、生活用品快速流通，省钱也环保。'
  }
]

const load = async () => { list.value = await itemPageApi(query) }
onMounted(async () => { categories.value = await categoryListApi(); await load() })
</script>
<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.hero-card,
.market-card {
  position: relative;
  overflow: hidden;
}

.hero-card::before,
.market-card::before,
.market-card::after {
  content: '';
  position: absolute;
  border-radius: 999px;
  pointer-events: none;
}

.hero-card::before {
  width: 240px;
  height: 240px;
  top: -130px;
  right: -120px;
  background: radial-gradient(circle, rgba(79, 124, 255, 0.25), rgba(79, 124, 255, 0));
}

.market-card::before {
  width: 180px;
  height: 180px;
  left: -90px;
  bottom: -80px;
  background: radial-gradient(circle, rgba(43, 196, 168, 0.22), rgba(43, 196, 168, 0));
}

.market-card::after {
  inset: 10px;
  border: 1px solid rgba(79, 124, 255, 0.12);
  border-radius: 14px;
}

.hero-slide {
  height: 100%;
  border-radius: 14px;
  padding: 28px 36px;
  background: linear-gradient(120deg, rgba(79, 124, 255, 0.92), rgba(70, 176, 166, 0.9));
  color: #fff;
}

.slide-tag {
  margin: 0;
  display: inline-block;
  padding: 5px 12px;
  border-radius: 99px;
  font-size: 12px;
  letter-spacing: 1px;
  border: 1px solid rgba(255, 255, 255, 0.45);
}

h2 {
  margin: 16px 0 10px;
  font-size: 30px;
}

.slide-desc {
  margin: 0;
  font-size: 15px;
  max-width: 500px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.92);
}

.market-header {
  margin-bottom: 12px;
  position: relative;
  z-index: 1;
}

.market-header .section-title {
  margin-bottom: 8px;
}

.filter-form,
.market-table {
  position: relative;
  z-index: 1;
}
</style>
