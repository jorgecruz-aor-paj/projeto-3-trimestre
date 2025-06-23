import useAuthStore from "../../stores/authStore";
import { login, logout } from "../../services/authService";
import { getSelfInfo } from "../../services/userService";

export const performLogin = async (username, password) => {
  const { setToken } = useAuthStore.getState();
try{
  const loginRes = await login(username, password);
  console.log("Login response:", loginRes);

    const { token } = loginRes;
    console.log(loginRes.data);
    setToken(token);
    await updateLoggedUserInfo();
  } catch (err) {
    throw new Error(`Login failed: ${err.response?.data?.message || err.message}`);
  }
};

export const performLogout = async () => {
  try {
    await logout();
  } catch (err) {
    console.warn("Logout failed (API), proceeding with local clear:", err);
  } finally {
    useAuthStore.getState().clearState();
  }
};

export const updateLoggedUserInfo = async () => {
  const { setUserInfo } = useAuthStore.getState();

  const res = await getSelfInfo();
  if (res?.status === 200) {
    const { admin, firstName, url, username } = res.data;
    const userInfo = { isAdmin: admin, firstName, url, username };
    setUserInfo(userInfo);
  } else {
    console.error("Failed to fetch user info");
  }
};
