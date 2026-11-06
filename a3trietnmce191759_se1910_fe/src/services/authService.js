import api from "../api/axios";

export const login = async (credentials) => {
  const { data } = await api.post("/auth/login", credentials);
  return data.data;
};

export const register = async (details) => {
  await api.post("/auth/register", details);
};

export const logout = async () => {
  await api.post("/auth/logout");
};