import { api } from '../api/axios.js'
import { getApiData, getPage } from './http.js'

export const accountService = {
  async list({ key = '', page = 0, size = 10 } = {}) {
    const response = await api.get('/admin/accounts', {
      params: { key, page, size },
    })
    return getPage(getApiData(response))
  },

  async create(payload) {
    const response = await api.post('/admin/accounts', payload)
    return getApiData(response)
  },

  async getProfile(userId) {
    const response = await api.get(`/admin/accounts/${userId}/profile`)
    return getApiData(response)
  },

  async updateName(userId, accountName) {
    const response = await api.put(`/admin/accounts/${userId}/name`, { accountName })
    return getApiData(response)
  },

  async changePassword(userId, payload) {
    const response = await api.patch(`/admin/accounts/${userId}/password`, payload)
    return getApiData(response)
  },

  async register(payload) {
    const response = await api.post('/admin/register', payload)
    return getApiData(response)
  },
}
