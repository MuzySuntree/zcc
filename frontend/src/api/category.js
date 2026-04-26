import request from '../utils/request'

export const categoryListApi = () => request.get('/api/categories')
export const categoryCreateApi = (data) => request.post('/api/categories', data)
export const categoryUpdateApi = (id, data) => request.put(`/api/categories/${id}`, data)
export const categoryDeleteApi = (id) => request.delete(`/api/categories/${id}`)
