import axiosInstance from "services/axiosInstance"; // Adjust the path as needed

export const login = async (email, password) => {
  const res = await axiosInstance.post(`/auth/login`, { email, password });
  console.log("Login response:", res.data);
  return res.data;
};

export const loggout = async () => {
  const res = await axiosInstance.post(`/auth/logout`);
  console.log("Logout response:", res.data);
  return res.data;
};
