<template>
  <div class="detail-page page-enter" v-if="detail.id">
    <section class="detail-hero">
      <div class="detail-main">
        <div class="detail-badge">物品详情</div>
        <h1>{{ detail.title }}</h1>
        <p class="detail-desc">
          {{ detail.description || '发布者暂未填写更多物品描述。' }}
        </p>

        <div class="detail-meta">
          <span class="meta-pill">分类：{{ detail.categoryName || '未分类' }}</span>
          <span class="meta-pill">发布者：{{ detail.ownerNickname || '未知用户' }}</span>
          <span class="meta-pill">新旧程度：{{ detail.conditionLevel || '-' }}/10</span>
          <span :class="getDetailStatusClass(detail.status)">
    状态：{{ getDetailStatusText(detail.status) }}
  </span>
        </div>
      </div>

      <div class="detail-side-card">
        <div class="side-title">交换提示</div>
        <ul>
          <li>确认物品状况后再发起申请</li>
          <li>尽量清楚说明可提供的交换物</li>
          <li>建议在线下安全地点完成交换</li>
        </ul>
      </div>
    </section>

    <div class="detail-content">
      <el-card class="soft-card gallery-card" shadow="never">
        <div class="card-head">
          <h3>物品展示</h3>
          <span>共 {{ detail.images?.length || 0 }} 张图片</span>
        </div>

        <el-carousel
            height="360px"
            v-if="detail.images?.length"
            indicator-position="outside"
            :autoplay="true"
            :interval="4000"
            arrow="always"
            class="detail-carousel"
        >
          <el-carousel-item v-for="img in detail.images" :key="img.id">
            <div class="carousel-item-wrap">
              <el-image :src="img.imageUrl" fit="contain" class="detail-image" />
            </div>
          </el-carousel-item>
        </el-carousel>

        <div v-else class="empty-gallery">
          暂无图片展示
        </div>
      </el-card>

      <el-card class="soft-card apply-card" shadow="never">
        <div class="card-head apply-head">
          <div>
            <h3>发起交换申请</h3>
            <p>填写留言与可提供的交换物信息，便于对方快速了解你的意向。</p>
          </div>
        </div>

        <el-form :model="apply" label-position="top">
          <el-form-item label="申请留言">
            <el-input
                v-model="apply.message"
                type="textarea"
                :rows="4"
                placeholder="例如：我对这件物品很感兴趣，希望进一步了解使用情况。"
            />
          </el-form-item>

          <el-form-item label="我可提供的交换物">
            <el-input
                v-model="apply.offeredItemDesc"
                placeholder="例如：九成新台灯 / 二手教材 / 宿舍收纳架"
            />
          </el-form-item>

          <el-button type="primary" class="submit-btn" @click="applyExchange">
            发起交换申请
          </el-button>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage} from 'element-plus'
import {itemDetailApi} from '../../api/item'
import {exchangeCreateApi} from '../../api/exchange'

const route = useRoute()
const detail = ref({})
const apply = ref({
  message: '',
  offeredItemDesc: ''
})

const load = async () => {
  detail.value = await itemDetailApi(route.params.id)
}

const applyExchange = async () => {
  await exchangeCreateApi({
    itemId: Number(route.params.id),
    ...apply.value
  })
  ElMessage.success('申请已发起')
  apply.value.message = ''
  apply.value.offeredItemDesc = ''
}

onMounted(load)
const getDetailStatusText = (status) => {
  if (status === 1) return '待交换'
  if (status === 3) return '交换中'
  if (status === 2) return '已下架'
  return '未知状态'
}

const getDetailStatusClass = (status) => {
  if (status === 1) return 'meta-pill status-waiting'
  if (status === 3) return 'meta-pill status-doing'
  if (status === 2) return 'meta-pill status-off'
  return 'meta-pill'
}
</script>

<style scoped>
.detail-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.detail-hero {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.detail-badge {
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

.detail-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.detail-desc {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 22px;
}

.meta-pill {
  padding: 10px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: #537a71;
  font-size: 13px;
  box-shadow: 0 10px 24px rgba(137, 174, 163, 0.08);
}

.detail-side-card {
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

.detail-side-card ul {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}

.detail-content {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 22px;
}

.gallery-card,
.apply-card {
  border-radius: 24px;
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.card-head h3 {
  margin: 0;
  font-size: 20px;
  color: #385f56;
}

.card-head span {
  font-size: 13px;
  color: #839c95;
}

.apply-head {
  align-items: flex-start;
}

.apply-head p {
  margin: 8px 0 0;
  color: #7d9992;
  font-size: 14px;
  line-height: 1.8;
}

.detail-carousel {
  border-radius: 20px;
  overflow: hidden;
}

.carousel-item-wrap {
  width: 100%;
  height: 360px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f7fcfa, #eef8ff);
}

.detail-image {
  width: 100%;
  height: 360px;
}

.empty-gallery {
  height: 260px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f6fcf9;
  color: #91a8a1;
  font-size: 14px;
}

.apply-card :deep(.el-form-item__label) {
  color: #5f7e76;
  font-weight: 600;
}

.apply-card :deep(.el-input__wrapper),
.apply-card :deep(.el-textarea__inner) {
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(199, 232, 223, 0.95) inset;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border-radius: 14px;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .detail-hero,
  .detail-content {
    grid-template-columns: 1fr;
  }

  .detail-main h1 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .detail-hero {
    padding: 22px;
  }

  .detail-main h1 {
    font-size: 24px;
  }

  .detail-desc {
    font-size: 14px;
  }
}
.status-waiting {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.status-doing {
  background: #fff3e6;
  color: #b26a00;
  border: 1px solid #ffd8a8;
}

.status-off {
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #d1d5db;
}
</style>