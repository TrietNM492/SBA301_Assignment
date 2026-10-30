import { useEffect, useState } from 'react'
import { useOutletContext } from 'react-router'
import { PageHeader } from '../components/ManagementPage.jsx'
import { accountService } from '../services/accountService.js'
import { getApiError } from '../services/http.js'

export default function ProfilePage() {
  const { user, onSignIn } = useOutletContext()
  const [profile, setProfile] = useState(null)
  const [name, setName] = useState(user.name || '')
  const [password, setPassword] = useState({ oldPass: '', newPass: '' })
  const [loading, setLoading] = useState(true)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')

  useEffect(() => {
    let active = true
    accountService.getProfile(user.id)
      .then((result) => {
        if (!active) return
        setProfile(result)
        setName(result?.accountName || '')
      })
      .catch((requestError) => {
        if (active) setError(getApiError(requestError))
      })
      .finally(() => {
        if (active) setLoading(false)
      })
    return () => { active = false }
  }, [user.id])

  async function saveName(event) {
    event.preventDefault()
    setBusy(true)
    setError('')
    setNotice('')
    try {
      const updated = await accountService.updateName(user.id, name.trim())
      setProfile(updated)
      onSignIn({ ...user, name: updated?.accountName || name.trim() })
      setNotice('Your profile name was updated.')
    } catch (requestError) {
      setError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  async function savePassword(event) {
    event.preventDefault()
    setBusy(true)
    setError('')
    setNotice('')
    try {
      await accountService.changePassword(user.id, password)
      setPassword({ oldPass: '', newPass: '' })
      setNotice('Your password was updated.')
    } catch (requestError) {
      setError(getApiError(requestError))
    } finally {
      setBusy(false)
    }
  }

  return (
    <>
      <PageHeader
        eyebrow="ACCOUNT / PROFILE"
        title="My profile"
        description="Review your account details and update your name or password."
      />
      {error && <div className="notice notice-error" role="alert">{error}</div>}
      {notice && <div className="notice notice-success" role="status">{notice}</div>}
      {loading ? <div className="panel loading-state">Loading your profile…</div> : profile && (
        <div className="profile-grid">
          <section className="panel profile-summary">
            <div className="profile-avatar">{(profile.accountName || 'U').slice(0, 1).toUpperCase()}</div>
            <p className="eyebrow">SIGNED IN AS</p>
            <h2>{profile.accountName}</h2>
            <p>{profile.accountEmail}</p>
            <div className="profile-meta">
              <span>Account ID</span><strong>#{profile.id}</strong>
              <span>Role</span><strong>{profile.accountRole === 1 ? 'Admin · role 1' : 'Staff · role 2'}</strong>
            </div>
          </section>
          <section className="panel profile-forms">
            <div className="profile-section">
              <div><h2>Personal details</h2><p>Update the name shown in the workspace.</p></div>
              <form className="form-stack" onSubmit={saveName}>
                <label className="form-field">
                  <span>Account name</span>
                  <input value={name} onChange={(event) => setName(event.target.value)} required maxLength={100} />
                </label>
                <button className="button button-primary align-start" type="submit" disabled={busy}>Save name</button>
              </form>
            </div>
            <div className="profile-section">
              <div><h2>Change password</h2><p>Enter your current password and choose a new one.</p></div>
              <form className="form-stack" onSubmit={savePassword}>
                <label className="form-field">
                  <span>Current password</span>
                  <input type="password" value={password.oldPass} onChange={(event) => setPassword({ ...password, oldPass: event.target.value })} required autoComplete="current-password" />
                </label>
                <label className="form-field">
                  <span>New password</span>
                  <input type="password" value={password.newPass} onChange={(event) => setPassword({ ...password, newPass: event.target.value })} required autoComplete="new-password" />
                </label>
                <button className="button button-outline align-start" type="submit" disabled={busy}>Update password</button>
              </form>
            </div>
          </section>
        </div>
      )}
    </>
  )
}
