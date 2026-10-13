import { useState } from 'react'
import { Card, Form, Button, Alert } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
    const [form, setForm] = useState({ username: '', password: '' })
    const [errors, setErrors] = useState({})
    const [message, setMessage] = useState('')
    const { login } = useAuth()
    const navigate = useNavigate()

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

    const handleSubmit = (e) => {
        e.preventDefault()
        const errs = {}
        if (!form.username.trim()) errs.username = 'Vui lòng nhập username.'
        if (!form.password) errs.password = 'Vui lòng nhập password.'
        setErrors(errs)
        setMessage('')
        if (Object.keys(errs).length) return
        const result = login(form.username, form.password) // kiểm tra với users trong state
        if (result.ok) navigate('/')
        else setMessage(result.message)
    }

    return (
        <Card className="border-0 shadow-lg w-100" style={{ maxWidth: 420, borderRadius: 16 }}>
            <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                    <img src="/logo.svg" alt="logo" width="56" height="56" />
                    <h4 className="mt-3 mb-1 fw-bold">Chào mừng trở lại</h4>
                    <p className="text-muted mb-0">Đăng nhập để vào FUNews Admin</p>
                </div>
                {message && <Alert variant="danger" className="py-2">{message}</Alert>}
                <Form onSubmit={handleSubmit} noValidate>
                    <Form.Group className="mb-3">
                        <Form.Label className="fw-medium">Username</Form.Label>
                        <Form.Control name="username" size="lg" autoFocus autoComplete="username"
                                      value={form.username} onChange={handleChange} isInvalid={!!errors.username} />
                        <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-medium">Password</Form.Label>
                        <Form.Control name="password" type="password" size="lg" autoComplete="current-password"
                                      value={form.password} onChange={handleChange} isInvalid={!!errors.password} />
                        <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                    </Form.Group>
                    <Button type="submit" size="lg" className="w-100">Đăng nhập</Button>
                </Form>
            </Card.Body>
        </Card>
    )
}
