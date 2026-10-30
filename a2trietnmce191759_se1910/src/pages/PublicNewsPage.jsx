import { useCallback, useState } from 'react'
import { Link } from 'react-router'
import usePagedData from '../hooks/usePagedData.js'
import { newsService } from '../services/newsService.js'
import { PageHeader, Pagination, SearchBar } from '../components/ManagementPage.jsx'

export default function PublicNewsPage({ user }) {
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const fetchPage = useCallback(
    () => newsService.list({ key: search, page, size: 12 }),
    [search, page],
  )
  const { items, pagination, loading, error } = usePagedData(fetchPage, search, page, 0)
  const articles = items.filter((article) => article.newsStatus === true)

  return (
    <div className="public-page">
      <header className="public-nav">
        <Link className="brand" to="/"><span className="brand-mark">F</span><span>FUNews<span className="brand-subtitle">THE NEWSROOM</span></span></Link>
        <Link className="button button-outline" to={user ? (user.role === 'admin' ? '/admin/accounts' : '/staff/news') : '/login'}>
          {user ? 'Open workspace' : 'Staff sign in'} <span>→</span>
        </Link>
      </header>
      <section className="public-hero">
        <div className="public-hero-copy">
          <p className="eyebrow">LATEST FROM THE NEWSROOM</p>
          <h1>Stories that<br /><em>move us forward.</em></h1>
          <p>Discover the latest updates and perspectives from our editorial team.</p>
        </div>
        <div className="hero-stamp"><span>F</span><small>FACTS<br />FIRST</small></div>
      </section>
      <section className="public-content">
        <PageHeader
          eyebrow="PUBLICATION"
          title="Latest stories"
          description="Published articles available to everyone."
          action={<SearchBar value={search} onChange={(value) => { setSearch(value); setPage(0) }} placeholder="Search articles" />}
        />
        {error && <div className="notice notice-error" role="alert">{error}</div>}
        {loading ? <div className="loading-state">Loading the latest stories…</div> : (
          <div className="article-grid">
            {articles.map((article, index) => (
              <article className={`article-card article-card-${index % 3}`} key={article.newsArticleID}>
                <div className="article-art"><span>0{index + 1}</span><b>FUNews<br />Journal</b></div>
                <div className="article-copy">
                  <div className="article-meta">
                    <span>{article.newsSource || 'FUNews'}</span>
                    <time>{article.createdDate ? new Date(article.createdDate).toLocaleDateString() : 'Recently published'}</time>
                  </div>
                  <h2>{article.newsTitle}</h2>
                  {article.headline && <p className="article-headline">{article.headline}</p>}
                  <p className="article-content">{article.newsContent || 'Read the latest update from our newsroom.'}</p>
                </div>
              </article>
            ))}
            {!articles.length && <div className="empty-panel">No active articles found. Try another search.</div>}
          </div>
        )}
        {!loading && pagination && <Pagination pagination={pagination} page={page} onPageChange={setPage} />}
      </section>
      <footer className="public-footer"><span>FUNews Editorial</span><span>Independent stories, thoughtfully told.</span></footer>
    </div>
  )
}
