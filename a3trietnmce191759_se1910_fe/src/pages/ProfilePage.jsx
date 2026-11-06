import { useEffect, useState } from "react";
import AppAlert from "../components/AppAlert";
import AppButton from "../components/AppButton";
import { useAuth } from "../context/authState";
import { getProfile, updateProfile } from "../services/profileService";

export default function ProfilePage() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [notice, setNotice] = useState({ text: "", tone: "error" });
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    getProfile().then(setProfile).catch((error) => setNotice({ text: error.response?.data?.message || "Không thể tải hồ sơ.", tone: "error" }));
  }, []);

  const submit = async (event) => {
    event.preventDefault();
    setBusy(true);
    setNotice({ text: "", tone: "error" });
    const form = Object.fromEntries(new FormData(event.currentTarget).entries());
    try {
      const updated = await updateProfile({
        customerFullName: form.customerFullName,
        telephone: form.telephone,
        customerBirthday: form.customerBirthday || null,
      });
      setProfile(updated);
      setNotice({ text: "Thông tin hồ sơ đã được cập nhật.", tone: "success" });
    } catch (error) {
      setNotice({ text: error.response?.data?.message || "Không thể lưu thay đổi.", tone: "error" });
    } finally {
      setBusy(false);
    }
  };

  return (
    <section className="section account-section">
      <div className="page-heading"><span className="eyebrow">TÀI KHOẢN CỦA BẠN</span><h1>Hồ sơ cá nhân</h1><p>Quản lý thông tin để chúng tôi phục vụ bạn tốt hơn.</p></div>
      <div className="profile-layout">
        <aside className="profile-aside"><div className="avatar">{(profile?.customerFullName || user?.profile?.customerFullName || "C").slice(0, 1).toUpperCase()}</div><h3>{profile?.customerFullName || "Thành viên Casa"}</h3><p>{profile?.emailAddress || user?.profile?.emailAddress}</p><span className="status-pill status-active">Thành viên</span></aside>
        <div className="panel profile-panel">
          <h2>Thông tin cá nhân</h2><p className="muted">Email đăng nhập không thể thay đổi.</p>
          <AppAlert tone={notice.tone}>{notice.text}</AppAlert>
          {!profile ? <div className="loading-state">Đang tải thông tin...</div> : (
            <form className="form-grid" onSubmit={submit}>
              <label>Họ và tên<input name="customerFullName" required maxLength="50" defaultValue={profile.customerFullName || ""} /></label>
              <label>Email<input value={profile.emailAddress || ""} disabled /></label>
              <label>Số điện thoại<input name="telephone" maxLength="12" defaultValue={profile.telephone || ""} /></label>
              <label>Ngày sinh<input name="customerBirthday" type="date" defaultValue={profile.customerBirthday || ""} /></label>
              <div className="form-actions"><AppButton type="submit" disabled={busy}>{busy ? "Đang lưu..." : "Lưu thay đổi"}</AppButton></div>
            </form>
          )}
        </div>
      </div>
    </section>
  );
}
