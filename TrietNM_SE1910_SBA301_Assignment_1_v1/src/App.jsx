import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { DataProvider } from './context/DataContext'
import LoginLayout from './layouts/LoginLayout'
import AdminLayout from './layouts/AdminLayout'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import CategoryManagement from './pages/CategoryManagement'
import NewsManagement from './pages/NewsManagement'
import UserManagement from './pages/UserManagement'
import SettingsPage from './pages/SettingsPage'

function App() {
  return (
      <DataProvider>
        <AuthProvider>
          <BrowserRouter>
            <Routes>
              <Route element={<LoginLayout />}>
                <Route path="/login" element={<LoginPage />} />
              </Route>
              <Route element={<AdminLayout />}>
                <Route index element={<DashboardPage />} />
                <Route path="categories" element={<CategoryManagement />} />
                <Route path="news" element={<NewsManagement />} />
                <Route path="users" element={<UserManagement />} />
                <Route path="settings" element={<SettingsPage />} />
              </Route>
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </BrowserRouter>
        </AuthProvider>
      </DataProvider>
  )
}

export default App
