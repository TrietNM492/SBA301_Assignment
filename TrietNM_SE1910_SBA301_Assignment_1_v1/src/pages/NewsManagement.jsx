import EntityManager from '../components//common/EntityManager'
import { useData } from '../context/DataContext'
import { useAuth } from '../context/AuthContext'
import { StatusBadge } from '../components/common/ui.jsx'

export default function NewsManagement() {
    const { news, setNews, categories } = useData()
    const { currentUser } = useAuth()
    const catName = (id) => categories.find((c) => c.id === id)?.name ?? '(deleted)'

    return (
        <EntityManager
            title="News"
            items={news}
            setItems={setNews}
            columns={[
                { key: 'id', label: 'ID' },
                { key: 'title', label: 'Title' },
                { label: 'Category', render: (n) => catName(n.categoryId) },
                { key: 'createdBy', label: 'Created By' },
                { key: 'tags', label: 'Tags' },
                { label: 'Status', render: (n) => <StatusBadge status={n.status} /> },
            ]}
            fields={[
                { name: 'title', label: 'Title' },
                { name: 'content', label: 'Content', textarea: true },
                { name: 'categoryId', label: 'Category', number: true, options: categories.map((c) => ({ value: c.id, label: c.name })) },
                { name: 'tags', label: 'Tags (cách nhau bằng dấu phẩy)' },
                { name: 'status', label: 'Status', number: true, options: [{ value: 1, label: 'Active' }, { value: 0, label: 'Inactive' }] },
            ]}
            emptyForm={{ title: '', content: '', categoryId: categories[0]?.id ?? 0, createdBy: currentUser.username, status: 1, tags: '' }}
            searchText={(n) => `${n.title} ${n.content}`}
            validate={(f) => {
                if (!f.title.trim()) return 'Title không được để trống.'
                if (!f.content.trim()) return 'Content không được để trống.'
                if (!categories.some((c) => c.id === f.categoryId)) return 'Vui lòng chọn Category hợp lệ.'
                return ''
            }}
            deleteMessage={(n) => `Bạn có chắc muốn xóa bài "${n.title}"?`}
        />
    )
}
