import api from "../api/axios";

const unwrap = (response) => response?.data ?? response;

export const getStaffRooms = async (page = 0, size = 10, keyword = "") => {
  const { data } = await api.get("/staff/rooms", {
    params: { page, size, roomNumber: keyword || undefined },
  });
  return unwrap(data);
};

export const createRoom = async (room) => {
  const { data } = await api.post("/staff/rooms", room);
  return unwrap(data);
};

export const updateRoom = async (id, room) => {
  const { data } = await api.put(`/staff/rooms/${id}`, room);
  return unwrap(data);
};

export const deleteRoom = async (id) => {
  await api.delete(`/staff/rooms/${id}`);
};
