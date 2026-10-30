import { useState } from 'react'
import { BrowserRouter } from 'react-router'
import RouteConfig from './routes/RouteConfig.jsx'
import { authService } from './services/authService.js'

const SESSION_KEY = 'funews-session'

function readSession() {
  try {
    const stored = localStorage.getItem(SESSION_KEY)
    return stored ? JSON.parse(stored) : null
  } catch {
    return null
  }
}

function App() {
  const [user, setUser] = useState(readSession)

  function signIn(nextUser) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(nextUser))
    setUser(nextUser)
  }

  function signOut() {
    localStorage.removeItem(SESSION_KEY)
    setUser(null)
    authService.logout().catch((error) => {
      console.error('Could not invalidate the backend session during logout.', error)
    })
  }

  return (
    <BrowserRouter>
      <RouteConfig user={user} onSignIn={signIn} onSignOut={signOut} />
    </BrowserRouter>
  )
}

export default App
