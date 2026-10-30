export function getApiData(response) {
  const data = response?.data?.data
  if (data === undefined || data === null) {
    throw new Error(response?.data?.message || 'The API response did not include data.')
  }
  return data
}

export function getPage(data) {
  if (!data || !Array.isArray(data.content) || !data.pagination) {
    throw new Error('The API returned an invalid paginated response.')
  }
  return {
    items: data.content,
    pagination: data.pagination,
  }
}

export function getApiError(error) {
  return error?.response?.data?.message
    || error?.response?.data?.error
    || error?.message
    || 'An unexpected error occurred.'
}
