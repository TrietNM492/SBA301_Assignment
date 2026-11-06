import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AppAlert from "../components/AppAlert";
import AppButton from "../components/AppButton";
import { useAuth } from "../context/authState";
import { register } from "../services/authService";

export default function AuthPage({ mode }) {
  const location = useLocation();
  const isRegister = mode === "register";
  const isStaff = location.pathname === "/login/staff";
  const { signIn } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  const submit = async (event) => {
    event.preventDefault();
    setError("");
    setBusy(true);
    const form = Object.fromEntries(new FormData(event.currentTarget).entries());
    try {
      if (isRegister) {
        if (form.password !== form.rePassword) throw new Error("Mật khẩu xác nhận chưa khớp.");
        await register({
          email: form.email,
          password: form.password,
          rePassword: form.rePassword,
          customerFullName: form.customerFullName,
          telephone: form.telephone,
          customerBirthday: form.customerBirthday || null,
        });
        navigate("/login/customer", { replace: true, state: { message: "Tạo tài khoản thành công. Hãy đăng nhập để tiếp tục." } });
      } else {
        const user = await signIn({ email: form.email, password: form.password }, isStaff ? "STAFF" : "CUSTOMER");
        navigate(user.role === "STAFF" ? "/staff" : "/profile", { replace: true });
      }
    } catch (requestError) {
      setError(requestError.response?.data?.message || requestError.message || "Không thể hoàn tất yêu cầu.");
    } finally {
      setBusy(false);
    }
  };

  return (
    <section className="auth-page">
      <div className="auth-image">
        <div className="auth-image-copy"><span className="eyebrow eyebrow-light">A PLACE TO EXHALE</span><h2>Một chốn nghỉ<br />cho tâm hồn.</h2><p>Hãy để Casa chăm sóc phần còn lại.</p></div>
      </div>
      <div className="auth-panel">
        <div className="auth-card">
          <span className="eyebrow">{isRegister ? "THÀNH VIÊN CASA" : isStaff ? "CASA · STAFF PORTAL" : "CHÀO MỪNG TRỞ LẠI"}</span>
          <h1>{isRegister ? "Tạo tài khoản" : isStaff ? "Đăng nhập nhân viên" : "Đăng nhập"}</h1>
          <p className="auth-intro">{isRegister ? "Tạo tài khoản để đặt phòng và quản lý kỳ nghỉ." : isStaff ? "Đăng nhập bằng tài khoản nhân viên được quản lý trong hệ thống." : "Đăng nhập để tiếp tục hành trình cùng Casa."}</p>
          {location.state?.message && <AppAlert tone="success">{location.state.message}</AppAlert>}
          <AppAlert>{error}</AppAlert>
          <form className="form-stack" onSubmit={submit}>
            {isRegister && <label>Họ và tên<input name="customerFullName" autoComplete="name" required maxLength="50" /></label>}
            <label>Email<input name="email" type="email" autoComplete="email" required /></label>
            {isRegister && <label>Số điện thoại<input name="telephone" autoComplete="tel" required maxLength="12" /></label>}
            {isRegister && <label>Ngày sinh <span className="optional">(không bắt buộc)</span><input name="customerBirthday" type="date" /></label>}
            <label>Mật khẩu<input name="password" type="password" autoComplete={isRegister ? "new-password" : "current-password"} required minLength={isRegister ? 8 : undefined} /></label>
            {isRegister && <label>Xác nhận mật khẩu<input name="rePassword" type="password" autoComplete="new-password" required /></label>}
            <AppButton type="submit" disabled={busy}>{busy ? "Đang xử lý..." : isRegister ? "Tạo tài khoản" : "Đăng nhập"} <span aria-hidden="true">→</span></AppButton>
          </form>
          <div className="auth-switch">
            {isRegister ? <>Đã là thành viên? <Link to="/login/customer">Đăng nhập</Link></> : isStaff ? <>Bạn là khách hàng? <Link to="/login/customer">Đăng nhập khách hàng</Link></> : <>Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link></>}
          </div>
          {!isRegister && !isStaff && <Link className="staff-login-link" to="/login/staff">Đăng nhập dành cho nhân viên <span aria-hidden="true">↗</span></Link>}
        </div>
      </div>
    </section>
  );
}
