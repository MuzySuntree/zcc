<template>
  <div class="profile-page page-enter">
    <section class="profile-hero">
      <div class="profile-hero-main">
        <div class="profile-badge">Profile Center</div>
        <h1>{{ profile.nickname || '个人中心' }}</h1>
        <p>
          这里用于查看你的当前正式资料与账号信息。
          如需修改资料，请在下方填写申请提交给管理员审核。
        </p>
      </div>

      <div class="profile-hero-card large-card">
        <div class="hero-card-title">当前正式资料</div>
        <div class="hero-card-desc">以下内容是当前已经生效的账号资料</div>

        <div class="hero-card-grid">
          <div class="hero-card-item">
            <span>昵称</span>
            <strong>{{ profile.nickname || '未设置' }}</strong>
          </div>
          <div class="hero-card-item">
            <span>真实姓名</span>
            <strong>{{ profile.realName || '未填写' }}</strong>
          </div>
          <div class="hero-card-item">
            <span>手机号</span>
            <strong>{{ profile.phone || '未填写' }}</strong>
          </div>
          <div class="hero-card-item">
            <span>邮箱</span>
            <strong>{{ profile.email || '未填写' }}</strong>
          </div>
        </div>
      </div>
    </section>

    <div class="profile-content">
      <el-card class="soft-card profile-card" shadow="never">
        <div class="card-head">
          <h3>联系管理员修改</h3>
          <p>下面填写的是你希望修改后的资料，提交后不会立即生效，需等待管理员审核</p>
        </div>

        <el-form :model="changeForm" label-position="top" class="profile-form">
          <el-form-item label="新昵称">
            <el-input v-model="changeForm.newNickname" placeholder="请输入新昵称" />
          </el-form-item>

          <el-form-item label="新真实姓名">
            <el-input v-model="changeForm.newRealName" placeholder="请输入新真实姓名" />
          </el-form-item>

          <el-form-item label="新手机号">
            <el-input v-model="changeForm.newPhone" placeholder="请输入新手机号" />
          </el-form-item>

          <el-form-item label="新邮箱">
            <el-input v-model="changeForm.newEmail" placeholder="请输入新邮箱" />
          </el-form-item>

          <el-form-item label="申请说明">
            <el-input
                v-model="changeForm.reason"
                type="textarea"
                :rows="3"
                placeholder="可填写修改原因，便于管理员查看"
            />
          </el-form-item>

          <el-button type="primary" class="submit-btn" @click="submitChangeRequest">
            联系管理员修改
          </el-button>
        </el-form>
      </el-card>

      <!-- 管理员：直接修改密码 -->
      <el-card v-if="isAdmin" class="soft-card pwd-card" shadow="never">
        <div class="card-head">
          <h3>修改密码</h3>
          <p>管理员可直接修改自己的登录密码</p>
        </div>

        <el-form :model="pwdForm" label-position="top">
          <el-form-item label="原密码">
            <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
          </el-form-item>

          <el-form-item label="新密码">
            <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码" />
          </el-form-item>

          <el-form-item label="确认新密码">
            <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
          </el-form-item>

          <el-button type="primary" class="submit-btn" @click="submitPwd">
            修改密码
          </el-button>
        </el-form>
      </el-card>

      <!-- 普通用户：密码说明 -->
      <el-card v-else class="soft-card pwd-card" shadow="never">
        <div class="card-head">
          <h3>密码处理说明</h3>
          <p>普通用户不能直接修改密码，如需处理，请联系管理员在后台执行重置</p>
        </div>

        <div class="pwd-tip-box">
          <div class="pwd-tip-title">当前处理规则</div>
          <div class="pwd-tip-item">1. 用户不能查看自己的原密码</div>
          <div class="pwd-tip-item">2. 用户不能在此页面直接修改密码</div>
          <div class="pwd-tip-item">3. 如需修改，请联系管理员在后台重置</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { submitProfileChangeRequestApi } from '../../api/profileChangeRequest'
import { changePasswordApi } from '../../api/user'

const store = useUserStore()
const router = useRouter()

const profile = reactive({
  ...(store.userInfo || {})
})

const isAdmin = computed(() => {
  return store.userInfo?.roles?.includes('ROLE_ADMIN')
})

const changeForm = reactive({
  newNickname: profile.nickname || '',
  newRealName: profile.realName || '',
  newPhone: profile.phone || '',
  newEmail: profile.email || '',
  reason: ''
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const submitChangeRequest = async () => {
  await submitProfileChangeRequestApi(changeForm)
  ElMessage.success('修改申请已提交，请等待管理员审核')
  changeForm.reason = ''
}

const submitPwd = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword || !pwdForm.confirmPassword) {
    ElMessage.warning('请完整填写密码信息')
    return
  }

  if (pwdForm.newPassword.length < 8 || pwdForm.newPassword.length > 32) {
    ElMessage.warning('新密码需为8-32位')
    return
  }

  if (pwdForm.newPassword !== pwdForm.confirmPassword) {
    ElMessage.error('两次输入的新密码不一致')
    return
  }

  await changePasswordApi({
    oldPassword: pwdForm.oldPassword,
    newPassword: pwdForm.newPassword
  })

  ElMessage.success('密码修改成功，请重新登录')
  store.logout()
  await router.push('/login')
}
</script>

<style scoped>
.profile-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.profile-hero {
  display: grid;
  grid-template-columns: 0.95fr 1.05fr;
  gap: 24px;
  padding: 32px;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.profile-badge {
  display: inline-flex;
  width: fit-content;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(122, 202, 176, 0.14);
  color: #55a08c;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 16px;
}

.profile-hero-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.profile-hero-main p {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.profile-hero-card {
  padding: 26px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: inset 0 0 0 1px rgba(219, 240, 233, 0.95);
}

.large-card {
  min-height: 260px;
}

.hero-card-title {
  font-size: 22px;
  font-weight: 800;
  color: #355d53;
  margin-bottom: 8px;
}

.hero-card-desc {
  color: #79958e;
  font-size: 14px;
  margin-bottom: 20px;
}

.hero-card-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px 18px;
}

.hero-card-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 18px;
  border-radius: 18px;
  background: #f8fffb;
  border: 1px solid rgba(206, 234, 224, 0.95);
}

.hero-card-item span {
  color: #7d9992;
  font-size: 13px;
}

.hero-card-item strong {
  color: #345d53;
  font-size: 16px;
  line-height: 1.5;
  word-break: break-all;
}

.profile-content {
  display: grid;
  grid-template-columns: 1fr 0.85fr;
  gap: 22px;
}

.profile-card,
.pwd-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 18px;
}

.card-head h3 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #385f56;
}

.card-head p {
  margin: 0;
  color: #7d9992;
  font-size: 14px;
  line-height: 1.8;
}

.profile-form :deep(.el-form-item__label),
.pwd-card :deep(.el-form-item__label) {
  color: #5f7e76;
  font-weight: 600;
}

.profile-form :deep(.el-input__wrapper),
.pwd-card :deep(.el-input__wrapper) {
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(199, 232, 223, 0.95) inset;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border-radius: 14px;
  font-weight: 600;
}

.pwd-tip-box {
  padding: 18px;
  border-radius: 18px;
  background: #f8fffb;
  border: 1px solid rgba(206, 234, 224, 0.95);
}

.pwd-tip-title {
  font-size: 16px;
  font-weight: 700;
  color: #3b6259;
  margin-bottom: 12px;
}

.pwd-tip-item {
  color: #6f8d86;
  line-height: 1.9;
  font-size: 14px;
}

@media (max-width: 1100px) {
  .profile-hero,
  .profile-content {
    grid-template-columns: 1fr;
  }

  .profile-hero-main h1 {
    font-size: 28px;
  }

  .hero-card-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .profile-hero {
    padding: 22px;
  }

  .profile-hero-main h1 {
    font-size: 24px;
  }

  .profile-hero-main p {
    font-size: 14px;
  }
}
</style>