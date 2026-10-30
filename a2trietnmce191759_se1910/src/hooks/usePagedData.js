import { useEffect, useState } from 'react'
import { getApiError } from '../services/http.js'

export default function usePagedData(fetchPage, query, page, refreshKey) {
  const [result, setResult] = useState({ items: [], pagination: null })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    const timer = window.setTimeout(async () => {
      setLoading(true)
      setError('')
      try {
        const nextResult = await fetchPage()
        if (active) setResult(nextResult)
      } catch (requestError) {
        if (active) setError(getApiError(requestError))
      } finally {
        if (active) setLoading(false)
      }
    }, 250)

    return () => {
      active = false
      window.clearTimeout(timer)
    }
  }, [fetchPage, query, page, refreshKey])

  return { ...result, loading, error }
}
