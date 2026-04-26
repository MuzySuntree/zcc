<template>
  <div class="carousel-panel">
    <el-carousel height="420px" indicator-position="outside" :interval="4000" arrow="always">
      <el-carousel-item v-for="item in banners" :key="item.title">
        <div class="banner-item" :style="{ background: item.bg }">
          <div class="banner-mask">
            <div class="banner-content">
              <div class="tag">{{ item.tag }}</div>
              <h2>{{ item.title }}</h2>
              <p>{{ item.desc }}</p>

              <div class="feature-list">
                <div class="feature-card" v-for="feature in item.features" :key="feature">
                  {{ feature }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
const banners = [
  {
    tag: '校园闲置交换',
    title: '让每一件闲置，都找到下一位主人',
    desc: '教材、数码、生活用品、宿舍好物，在这里更高效地流转，让交换变得简单、安心、有温度。',
    bg: 'linear-gradient(135deg, #dff7f2 0%, #e8f6ff 50%, #f7fbef 100%)',
    features: ['好物循环利用', '同校交易更安心', '闲置发布更方便']
  },
  {
    tag: '绿色与便捷',
    title: '二手交换，不只是省钱，更是轻生活',
    desc: '通过清爽直观的平台设计，快速浏览、发布与交换，让校园闲置流通更轻松。',
    bg: 'linear-gradient(135deg, #e8fff3 0%, #eef9ff 55%, #fff7e8 100%)',
    features: ['发布流程清晰', '交换记录可追踪', '个人中心统一管理']
  },
  {
    tag: '平台特色',
    title: '在熟悉的校园环境里，完成放心交换',
    desc: '围绕学生场景设计，支持物品展示、交换申请、记录管理与个人主页展示。',
    bg: 'linear-gradient(135deg, #edf8ff 0%, #f2fff3 45%, #fff3f7 100%)',
    features: ['校园场景适配', '页面风格清新', '操作体验轻量']
  }
]
</script>

<style scoped>
.carousel-panel {
  width: 100%;
}

.banner-item {
  height: 420px;
  border-radius: 28px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 16px 40px rgba(109, 170, 155, 0.12);
}

.banner-mask {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: stretch;
  background:
      radial-gradient(circle at top right, rgba(255,255,255,0.75), transparent 30%),
      radial-gradient(circle at bottom left, rgba(255,255,255,0.55), transparent 28%);
}

.banner-content {
  padding: 48px 42px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #355b52;
  width: 100%;
}

.tag {
  display: inline-flex;
  align-items: center;
  width: fit-content;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.7);
  color: #4d8d7d;
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 18px;
  box-shadow: 0 8px 18px rgba(120, 170, 155, 0.08);
}

.banner-content h2 {
  margin: 0 0 18px;
  font-size: 34px;
  line-height: 1.35;
  color: #2f5a50;
}

.banner-content p {
  margin: 0;
  max-width: 520px;
  font-size: 15px;
  line-height: 1.9;
  color: #53766e;
}

.feature-list {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 28px;
}

.feature-card {
  padding: 12px 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.72);
  color: #4e776d;
  font-size: 14px;
  box-shadow: 0 10px 24px rgba(132, 172, 160, 0.08);
}

:deep(.el-carousel__indicator--horizontal .el-carousel__button) {
  width: 24px;
  height: 4px;
  border-radius: 999px;
  background-color: #bfded3;
}

@media (max-width: 992px) {
  .banner-item {
    height: 320px;
  }

  .banner-content {
    padding: 32px 26px;
  }

  .banner-content h2 {
    font-size: 26px;
  }

  .banner-content p {
    font-size: 14px;
    line-height: 1.75;
  }
}
</style>