<template>
  <el-card>
    <h3>物品管理</h3>
    <el-table :data="list.records">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="ownerNickname" label="发布者" width="120"/>
      <el-table-column prop="status" label="状态" width="100"/>
      <el-table-column label="操作" width="120"><template #default="{row}"><el-button link type="danger" @click="remove(row.id)">删除</el-button></template></el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { itemDeleteApi, itemPageApi } from '../../api/item'

const list = ref({ records: [] })
const load = async () => { list.value = await itemPageApi({ pageNum: 1, pageSize: 30, status: 1 }) }
const remove = async (id) => { await itemDeleteApi(id); ElMessage.success('已删除'); load() }
onMounted(load)
</script>
