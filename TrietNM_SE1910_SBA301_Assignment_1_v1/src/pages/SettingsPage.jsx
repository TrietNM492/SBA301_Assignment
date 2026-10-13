import { Row, Col, Button } from 'react-bootstrap'
import { useAuth } from '../context/AuthContext'
import { useData } from '../context/DataContext'
import { RoleBadge } from '../components/common/ui'

export default function SettingsPage() {
    const { currentUser } = useAuth()
    const { resetData } = useData()
    return (
        <>
            <div className="mb-4"><h4 className="fw-bold mb-1">Settings</h4><span className="text-muted">Hồ sơ và dữ liệu hệ thống</span></div>
            <Row className="g-3">
                <Col md={6}>
                    <div className="page-card p-4 d-flex align-items-center gap-3">
                        <div className="avatar">{currentUser.username[0].toUpperCase()}</div>
                        <div>
                            <div className="fw-semibold fs-5">{currentUser.username}</div>
                            <div className="text-muted mb-1">{currentUser.email || 'Chưa có email'}</div>
                            <RoleBadge role={currentUser.role} />
                        </div>
                    </div>
                </Col>
                <Col md={6}>
                    <div className="page-card p-4">
                        <div className="fw-semibold mb-1">Dữ liệu mẫu</div>
                        <p className="text-muted small">Khôi phục Category, News, Users về dữ liệu ban đầu. Tài khoản đang đăng nhập có thể bị đăng xuất nếu không còn trong dữ liệu mẫu.</p>
                        <Button variant="outline-danger" onClick={() => window.confirm('Reset toàn bộ dữ liệu về mặc định?') && resetData()}>Reset mock data</Button>
                    </div>
                </Col>
            </Row>
        </>
    )
}
