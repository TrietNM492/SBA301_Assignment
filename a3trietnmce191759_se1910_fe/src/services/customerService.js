import api from "../api/axios";

export const getCustomers = async (page = 0, size = 10, keyword = "") => {
  const { data } = await api.get("/staff/accounts", {
    params: { page, size, keyword: keyword || undefined },
  });
  return data.data;
};

export const createCustomer = async (customer) => {
  const { data } = await api.post("/staff/accounts", customer);
  return data.data;
};

export const updateCustomer = async (id, customer) => {
  const { data } = await api.put(`/staff/accounts/${id}`, customer);
  return data.data;
};

export const deleteCustomer = async (id) => {
  await api.delete(`/staff/accounts/${id}`);
};
