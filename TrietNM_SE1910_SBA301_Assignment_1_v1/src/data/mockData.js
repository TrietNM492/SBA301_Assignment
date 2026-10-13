// status: 1 = active, 0 = inactive | role: 1 = Admin, 2 = Staff
export const initCategories = [
    { id: 1, name: 'Technology', status: 1 },
    { id: 2, name: 'Sports', status: 1 },
    { id: 3, name: 'Education', status: 0 },
]
export const initNews = [
    { id: 1, title: 'React 19 released', content: 'New features in React 19.', categoryId: 1, createdBy: 'Admin', status: 1, tags: 'react,web' },
    { id: 2, title: 'Final match tonight', content: 'Big football match.', categoryId: 2, createdBy: 'Staff01', status: 1, tags: 'football' },
    { id: 3, title: 'New semester plan', content: 'Schedule for next term.', categoryId: 3, createdBy: 'Admin', status: 0, tags: '' },
]
export const initUsers = [
    { id: 1, username: 'Admin', password: 'Admin', email: 'admin@fun.edu', role: 1 },
    { id: 2, username: 'Staff01', password: '123456', email: 'staff01@fun.edu', role: 2 },
]
export const roleLabel = (r) => (r === 1 ? 'Admin' : 'Staff')
export const statusLabel = (s) => (s === 1 ? 'Active' : 'Inactive')
