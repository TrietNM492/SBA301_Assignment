import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AppAlert from "../components/AppAlert";
import AppButton from "../components/AppButton";
import Paging from "../components/Paging";
import { getRooms } from "../services/roomService";

const money = (value) => Number(value || 0).toLocaleString("vi-VN");

export default function HomePage() {
  const [rooms, setRooms] = useState([]);
  const [pagination, setPagination] = useState({ pageNumber: 0, totalPages: 0 });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const loadRooms = async (page = 0) => {
    setLoading(true);
    setError("");
    try {
      const response = await getRooms(page);
      setRooms((response.content || []).filter((room) => room.roomStatus === 1));
      setPagination(response.pagination || { pageNumber: page, totalPages: 0 });
    } catch (requestError) {
      setError(requestError.response?.data?.message || "Không thể tải danh sách phòng. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let active = true;
    getRooms().then((response) => {
      if (!active) return;
      setRooms((response.content || []).filter((room) => room.roomStatus === 1));
      setPagination(response.pagination || { pageNumber: 0, totalPages: 0 });
    }).catch((requestError) => {
      if (active) setError(requestError.response?.data?.message || "Không thể tải danh sách phòng. Vui lòng thử lại.");
    }).finally(() => {
      if (active) setLoading(false);
    });
    return () => { active = false; };
  }, []);

  return (
    <>
      <section className="hero-section">
        <div className="hero-shade" />
        <div className="hero-content">
          <span className="eyebrow eyebrow-light">KỲ NGHỈ CỦA RIÊNG BẠN</span>
          <h1>Nơi những ngày bình yên<br />bắt đầu.</h1>
          <p>Không gian nghỉ dưỡng tinh tế, dịch vụ chu đáo và những khoảnh khắc đáng nhớ đang chờ bạn.</p>
          <Link to="/booking" className="button button-gold">Khám phá phòng <span aria-hidden="true">→</span></Link>
        </div>
        <div className="hero-caption">CASA RESORT · EST. 2018</div>
      </section>
      <section className="section room-section">
        <div className="section-heading">
          <div><span className="eyebrow">LƯU TRÚ & TRẢI NGHIỆM</span><h2>Chọn không gian của bạn</h2></div>
          <p>Mỗi căn phòng là một khoảng lặng được chăm chút dành riêng cho bạn.</p>
        </div>
        <AppAlert>{error}</AppAlert>
        {loading ? <div className="loading-state">Đang tải phòng...</div> : rooms.length ? (
          <div className="room-grid">
            {rooms.map((room, index) => (
              <article className="room-card" key={room.roomId}>
                <div className={`room-art room-art-${index % 3}`}>
                  <span className="room-type">{room.roomTypeName || "CASA SUITE"}</span>
                  <span className="room-art-number">{String(room.roomNumber || room.roomId).padStart(2, "0")}</span>
                </div>
                <div className="room-card-body">
                  <div className="room-card-title"><h3>Phòng {room.roomNumber}</h3><span>◈ {room.roomMaxCapacity || 2} khách</span></div>
                  <p>{room.roomDetailDescription || "Không gian nghỉ dưỡng tiện nghi, được thiết kế để bạn tận hưởng trọn vẹn kỳ nghỉ."}</p>
                  <div className="room-card-bottom">
                    <span><strong>{money(room.roomPricePerDay)}đ</strong> / đêm</span>
                    <Link to="/booking" className="room-link">Chi tiết <span aria-hidden="true">↗</span></Link>
                  </div>
                </div>
              </article>
            ))}
          </div>
        ) : <div className="empty-state">Hiện chưa có phòng để hiển thị.</div>}
        <Paging page={pagination.pageNumber || 0} totalPages={pagination.totalPages || 0} onChange={loadRooms} />
      </section>
      <section className="experience-band">
        <div><span className="eyebrow eyebrow-light">CASA MOMENTS</span><h2>Chậm lại một nhịp.<br />Tận hưởng nhiều hơn.</h2></div>
        <p>Từ buổi sáng ngập nắng đến những tối an yên, Casa là nơi bạn tìm thấy cảm giác thân thuộc trong một trải nghiệm thật khác.</p>
        <Link to="/register" className="text-link">Bắt đầu hành trình <span aria-hidden="true">→</span></Link>
      </section>
      <section className="section concierge">
        <div className="concierge-icon">✳</div>
        <div><span className="eyebrow">ĐỒNG HÀNH CÙNG BẠN</span><h2>Một kỳ nghỉ dễ dàng hơn</h2><p>Đăng nhập để quản lý hồ sơ, đặt phòng và theo dõi lịch sử lưu trú của bạn.</p></div>
        <Link to="/login/customer" className="button button-outline">Tài khoản của tôi</Link>
        <AppButton variant="text" onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}>Lên đầu trang ↑</AppButton>
      </section>
    </>
  );
}
