<template>
  <el-card v-if="detail.id">
    <h2>{{ detail.title }}</h2>
    <div>分类：{{ detail.categoryName }} ｜ 发布者：{{ detail.ownerNickname }}</div>
    <p style="margin:12px 0">{{ detail.description }}</p>
    <el-carousel height="260px" v-if="detail.images?.length">
      <el-carousel-item v-for="img in detail.images" :key="img.id">
        <el-image :src="img.imageUrl" fit="contain" style="width:100%;height:260px" />
      </el-carousel-item>
    </el-carousel>
    <el-divider />
    <el-input v-model="apply.message" placeholder="申请留言" />
    <el-input v-model="apply.offeredItemDesc" placeholder="我可提供的交换物" style="margin-top:8px" />
    <el-button type="primary" style="margin-top:8px" @click="applyExchange">发起交换申请</el-button>
  </el-card>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { itemDetailApi } from '../../api/item'
import { exchangeCreateApi } from '../../api/exchange'

const route = useRoute()
const detail = ref({})
const apply = ref({ message: '', offeredItemDesc: '' })
const load = async () => { detail.value = await itemDetailApi(route.params.id) }
const applyExchange = async () => {
  await exchangeCreateApi({ itemId: Number(route.params.id), ...apply.value })
  ElMessage.success('申请已发起')
}
onMounted(load)
</script>
