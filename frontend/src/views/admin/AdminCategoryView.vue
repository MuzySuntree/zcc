<template>
  <el-card class="soft-card page-enter">
    <h3 class="section-title">分类管理</h3>
    <el-space>
      <el-input v-model="form.categoryName" placeholder="分类名" style="width:160px"/>
      <el-input-number v-model="form.sortNo" :min="0"/>
      <el-button type="primary" @click="create">新增</el-button>
    </el-space>
    <el-table :data="list" style="margin-top:12px">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="categoryName" label="名称"/>
      <el-table-column prop="sortNo" label="排序" width="100"/>
      <el-table-column label="操作" width="120"><template #default="{row}"><el-button link type="danger" @click="del(row.id)">删除</el-button></template></el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { categoryCreateApi, categoryDeleteApi, categoryListApi } from '../../api/category'

const list = ref([])
const form = reactive({ categoryName: '', sortNo: 0, icon: '', status: 1 })
const load = async () => { list.value = await categoryListApi() }
const create = async () => { await categoryCreateApi(form); ElMessage.success('新增成功'); load() }
const del = async (id) => { await categoryDeleteApi(id); ElMessage.success('删除成功'); load() }
onMounted(load)
</script>
