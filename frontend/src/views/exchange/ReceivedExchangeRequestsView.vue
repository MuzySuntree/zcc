<template>
  <el-card>
    <h3>我收到的申请</h3>
    <el-table :data="list.records">
      <el-table-column prop="itemTitle" label="物品" />
      <el-table-column prop="fromUserNickname" label="申请人" width="120" />
      <el-table-column prop="offeredItemDesc" label="交换物" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <template v-if="row.status===1">
            <el-button link type="primary" @click="handle(row.id,'AGREE')">同意</el-button>
            <el-button link type="danger" @click="handle(row.id,'REJECT')">拒绝</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { exchangeHandleApi, exchangeReceivedApi } from '../../api/exchange'

const list = ref({ records: [] })
const load = async () => { list.value = await exchangeReceivedApi({ pageNum: 1, pageSize: 20 }) }
const handle = async (id, action) => {
  await exchangeHandleApi(id, { action, exchangeLocation: '线下约定', note: '前端操作' })
  ElMessage.success('处理成功')
  load()
}
onMounted(load)
</script>
