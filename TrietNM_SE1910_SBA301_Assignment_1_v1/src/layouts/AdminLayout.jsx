import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Header from "../components/common/Header.jsx";
import Sidebar from "../components/common/SideBar.jsx";
import Footer from "../components/common/Footer.jsx";


export default function AdminLayout() {
    const { currentUser } = useAuth()
    if (!currentUser) return <Navigate to="/login" replace /> // auth guard
    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />
            <div className="d-flex flex-grow-1 flex-column flex-md-row">
                <Sidebar/>
                <main className="flex-grow-1 p-3 p-md-4" style={{ minWidth: 0 }}><Outlet /></main>
            </div>
            <Footer />
        </div>
    )
}
