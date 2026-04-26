import request from '../utils/request'

export const submitPasswordResetRequestApi = (data) =>
    request.post('/api/password-reset', data)

export const adminPasswordResetListApi = () =>
    request.get('/api/password-reset/admin')

export const adminHandlePasswordResetApi = (id) =>
    request.put(`/api/password-reset/admin/${id}/reset`)