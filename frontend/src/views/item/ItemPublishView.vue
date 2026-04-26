<template>
  <div class="publish-page page-enter">
    <section class="publish-hero">
      <div class="publish-hero-main">
        <div class="publish-badge">Publish Item</div>
        <h1>发布你的闲置物品</h1>
        <p>
          填写物品基础信息、交换期望与联系方式，
          让更多同学快速看到并发起交换申请。
        </p>
      </div>

      <div class="publish-tips">
        <div class="tips-title">发布建议</div>
        <ul>
          <li>标题尽量简洁明确，例如“九成新台灯”</li>
          <li>描述中写清物品使用情况与细节</li>
          <li>上传清晰图片，更容易被看到</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card publish-card" shadow="never">
      <div class="card-head">
        <h3 class="section-title">物品信息填写</h3>
        <div class="muted">请尽量完善内容，提升物品展示效果</div>
      </div>

      <el-form :model="form" label-position="top" class="publish-form">
        <div class="form-grid">
          <el-form-item label="分类">
            <el-select v-model="form.categoryId" placeholder="请选择分类">
              <el-option
                  v-for="c in categories"
                  :key="c.id"
                  :value="c.id"
                  :label="c.categoryName"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="新旧程度">
            <el-input-number v-model="form.conditionLevel" :min="1" :max="10" />
          </el-form-item>
        </div>

        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入物品标题" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input
              type="textarea"
              v-model="form.description"
              :rows="5"
              placeholder="请输入物品描述，例如使用时长、外观情况、是否有瑕疵等"
          />
        </el-form-item>

        <div class="form-grid">
          <el-form-item label="期望交换">
            <el-input v-model="form.expectedItemDesc" placeholder="例如：希望交换教材、台灯、风扇等" />
          </el-form-item>

          <el-form-item label="交易地点">
            <el-input v-model="form.campusLocation" placeholder="例如：一食堂门口 / 图书馆前" />
          </el-form-item>
        </div>

        <el-form-item label="联系信息">
          <el-input v-model="form.contactInfo" placeholder="请输入手机号、微信号或其他联系方式" />
        </el-form-item>

        <el-form-item label="图片上传">
          <div class="upload-row">
            <el-upload
                :auto-upload="false"
                :on-change="handleFile"
                :show-file-list="false"
            >
              <el-button plain>选择图片</el-button>
            </el-upload>

            <div class="upload-count">
              已上传 <strong>{{ form.imageUrls.length }}</strong> 张
            </div>
          </div>
        </el-form-item>

        <el-button type="primary" class="submit-btn" @click="submit">
          发布物品
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryListApi } from '../../api/category'
import { itemCreateApi, uploadFileApi } from '../../api/item'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()
const categories = ref([])

const form = reactive({
  categoryId: null,
  title: '',
  description: '',
  conditionLevel: 3,
  expectedItemDesc: '',
  campusLocation: '',
  contactInfo: userStore.userInfo?.phone || '',
  imageUrls: []
})

const handleFile = async (file) => {
  const r = await uploadFileApi(file.raw)
  form.imageUrls.push(r.url)
}

const submit = async () => {
  await itemCreateApi(form)
  ElMessage.success('发布成功')

  form.categoryId = null
  form.title = ''
  form.description = ''
  form.conditionLevel = 3
  form.expectedItemDesc = ''
  form.campusLocation = ''
  form.contactInfo = userStore.userInfo?.phone || ''
  form.imageUrls = []
}

onMounted(async () => {
  categories.value = await categoryListApi()
  if (!form.contactInfo) {
    form.contactInfo = userStore.userInfo?.phone || ''
  }
})
</script>

<style scoped>
.publish-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.publish-hero {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background:
      linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.publish-badge {
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

.publish-hero-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.publish-hero-main p {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.publish-tips {
  padding: 22px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: inset 0 0 0 1px rgba(219, 240, 233, 0.9);
}

.tips-title {
  font-size: 18px;
  font-weight: 700;
  color: #41695f;
  margin-bottom: 16px;
}

.publish-tips ul {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}

.publish-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 16px;
}

.publish-form :deep(.el-form-item__label) {
  color: #5f7e76;
  font-weight: 600;
}

.publish-form :deep(.el-input__wrapper),
.publish-form :deep(.el-textarea__inner),
.publish-form :deep(.el-select__wrapper) {
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(199, 232, 223, 0.95) inset;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.upload-row {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
}

.upload-count {
  color: #75918a;
  font-size: 14px;
}

.upload-count strong {
  color: #468173;
  font-weight: 700;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border-radius: 14px;
  font-weight: 600;
  margin-top: 6px;
}

@media (max-width: 1100px) {
  .publish-hero {
    grid-template-columns: 1fr;
  }

  .publish-hero-main h1 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .publish-hero {
    padding: 22px;
  }

  .publish-hero-main h1 {
    font-size: 24px;
  }

  .publish-hero-main p {
    font-size: 14px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>