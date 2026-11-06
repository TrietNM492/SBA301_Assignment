import { useEffect, useState } from "react";
import AppAlert from "../components/AppAlert";
import AppButton from "../components/AppButton";
import AppTable from "../components/AppTable";
import Paging from "../components/Paging";
import { createCustomer, deleteCustomer, getCustomers, updateCustomer } from "../services/customerService";
import { getStaffBookings, updateBookingStatus } from "../services/staffBookingService";
import { createRoom, deleteRoom, getStaffRooms, updateRoom } from "../services/staffRoomService";

const TABS = [
  { id: "customers", label: "Khách hàng", title: "Quản lý khách hàng" },
  { id: "rooms", label: "Phòng", title: "Quản lý phòng" },
  { id: "bookings", label: "Đặt phòng", title: "Quản lý đặt phòng" },
];
const statusLabel = (status) => ({ 0: "Chờ xác nhận", 1: "Đã xác nhận", 2: "Đã hủy" }[status] || "Không xác định");

const getStaffData = (tab, page, search) => {
  if (tab === "customers") return getCustomers(page, 10, search);
  if (tab === "rooms") return getStaffRooms(page, 10, search);
  return getStaffBookings(page, 10, search);
};

export default function StaffDashboard() {
  const [tab, setTab] = useState("customers");
  const [page, setPage] = useState(0);
  const [rows, setRows] = useState([]);
  const [pagination, setPagination] = useState({ totalElements: 0, pageNumber: 0, totalPages: 0 });
  const [keyword, setKeyword] = useState("");
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState({ text: "", tone: "error" });
  const [editing, setEditing] = useState(null);
  const [saving, setSaving] = useState(false);
  const [bookingStatuses, setBookingStatuses] = useState({});
  const currentTab = TABS.find((item) => item.id === tab);

  const reload = async () => {
    setLoading(true);
    try {
      const response = await getStaffData(tab, page, search);
      setRows(response?.content || []);
      setPagination(response?.pagination || { totalElements: 0, pageNumber: page, totalPages: 0 });
    } catch (error) {
      setNotice({ text: error.response?.data?.message || "Không thể tải dữ liệu quản trị.", tone: "error" });
      setRows([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let active = true;
    getStaffData(tab, page, search).then((response) => {
      if (!active) return;
      setRows(response?.content || []);
      setPagination(response?.pagination || { totalElements: 0, pageNumber: page, totalPages: 0 });
    }).catch((error) => {
      if (active) {
        setNotice({ text: error.response?.data?.message || "Không thể tải dữ liệu quản trị.", tone: "error" });
        setRows([]);
      }
    }).finally(() => {
      if (active) setLoading(false);
    });
    return () => { active = false; };
  }, [tab, page, search]);

  const selectTab = (nextTab) => {
    if (nextTab === tab) return;
    setLoading(true);
    setNotice({ text: "", tone: "error" });
    setTab(nextTab);
    setPage(0);
    setSearch("");
    setKeyword("");
  };

  const save = async (event) => {
    event.preventDefault();
    setSaving(true);
    setNotice({ text: "", tone: "error" });
    const form = Object.fromEntries(new FormData(event.currentTarget).entries());
    try {
      if (tab === "customers") {
        const customer = {
          customerFullName: form.customerFullName,
          telephone: form.telephone,
          emailAddress: form.emailAddress,
          customerBirthday: form.customerBirthday || null,
          password: form.password || undefined,
        };
        if (editing?.id) await updateCustomer(editing.id, customer);
        else await createCustomer(customer);
      } else {
        const room = {
          roomNumber: form.roomNumber,
          roomDetailDescription: form.roomDetailDescription,
          roomMaxCapacity: Number(form.roomMaxCapacity),
          roomTypeId: Number(form.roomTypeId),
          roomStatus: Number(form.roomStatus),
          roomPricePerDay: Number(form.roomPricePerDay),
        };
        if (editing?.roomId) await updateRoom(editing.roomId, room);
        else await createRoom(room);
      }
      setEditing(null);
      setNotice({ text: "Đã lưu thay đổi.", tone: "success" });
      await reload();
    } catch (error) {
      setNotice({ text: error.response?.data?.message || error.message || "Không thể lưu thay đổi.", tone: "error" });
    } finally {
      setSaving(false);
    }
  };

  const remove = async (row) => {
    const label = tab === "customers" ? row.customerFullName : `phòng ${row.roomNumber}`;
    if (!window.confirm(`Xóa ${label}?`)) return;
    setNotice({ text: "", tone: "error" });
    try {
      if (tab === "customers") await deleteCustomer(row.id);
      else await deleteRoom(row.roomId);
      setNotice({ text: "Đã xử lý yêu cầu xóa.", tone: "success" });
      await reload();
    } catch (error) {
      setNotice({ text: error.response?.data?.message || "Không thể xóa mục này.", tone: "error" });
    }
  };

  const saveBookingStatus = async (booking) => {
    setNotice({ text: "", tone: "error" });
    try {
      await updateBookingStatus(booking.id, Number(bookingStatuses[booking.id] ?? booking.bookingStatus));
      setNotice({ text: `Đã cập nhật trạng thái CASA-${booking.id}.`, tone: "success" });
      await reload();
    } catch (error) {
      setNotice({ text: error.response?.data?.message || "Không thể cập nhật trạng thái đặt phòng.", tone: "error" });
    }
  };

  const columns = tab === "customers" ? [
    { key: "id", label: "Mã" },
    { key: "customerFullName", label: "Khách hàng", render: (row) => <><strong>{row.customerFullName}</strong><small className="table-subtitle">{row.emailAddress}</small></> },
    { key: "telephone", label: "Điện thoại" },
    { key: "customerStatus", label: "Tình trạng", render: (row) => <span className={`status-pill status-${row.customerStatus === 1 ? "active" : "inactive"}`}>{row.customerStatus === 1 ? "Hoạt động" : "Đã khóa"}</span> },
    { key: "actions", label: "Thao tác", render: (row) => <div className="row-actions"><button type="button" className="table-action" onClick={() => setEditing(row)}>Sửa</button><button type="button" className="table-action danger" onClick={() => remove(row)}>Xóa</button></div> },
  ] : tab === "rooms" ? [
    { key: "roomNumber", label: "Phòng", render: (row) => <strong>{row.roomNumber}</strong> },
    { key: "roomTypeName", label: "Loại phòng" },
    { key: "roomMaxCapacity", label: "Sức chứa", render: (row) => `${row.roomMaxCapacity || "—"} khách` },
    { key: "roomPricePerDay", label: "Giá / đêm", render: (row) => `${Number(row.roomPricePerDay || 0).toLocaleString("vi-VN")}đ` },
    { key: "roomStatus", label: "Tình trạng", render: (row) => <span className={`status-pill status-${row.roomStatus === 1 ? "active" : "inactive"}`}>{row.roomStatus === 1 ? "Đang mở" : "Ngừng"}</span> },
    { key: "actions", label: "Thao tác", render: (row) => <div className="row-actions"><button type="button" className="table-action" onClick={() => setEditing(row)}>Sửa</button><button type="button" className="table-action danger" onClick={() => remove(row)}>Xóa</button></div> },
  ] : [
    { key: "id", label: "Mã đặt phòng", render: (row) => <strong>CASA-{row.id}</strong> },
    { key: "customerResponse", label: "Khách hàng", render: (row) => row.customerResponse?.customerFullName || "—" },
    { key: "bookingDate", label: "Ngày tạo" },
    { key: "bookingDetails", label: "Chi tiết", render: (row) => row.bookingDetails?.map((detail) => `P.${detail.roomResponse?.roomNumber} · ${detail.startDate} – ${detail.endDate}`).join(", ") || "—" },
    { key: "totalPrice", label: "Tổng tiền", render: (row) => `${Number(row.totalPrice || 0).toLocaleString("vi-VN")}đ` },
    { key: "bookingStatus", label: "Trạng thái", render: (row) => <div className="booking-edit"><select aria-label={`Trạng thái CASA-${row.id}`} value={bookingStatuses[row.id] ?? row.bookingStatus} onChange={(event) => setBookingStatuses({ ...bookingStatuses, [row.id]: event.target.value })}><option value="0">Chờ xác nhận</option><option value="1">Đã xác nhận</option><option value="2">Đã hủy</option></select><button type="button" className="table-action" onClick={() => saveBookingStatus(row)}>Lưu</button><span className="sr-only">{statusLabel(row.bookingStatus)}</span></div> },
  ];

  const customerForm = editing ? {
    customerFullName: editing.customerFullName || "",
    telephone: editing.telephone || "",
    emailAddress: editing.emailAddress || "",
    customerBirthday: editing.customerBirthday || "",
    customerStatus: editing.customerStatus ?? 1,
  } : { customerFullName: "", telephone: "", emailAddress: "", customerBirthday: "", customerStatus: 1 };
  const roomForm = editing ? {
    roomNumber: editing.roomNumber || "",
    roomDetailDescription: editing.roomDetailDescription || "",
    roomMaxCapacity: editing.roomMaxCapacity || 2,
    roomTypeId: editing.roomTypeId || 1,
    roomStatus: editing.roomStatus ?? 1,
    roomPricePerDay: editing.roomPricePerDay || "",
  } : { roomNumber: "", roomDetailDescription: "", roomMaxCapacity: 2, roomTypeId: 1, roomStatus: 1, roomPricePerDay: "" };

  return (
    <section className="staff-page">
      <div className="staff-heading">
        <div><span className="eyebrow eyebrow-light">CASA · STAFF WORKSPACE</span><h1>Không gian quản trị</h1><p>Vận hành khách hàng, phòng và yêu cầu lưu trú.</p></div>
        <div className="staff-avatar">S</div>
      </div>
      <div className="staff-content">
        <div className="staff-tabs" role="tablist">
          {TABS.map((item) => <button key={item.id} type="button" role="tab" aria-selected={tab === item.id} className={tab === item.id ? "active" : ""} onClick={() => selectTab(item.id)}>{item.label}</button>)}
        </div>
        <div className="panel staff-panel">
          <div className="staff-panel-heading">
            <div><span className="eyebrow">DỮ LIỆU HỆ THỐNG</span><h2>{currentTab.title}</h2><p>{pagination.totalElements || 0} mục được tìm thấy</p></div>
            <div className="staff-tools">
              <form className="search-form" onSubmit={(event) => { event.preventDefault(); const nextSearch = keyword.trim(); if (nextSearch === search && page === 0) { reload(); return; } setNotice({ text: "", tone: "error" }); setLoading(true); setPage(0); setSearch(nextSearch); }}><input placeholder={tab === "rooms" ? "Tìm số phòng..." : "Tìm khách hàng..."} value={keyword} onChange={(event) => setKeyword(event.target.value)} /><button type="submit" aria-label="Tìm kiếm">⌕</button></form>
              {tab !== "bookings" && <AppButton onClick={() => setEditing({})}>＋ Thêm mới</AppButton>}
            </div>
          </div>
          <AppAlert tone={notice.tone}>{notice.text}</AppAlert>
          {loading ? <div className="loading-state">Đang tải dữ liệu...</div> : <AppTable columns={columns} rows={rows} rowKey={tab === "rooms" ? "roomId" : "id"} emptyText="Không tìm thấy dữ liệu phù hợp." />}
          <Paging page={pagination.pageNumber || 0} totalPages={pagination.totalPages || 0} onChange={(nextPage) => { setLoading(true); setPage(nextPage); }} />
        </div>
        <p className="staff-footnote">Quản lý đặt phòng hiện hỗ trợ cập nhật trạng thái; thao tác thêm/xóa đặt phòng không có trong API backend.</p>
      </div>
      {editing && tab !== "bookings" && (
        <div className="modal-backdrop" role="presentation" onMouseDown={(event) => { if (event.target === event.currentTarget) setEditing(null); }}>
          <div className="modal-card" role="dialog" aria-modal="true" aria-labelledby="modal-title">
            <button type="button" className="modal-close" aria-label="Đóng" onClick={() => setEditing(null)}>×</button>
            <span className="eyebrow">CASA MANAGEMENT</span><h2 id="modal-title">{editing.id || editing.roomId ? "Chỉnh sửa" : "Thêm"} {tab === "customers" ? "khách hàng" : "phòng"}</h2>
            <form className="form-stack modal-form" onSubmit={save}>
              {tab === "customers" ? <>
                <label>Họ và tên<input name="customerFullName" required maxLength="50" defaultValue={customerForm.customerFullName} /></label>
                <label>Email<input name="emailAddress" type="email" required defaultValue={customerForm.emailAddress} /></label>
                <label>Số điện thoại<input name="telephone" required maxLength="12" defaultValue={customerForm.telephone} /></label>
                <label>Ngày sinh<input name="customerBirthday" type="date" defaultValue={customerForm.customerBirthday} /></label>
                <label>{editing.id ? "Mật khẩu mới (không bắt buộc)" : "Mật khẩu"}<input name="password" type="password" minLength="8" required={!editing.id} /></label>
              </> : <>
                <div className="form-grid">
                  <label>Số phòng<input name="roomNumber" required defaultValue={roomForm.roomNumber} /></label>
                  <label>Sức chứa<input name="roomMaxCapacity" type="number" min="1" required defaultValue={roomForm.roomMaxCapacity} /></label>
                  <label>ID loại phòng<input name="roomTypeId" type="number" min="1" required defaultValue={roomForm.roomTypeId} /><small>Dùng ID loại phòng đã có trong cơ sở dữ liệu.</small></label>
                  <label>Giá / đêm<input name="roomPricePerDay" type="number" min="0" step="1000" required defaultValue={roomForm.roomPricePerDay} /></label>
                </div>
                <label>Mô tả<textarea name="roomDetailDescription" rows="3" defaultValue={roomForm.roomDetailDescription} /></label>
                <label>Trạng thái<select name="roomStatus" defaultValue={roomForm.roomStatus}><option value="1">Đang mở</option><option value="0">Ngừng hoạt động</option></select></label>
              </>}
              <div className="modal-actions"><AppButton type="button" variant="outline" onClick={() => setEditing(null)}>Hủy</AppButton><AppButton type="submit" disabled={saving}>{saving ? "Đang lưu..." : "Lưu thay đổi"}</AppButton></div>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}
