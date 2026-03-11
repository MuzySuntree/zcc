const TOKEN_KEY = 'campus_idle_token'
const USER_KEY = 'campus_idle_user'

export const storage = {
  getToken: () => localStorage.getItem(TOKEN_KEY) || '',
  setToken: (token) => localStorage.setItem(TOKEN_KEY, token || ''),
  removeToken: () => localStorage.removeItem(TOKEN_KEY),
  getUser: () => JSON.parse(localStorage.getItem(USER_KEY) || 'null'),
  setUser: (user) => localStorage.setItem(USER_KEY, JSON.stringify(user || null)),
  removeUser: () => localStorage.removeItem(USER_KEY)
}
