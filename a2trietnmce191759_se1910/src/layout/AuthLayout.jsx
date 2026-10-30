export default function AuthLayout({ children }) {
  return (
    <main className="auth-page">
      <div className="auth-brand"><span className="brand-mark">F</span> FUNews</div>
      {children}
      <p className="auth-footnote">Newsroom workspace · React client</p>
    </main>
  )
}
