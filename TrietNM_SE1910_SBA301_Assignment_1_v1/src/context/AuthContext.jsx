import { createContext, useContext, useState } from 'react'
import { useData } from './DataContext'

const AuthContext = createContext(null)
export const useAuth = () => useContext(AuthContext)

export function AuthProvider({ children }) {
    const { users } = useData()

    const [userId, setUserId] = useState(() => Number(localStorage.getItem('fun_userId')) || null)
    const currentUser = users.find((u) => u.id === userId && u.role === 1) || null


    const login = (username, password) => {
        const u = users.find(
            (x) => x.username.toLowerCase() === username.trim().toLowerCase() && x.password === password
        )
        if (!u) return { ok: false, message: 'Sai username hoặc password.' }
        if (u.role !== 1) return { ok: false, message: 'Tài khoản không có quyền truy cập trang quản trị.' }
        localStorage.setItem('fun_userId', u.id)
        setUserId(u.id)
        return { ok: true }
    }
    const logout = () => { localStorage.removeItem('fun_userId'); setUserId(null) }

    return <AuthContext.Provider value={{ currentUser, login, logout }}>{children}</AuthContext.Provider>
}
