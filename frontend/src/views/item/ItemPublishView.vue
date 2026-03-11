<template>
  <el-card class="soft-card page-enter">
    <h3 class="section-title">发布闲置物品</h3>
    <el-form :model="form" label-width="120px">
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" style="width:220px"><el-option v-for="c in categories" :key="c.id" :value="c.id" :label="c.categoryName"/></el-select>
      </el-form-item>
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="描述"><el-input type="textarea" v-model="form.description" /></el-form-item>
      <el-form-item label="新旧程度"><el-input-number v-model="form.conditionLevel" :min="1" :max="5" /></el-form-item>
      <el-form-item label="期望交换"><el-input v-model="form.expectedItemDesc" /></el-form-item>
      <el-form-item label="交易地点"><el-input v-model="form.campusLocation" /></el-form-item>
      <el-form-item label="联系信息"><el-input v-model="form.contactInfo" /></el-form-item>
      <el-form-item label="图片上传">
        <el-upload :auto-upload="false" :on-change="handleFile" :show-file-list="false"><el-button>选择图片</el-button></el-upload>
        <div style="margin-left:12px">已上传 {{ form.imageUrls.length }} 张</div>
      </el-form-item>
      <el-button type="primary" @click="submit">发布</el-button>
    </el-form>
  </el-card>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryListApi } from '../../api/category'
import { itemCreateApi, uploadFileApi } from '../../api/item'

const categories = ref([])
const form = reactive({ categoryId: null, title: '', description: '', conditionLevel: 3, expectedItemDesc: '', campusLocation: '', contactInfo: '', imageUrls: [] })
const handleFile = async (file) => {
  const r = await uploadFileApi(file.raw)
  form.imageUrls.push(r.url)
}
const submit = async () => { await itemCreateApi(form); ElMessage.success('发布成功') }
onMounted(async () => { categories.value = await categoryListApi() })
</script>
