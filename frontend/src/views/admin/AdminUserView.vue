<template>
  <div class="admin-user-page page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">User Management</div>
        <h1>用户管理</h1>
        <p>
          这里可以查看当前平台中的用户信息，也可以对普通用户资料进行编辑、删除和密码重置。
          为保证安全，管理员账号不展示重置密码操作。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">使用说明</div>
        <ul>
          <li>先刷新列表，确认当前用户数据是否最新</li>
          <li>编辑时只修改需要变动的资料信息</li>
          <li>密码不展示原值，只支持对普通用户执行重置</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card table-card" shadow="never">
      <div class="table-head">
        <div>
          <h3 class="section-title">用户列表</h3>
          <div class="muted">共 {{ list.length }} 个用户</div>
        </div>

        <div class="table-actions">
          <el-button type="primary" @click="load">刷新</el-button>
        </div>
      </div>

      <el-table :data="list" empty-text="暂无用户数据">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="130" />
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="realName" label="真实姓名" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />

        <el-table-column label="角色" width="140">
          <template #default="{ row }">
            <span :class="isAdminUser(row) ? 'role-pill admin' : 'role-pill user'">
              {{ isAdminUser(row) ? '管理员' : '普通用户' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link class="edit-link-btn" @click="edit(row)">编辑</el-button>

            <el-button
                v-if="!isAdminUser(row)"
                link
                class="reset-link-btn"
                @click="openResetPwd(row)"
            >
              重置密码
            </el-button>

            <el-button link class="delete-link-btn" @click="remove(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑用户信息" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>

        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="pwdDialogVisible" title="重置用户密码" width="460px">
      <el-form :model="pwdForm" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="pwdForm.username" disabled />
        </el-form-item>

        <el-form-item label="新密码">
          <el-input
              v-model="pwdForm.newPassword"
              type="password"
              show-password
              placeholder="请输入新的登录密码"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveResetPwd">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  adminDeleteUserApi,
  adminListUsersApi,
  adminResetUserPasswordApi,
  adminUpdateUserApi
} from '../../api/user'

const list = ref([])
const dialogVisible = ref(false)
const pwdDialogVisible = ref(false)

const form = reactive({
  id: null,
  nickname: '',
  realName: '',
  phone: '',
  email: ''
})

const pwdForm = reactive({
  id: null,
  username: '',
  newPassword: ''
})

const load = async () => {
  list.value = await adminListUsersApi()
}

const isAdminUser = (row) => {
  if (!row.roles) return false
  return Array.isArray(row.roles)
      ? row.roles.includes('ROLE_ADMIN')
      : String(row.roles).includes('ROLE_ADMIN')
}

const edit = (row) => {
  form.id = row.id
  form.nickname = row.nickname || ''
  form.realName = row.realName || ''
  form.phone = row.phone || ''
  form.email = row.email || ''
  dialogVisible.value = true
}

const saveEdit = async () => {
  await adminUpdateUserApi(form.id, {
    nickname: form.nickname,
    realName: form.realName,
    phone: form.phone,
    email: form.email
  })
  ElMessage.success('修改成功')
  dialogVisible.value = false
  await load()
}

const openResetPwd = (row) => {
  if (isAdminUser(row)) {
    ElMessage.warning('不能修改管理员密码')
    return
  }
  pwdForm.id = row.id
  pwdForm.username = row.username
  pwdForm.newPassword = ''
  pwdDialogVisible.value = true
}

const saveResetPwd = async () => {
  if (!pwdForm.newPassword || pwdForm.newPassword.length < 8) {
    ElMessage.warning('新密码长度不能少于8位')
    return
  }

  await adminResetUserPasswordApi(pwdForm.id, {
    newPassword: pwdForm.newPassword
  })
  ElMessage.success('密码已重置')
  pwdDialogVisible.value = false
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该用户吗？删除后将不再显示在列表中。', '提示', {
    type: 'warning'
  })
  await adminDeleteUserApi(id)
  ElMessage.success('删除成功')
  await load()
}

onMounted(load)
</script>

<style scoped>
.admin-user-page {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.hero-section {
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 22px;
  padding: 30px;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(238, 252, 246, 0.95), rgba(237, 247, 255, 0.95));
  box-shadow: 0 18px 38px rgba(125, 180, 164, 0.12);
  border: 1px solid rgba(214, 240, 232, 0.95);
}

.hero-badge {
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

.hero-main h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.3;
  color: #2f5c52;
}

.hero-main p {
  margin: 0;
  color: #67877f;
  font-size: 15px;
  line-height: 1.9;
}

.hero-side-card {
  padding: 22px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.72);
  box-shadow: inset 0 0 0 1px rgba(219, 240, 233, 0.9);
}

.side-title {
  font-size: 18px;
  font-weight: 700;
  color: #41695f;
  margin-bottom: 16px;
}

.hero-side-card ul {
  margin: 0;
  padding-left: 18px;
  color: #719089;
  line-height: 2;
  font-size: 14px;
}

.table-card {
  border-radius: 24px;
}

.table-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.table-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.role-pill {
  display: inline-flex;
  align-items: center;
  padding: 7px 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 700;
}

.role-pill.admin {
  background: #fff3e6;
  color: #b26a00;
  border: 1px solid #ffd8a8;
}

.role-pill.user {
  background: #eef8f4;
  color: #2f6b5c;
  border: 1px solid #cfe7dd;
}

.edit-link-btn {
  color: #2f6b5c !important;
  font-weight: 700;
}

.edit-link-btn:hover {
  color: #1f5448 !important;
}

.reset-link-btn {
  color: #4e79d9 !important;
  font-weight: 700;
}

.reset-link-btn:hover {
  color: #355fc0 !important;
}

.delete-link-btn {
  color: #e35b5b !important;
  font-weight: 700;
}

.delete-link-btn:hover {
  color: #c94040 !important;
}

@media (max-width: 1100px) {
  .hero-section {
    grid-template-columns: 1fr;
  }

  .hero-main h1 {
    font-size: 28px;
  }

  .table-head {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 22px;
  }

  .hero-main h1 {
    font-size: 24px;
  }

  .hero-main p {
    font-size: 14px;
  }
}
</style>