<template>
  <el-card class="soft-card page-enter">
    <h3 class="section-title">个人中心</h3>
    <el-form :model="profile" label-width="100px" style="max-width: 560px">
      <el-form-item label="昵称"><el-input v-model="profile.nickname" /></el-form-item>
      <el-form-item label="真实姓名"><el-input v-model="profile.realName" /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="profile.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="profile.email" /></el-form-item>
      <el-button type="primary" @click="saveProfile">保存资料</el-button>
    </el-form>
    <el-divider />
    <el-form :model="pwd" label-width="100px" style="max-width: 560px">
      <el-form-item label="旧密码"><el-input type="password" v-model="pwd.oldPassword" /></el-form-item>
      <el-form-item label="新密码"><el-input type="password" v-model="pwd.newPassword" /></el-form-item>
      <el-button type="warning" @click="savePwd">修改密码</el-button>
    </el-form>
  </el-card>
</template>
<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'
import { changePasswordApi, updateProfileApi } from '../../api/user'

const store = useUserStore()
const profile = reactive({ ...(store.userInfo || {}) })
const pwd = reactive({ oldPassword: '', newPassword: '' })
const saveProfile = async () => {
  const u = await updateProfileApi(profile)
  store.userInfo = u
  ElMessage.success('资料已保存')
}
const savePwd = async () => { await changePasswordApi(pwd); ElMessage.success('密码已更新') }
</script>
