import { api } from '../api/axios.js'
import { getApiData, getPage } from './http.js'

export const newsService = {
  async list({ key = '', page = 0, size = 10 } = {}) {
    const response = await api.get('/news', {
      params: { key, page, size },
    })
    return getPage(getApiData(response))
  },

  async create(payload, userId) {
    const response = await api.post('/news', payload, { params: { userId } })
    return getApiData(response)
  },

  async update(id, payload, userUpdateId) {
    const response = await api.put(`/news/${id}`, payload, {
      params: { userUpdateId },
    })
    return getApiData(response)
  },

  async remove(id, userId) {
    const response = await api.delete(`/news/${id}`, { params: { userId } })
    return getApiData(response)
  },

  async history(userId, { page = 0, size = 10 } = {}) {
    const response = await api.get(`/news/history/${userId}`, {
      params: { page, size },
    })
    return getPage(getApiData(response))
  },
}
