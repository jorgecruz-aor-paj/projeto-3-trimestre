import axiosInstance from "./axiosInstance"; // Adjust the path as needed

export const login = async (email, password) => {
  const res = await axiosInstance.post(`/auth/login`, { email, password });
  console.log("Login response:", res.data);
  return res.data;
};
