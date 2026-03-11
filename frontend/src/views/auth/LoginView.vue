<template>
  <el-card style="max-width:420px;margin:80px auto">
    <h2>登录</h2>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="用户名" prop="username"><el-input v-model="form.username" /></el-form-item>
      <el-form-item label="密码" prop="password"><el-input type="password" v-model="form.password" /></el-form-item>
      <el-button type="primary" @click="submit">登录</el-button>
      <el-button link @click="router.push('/register')">去注册</el-button>
    </el-form>
  </el-card>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const store = useUserStore()
const formRef = ref()
const form = ref({ username: 'user01', password: 'User@123456' })
const rules = { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }] }
const submit = async () => {
  await formRef.value.validate()
  await store.login(form.value)
  ElMessage.success('登录成功')
  router.push('/')
}
</script>
