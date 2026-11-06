import { useMemo, useState } from "react";
import { AuthContext } from "./authState";
import * as authService from "../services/authService";

const TOKEN_KEY = "hotel_access_token";
const USER_KEY = "hotel_user";

const readStoredUser = () => {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || "null");
  } catch {
    return null;
  }
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser);

  const signIn = async (credentials, expectedRole) => {
    const result = await authService.login(credentials);
    const role = result.customerResponse ? "CUSTOMER" : "STAFF";
    if (role !== expectedRole) {
      throw new Error(expectedRole === "STAFF"
        ? "Tài khoản này không phải tài khoản nhân viên."
        : "Tài khoản này không phải tài khoản khách hàng.");
    }

    const nextUser = { role, profile: result.customerResponse };
    localStorage.setItem(TOKEN_KEY, result.accessToken);
    localStorage.setItem(USER_KEY, JSON.stringify(nextUser));
    setUser(nextUser);
    return nextUser;
  };

  const signOut = async () => {
    try {
      await authService.logout();
    } finally {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
      setUser(null);
    }
  };

  const value = useMemo(() => ({ user, signIn, signOut }), [user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
