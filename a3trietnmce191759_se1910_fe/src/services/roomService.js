import api from "../api/axios";

export const getRooms = async (page = 0, size = 12) => {
  const { data } = await api.get("/rooms", { params: { page, size } });
  return data.data;
};

export const getRoom = async (id) => {
  const { data } = await api.get(`/rooms/${id}`);
  return data.data;
};
