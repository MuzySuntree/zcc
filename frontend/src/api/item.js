import request from '../utils/request'

export const itemPageApi = (params) => request.get('/api/items', { params })

export const itemMyPageApi = (params) => request.get('/api/items/my', { params })

export const itemDetailApi = (id) => request.get(`/api/items/${id}`)
export const itemCreateApi = (data) => request.post('/api/items', data)
export const itemUpdateApi = (id, data) => request.put(`/api/items/${id}`, data)
export const itemDeleteApi = (id) => request.delete(`/api/items/${id}`)
export const uploadFileApi = (file) => {
  const fd = new FormData()
  fd.append('file', file)
  return request.post('/api/files/upload', fd)
}