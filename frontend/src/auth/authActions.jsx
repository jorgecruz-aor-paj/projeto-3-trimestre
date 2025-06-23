import useAuthStore from "./useAuthStore";
import { fetchLogin, fetchLogout } from "../services/AuthService";
import { fetchUserInfo } from "../services/UserService";

export const performLogin = async (username, password) => {
  const { setToken } = useAuthStore.getState();

  const loginRes = await fetchLogin(username, password);
  if (loginRes?.status === 200) {
    const { token } = loginRes.data;
    setToken(token);
    await updateLoggedUserInfo();
  } else {
    throw new Error("Invalid login response");
  }
};

export const performLogout = async () => {
  try {
    await fetchLogout();
  } catch (err) {
    console.warn("Logout failed (API), proceeding with local clear:", err);
  } finally {
    useAuthStore.getState().clearState();
  }
};

export const updateLoggedUserInfo = async () => {
  const { setUserInfo } = useAuthStore.getState();

  const res = await fetchUserInfo();
  if (res?.status === 200) {
    const { admin, firstName, url, username } = res.data;
    const userInfo = { isAdmin: admin, firstName, url, username };
    setUserInfo(userInfo);
  } else {
    console.error("Failed to fetch user info");
  }
};
