<template>
  <div class="auth-page">
    <div class="bg-decoration bg-decoration-left"></div>
    <div class="bg-decoration bg-decoration-right"></div>

    <div class="auth-container">
      <div class="auth-left">
        <div class="brand-box">
          <div class="brand-badge">Campus Swap</div>
          <h1>校园二手交换平台</h1>
          <p>
            在轻松清新的界面中，完成闲置发布、二手浏览与交换申请。
            让旧物继续发光，也让校园生活更高效、更环保。
          </p>
        </div>

        <LoginCarousel />
      </div>

      <div class="auth-right">
        <el-card class="login-card" shadow="never">
          <div class="card-header">
            <h2>欢迎登录</h2>
            <p>登录后即可浏览物品、发起交换与管理个人信息</p>
          </div>

          <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="login-form">
            <el-form-item label="用户名" prop="username">
              <el-input
                  v-model="form.username"
                  placeholder="请输入用户名"
                  size="large"
                  autocomplete="off"
              />
            </el-form-item>

            <el-form-item label="密码" prop="password">
              <el-input
                  v-model="form.password"
                  type="password"
                  show-password
                  placeholder="请输入密码"
                  size="large"
                  autocomplete="current-password"
              />
            </el-form-item>

            <div class="tips-row">
              <span>登录后可进入首页浏览和交换物品</span>
            </div>

            <el-button class="submit-btn" type="primary" size="large" @click="submit">
              登录
            </el-button>

            <div class="extra-row">
              <el-button link class="forget-btn" @click="openForgetDialog">
                忘记密码？联系管理员重置
              </el-button>
            </div>

            <div class="action-row">
              <span>还没有账号？</span>
              <el-button link class="register-link-btn" @click="goRegister">立即注册</el-button>
            </div>
          </el-form>

          <el-dialog v-model="forgetVisible" title="密码重置申请" width="400px">
            <el-form :model="forgetForm" label-position="top">
              <el-form-item label="用户名">
                <el-input v-model="forgetForm.username" />
              </el-form-item>

              <el-form-item label="手机号">
                <el-input v-model="forgetForm.phone" />
              </el-form-item>

              <el-form-item label="邮箱">
                <el-input v-model="forgetForm.email" />
              </el-form-item>

              <el-form-item label="说明">
                <el-input
                    v-model="forgetForm.reason"
                    type="textarea"
                    placeholder="例如：忘记密码，无法登录"
                />
              </el-form-item>
            </el-form>

            <template #footer>
              <el-button @click="forgetVisible = false">取消</el-button>
              <el-button type="primary" @click="submitForget">提交申请</el-button>
            </template>
          </el-dialog>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'
import LoginCarousel from '../../components/LoginCarousel.vue'
import { submitPasswordResetRequestApi } from '../../api/passwordReset'

const router = useRouter()
const store = useUserStore()
const formRef = ref()

const form = ref({
  username: '',
  password: ''
})

const forgetVisible = ref(false)

const forgetForm = ref({
  username: '',
  phone: '',
  email: '',
  reason: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const submit = async () => {
  await formRef.value.validate()
  await store.login(form.value)
  ElMessage.success('登录成功')
  await router.push('/')
}

const goRegister = async () => {
  await router.push('/register')
}

const openForgetDialog = () => {
  forgetVisible.value = true
}

const submitForget = async () => {
  if (!forgetForm.value.username || !forgetForm.value.phone) {
    ElMessage.warning('请填写用户名和手机号')
    return
  }

  try {
    await submitPasswordResetRequestApi(forgetForm.value)

    ElMessage.success('申请已提交，请等待管理员处理')
    forgetVisible.value = false
    forgetForm.value = {
      username: '',
      phone: '',
      email: '',
      reason: ''
    }
  } catch (e) {
    console.error('密码重置申请失败：', e)
    ElMessage.error('提交失败，请检查后端接口是否已启用')
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background:
      linear-gradient(135deg, #f5fff9 0%, #eefaf6 32%, #edf8ff 68%, #f9fffd 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
}

.bg-decoration {
  position: absolute;
  border-radius: 50%;
  filter: blur(8px);
  opacity: 0.45;
  pointer-events: none;
}

.bg-decoration-left {
  width: 280px;
  height: 280px;
  background: rgba(165, 224, 205, 0.45);
  top: -70px;
  left: -40px;
}

.bg-decoration-right {
  width: 360px;
  height: 360px;
  background: rgba(188, 224, 255, 0.42);
  right: -80px;
  bottom: -80px;
}

.auth-container {
  width: min(1320px, 100%);
  display: grid;
  grid-template-columns: 1.25fr 0.82fr;
  gap: 28px;
  position: relative;
  z-index: 2;
}

.auth-left {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.brand-box {
  padding: 10px 6px 0;
}

.brand-badge {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(121, 201, 174, 0.14);
  color: #56a18d;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 18px;
}

.brand-box h1 {
  margin: 0 0 16px;
  font-size: 42px;
  line-height: 1.25;
  color: #2f5c52;
}

.brand-box p {
  margin: 0;
  max-width: 680px;
  font-size: 16px;
  line-height: 1.9;
  color: #62857c;
}

.auth-right {
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 100%;
  max-width: 440px;
  border-radius: 28px;
  border: 1px solid rgba(190, 231, 220, 0.7);
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(10px);
  box-shadow: 0 20px 40px rgba(122, 167, 154, 0.12);
}

.card-header {
  margin-bottom: 24px;
}

.card-header h2 {
  margin: 0 0 10px;
  font-size: 28px;
  color: #315d53;
}

.card-header p {
  margin: 0;
  color: #7a978f;
  line-height: 1.8;
  font-size: 14px;
}

.login-form :deep(.el-form-item__label) {
  color: #57776f;
  font-weight: 600;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 14px;
  min-height: 44px;
  box-shadow: 0 0 0 1px rgba(197, 231, 221, 0.95) inset;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #90d0bd inset;
}

.tips-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #88a39c;
  font-size: 13px;
  margin: 4px 0 18px;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #73d0b3, #6db9d8);
  box-shadow: 0 14px 28px rgba(109, 185, 216, 0.18);
  font-size: 15px;
  font-weight: 600;
}

.action-row {
  margin-top: 18px;
  text-align: center;
  color: #7f9a93;
  font-size: 14px;
}

.register-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.register-link-btn:hover {
  color: #1f5448 !important;
}

.extra-row {
  margin-top: 12px;
  text-align: center;
}

.forget-btn {
  color: #6c8f88 !important;
  font-size: 13px;
  font-weight: 600;
}

.forget-btn:hover {
  color: #2f6b5c !important;
}

@media (max-width: 1100px) {
  .auth-container {
    grid-template-columns: 1fr;
  }

  .auth-left {
    order: 2;
  }

  .auth-right {
    order: 1;
  }

  .brand-box h1 {
    font-size: 34px;
  }

  .login-card {
    max-width: 100%;
  }
}

@media (max-width: 768px) {
  .auth-page {
    padding: 18px;
  }

  .brand-box h1 {
    font-size: 28px;
  }

  .brand-box p {
    font-size: 14px;
  }
}
</style>