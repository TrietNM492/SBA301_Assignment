import { NavLink, Outlet, useOutletContext } from 'react-router'

export default function WorkspaceLayout({ role }) {
  const { user, onSignIn, onSignOut } = useOutletContext()
  const links = role === 'admin'
    ? [
      { to: '/admin/accounts', label: 'Accounts', icon: 'A' },
      { to: '/admin/profile', label: 'My profile', icon: 'P' },
    ]
    : [
      { to: '/staff/news', label: 'News articles', icon: 'N' },
      { to: '/staff/categories', label: 'Categories', icon: 'C' },
      { to: '/staff/history', label: 'News history', icon: 'H' },
      { to: '/staff/profile', label: 'My profile', icon: 'P' },
    ]

  return (
    <div className="workspace">
      <aside className="sidebar">
        <NavLink className="brand" to="/">
          <span className="brand-mark">F</span>
          <span>FUNews<span className="brand-subtitle">EDITORIAL CONSOLE</span></span>
        </NavLink>
        <div className="sidebar-label">WORKSPACE</div>
        <nav className="sidebar-nav" aria-label={`${role} navigation`}>
          {links.map((link) => (
            <NavLink key={link.to} className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`} to={link.to}>
              <span className="nav-icon">{link.icon}</span>
              {link.label}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-bottom">
          <div className="user-card">
            <div className="avatar">{(user?.name || 'U').slice(0, 1).toUpperCase()}</div>
            <div className="user-details">
              <strong>{user?.name || 'Workspace user'}</strong>
              <span>{role === 'admin' ? 'Administrator' : 'Staff member'}</span>
            </div>
          </div>
          <button className="signout-button" type="button" onClick={onSignOut}>Sign out <span aria-hidden="true">↗</span></button>
        </div>
      </aside>
      <main className="workspace-main">
        <header className="topbar">
          <span>{role === 'admin' ? 'Administration' : 'Editorial workspace'}</span>
          <span className="topbar-role">{role === 'admin' ? 'ADMIN' : 'STAFF'} · #{user?.id ?? '—'}</span>
        </header>
        <div className="workspace-content">
          <Outlet context={{ user, onSignIn, onSignOut }} />
        </div>
      </main>
    </div>
  )
}
