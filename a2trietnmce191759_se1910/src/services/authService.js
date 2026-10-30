import { api } from '../api/axios.js'
import { getApiData } from './http.js'

export const authService = {
  async login(credentials) {
    const response = await api.post('/auth/login', credentials)
    return getApiData(response)
  },

  async logout() {
    const response = await api.post('/auth/logout')
    return getApiData(response)
  },
}
