import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import SiteLayout from "./layout/SiteLayout";
import AuthPage from "./pages/AuthPage";
import BookingPage from "./pages/BookingPage";
import HistoryPage from "./pages/HistoryPage";
import HomePage from "./pages/HomePage";
import ProfilePage from "./pages/ProfilePage";
import StaffDashboard from "./pages/StaffDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route element={<SiteLayout />}>
            <Route index element={<HomePage />} />
            <Route path="login/customer" element={<AuthPage />} />
            <Route path="login/staff" element={<AuthPage />} />
            <Route path="register" element={<AuthPage mode="register" />} />
            <Route element={<ProtectedRoute role="CUSTOMER" />}>
              <Route path="profile" element={<ProfilePage />} />
              <Route path="booking" element={<BookingPage />} />
              <Route path="history" element={<HistoryPage />} />
            </Route>
            <Route element={<ProtectedRoute role="STAFF" />}>
              <Route path="staff" element={<StaffDashboard />} />
            </Route>
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
