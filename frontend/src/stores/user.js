import { defineStore } from 'pinia'
import { loginApi, meApi } from '../api/auth'
import { storage } from '../utils/storage'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: storage.getToken(),
    userInfo: storage.getUser()
  }),
  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => (state.userInfo?.roles || []).includes('ROLE_ADMIN')
  },
  actions: {
    async login(form) {
      const data = await loginApi(form)
      this.token = data.token
      this.userInfo = data.userInfo
      storage.setToken(data.token)
      storage.setUser(data.userInfo)
    },
    async refreshMe() {
      const me = await meApi()
      this.userInfo = me
      storage.setUser(me)
    },
    logout() {
      this.token = ''
      this.userInfo = null
      storage.removeToken()
      storage.removeUser()
    }
  }
})
