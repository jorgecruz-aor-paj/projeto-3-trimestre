// src/api/axiosInstance.js
import axios from 'axios';

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

// Response interceptor: handle errors globally
axiosInstance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      //authenticationService.logout(); // If you have a logout function
      window.location.href = '/login';
    }

    return Promise.reject(error);
  }
);
