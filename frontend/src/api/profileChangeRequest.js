import request from '../utils/request'

export const submitProfileChangeRequestApi = (data) =>
    request.post('/api/profile-change-requests', data)

export const myProfileChangeRequestListApi = (params) =>
    request.get('/api/profile-change-requests/my', { params })

export const adminProfileChangeRequestListApi = (params) =>
    request.get('/api/admin/profile-change-requests', { params })

export const approveProfileChangeRequestApi = (id) =>
    request.put(`/api/admin/profile-change-requests/${id}/approve`)

export const rejectProfileChangeRequestApi = (id) =>
    request.put(`/api/admin/profile-change-requests/${id}/reject`)