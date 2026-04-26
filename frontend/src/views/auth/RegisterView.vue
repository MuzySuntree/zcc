<template>
  <div class="auth-page">
    <div class="bg-decoration bg-decoration-left"></div>
    <div class="bg-decoration bg-decoration-right"></div>

    <div class="register-wrapper">
      <div class="register-side">
        <div class="side-badge">Join Campus Swap</div>
        <h1>创建你的平台账号</h1>
        <p>
          注册后即可发布闲置物品、浏览交换信息、管理个人资料，
          开启更轻松的校园二手交换体验。
        </p>

        <div class="side-cards">
          <div class="mini-card">
            <h3>便捷发布</h3>
            <p>快速填写物品信息，清晰展示自己的闲置物品。</p>
          </div>
          <div class="mini-card">
            <h3>交换管理</h3>
            <p>统一查看发出的申请与收到的申请，流程更清楚。</p>
          </div>
          <div class="mini-card">
            <h3>校园场景</h3>
            <p>更适合学生间物品流转，交流与交换更自然。</p>
          </div>
        </div>
      </div>

      <div class="register-main">
        <el-card class="register-card" shadow="never">
          <div class="card-header">
            <h2>注册账号</h2>
            <p>请完整填写以下信息，创建一个新的平台账号</p>
          </div>

          <el-form :model="form" label-position="top">
            <el-form-item label="用户名">
              <el-input
                  v-model="form.username"
                  placeholder="请输入用户名（4-20位）"
                  size="large"
                  autocomplete="off"
              />
              <div class="input-tip">4-20位，用于登录</div>
            </el-form-item>

            <el-form-item label="密码">
              <el-input
                  v-model="form.password"
                  type="password"
                  show-password
                  placeholder="请输入密码（8-32位）"
                  size="large"
                  autocomplete="new-password"
              />
              <div class="input-tip">8-32位，建议包含字母和数字</div>
            </el-form-item>

            <el-form-item label="昵称">
              <el-input v-model="form.nickname" placeholder="请输入昵称" size="large" />
            </el-form-item>

            <el-form-item label="真实姓名">
              <el-input v-model="form.realName" placeholder="请输入真实姓名" size="large" />
            </el-form-item>

            <el-form-item label="手机号">
              <el-input
                  v-model="form.phone"
                  placeholder="请输入11位手机号"
                  size="large"
              />
              <div class="input-tip">格式：1开头的11位手机号</div>
            </el-form-item>

            <el-form-item label="邮箱">
              <el-input
                  v-model="form.email"
                  placeholder="请输入邮箱"
                  size="large"
              />
              <div class="input-tip">请填写常用邮箱方便联系</div>
            </el-form-item>

            <el-button class="submit-btn" type="primary" size="large" @click="submit">
              提交注册
            </el-button>

            <div class="action-row">
              <span>已经有账号？</span>
              <el-button link class="login-link-btn" @click="router.push('/login')">去登录</el-button>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { registerApi } from '../../api/auth'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  nickname: '',
  realName: '',
  phone: '',
  email: ''
})

const submit = async () => {
  const { username, password, nickname, realName, phone, email } = form.value

  if (!username || !password || !nickname || !realName || !phone || !email) {
    ElMessage.warning('请完整填写所有注册信息')
    return
  }

  if (username.length < 4 || username.length > 20) {
    ElMessage.warning('用户名需为4-20位')
    return
  }

  if (password.length < 8 || password.length > 32) {
    ElMessage.warning('密码需为8-32位')
    return
  }

  if (!/^1[3-9]\d{9}$/.test(phone)) {
    ElMessage.warning('手机号格式不正确')
    return
  }

  if (!/^\S+@\S+\.\S+$/.test(email)) {
    ElMessage.warning('邮箱格式不正确')
    return
  }

  await registerApi(form.value)
  ElMessage.success('注册成功，请登录')
  router.push('/login')
}

</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background:
      linear-gradient(135deg, #f7fff9 0%, #effbf7 35%, #eef8ff 70%, #fcfffd 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
}

.bg-decoration {
  position: absolute;
  border-radius: 50%;
  opacity: 0.42;
  pointer-events: none;
}

.bg-decoration-left {
  width: 320px;
  height: 320px;
  background: rgba(184, 237, 214, 0.5);
  top: -100px;
  left: -40px;
}

.bg-decoration-right {
  width: 360px;
  height: 360px;
  background: rgba(191, 225, 255, 0.48);
  right: -100px;
  bottom: -100px;
}

.register-wrapper {
  width: min(1260px, 100%);
  display: grid;
  grid-template-columns: 1fr 0.95fr;
  gap: 28px;
  position: relative;
  z-index: 2;
}

.register-side {
  padding: 22px 8px 0;
}

.side-badge {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(121, 201, 174, 0.14);
  color: #56a18d;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 18px;
}

.register-side h1 {
  margin: 0 0 16px;
  font-size: 40px;
  line-height: 1.28;
  color: #2f5c52;
}

.register-side p {
  margin: 0;
  max-width: 620px;
  line-height: 1.9;
  color: #64877e;
  font-size: 16px;
}

.side-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-top: 30px;
}

.mini-card {
  padding: 20px 18px;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 14px 28px rgba(137, 173, 162, 0.08);
  border: 1px solid rgba(208, 235, 226, 0.8);
}

.mini-card h3 {
  margin: 0 0 10px;
  color: #466c63;
  font-size: 17px;
}

.mini-card p {
  font-size: 14px;
  line-height: 1.8;
  color: #76948c;
}

.register-main {
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-card {
  width: 100%;
  max-width: 500px;
  border-radius: 28px;
  border: 1px solid rgba(190, 231, 220, 0.7);
  background: rgba(255, 255, 255, 0.84);
  backdrop-filter: blur(10px);
  box-shadow: 0 20px 40px rgba(122, 167, 154, 0.12);
}

.card-header {
  margin-bottom: 22px;
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

:deep(.el-form-item__label) {
  color: #57776f;
  font-weight: 600;
}

:deep(.el-input__wrapper) {
  border-radius: 14px;
  min-height: 44px;
  box-shadow: 0 0 0 1px rgba(197, 231, 221, 0.95) inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #90d0bd inset;
}

.submit-btn {
  width: 100%;
  height: 46px;
  margin-top: 8px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #73d0b3, #6db9d8);
  box-shadow: 0 14px 28px rgba(109, 185, 216, 0.18);
  font-size: 15px;
  font-weight: 600;
}

.action-row {
  margin-top: 16px;
  text-align: center;
  color: #7f9a93;
  font-size: 14px;
}

.login-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.login-link-btn:hover {
  color: #1f5448 !important;
}

@media (max-width: 1100px) {
  .register-wrapper {
    grid-template-columns: 1fr;
  }

  .side-cards {
    grid-template-columns: 1fr;
  }

  .register-side h1 {
    font-size: 32px;
  }

  .register-card {
    max-width: 100%;
  }
}

@media (max-width: 768px) {
  .auth-page {
    padding: 18px;
  }

  .register-side h1 {
    font-size: 28px;
  }

  .register-side p {
    font-size: 14px;
  }
}
.input-tip {
  font-size: 12px;
  color: #8aa39c;
  margin-top: 4px;
}
</style>