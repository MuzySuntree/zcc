<template>
  <el-card>
    <h3>交换记录管理</h3>
    <el-table :data="list.records">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="itemTitle" label="物品"/>
      <el-table-column prop="ownerNickname" label="拥有者" width="120"/>
      <el-table-column prop="requesterNickname" label="申请人" width="120"/>
      <el-table-column prop="exchangeLocation" label="地点"/>
    </el-table>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { exchangeMyRecordsApi } from '../../api/exchange'

const list = ref({ records: [] })
const load = async () => { list.value = await exchangeMyRecordsApi({ pageNum: 1, pageSize: 20 }) }
onMounted(load)
</script>
