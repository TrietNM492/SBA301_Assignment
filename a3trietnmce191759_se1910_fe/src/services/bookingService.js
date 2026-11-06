import api from "../api/axios";

export const createBooking = async (booking) => {
  const { data } = await api.post("/bookings", booking);
  return data.data;
};

export const getMyBookings = async (page = 0, size = 10) => {
  const { data } = await api.get("/bookings", { params: { page, size } });
  return data.data;
};