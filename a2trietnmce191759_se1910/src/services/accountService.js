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

  async getMyProfile() {
    const response = await api.get('/admin/accounts/me/profile')
    return getApiData(response)
  },

  async updateMyName(accountName) {
    const response = await api.put('/admin/accounts/me/name', { accountName })
    return getApiData(response)
  },

  async changeMyPassword(payload) {
    const response = await api.patch('/admin/accounts/me/password', payload)
    return getApiData(response)
  },

  async updateAccountName(userId, accountName) {
    const response = await api.put(`/admin/accounts/${userId}/name`, { accountName })
    return getApiData(response)
  },

  async register(payload) {
    const response = await api.post('/admin/register', payload)
    return getApiData(response)
  },
}
