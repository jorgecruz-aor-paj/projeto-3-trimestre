import axiosInstance from "./axiosInstance"; // Adjust the path as needed

export const getSelfInfo = async () => {
  const res = await axiosInstance.get(`/users/me`);
  console.log("get self info response:", res.data);
  return res.data;
};