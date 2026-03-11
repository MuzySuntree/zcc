<template>
  <el-card class="soft-card page-enter">
    <h3 class="section-title">我的物品</h3>
    <el-table :data="list.records">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column label="操作" width="220">
        <template #default="{row}">
          <el-button link @click="edit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { itemDeleteApi, itemDetailApi, itemPageApi, itemUpdateApi } from '../../api/item'

const list = ref({ records: [] })
const load = async () => { list.value = await itemPageApi({ pageNum: 1, pageSize: 50 }) }
const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该物品?')
  await itemDeleteApi(id)
  ElMessage.success('删除成功')
  load()
}
const edit = async (row) => {
  const d = await itemDetailApi(row.id)
  await itemUpdateApi(row.id, {
    categoryId: d.categoryId, title: d.title + '（已编辑）', description: d.description,
    conditionLevel: d.conditionLevel, expectedItemDesc: d.expectedItemDesc,
    campusLocation: d.campusLocation, contactInfo: d.contactInfo,
    imageUrls: (d.images || []).map(i => i.imageUrl)
  })
  ElMessage.success('已快速更新标题')
  load()
}
onMounted(load)
</script>
