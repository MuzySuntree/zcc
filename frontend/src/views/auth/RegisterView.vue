<template>
  <div class="auth-wrap page-enter">
    <el-card class="soft-card auth-card">
      <h2 class="section-title">注册账号</h2>
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input type="password" show-password v-model="form.password" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-space>
          <el-button type="primary" @click="submit">提交</el-button>
          <el-button link @click="router.push('/login')">去登录</el-button>
        </el-space>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { registerApi } from '../../api/auth'
const router = useRouter(); const form = ref({ username: '', password: '', nickname: '', phone: '', email: '' })
const submit = async () => { await registerApi(form.value); ElMessage.success('注册成功，请登录'); router.push('/login') }
</script>
<style scoped>
.auth-wrap { min-height: 100vh; display: flex; align-items: center; justify-content: center; }
.auth-card { width: min(520px, 94vw); }
</style>
