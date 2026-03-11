import request from '../utils/request'

export const updateProfileApi = (data) => request.put('/api/users/profile', data)
export const changePasswordApi = (data) => request.put('/api/users/password', data)
