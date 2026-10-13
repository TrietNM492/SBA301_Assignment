import { Button } from 'react-bootstrap'
import { Icon } from './ui'
import {useAuth} from "../../context/AuthContext.jsx";

export default function Header() {
    const { currentUser, logout } = useAuth()
    return (
        <header className="app-header d-flex align-items-center justify-content-between px-3 px-md-4">
            <div className="d-flex align-items-center gap-2 fw-semibold">
                <img src="/FUNBie_v2.png" alt="logo" width="34" height="34" />
                <span className="d-none d-sm-inline">FUNewsManagementSystem</span>
            </div>
            <div className="d-flex align-items-center gap-3">
                <div className="text-end lh-sm">
                    <div className="fw-medium">{currentUser.username}</div>
                    <small className="text-muted">Administrator</small>
                </div>
                <Button size="sm" variant="outline-secondary" onClick={logout}><Icon name="logout" size={16} />Logout</Button>
            </div>
        </header>
    )
}
