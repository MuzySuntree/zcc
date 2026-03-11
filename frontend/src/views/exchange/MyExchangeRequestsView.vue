<template>
  <el-card>
    <h3>我发起的交换申请</h3>
    <el-table :data="list.records">
      <el-table-column prop="itemTitle" label="物品" />
      <el-table-column prop="toUserNickname" label="对方" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button v-if="row.status===1" link type="danger" @click="cancel(row.id)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { exchangeCancelApi, exchangeSentApi } from '../../api/exchange'

const list = ref({ records: [] })
const load = async () => { list.value = await exchangeSentApi({ pageNum: 1, pageSize: 20 }) }
const cancel = async (id) => { await exchangeCancelApi(id); ElMessage.success('已取消'); load() }
onMounted(load)
</script>
