import { useCallback, useEffect, useState } from 'react'
import { useOutletContext } from 'react-router'
import DataTable from '../components/DataTable.jsx'
import Modal from '../components/Modal.jsx'
import { PageHeader, Pagination, SearchBar, StatusPill } from '../components/ManagementPage.jsx'
import usePagedData from '../hooks/usePagedData.js'
import { categoryService } from '../services/categoryService.js'
import { newsService } from '../services/newsService.js'
import { getApiError } from '../services/http.js'

const blankForm = {
  newsTitle: '',
  headline: '',
  newsContent: '',
  newsSource: '',
  categoryID: '',
}

export default function StaffNewsPage({ history = false }) {
  const { user } = useOutletContext()
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [refresh, setRefresh] = useState(0)
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(blankForm)
  const [categories, setCategories] = useState([])
  const [categoryError, setCategoryError] = useState('')
  const [formError, setFormError] = useState('')
  const [busy, setBusy] = useState(false)

  const fetchPage = useCallback(
    () => history
      ? newsService.history(user.id, { page, size: 10 })
      : newsService.list({ key: search, page, size: 10 }),
    [history, user.id, search, page],
  )
  const { items, pagination, loading, error } = usePagedData(fetchPage, search, page, refresh)

  useEffect(() => {
    let active = true
    categoryService.list({ page: 0, size: 100 })
      .then((result) => {
        if (active) setCategories(result.items.filter((category) => category.isActive))
      })
      .catch((requestError) => {
        if (active) setCategoryError(getApiError(requestError))
      })
    return () => { active = false }
  }, [])

  function openCreate() {
    setForm({ ...blankForm, categoryID: categories[0]?.id ?? '' })
    setFormError('')
    setModal({ mode: 'create' })
  }

  function openEdit(article) {
    setForm({
      newsTitle: article.newsTitle || '',
      headline: article.headline || '',
      newsContent: article.newsContent || '',
      newsSource: article.newsSource || '',
      categoryID: article.categoryID ?? '',
    })
    setFormError('')
    setModal({ mode: 'edit', article })
  }

  async function submit(event) {
    event.preventDefault()
    setBusy(true)
    setFormError('')
    const payload = {
      newsTitle: form.newsTitle.trim(),
      headline: form.headline.trim(),
      newsContent: form.newsContent.trim(),
      newsSource: form.newsSource.trim(),
      categoryID: { id: Number(form.categoryID) },
    }
    try {
      if (modal.mode === 'create') {
        await newsService.create(payload, user.id)
      } else {
        await newsService.update(modal.article.newsArticleID, payload, user.id)
      }
      setModal(null)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  async function removeArticle() {
    setBusy(true)
    setFormError('')
    try {
      await newsService.remove(modal.article.newsArticleID, user.id)
      setModal(null)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  const shownItems = history
    ? items.filter((article) => `${article.newsTitle || ''} ${article.headline || ''}`.toLowerCase().includes(search.toLowerCase()))
    : items
  const columns = [
    { key: 'newsArticleID', label: 'ID', render: (row) => <span className="id-label">#{row.newsArticleID}</span> },
    {
      key: 'newsTitle',
      label: 'Article',
      render: (row) => <div className="table-title"><strong>{row.newsTitle}</strong><span>{row.headline}</span></div>,
    },
    { key: 'categoryID', label: 'Category', render: (row) => categories.find((category) => category.id === row.categoryID)?.categoryName || `Category #${row.categoryID ?? '—'}` },
    { key: 'createdDate', label: 'Created', render: (row) => row.createdDate ? new Date(row.createdDate).toLocaleDateString() : '—' },
    { key: 'newsStatus', label: 'Status', render: (row) => <StatusPill active={row.newsStatus} /> },
    {
      key: 'actions',
      label: 'Actions',
      render: (row) => (
        <div className="table-actions">
          <button className="text-button" onClick={() => openEdit(row)}>Edit</button>
          <button className="text-button text-button-danger" onClick={() => { setFormError(''); setModal({ mode: 'delete', article: row }) }}>Delete</button>
        </div>
      ),
    },
  ]

  return (
    <>
      <PageHeader
        eyebrow={history ? 'EDITORIAL / HISTORY' : 'EDITORIAL / NEWS'}
        title={history ? 'My news history' : 'News articles'}
        description={history ? 'Articles authored by your account, from the news history API.' : 'Create and maintain newsroom articles and their publication categories.'}
        action={!history && <button className="button button-primary" onClick={openCreate}><span>＋</span> New article</button>}
      />
      <div className="notice notice-info">
        Tags cannot be managed yet: the current NewsRequest and NewsResponse DTOs contain no tag fields or tag endpoints. Article status is set by the backend and has no separate update endpoint.
      </div>
      {categoryError && <div className="notice notice-error" role="alert">Could not load categories: {categoryError}</div>}
      {formError && !modal && <div className="notice notice-error" role="alert">{formError}</div>}
      <section className="panel">
        <div className="panel-toolbar">
          <div><h2>{history ? 'Authored articles' : 'News directory'}</h2><p>{history ? 'Search is limited to the currently loaded history page.' : 'Search by article title.'}</p></div>
          <SearchBar value={search} onChange={(value) => { setSearch(value); setPage(0) }} placeholder="Search news articles" />
        </div>
        {error && <div className="notice notice-error" role="alert">{error}</div>}
        {loading ? <div className="loading-state">Loading articles…</div> : (
          <DataTable columns={columns} rows={shownItems} rowKey={(row) => row.newsArticleID} emptyMessage="No articles match your search." />
        )}
        {!loading && pagination && <Pagination pagination={pagination} page={page} onPageChange={setPage} />}
      </section>
      {modal && (
        <Modal
          title={modal.mode === 'delete' ? 'Delete article?' : modal.mode === 'create' ? 'Create article' : 'Update article'}
          onClose={() => setModal(null)}
          footer={modal.mode === 'delete' ? (
            <>
              <button className="button button-outline" onClick={() => setModal(null)}>Cancel</button>
              <button className="button button-danger" onClick={removeArticle} disabled={busy}>{busy ? 'Deleting…' : 'Confirm delete'}</button>
            </>
          ) : (
            <>
              <button className="button button-outline" onClick={() => setModal(null)}>Cancel</button>
              <button className="button button-primary" type="submit" form="news-form" disabled={busy || categories.length === 0}>
                {busy ? 'Saving…' : 'Save article'}
              </button>
            </>
          )}
        >
          {modal.mode === 'delete' ? (
            <div className="form-stack">
              <p>Delete <strong>{modal.article.newsTitle}</strong>? This action cannot be undone.</p>
              <div className="notice notice-warning">The API only permits deletion by the article's author. If this article belongs to another staff member, the request will fail.</div>
              {formError && <div className="notice notice-error" role="alert">{formError}</div>}
            </div>
          ) : (
            <form id="news-form" className="form-stack" onSubmit={submit}>
              {formError && <div className="notice notice-error" role="alert">{formError}</div>}
              {!categories.length && <div className="notice notice-warning">An active category is required before an article can be saved.</div>}
              <label className="form-field">
                <span>Title</span>
                <input value={form.newsTitle} onChange={(event) => setForm({ ...form, newsTitle: event.target.value })} required maxLength={200} />
              </label>
              <label className="form-field">
                <span>Headline</span>
                <input value={form.headline} onChange={(event) => setForm({ ...form, headline: event.target.value })} />
              </label>
              <label className="form-field">
                <span>Content</span>
                <textarea rows="7" value={form.newsContent} onChange={(event) => setForm({ ...form, newsContent: event.target.value })} required />
              </label>
              <div className="form-grid">
                <label className="form-field">
                  <span>Source</span>
                  <input value={form.newsSource} onChange={(event) => setForm({ ...form, newsSource: event.target.value })} />
                </label>
                <label className="form-field">
                  <span>Category</span>
                  <select value={form.categoryID} onChange={(event) => setForm({ ...form, categoryID: event.target.value })} required>
                    <option value="" disabled>Select a category</option>
                    {categories.map((category) => <option key={category.id} value={category.id}>{category.categoryName}</option>)}
                  </select>
                </label>
              </div>
              <div className="notice notice-info">Tags are omitted from this form because the API cannot persist them yet.</div>
            </form>
          )}
        </Modal>
      )}
    </>
  )
}
