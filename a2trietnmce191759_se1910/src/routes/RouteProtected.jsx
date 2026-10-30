import { Navigate, Outlet } from 'react-router'

export default function RouteProtected({ user, roles, onSignIn, onSignOut }) {
  if (!user) return <Navigate to="/login" replace />
  if (!roles.includes(user.role)) {
    return <Navigate to={user.role === 'admin' ? '/admin/accounts' : '/staff/news'} replace />
  }
  return <Outlet context={{ user, onSignIn, onSignOut }} />
}
