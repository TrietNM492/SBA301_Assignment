import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AppAlert from "../components/AppAlert";
import AppTable from "../components/AppTable";
import Paging from "../components/Paging";
import { getMyBookings } from "../services/bookingService";

const statusLabel = (status) => ({ 0: "Chờ xác nhận", 1: "Đã xác nhận", 2: "Đã hủy" }[status] || "Không xác định");

export default function HistoryPage() {
  const location = useLocation();
  const [result, setResult] = useState({ content: [], pagination: { pageNumber: 0, totalPages: 0 } });
  const [error, setError] = useState("");

  const load = async (page = 0) => {
    try {
      setError("");
      setResult(await getMyBookings(page));
    } catch (requestError) {
      setError(requestError.response?.data?.message || "Không thể tải lịch sử đặt phòng.");
    }
  };

  useEffect(() => {
    let active = true;
    getMyBookings().then((response) => {
      if (active) setResult(response);
    }).catch((requestError) => {
      if (active) setError(requestError.response?.data?.message || "Không thể tải lịch sử đặt phòng.");
    });
    return () => { active = false; };
  }, []);

  const columns = [
    { key: "id", label: "Mã đặt phòng", render: (row) => <strong>CASA-{row.id}</strong> },
    { key: "bookingDate", label: "Ngày tạo" },
    { key: "bookingDetails", label: "Lưu trú", render: (row) => row.bookingDetails?.map((detail) => `Phòng ${detail.roomResponse?.roomNumber} · ${detail.startDate} – ${detail.endDate}`).join(", ") || "—" },
    { key: "totalPrice", label: "Tổng tiền", render: (row) => `${Number(row.totalPrice || 0).toLocaleString("vi-VN")}đ` },
    { key: "bookingStatus", label: "Trạng thái", render: (row) => <span className={`status-pill status-${row.bookingStatus}`}>{statusLabel(row.bookingStatus)}</span> },
  ];

  return (
    <section className="section account-section">
      <div className="page-heading"><span className="eyebrow">NHỮNG CHUYẾN ĐI CỦA BẠN</span><h1>Lịch sử đặt phòng</h1><p>Tất cả yêu cầu lưu trú được tổng hợp tại đây.</p></div>
      {location.state?.message && <AppAlert tone="success">{location.state.message}</AppAlert>}
      <AppAlert>{error}</AppAlert>
      <div className="panel history-panel"><AppTable columns={columns} rows={result.content || []} emptyText="Bạn chưa có yêu cầu đặt phòng nào." /><Paging page={result.pagination?.pageNumber || 0} totalPages={result.pagination?.totalPages || 0} onChange={load} /></div>
    </section>
  );
}
