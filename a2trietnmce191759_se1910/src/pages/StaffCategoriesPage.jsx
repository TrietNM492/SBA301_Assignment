import { useCallback, useState } from 'react'
import DataTable from '../components/DataTable.jsx'
import Modal from '../components/Modal.jsx'
import { PageHeader, Pagination, SearchBar, StatusPill } from '../components/ManagementPage.jsx'
import usePagedData from '../hooks/usePagedData.js'
import { categoryService } from '../services/categoryService.js'
import { getApiError } from '../services/http.js'

const blankForm = { categoryName: '', categoryDesciption: '', parentCategoryID: '' }

export default function StaffCategoriesPage() {
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [refresh, setRefresh] = useState(0)
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(blankForm)
  const [busy, setBusy] = useState(false)
  const [formError, setFormError] = useState('')
  const fetchPage = useCallback(
    () => categoryService.list({ key: search, page, size: 10 }),
    [search, page],
  )
  const { items, pagination, loading, error } = usePagedData(fetchPage, search, page, refresh)

  function openCreate() {
    setForm(blankForm)
    setFormError('')
    setModal({ mode: 'create' })
  }

  function openEdit(category) {
    setForm({
      categoryName: category.categoryName || '',
      categoryDesciption: category.categoryDesciption || '',
      parentCategoryID: category.parentCategoryID ?? '',
    })
    setFormError('')
    setModal({ mode: 'edit', category })
  }

  async function submit(event) {
    event.preventDefault()
    setBusy(true)
    setFormError('')
    const payload = {
      categoryName: form.categoryName.trim(),
      categoryDesciption: form.categoryDesciption.trim(),
      parentCategoryID: form.parentCategoryID ? { id: Number(form.parentCategoryID) } : null,
    }
    try {
      if (modal.mode === 'create') await categoryService.create(payload)
      else await categoryService.update(modal.category.id, payload)
      setModal(null)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  async function removeCategory() {
    setBusy(true)
    setFormError('')
    try {
      await categoryService.remove(modal.category.id)
      setModal(null)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  async function toggleStatus(category) {
    setFormError('')
    try {
      await categoryService.toggleStatus(category.id)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    }
  }

  const columns = [
    { key: 'id', label: 'ID', render: (row) => <span className="id-label">#{row.id}</span> },
    { key: 'categoryName', label: 'Category', render: (row) => <strong>{row.categoryName}</strong> },
    { key: 'categoryDesciption', label: 'Description', render: (row) => <span className="truncate-cell">{row.categoryDesciption || '—'}</span> },
    { key: 'parentCategoryID', label: 'Parent', render: (row) => items.find((item) => item.id === row.parentCategoryID)?.categoryName || '—' },
    { key: 'isActive', label: 'Status', render: (row) => <StatusPill active={row.isActive} /> },
    {
      key: 'actions',
      label: 'Actions',
      render: (row) => (
        <div className="table-actions">
          <button className="text-button" onClick={() => openEdit(row)}>Edit</button>
          <button className="text-button" onClick={() => toggleStatus(row)}>{row.isActive ? 'Deactivate' : 'Activate'}</button>
          <button className="text-button text-button-danger" onClick={() => { setFormError(''); setModal({ mode: 'delete', category: row }) }}>Delete</button>
        </div>
      ),
    },
  ]

  return (
    <>
      <PageHeader
        eyebrow="EDITORIAL / TAXONOMY"
        title="Categories"
        description="Organize the newsroom and control which categories are active."
        action={<button className="button button-primary" onClick={openCreate}><span>＋</span> New category</button>}
      />
      {formError && !modal && <div className="notice notice-error" role="alert">{formError}</div>}
      <section className="panel">
        <div className="panel-toolbar">
          <div><h2>Category list</h2><p>Create, update, search, and change category status.</p></div>
          <SearchBar value={search} onChange={(value) => { setSearch(value); setPage(0) }} placeholder="Search categories" />
        </div>
        {error && <div className="notice notice-error" role="alert">{error}</div>}
        {loading ? <div className="loading-state">Loading categories…</div> : (
          <DataTable columns={columns} rows={items} rowKey={(row) => row.id} emptyMessage="No categories match your search." />
        )}
        {!loading && pagination && <Pagination pagination={pagination} page={page} onPageChange={setPage} />}
      </section>
      {modal?.mode !== undefined && (
        <Modal
          title={modal.mode === 'delete' ? 'Delete category?' : modal.mode === 'create' ? 'Create category' : `Update ${modal.category.categoryName}`}
          onClose={() => setModal(null)}
          footer={modal.mode === 'delete' ? (
            <>
              <button className="button button-outline" onClick={() => setModal(null)}>Cancel</button>
              <button className="button button-danger" onClick={removeCategory} disabled={busy}>{busy ? 'Deleting…' : 'Confirm delete'}</button>
            </>
          ) : (
            <>
              <button className="button button-outline" onClick={() => setModal(null)}>Cancel</button>
              <button className="button button-primary" type="submit" form="category-form" disabled={busy}>{busy ? 'Saving…' : 'Save category'}</button>
            </>
          )}
        >
          {modal.mode === 'delete' ? (
            <div className="form-stack">
              <p>Delete <strong>{modal.category.categoryName}</strong>? This action cannot be undone.</p>
              <div className="notice notice-warning">The service does not check article references before deletion. The database may reject this request if the category is still in use.</div>
              {formError && <div className="notice notice-error" role="alert">{formError}</div>}
            </div>
          ) : (
            <form id="category-form" className="form-stack" onSubmit={submit}>
              {formError && <div className="notice notice-error" role="alert">{formError}</div>}
              <label className="form-field">
                <span>Category name</span>
                <input value={form.categoryName} onChange={(event) => setForm({ ...form, categoryName: event.target.value })} required maxLength={100} />
              </label>
              <label className="form-field">
                <span>Description</span>
                <textarea rows="4" value={form.categoryDesciption} onChange={(event) => setForm({ ...form, categoryDesciption: event.target.value })} />
              </label>
              <label className="form-field">
                <span>Parent category <small>optional</small></span>
                <select value={form.parentCategoryID} onChange={(event) => setForm({ ...form, parentCategoryID: event.target.value })}>
                  <option value="">No parent</option>
                  {items.filter((category) => category.id !== modal.category?.id).map((category) => (
                    <option key={category.id} value={category.id}>{category.categoryName}</option>
                  ))}
                </select>
              </label>
            </form>
          )}
        </Modal>
      )}
    </>
  )
}
