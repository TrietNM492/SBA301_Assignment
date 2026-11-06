import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/authState";

export default function ProtectedRoute({ role }) {
  const { user } = useAuth();
  if (!user) return <Navigate to={role === "STAFF" ? "/login/staff" : "/login/customer"} replace />;
  if (user.role !== role) return <Navigate to={user.role === "STAFF" ? "/staff" : "/"} replace />;
  return <Outlet />;
}
