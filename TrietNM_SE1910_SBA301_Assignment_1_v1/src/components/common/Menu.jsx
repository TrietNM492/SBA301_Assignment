import { Nav } from 'react-bootstrap'
import { NavLink } from 'react-router-dom'
import { Icon } from './ui'


export const menuItems = [
    { to: '/', label: 'Dashboard', icon: 'dashboard', end: true },
    { to: '/categories', label: 'Category', icon: 'tag' },
    { to: '/news', label: 'News', icon: 'news' },
    { to: '/users', label: 'Users', icon: 'users' },
    { to: '/settings', label: 'Settings', icon: 'settings' },
]

export default function Menu() {
    return (
        <Nav className="flex-row flex-md-column flex-nowrap gap-1">
            {menuItems.map((m) => (
                <Nav.Link key={m.to} as={NavLink} to={m.to} end={m.end}>
                    <Icon name={m.icon} />{m.label}
                </Nav.Link>
            ))}
        </Nav>
    )
}
