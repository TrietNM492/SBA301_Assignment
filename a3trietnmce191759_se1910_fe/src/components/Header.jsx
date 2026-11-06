import { useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import AppAlert from "./AppAlert";
import { useAuth } from "../context/authState";

export default function Header() {
  const { user, signOut } = useAuth();
  const navigate = useNavigate();
  const [logoutError, setLogoutError] = useState("");

  const handleSignOut = async () => {
    setLogoutError("");
    try {
      await signOut();
    } catch {
      setLogoutError("Phiên đăng nhập đã được xóa trên thiết bị, nhưng máy chủ chưa xác nhận đăng xuất.");
    } finally {
      navigate("/");
    }
  };

  return (
    <>
      <header className="site-header">
        <div className="header-inner">
          <Link to="/" className="brand"><span className="brand-mark">C</span><span>CASA <small>HOTEL & RESORT</small></span></Link>
          <nav className="main-nav" aria-label="Điều hướng chính">
            <NavLink to="/">Trang chủ</NavLink>
            {user?.role === "CUSTOMER" && <>
              <NavLink to="/booking">Đặt phòng</NavLink>
              <NavLink to="/history">Lịch sử đặt</NavLink>
              <NavLink to="/profile">Hồ sơ</NavLink>
            </>}
            {user?.role === "STAFF" && <NavLink to="/staff">Quản trị</NavLink>}
          </nav>
          <div className="header-actions">
            {user ? (
              <button type="button" className="text-button" onClick={handleSignOut}>Đăng xuất</button>
            ) : (
              <>
                <Link to="/login/customer" className="header-login">Đăng nhập</Link>
                <Link to="/register" className="button button-primary header-register">Đăng ký</Link>
              </>
            )}
          </div>
        </div>
      </header>
      <div className="header-notice">{logoutError && <AppAlert>{logoutError}</AppAlert>}</div>
    </>
  );
}