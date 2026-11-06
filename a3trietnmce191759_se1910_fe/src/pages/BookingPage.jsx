import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AppAlert from "../components/AppAlert";
import AppButton from "../components/AppButton";
import { getRooms } from "../services/roomService";
import { createBooking } from "../services/bookingService";

export default function BookingPage() {
  const [rooms, setRooms] = useState([]);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    getRooms(0, 100).then((response) => setRooms((response.content || []).filter((room) => room.roomStatus === 1)))
      .catch((requestError) => setError(requestError.response?.data?.message || "Không thể tải danh sách phòng."));
  }, []);

  const submit = async (event) => {
    event.preventDefault();
    setError("");
    setBusy(true);
    const form = Object.fromEntries(new FormData(event.currentTarget).entries());
    if (new Date(form.endDate) <= new Date(form.startDate)) {
      setError("Ngày trả phòng phải sau ngày nhận phòng.");
      setBusy(false);
      return;
    }
    try {
      await createBooking({
        bookingDate: new Date().toISOString().slice(0, 10),
        bookingDetails: [{ roomId: Number(form.roomId), startDate: form.startDate, endDate: form.endDate }],
      });
      navigate("/history", { state: { message: "Yêu cầu đặt phòng đã được gửi." } });
    } catch (requestError) {
      setError(requestError.response?.data?.message || "Không thể tạo đặt phòng. Vui lòng thử lại.");
    } finally {
      setBusy(false);
    }
  };

  return (
    <section className="section account-section">
      <div className="page-heading"><span className="eyebrow">KỲ NGHỈ TIẾP THEO</span><h1>Đặt phòng</h1><p>Chọn phòng và ngày lưu trú, chúng tôi sẽ xác nhận yêu cầu của bạn.</p></div>
      <div className="booking-layout">
        <div className="panel booking-panel"><AppAlert>{error}</AppAlert>
          {rooms.length ? <form className="form-stack" onSubmit={submit}>
            <label>Phòng lưu trú<select name="roomId" required defaultValue=""><option value="" disabled>Chọn phòng</option>{rooms.map((room) => <option key={room.roomId} value={room.roomId}>Phòng {room.roomNumber} · {room.roomTypeName || "Casa"} · {Number(room.roomPricePerDay || 0).toLocaleString("vi-VN")}đ / đêm</option>)}</select></label>
            <div className="form-grid">
              <label>Ngày nhận phòng<input name="startDate" type="date" min={new Date().toISOString().slice(0, 10)} required /></label>
              <label>Ngày trả phòng<input name="endDate" type="date" min={new Date().toISOString().slice(0, 10)} required /></label>
            </div>
            <p className="muted">Giá cuối cùng được tính theo số đêm và giá phòng hiện tại.</p>
            <AppButton type="submit" disabled={busy}>{busy ? "Đang gửi yêu cầu..." : "Gửi yêu cầu đặt phòng"} <span aria-hidden="true">→</span></AppButton>
          </form> : <div className="empty-state">Hiện chưa có phòng đang mở đặt. <Link to="/">Quay lại trang chủ</Link></div>}
        </div>
        <aside className="booking-note"><span className="eyebrow">LƯU Ý</span><h3>Đặt phòng linh hoạt</h3><p>Yêu cầu của bạn sẽ ở trạng thái chờ xác nhận. Theo dõi trạng thái mới nhất trong lịch sử đặt phòng.</p><Link to="/history" className="text-link">Xem lịch sử đặt <span aria-hidden="true">→</span></Link></aside>
      </div>
    </section>
  );
}
