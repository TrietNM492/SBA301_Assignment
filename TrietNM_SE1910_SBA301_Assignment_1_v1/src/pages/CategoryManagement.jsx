import EntityManager from '../components/common/EntityManager'
import { useData } from '../context/DataContext'
import { StatusBadge } from '../components/common/ui'

export default function CategoryManagement() {
    const { categories, setCategories, news } = useData()

    return (
        <EntityManager
            title="Category"
            items={categories}
            setItems={(next) => {

                const removed = categories.filter((c) => !next.find((n) => n.id === c.id))
                if (removed.some((c) => news.some((n) => n.categoryId === c.id)))
                    return alert('Không thể xóa: Category đang được News sử dụng.')
                setCategories(next)
            }}
            columns={[
                { key: 'id', label: 'ID' },
                { key: 'name', label: 'Name' },
                { label: 'Status', render: (c) => <StatusBadge status={c.status} /> },
            ]}
            fields={[
                { name: 'name', label: 'Name' },
                { name: 'status', label: 'Status', number: true, options: [{ value: 1, label: 'Active' }, { value: 0, label: 'Inactive' }] },
            ]}
            emptyForm={{ name: '', status: 1 }}
            searchText={(c) => c.name}
            validate={(f) => (!f.name.trim() ? 'Name không được để trống.' : '')}
            deleteMessage={(c) => `Bạn có chắc muốn xóa category "${c.name}"?`}
        />
    )
}
