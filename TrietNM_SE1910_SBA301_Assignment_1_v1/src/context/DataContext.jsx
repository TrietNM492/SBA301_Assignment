import { createContext, useContext, useEffect, useState } from 'react'
import { initCategories, initNews, initUsers } from '../data/mockData'

const DataContext = createContext(null)
export const useData = () => useContext(DataContext)


function usePersistentList(key, initial) {
    const [list, setList] = useState(() => {
        try {
            const v = JSON.parse(localStorage.getItem(key))
            return Array.isArray(v) ? v : initial
        } catch { return initial }
    })
    useEffect(() => { localStorage.setItem(key, JSON.stringify(list)) }, [key, list])
    return [list, setList]
}

export function DataProvider({ children }) {
    const [categories, setCategories] = usePersistentList('fun_categories', initCategories)
    const [news, setNews] = usePersistentList('fun_news', initNews)
    const [users, setUsers] = usePersistentList('fun_users', initUsers)
    const resetData = () => { setCategories(initCategories); setNews(initNews); setUsers(initUsers) }
    return (
        <DataContext.Provider value={{ categories, setCategories, news, setNews, users, setUsers, resetData }}>
            {children}
        </DataContext.Provider>
    )
}
