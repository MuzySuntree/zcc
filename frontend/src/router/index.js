import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    component: () => import('../views/auth/LoginView.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    component: () => import('../views/auth/RegisterView.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    children: [
      { path: '', component: () => import('../views/item/HomeView.vue') },
      { path: 'item/:id', component: () => import('../views/item/ItemDetailView.vue') },
      { path: 'publish', component: () => import('../views/item/ItemPublishView.vue') },
      { path: 'item/edit/:id', component: () => import('../views/item/ItemEditView.vue') },
      { path: 'my-items', component: () => import('../views/item/MyItemsView.vue') },
      { path: 'exchange/sent', component: () => import('../views/exchange/MyExchangeRequestsView.vue') },
      { path: 'exchange/received', component: () => import('../views/exchange/ReceivedExchangeRequestsView.vue') },
      { path: 'profile', component: () => import('../views/profile/ProfileView.vue') },
      { path: 'admin', component: () => import('../views/admin/AdminDashboardView.vue'), meta: { admin: true } },
      { path: 'admin/categories', component: () => import('../views/admin/AdminCategoryView.vue'), meta: { admin: true } },
      { path: 'admin/items', component: () => import('../views/admin/AdminItemView.vue'), meta: { admin: true } },
      { path: 'admin/exchange-records', component: () => import('../views/admin/AdminExchangeRecordView.vue'), meta: { admin: true } },
      { path: 'admin/users', component: () => import('../views/admin/AdminUserView.vue'), meta: { admin: true } },
      { path: 'admin/profile-change-requests', component: () => import('../views/admin/AdminProfileChangeRequestView.vue'), meta: { admin: true } },
      { path: 'admin/password-reset', component: () => import('../views/admin/AdminPasswordResetRequestView.vue'), meta: { admin: true } },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to) => {
  const store = useUserStore()

  if (to.meta.public) return true
  if (!store.isLogin) return '/login'

  if (!store.userInfo) {
    await store.refreshMe()
  }

  if (to.meta.admin && !store.isAdmin) {
    return '/'
  }

  return true
})

export default router