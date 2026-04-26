<template>
  <div class="edit-page page-enter">
    <section class="edit-hero">
      <div class="edit-hero-main">
        <div class="edit-badge">编辑物品</div>
        <h1>修改已发布物品信息</h1>
        <p>
          你可以在这里更新标题、描述、交换意向、地点、联系方式和图片信息，
          保存后会同步更新到平台展示页面。
        </p>
      </div>

      <div class="edit-tips">
        <div class="tips-title">编辑建议</div>
        <ul>
          <li>标题尽量简洁明确，便于其他用户快速理解</li>
          <li>描述中补充使用情况和细节，提升交换成功率</li>
          <li>如物品图片不合适，可重新上传更清晰的图片</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card edit-card" shadow="never">
      <div class="card-head">
        <h3 class="section-title">物品信息编辑</h3>
        <div class="muted">请确认修改内容后再保存</div>
      </div>

      <el-form :model="form" label-position="top" class="edit-form">
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
              placeholder="请输入物品描述"
          />
        </el-form-item>

        <div class="form-grid">
          <el-form-item label="期望交换">
            <el-input v-model="form.expectedItemDesc" placeholder="请输入期望交换内容" />
          </el-form-item>

          <el-form-item label="交易地点">
            <el-input v-model="form.campusLocation" placeholder="请输入交易地点" />
          </el-form-item>
        </div>

        <el-form-item label="联系信息">
          <el-input v-model="form.contactInfo" placeholder="请输入联系方式" />
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

        <div class="action-row">
          <el-button @click="router.push('/my-items')">返回</el-button>
          <el-button type="primary" class="submit-btn" @click="submit">保存修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { categoryListApi } from '../../api/category'
import { itemDetailApi, itemUpdateApi, uploadFileApi } from '../../api/item'

const route = useRoute()
const router = useRouter()

const categories = ref([])

const form = reactive({
  categoryId: null,
  title: '',
  description: '',
  conditionLevel: 3,
  expectedItemDesc: '',
  campusLocation: '',
  contactInfo: '',
  imageUrls: []
})

const loadDetail = async () => {
  const d = await itemDetailApi(route.params.id)
  form.categoryId = d.categoryId
  form.title = d.title
  form.description = d.description
  form.conditionLevel = d.conditionLevel
  form.expectedItemDesc = d.expectedItemDesc
  form.campusLocation = d.campusLocation
  form.contactInfo = d.contactInfo
  form.imageUrls = (d.images || []).map(i => i.imageUrl)
}

const handleFile = async (file) => {
  const r = await uploadFileApi(file.raw)
  form.imageUrls.push(r.url)
}

const submit = async () => {
  await itemUpdateApi(route.params.id, {
    categoryId: form.categoryId,
    title: form.title,
    description: form.description,
    conditionLevel: form.conditionLevel,
    expectedItemDesc: form.expectedItemDesc,
    campusLocation: form.campusLocation,
    contactInfo: form.contactInfo,
    imageUrls: form.imageUrls
  })
  ElMessage.success('修改成功')
  router.push('/my-items')
}

onMounted(async () => {
  categories.value = await categoryListApi()
  await loadDetail()
})
</script>

<style scoped>
.edit-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.edit-hero {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.edit-badge {
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

.edit-hero-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.edit-hero-main p {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.edit-tips {
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

.edit-tips ul {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}

.edit-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 16px;
}

.edit-form :deep(.el-form-item__label) {
  color: #5f7e76;
  font-weight: 600;
}

.edit-form :deep(.el-input__wrapper),
.edit-form :deep(.el-textarea__inner),
.edit-form :deep(.el-select__wrapper) {
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

.action-row {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}

.submit-btn {
  min-width: 120px;
  height: 44px;
  border-radius: 14px;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .edit-hero {
    grid-template-columns: 1fr;
  }

  .edit-hero-main h1 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .edit-hero {
    padding: 22px;
  }

  .edit-hero-main h1 {
    font-size: 24px;
  }

  .edit-hero-main p {
    font-size: 14px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .action-row {
    justify-content: stretch;
    flex-direction: column;
  }
}
</style>