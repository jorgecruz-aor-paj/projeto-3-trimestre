// src/api/axiosInstance.js
import axios from 'axios';
import { useAuthStore } from 'stores/authStore';

const axiosInstance = axios.create({
  baseURL: 'https://localhost:8443/jorgeosorio-proj3t/api',
});

// Request interceptor: attach token
axiosInstance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token'); // or grab from store
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

let isHandling401 = false;

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const { response } = error;

  if (!response) {
  return Promise.reject({
    code: "NETWORK_ERROR",
    message: "Network connection lost or server unreachable.",
    original: error,
  });
}

    if (response?.status === 401 && !isHandling401) {
      isHandling401 = true;

      try {
        // Clear state, redirect to login, show toast or modal
        useAuthStore.getState().clearState?.();
        alert("Session expired. Please log in again.");
        window.location.replace("/login");
      } finally {
        // Reset lockout flag
        setTimeout(() => (isHandling401 = false), 1000);
      }
    }

    // Format error object
    const structuredError = {
      code: response?.status || "NETWORK_ERROR",
      message:
        response?.data?.message ||
        response?.statusText ||
        "Something went wrong. Please try again.",
      original: error,
    };

    return Promise.reject(structuredError);
  }
);
