import { useState } from 'react'
import { Table, Button, Modal, Form, Alert, InputGroup } from 'react-bootstrap'
import ConfirmModal from './ConfirmModal'
import { Icon } from './ui'


export default function EntityManager({ title, items, setItems, columns, fields, emptyForm, searchText, validate, deleteMessage }) {
    const [keyword, setKeyword] = useState('')
    const [modal, setModal] = useState({ show: false, mode: 'create' })
    const [form, setForm] = useState(emptyForm)
    const [error, setError] = useState('')
    const [toDelete, setToDelete] = useState(null)

    const kw = keyword.trim().toLowerCase()
    const shown = items.filter((i) => (searchText(i) || '').toLowerCase().includes(kw))

    const openCreate = () => { setForm(emptyForm); setError(''); setModal({ show: true, mode: 'create' }) }
    const openEdit = (item) => { setForm({ ...item }); setError(''); setModal({ show: true, mode: 'update' }) }
    const closeModal = () => setModal((m) => ({ ...m, show: false }))

    const handleChange = (f, value) =>
        setForm({ ...form, [f.name]: f.number ? Number(value) : value })

    const handleSubmit = (e) => {
        e.preventDefault()
        const err = validate(form, items, modal.mode)
        if (err) return setError(err)
        if (modal.mode === 'create') {
            const id = Math.max(0, ...items.map((i) => i.id)) + 1
            setItems([...items, { ...form, id }])
        } else {
            setItems(items.map((i) => (i.id === form.id ? { ...i, ...form } : i)))
        }
        closeModal()
    }

    const confirmDelete = () => {
        setItems(items.filter((i) => i.id !== toDelete.id)) // immutable remove theo id
        setToDelete(null)
    }

    return (
        <>
            <div className="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
                <div>
                    <h4 className="fw-bold mb-1">{title} Management</h4>
                    <span className="text-muted">{items.length} bản ghi</span>
                </div>
                <Button onClick={openCreate}><Icon name="plus" size={16} />Add {title}</Button>
            </div>

            <div className="page-card">
                <div className="p-3 border-bottom">
                    <InputGroup style={{ maxWidth: 380 }}>
                        <InputGroup.Text className="bg-white"><Icon name="search" size={16} /></InputGroup.Text>
                        <Form.Control placeholder="Search..." value={keyword} onChange={(e) => setKeyword(e.target.value)} />
                        {keyword && <Button variant="outline-secondary" onClick={() => setKeyword('')}>Clear</Button>}
                    </InputGroup>
                </div>
                <Table responsive hover className="mb-0 align-middle">
                    <thead>
                    <tr>{columns.map((c) => <th key={c.label}>{c.label}</th>)}<th className="text-end" style={{ width: 120 }}>Actions</th></tr>
                    </thead>
                    <tbody>
                    {shown.length === 0 ? (
                        <tr><td colSpan={columns.length + 1} className="text-center text-muted py-5">
                            {items.length === 0 ? 'Chưa có dữ liệu.' : 'Không tìm thấy kết quả.'}
                        </td></tr>
                    ) : shown.map((item) => (
                        <tr key={item.id}>
                            {columns.map((c) => <td key={c.label}>{c.render ? c.render(item) : item[c.key]}</td>)}
                            <td className="text-end text-nowrap">
                                <Button size="sm" variant="light" className="me-1" title="Edit" onClick={() => openEdit(item)}><Icon name="edit" size={15} /></Button>
                                <Button size="sm" variant="light" className="text-danger" title="Delete" onClick={() => setToDelete(item)}><Icon name="trash" size={15} /></Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>

            <Modal show={modal.show} onHide={closeModal} centered backdrop="static">
                <Form onSubmit={handleSubmit} noValidate>
                    <Modal.Header closeButton>
                        <Modal.Title>{modal.mode === 'create' ? 'Add' : 'Update'} {title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {error && <Alert variant="danger">{error}</Alert>}
                        {fields.map((f) => (
                            <Form.Group className="mb-3" key={f.name}>
                                <Form.Label>{f.label}</Form.Label>
                                {f.options ? (
                                    <Form.Select value={form[f.name]} onChange={(e) => handleChange(f, e.target.value)}>
                                        {f.options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
                                    </Form.Select>
                                ) : (
                                    <Form.Control
                                        as={f.textarea ? 'textarea' : 'input'} rows={3}
                                        type={f.type || 'text'} value={form[f.name] ?? ''}
                                        onChange={(e) => handleChange(f, e.target.value)}
                                    />
                                )}
                            </Form.Group>
                        ))}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={closeModal}>Cancel</Button>
                        <Button type="submit">Save</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <ConfirmModal
                show={!!toDelete}
                message={toDelete ? deleteMessage(toDelete) : ''}
                onConfirm={confirmDelete}
                onCancel={() => setToDelete(null)}
            />
        </>
    )
}
