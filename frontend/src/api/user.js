import request from '../utils/request'

export const updateProfileApi = (data) => request.put('/api/users/profile', data)
export const changePasswordApi = (data) => request.put('/api/users/password', data)

export const adminListUsersApi = () => request.get('/api/admin/users')
export const adminUpdateUserApi = (id, data) => request.put(`/api/admin/users/${id}`, data)
export const adminDeleteUserApi = (id) => request.delete(`/api/admin/users/${id}`)
export const adminResetUserPasswordApi = (id, data) =>
    request.put(`/api/users/admin/${id}/password`, data)