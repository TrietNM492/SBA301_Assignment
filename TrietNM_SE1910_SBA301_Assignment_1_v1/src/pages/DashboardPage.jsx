import { Row, Col, ProgressBar, Table } from 'react-bootstrap'
import { useData } from '../context/DataContext'
import { Icon, StatusBadge } from '../components/common/ui'

export default function DashboardPage() {
    const { categories, news, users } = useData()
    const stats = [
        { label: 'Categories', icon: 'tag', color: 'primary', total: categories.length, active: categories.filter((c) => c.status === 1).length, sub: 'active' },
        { label: 'News', icon: 'news', color: 'success', total: news.length, active: news.filter((n) => n.status === 1).length, sub: 'active' },
        { label: 'Users', icon: 'users', color: 'warning', total: users.length, active: users.filter((u) => u.role === 1).length, sub: 'admin' },
    ]
    const catName = (id) => categories.find((c) => c.id === id)?.name ?? '—'
    const latest = [...news].sort((a, b) => b.id - a.id).slice(0, 5)

    return (
        <>
            <div className="mb-4"><h4 className="fw-bold mb-1">Dashboard</h4><span className="text-muted">Tổng quan hệ thống</span></div>
            <Row className="g-3 mb-4">
                {stats.map((s) => (
                    <Col md={4} key={s.label}>
                        <div className="page-card p-3">
                            <div className="d-flex align-items-center gap-3 mb-3">
                                <div className={`stat-icon bg-${s.color}-subtle text-${s.color}`}><Icon name={s.icon} size={22} /></div>
                                <div><div className="text-muted small">{s.label}</div><div className="fs-3 fw-bold lh-1">{s.total}</div></div>
                            </div>
                            <ProgressBar variant={s.color} now={s.total ? (s.active / s.total) * 100 : 0} style={{ height: 6 }} />
                            <small className="text-muted">{s.active}/{s.total} {s.sub}</small>
                        </div>
                    </Col>
                ))}
            </Row>
            <div className="page-card">
                <div className="p-3 fw-semibold border-bottom">Tin mới nhất</div>
                <Table responsive hover className="mb-0 align-middle">
                    <thead><tr><th>Title</th><th>Category</th><th>Created by</th><th>Status</th></tr></thead>
                    <tbody>
                    {latest.length === 0 ? <tr><td colSpan={4} className="text-center text-muted py-4">Chưa có dữ liệu.</td></tr>
                        : latest.map((n) => (
                            <tr key={n.id}><td className="fw-medium">{n.title}</td><td>{catName(n.categoryId)}</td><td>{n.createdBy}</td><td><StatusBadge status={n.status} /></td></tr>
                        ))}
                    </tbody>
                </Table>
            </div>
        </>
    )
}
