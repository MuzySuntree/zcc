<template>
  <el-card>
    <el-form :inline="true" :model="query">
      <el-form-item label="关键词"><el-input v-model="query.keyword" /></el-form-item>
      <el-form-item label="分类">
        <el-select v-model="query.categoryId" clearable style="width:180px">
          <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list.records">
      <el-table-column label="封面" width="90">
        <template #default="{row}"><el-image :src="row.coverImage" style="width:60px;height:60px" /></template>
      </el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column prop="ownerNickname" label="发布者" width="120" />
      <el-table-column label="操作" width="120">
        <template #default="{row}"><el-button link @click="router.push(`/item/${row.id}`)">详情</el-button></template>
      </el-table-column>
    </el-table>
  </el-card>
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

const load = async () => { list.value = await itemPageApi(query) }
onMounted(async () => { categories.value = await categoryListApi(); await load() })
</script>
