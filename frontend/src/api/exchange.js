import request from '../utils/request'

export const exchangeCreateApi = (data) => request.post('/api/exchange-requests', data)
export const exchangeSentApi = (params) => request.get('/api/exchange-requests/sent', { params })
export const exchangeReceivedApi = (params) => request.get('/api/exchange-requests/received', { params })
export const exchangeHandleApi = (id, data) => request.put(`/api/exchange-requests/${id}/handle`, data)
export const exchangeCancelApi = (id) => request.put(`/api/exchange-requests/${id}/cancel`)
export const exchangeMyRecordsApi = (params) => request.get('/api/exchange-records/my', { params })
