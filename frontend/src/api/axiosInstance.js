import axios from 'axios';
import { API_BASE_URL } from './apiConfig';
import { useAuthStore } from 'stores/AuthStore';

// Global flag to prevent multiple simultaneous logout processes
let isHandling401 = false;

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: attach token
axiosInstance.interceptors.request.use(
  config => {
    const token =
      useAuthStore.getState()?.token || sessionStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Function to handle automatic logout
const handleAutomaticLogout = () => {
  if (isHandling401) return;
  isHandling401 = true;

  try {
    // Import AlertContext dynamically to avoid circular dependencies
    const { AlertProvider } = require('contexts/AlertContext');

    // Clear auth store
    const authStore = useAuthStore.getState();
    authStore.clearState?.();

    // Clear session storage
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('useAuthStore');
    sessionStorage.removeItem('workplaces');

    // Clear any other stores that might have user-related data
    // We'll import and clear these stores dynamically to avoid circular dependencies

    // Clear localStorage (if any auth data is stored there)
    const authKeys = ['token', 'user', 'authStore', 'workplaces'];
    authKeys.forEach(key => {
      localStorage.removeItem(key);
      sessionStorage.removeItem(key);
    });

    // Disconnect WebSocket if connected
    try {
      const { websocketService } = require('services/websocketService');
      websocketService.disconnect();
    } catch (e) {
      console.warn('WebSocket service not available during logout:', e);
    }

    // Show alert to user before redirect
    try {
      // We're accessing the global alertContext which should be available
      // from the AlertProvider at the app root
      const showAlert =
        window.showGlobalAlert ||
        (() => {
          // Fallback if global alert function is not available: use a browser alert
          alert('Your session has expired. Please log in again to continue.');
        });

      // We'll store redirect info to be used after the alert is dismissed
      const currentPath = window.location.pathname;
      const shouldRedirect =
        currentPath !== '/login' &&
        currentPath !== '/register' &&
        currentPath !== '/';

      // Use the alert context to show a message to the user
      if (shouldRedirect) {
        // Set a flag in sessionStorage to show the message after redirect
        // The LoginAuthForm component will read this flag and show a properly
        // translated message using the i18n context available in the component
        sessionStorage.setItem('sessionExpired', 'true');

        // Redirect to login page
        // Use window.location to ensure a clean redirect without React Router state issues
        window.location.href = '/login';
      }
    } catch (alertError) {
      console.error('Error showing alert during logout:', alertError);

      // Fallback redirect if alert fails
      const currentPath = window.location.pathname;
      if (
        currentPath !== '/login' &&
        currentPath !== '/register' &&
        currentPath !== '/'
      ) {
        window.location.href = '/login';
      }
    }
  } catch (error) {
    console.error('Error during automatic logout:', error);

    // Fallback redirect if anything fails
    window.location.href = '/login';
  } finally {
    // Reset flag after a delay to allow the logout process to complete
    setTimeout(() => {
      isHandling401 = false;
    }, 2000);
  }
};

axiosInstance.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    const { response, config } = error;

    // Log profile picture errors
    if (config && config.url && config.url.includes('/profile/picture')) {
      console.error('Profile picture request failed:', {
        url: config.url,
        method: config.method,
        response: response,
      });
    }

    // Handle network errors
    if (!response) {
      return Promise.reject({
        code: 'NETWORK_ERROR',
        message: 'Network connection lost or server unreachable.',
        type: 'network',
        original: error,
      });
    }

    // Handle 401 - unauthorized/expired token
    if (response?.status === 401) {
      //console.log(
      //  '401 Unauthorized response received - performing automatic logout'
      //);
      handleAutomaticLogout();

      // Return structured error for components to handle
      return Promise.reject({
        code: 'UNAUTHORIZED',
        message: 'Session expired. Please log in again.',
        type: 'auth',
        shouldRedirect: true,
        redirectTo: '/login',
        original: error,
      });
    }

    // Handle 403 - forbidden
    if (response?.status === 403) {
      return Promise.reject({
        code: 'FORBIDDEN',
        message:
          'Access denied. You do not have permission to access this resource.',
        type: 'auth',
        shouldRedirect: true,
        redirectTo: '/error/403',
        original: error,
      });
    }

    // Format all other errors consistently
    const structuredError = {
      code: response?.status || 'UNKNOWN_ERROR',
      message:
        response?.data?.message ||
        response?.statusText ||
        'Something went wrong. Please try again.',
      type: response?.status >= 500 ? 'server' : 'client',
      original: error,
    };

    return Promise.reject(structuredError);
  }
);

export { axiosInstance };
