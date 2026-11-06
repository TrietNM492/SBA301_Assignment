import AppButton from "./AppButton";

export default function Paging({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null;
  return (
    <div className="paging">
      <AppButton variant="outline" disabled={page <= 0} onClick={() => onChange(page - 1)}>Trước</AppButton>
      <span>Trang {page + 1} / {totalPages}</span>
      <AppButton variant="outline" disabled={page >= totalPages - 1} onClick={() => onChange(page + 1)}>Tiếp</AppButton>
    </div>
  );
}