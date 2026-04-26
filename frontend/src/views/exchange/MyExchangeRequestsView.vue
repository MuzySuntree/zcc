<template>
  <div class="exchange-page page-enter">
    <section class="hero-section">
      <div class="hero-main">
        <div class="hero-badge">Sent Requests</div>
        <h1>我发起的交换申请</h1>
        <p>
          这里会展示你主动发出的交换申请，
          方便随时查看状态，并对待处理申请进行取消操作。
        </p>
      </div>

      <div class="hero-side-card">
        <div class="side-title">状态说明</div>
        <ul>
          <li>待处理：对方尚未处理你的申请</li>
          <li>已同意：已进入交换中，可确认是否完成</li>
          <li>已拒绝：对方未接受本次交换申请</li>
        </ul>
      </div>
    </section>

    <el-card class="soft-card list-card" shadow="never">
      <div class="card-head">
        <div>
          <h3 class="section-title">申请记录</h3>
          <div class="muted">共 {{ list.records?.length || 0 }} 条申请</div>
        </div>
      </div>

      <el-table :data="list.records" empty-text="你暂时还没有发起交换申请">
        <el-table-column prop="itemTitle" label="物品" min-width="260" />
        <el-table-column prop="toUserNickname" label="对方" width="140" />

        <el-table-column label="状态" width="130">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" effect="light" round>
              {{ statusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 1">
              <el-button link type="danger" @click="cancel(row.id)">取消</el-button>
              <el-button link class="chat-link-btn" @click="openChat(row)">沟通</el-button>
            </template>

            <template v-else-if="row.status === 2">
              <template v-if="row.ownerConfirmed === 1 && row.requesterConfirmed === 1">
                <span class="muted">交换完成</span>
                <el-button link class="chat-link-btn" @click="openChat(row)">沟通</el-button>
              </template>

              <template v-else-if="row.requesterConfirmed === 1">
                <span class="muted">等待对方确认</span>
                <el-button link class="chat-link-btn" @click="openChat(row)">沟通</el-button>
              </template>

              <template v-else>
                <el-button class="confirm-action-btn" @click="confirmComplete(row.id)">
                  确认交换完成
                </el-button>
                <el-button link class="chat-link-btn" @click="openChat(row)">沟通</el-button>
              </template>
            </template>

            <template v-else>
              <span class="muted">-</span>
              <el-button link class="chat-link-btn" @click="openChat(row)">沟通</el-button>

            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="chatVisible" title="协商沟通" width="520px">
      <div class="chat-box">
        <div v-for="m in messages" :key="m.id" class="chat-item">
          <b>{{ m.fromUserId === userStore.userInfo.id ? '我' : '对方' }}：</b>{{ m.message }}
        </div>
      </div>

      <el-input
          v-model="newMsg"
          type="textarea"
          :rows="3"
          placeholder="请输入协商内容，例如交换时间、地点变更等"
      />

      <template #footer>
        <el-button @click="chatVisible = false">关闭</el-button>
        <el-button type="primary" @click="sendMsg">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { exchangeMessageListApi, exchangeMessageSendApi } from '../../api/exchangeMessage'
import { useUserStore } from '../../stores/user'
import {onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {
  exchangeCancelApi,
  exchangeSentApi,
  exchangeConfirmCompleteApi
} from '../../api/exchange'

const list = ref({records: []})
const userStore = useUserStore()
const chatVisible = ref(false)
const messages = ref([])
const newMsg = ref('')
const currentRequestId = ref(null)

const openChat = async (row) => {
  currentRequestId.value = row.id
  messages.value = await exchangeMessageListApi(row.id)
  chatVisible.value = true
}

const sendMsg = async () => {
  if (!newMsg.value) {
    ElMessage.warning('请输入沟通内容')
    return
  }

  await exchangeMessageSendApi({
    requestId: currentRequestId.value,
    message: newMsg.value
  })
  newMsg.value = ''
  messages.value = await exchangeMessageListApi(currentRequestId.value)
  ElMessage.success('发送成功')
}
const load = async () => {
  list.value = await exchangeSentApi({pageNum: 1, pageSize: 20})
}

const cancel = async (id) => {
  await exchangeCancelApi(id)
  ElMessage.success('已取消')
  await load()
}

const confirmComplete = async (id) => {
  await exchangeConfirmCompleteApi(id)
  ElMessage.success('已确认，等待对方确认')
  await load()
}

const statusText = (status) => {
  if (status === 1) return '待处理'
  if (status === 2) return '已同意'
  if (status === 3) return '已拒绝'
  if (status === 4) return '已取消'
  return `状态${status}`
}

const statusTagType = (status) => {
  if (status === 1) return 'warning'
  if (status === 2) return 'success'
  if (status === 3) return 'danger'
  if (status === 4) return 'info'
  return 'info'
}

onMounted(load)
</script>

<style scoped>
.exchange-page {
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

.list-card {
  border-radius: 24px;
}

.card-head {
  margin-bottom: 18px;
}

.confirm-action-btn {
  border: 1px solid #bfe3d7;
  background: #eef8f4;
  color: #2f6b5c;
  border-radius: 10px;
  font-weight: 700;
  padding: 6px 14px;
}

.confirm-action-btn:hover {
  background: #e2f4ee;
  color: #224f44;
  border-color: #a9d7c8;
}

@media (max-width: 1100px) {
  .hero-section {
    grid-template-columns: 1fr;
  }

  .hero-main h1 {
    font-size: 28px;
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
.chat-box {
  max-height: 260px;
  overflow-y: auto;
  margin-bottom: 14px;
  padding: 12px;
  border-radius: 14px;
  background: #f8fffb;
  border: 1px solid rgba(206, 234, 224, 0.95);
}

.chat-item {
  margin-bottom: 10px;
  line-height: 1.8;
  color: #45675f;
  word-break: break-all;
}
.chat-link-btn {
  color: #4e79d9 !important;
  font-weight: 700;
}

.chat-link-btn:hover {
  color: #355fc0 !important;
}
</style>