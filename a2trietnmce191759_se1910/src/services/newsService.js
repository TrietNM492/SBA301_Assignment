import { api } from '../api/axios.js'
import { getApiData, getPage } from './http.js'

export const newsService = {
  async list({ key = '', page = 0, size = 10 } = {}) {
    const response = await api.get('/news', {
      params: { key, page, size },
    })
    return getPage(getApiData(response))
  },

  async create(payload) {
    const response = await api.post('/news', payload)
    return getApiData(response)
  },

  async update(id, payload) {
    const response = await api.put(`/news/${id}`, payload)
    return getApiData(response)
  },

  async remove(id) {
    const response = await api.delete(`/news/${id}`)
    return getApiData(response)
  },

  async history({ page = 0, size = 10 } = {}) {
    const response = await api.get('/news/history', {
      params: { page, size },
    })
    return getPage(getApiData(response))
  },
}
