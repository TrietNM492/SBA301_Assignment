export default function AppAlert({ children, tone = "error", onClose }) {
  if (!children) return null;
  return (
    <div className={`alert alert-${tone}`} role={tone === "error" ? "alert" : "status"}>
      <span>{children}</span>
      {onClose && <button type="button" aria-label="Đóng thông báo" onClick={onClose}>×</button>}
    </div>
  );
}