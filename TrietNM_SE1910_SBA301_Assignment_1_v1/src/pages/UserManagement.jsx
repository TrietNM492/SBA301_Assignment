import EntityManager from '../components/common/EntityManager'
import { useData } from '../context/DataContext'
import { RoleBadge } from '../components/common/ui'

export default function UserManagement() {
    const { users, setUsers } = useData()
    return (
        <EntityManager
            title="User"
            items={users}
            setItems={(next) => (next.some((u) => u.role === 1) ? setUsers(next) : alert('Phải còn ít nhất một tài khoản Admin.'))}
            columns={[
                { key: 'id', label: 'ID' },
                { key: 'username', label: 'Username' },
                { key: 'email', label: 'Email' },
                { label: 'Role', render: (u) => <RoleBadge role={u.role} /> },
            ]}
            fields={[
                { name: 'username', label: 'Username' },
                { name: 'password', label: 'Password', type: 'password' },
                { name: 'email', label: 'Email', type: 'email' },
                { name: 'role', label: 'Role', number: true, options: [{ value: 1, label: 'Admin' }, { value: 2, label: 'Staff' }] },
            ]}
            emptyForm={{ username: '', password: '', email: '', role: 2 }}
            searchText={(u) => u.username}
            validate={(f, items) => {
                if (!f.username.trim()) return 'Username không được để trống.'
                if (!f.password.trim()) return 'Password không được để trống.'
                if (items.some((u) => u.id !== f.id && u.username.toLowerCase() === f.username.trim().toLowerCase()))
                    return 'Username đã tồn tại.'
                return ''
            }}
            deleteMessage={(u) => `Bạn có chắc muốn xóa user "${u.username}"?`}
        />
    )
}
