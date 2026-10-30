import { Navigate, Route, Routes } from "react-router"
import AdminLayout from '../layout/AdminLayout.jsx'
import StaffLayout from '../layout/StaffLayout.jsx'
import AdminAccountsPage from '../pages/AdminAccountsPage.jsx'
import LoginPage from '../pages/LoginPage.jsx'
import ProfilePage from '../pages/ProfilePage.jsx'
import PublicNewsPage from '../pages/PublicNewsPage.jsx'
import StaffCategoriesPage from '../pages/StaffCategoriesPage.jsx'
import StaffNewsPage from '../pages/StaffNewsPage.jsx'
import RouteProtected from './RouteProtected.jsx'
import { ROUTES } from './RouteConstant.js'

export default function RouteConfig({ user, onSignIn, onSignOut }) {
  const defaultPath = user?.role === 'admin' ? ROUTES.adminAccounts : ROUTES.staffNews

  return (
    <Routes>
      <Route path={ROUTES.home} element={<PublicNewsPage user={user} />} />
      <Route
        path={ROUTES.login}
        element={user ? <Navigate to={defaultPath} replace /> : <LoginPage onSignIn={onSignIn} />}
      />
      <Route element={<RouteProtected user={user} roles={['admin']} onSignIn={onSignIn} onSignOut={onSignOut} />}>
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<Navigate to="accounts" replace />} />
          <Route path="accounts" element={<AdminAccountsPage />} />
          <Route path="profile" element={<ProfilePage />} />
        </Route>
      </Route>
      <Route element={<RouteProtected user={user} roles={['staff']} onSignIn={onSignIn} onSignOut={onSignOut} />}>
        <Route path="/staff" element={<StaffLayout />}>
          <Route index element={<Navigate to="news" replace />} />
          <Route path="news" element={<StaffNewsPage />} />
          <Route path="categories" element={<StaffCategoriesPage />} />
          <Route path="history" element={<StaffNewsPage history />} />
          <Route path="profile" element={<ProfilePage />} />
        </Route>
      </Route>
      <Route path="*" element={<Navigate to={ROUTES.home} replace />} />
    </Routes>
  )
}
