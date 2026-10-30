export function PageHeader({ eyebrow, title, description, action }) {
  return (
    <div className="page-heading">
      <div>
        <p className="eyebrow">{eyebrow}</p>
        <h1>{title}</h1>
        <p className="page-description">{description}</p>
      </div>
      {action}
    </div>
  )
}

export function SearchBar({ value, onChange, placeholder = 'Search by keyword' }) {
  return (
    <label className="search-field">
      <span aria-hidden="true">⌕</span>
      <input
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder={placeholder}
        aria-label={placeholder}
      />
    </label>
  )
}

export function Pagination({ pagination, page, onPageChange }) {
  const totalPages = pagination?.totalPages ?? 0
  const totalElements = pagination?.totalElements ?? 0
  return (
    <div className="pagination-bar">
      <span>{totalElements} total · Page {totalPages ? page + 1 : 0} of {totalPages}</span>
      <div className="pagination-actions">
        <button className="button button-outline" disabled={page <= 0} onClick={() => onPageChange(page - 1)}>
          Previous
        </button>
        <button className="button button-outline" disabled={!totalPages || page + 1 >= totalPages} onClick={() => onPageChange(page + 1)}>
          Next
        </button>
      </div>
    </div>
  )
}

export function StatusPill({ active }) {
  return <span className={`status-pill ${active ? 'status-active' : 'status-inactive'}`}>{active ? 'Active' : 'Inactive'}</span>
}
