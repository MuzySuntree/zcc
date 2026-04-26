import request from '../utils/request'

export const exchangeMessageListApi = (requestId) =>
    request.get(`/api/exchange-messages/${requestId}`)

export const exchangeMessageSendApi = (data) =>
    request.post('/api/exchange-messages', data)