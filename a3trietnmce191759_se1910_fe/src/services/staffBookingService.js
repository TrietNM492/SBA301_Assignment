import api from "../api/axios";

export const getStaffBookings = async (page = 0, size = 10, keyword = "") => {
  const { data } = await api.get("/staff/bookings", {
    params: { page, size, keyword: keyword || undefined },
  });
  return data.data;
};

export const updateBookingStatus = async (id, bookingStatus) => {
  const { data } = await api.patch(`/staff/bookings/${id}/status`, { bookingStatus });
  return data.data;
};
