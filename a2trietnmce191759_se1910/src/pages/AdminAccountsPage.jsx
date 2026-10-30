import { useCallback, useState } from 'react'
import DataTable from '../components/DataTable.jsx'
import Modal from '../components/Modal.jsx'
import { PageHeader, Pagination, SearchBar } from '../components/ManagementPage.jsx'
import usePagedData from '../hooks/usePagedData.js'
import { accountService } from '../services/accountService.js'
import { getApiError } from '../services/http.js'

const emptyForm = { accountName: '', accountEmail: '', accountRole: '2' }

export default function AdminAccountsPage() {
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [refresh, setRefresh] = useState(0)
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(emptyForm)
  const [busy, setBusy] = useState(false)
  const [formError, setFormError] = useState('')
  const fetchPage = useCallback(
    () => accountService.list({ key: search, page, size: 10 }),
    [search, page],
  )
  const { items, pagination, loading, error } = usePagedData(fetchPage, search, page, refresh)

  function openCreate() {
    setForm(emptyForm)
    setFormError('')
    setModal({ mode: 'create' })
  }

  function openEdit(account) {
    setForm({ accountName: account.accountName || '' })
    setFormError('')
    setModal({ mode: 'edit', account })
  }

  async function submit(event) {
    event.preventDefault()
    setBusy(true)
    setFormError('')
    try {
      if (modal.mode === 'create') {
        await accountService.create({
          accountName: form.accountName.trim(),
          accountEmail: form.accountEmail.trim(),
          accountRole: Number(form.accountRole),
        })
      } else {
        await accountService.updateName(modal.account.id, form.accountName.trim())
      }
      setModal(null)
      setRefresh((value) => value + 1)
    } catch (requestError) {
      setFormError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  const columns = [
    { key: 'id', label: 'Account ID', render: (row) => <span className="id-label">#{row.id}</span> },
    { key: 'accountName', label: 'Name', render: (row) => <strong>{row.accountName}</strong> },
    { key: 'accountEmail', label: 'Email' },
    { key: 'accountRole', label: 'Role', render: (row) => <span className={`role-pill ${row.accountRole === 1 ? 'role-admin' : 'role-staff'}`}>{row.accountRole === 1 ? 'Admin' : 'Staff'}</span> },
    {
      key: 'actions',
      label: 'Actions',
      render: (row) => (
        <div className="table-actions">
          <button className="text-button" onClick={() => openEdit(row)}>Edit name</button>
          <button className="text-button text-button-disabled" disabled title="The backend does not expose an account delete endpoint">Delete</button>
        </div>
      ),
    },
  ]

  return (
    <>
      <PageHeader
        eyebrow="ADMINISTRATION / ACCOUNTS"
        title="Accounts"
        description="Create accounts, search the directory, and update account names."
        action={<button className="button button-primary" onClick={openCreate}><span>＋</span> New account</button>}
      />
      <div className="notice notice-info">
        Account deletion and role editing are unavailable because the current backend exposes no corresponding endpoints. New accounts receive an automatically generated password from the API.
      </div>
      <section className="panel">
        <div className="panel-toolbar">
          <div><h2>Account directory</h2><p>Search uses the account email field.</p></div>
          <SearchBar value={search} onChange={(value) => { setSearch(value); setPage(0) }} placeholder="Search accounts by email" />
        </div>
        {error && <div className="notice notice-error" role="alert">{error}</div>}
        {loading ? <div className="loading-state">Loading accounts…</div> : (
          <DataTable columns={columns} rows={items} rowKey={(row) => row.id} emptyMessage="No accounts match your search." />
        )}
        {!loading && pagination && <Pagination pagination={pagination} page={page} onPageChange={setPage} />}
      </section>
      {modal && (
        <Modal
          title={modal.mode === 'create' ? 'Create account' : `Update account #${modal.account.id}`}
          onClose={() => setModal(null)}
          footer={(
            <>
              <button className="button button-outline" type="button" onClick={() => setModal(null)}>Cancel</button>
              <button className="button button-primary" type="submit" form="account-form" disabled={busy}>
                {busy ? 'Saving…' : modal.mode === 'create' ? 'Create account' : 'Save name'}
              </button>
            </>
          )}
        >
          <form id="account-form" className="form-stack" onSubmit={submit}>
            {formError && <div className="notice notice-error" role="alert">{formError}</div>}
            <label className="form-field">
              <span>Account name</span>
              <input value={form.accountName} onChange={(event) => setForm({ ...form, accountName: event.target.value })} required maxLength={100} />
            </label>
            {modal.mode === 'create' && (
              <>
                <label className="form-field">
                  <span>Email</span>
                  <input type="email" value={form.accountEmail} onChange={(event) => setForm({ ...form, accountEmail: event.target.value })} required />
                </label>
                <label className="form-field">
                  <span>Role</span>
                  <select value={form.accountRole} onChange={(event) => setForm({ ...form, accountRole: event.target.value })}>
                    <option value="1">Admin (role 1)</option>
                    <option value="2">Staff (role 2)</option>
                  </select>
                </label>
              </>
            )}
          </form>
        </Modal>
      )}
    </>
  )
}
