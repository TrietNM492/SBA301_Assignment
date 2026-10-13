import { Modal, Button } from 'react-bootstrap'

export default function ConfirmModal({ show, message, onConfirm, onCancel }) {
    return (
        <Modal show={show} onHide={onCancel} centered>
            <Modal.Header closeButton><Modal.Title>Xác nhận xóa</Modal.Title></Modal.Header>
            <Modal.Body>{message}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onCancel}>Cancel</Button>
                <Button variant="danger" onClick={onConfirm}>Delete</Button>
            </Modal.Footer>
        </Modal>
    )
}
