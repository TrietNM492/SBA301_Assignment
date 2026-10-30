import { useState } from 'react'
import { Link, useNavigate } from 'react-router'
import AuthLayout from '../layout/AuthLayout.jsx'
import { authService } from '../services/authService.js'
import { getApiError } from '../services/http.js'

export default function LoginPage({ onSignIn }) {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  async function submit(event) {
    event.preventDefault()
    setError('')
    setIsSubmitting(true)

    try {
      const account = await authService.login({ username: username.trim(), password })
      const roles = { 1: 'admin', 2: 'staff' }
      const role = roles[account.accountRole]
      if (!role) {
        throw new Error('This account does not have a supported workspace role.')
      }

      const user = {
        id: account.id,
        name: account.accountName,
        email: account.accountEmail,
        role,
      }
      onSignIn(user)
      navigate(role === 'admin' ? '/admin/accounts' : '/staff/news')
    } catch (loginError) {
      setError(getApiError(loginError))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthLayout>
      <section className="auth-card">
        <p className="eyebrow">WELCOME BACK</p>
        <h1>Sign in to your workspace</h1>
        <p className="auth-intro">Sign in with your account credentials.</p>
        {error && <div className="notice notice-error" role="alert">{error}</div>}
        <form className="form-stack" onSubmit={submit}>
          <label className="form-field">
            <span>Username</span>
            <input
              name="username"
              autoComplete="username"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              required
            />
          </label>
          <label className="form-field">
            <span>Password</span>
            <input
              name="password"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />
          </label>
          <button className="button button-primary button-wide" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Signing in…' : 'Continue to workspace'} {!isSubmitting && <span>→</span>}
          </button>
        </form>
        <Link className="auth-public-link" to="/">Browse published news instead →</Link>
      </section>
    </AuthLayout>
  )
}
