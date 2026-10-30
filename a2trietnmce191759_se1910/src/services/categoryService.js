import { api } from '../api/axios.js'
import { getApiData, getPage } from './http.js'

export const categoryService = {
  async list({ key = '', page = 0, size = 10 } = {}) {
    const response = await api.get('/categories', {
      params: { key, page, size },
    })
    return getPage(getApiData(response))
  },

  async create(payload) {
    const response = await api.post('/categories', payload)
    return getApiData(response)
  },

  async update(id, payload) {
    const response = await api.put(`/categories/${id}`, payload)
    return getApiData(response)
  },

  async remove(id) {
    const response = await api.delete(`/categories/${id}`)
    return getApiData(response)
  },

  async toggleStatus(id) {
    const response = await api.patch(`/categories/${id}/status`)
    return getApiData(response)
  },
}
