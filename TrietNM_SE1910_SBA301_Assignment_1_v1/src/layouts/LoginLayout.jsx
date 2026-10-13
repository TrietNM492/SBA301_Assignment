import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginLayout() {
    const { currentUser } = useAuth()
    if (currentUser) return <Navigate to="/" replace />
    return (
        <div className="login-bg d-flex justify-content-center align-items-center p-3">
            <Outlet />
        </div>
    )
}
