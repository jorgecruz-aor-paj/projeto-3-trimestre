import React from 'react';
import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { LanguageProvider } from 'contexts/LanguageContext';
import { AlertProvider } from 'contexts/AlertContext';

// Setup test-specific i18n configuration
jest.mock('config/i18n', () => {
  const i18next = require('i18next');
  const { initReactI18next } = require('react-i18next');
  const fs = require('fs');
  const path = require('path');

  const i18nInstance = i18next.createInstance().use(initReactI18next);

  // Load actual translation files
  const loadTranslations = () => {
    try {
      const enPath = path.join(process.cwd(), 'public/locale/en.json');
      const ptPath = path.join(process.cwd(), 'public/locale/pt.json');

      const enTranslations = JSON.parse(fs.readFileSync(enPath, 'utf8'));
      const ptTranslations = JSON.parse(fs.readFileSync(ptPath, 'utf8'));

      return {
        en: { translation: enTranslations },
        pt: { translation: ptTranslations },
      };
    } catch (error) {
      console.warn(
        'Could not load translation files, using fallback:',
        error.message
      );
      // Fallback minimal translations if files can't be loaded
      return {
        en: {
          translation: {
            auth: {
              loginTitle: 'Welcome Back',
              login: 'Log In',
              register: 'Sign Up',
            },
            common: { appName: 'Performance Management System' },
            nav: {
              home: 'Home',
              trainings: 'Trainings',
              evaluations: 'Evaluations',
            },
          },
        },
      };
    }
  };

  // Initialize with actual translation files
  i18nInstance.init({
    lng: 'en',
    fallbackLng: 'en',
    debug: false,
    interpolation: {
      escapeValue: false,
    },
    resources: loadTranslations(),
    react: {
      useSuspense: false,
    },
  });

  return {
    i18n: i18nInstance,
    setupI18n: jest.fn().mockResolvedValue(i18nInstance),
  };
});

// Mock zustand stores
const mockAuthStore = {
  token: null,
  user: {
    role: null,
    firstName: null,
    lastName: null,
    id: null,
    email: null,
  },
  setToken: jest.fn(),
  setUserInfo: jest.fn(),
  clearState: jest.fn(),
};

const mockMessageStore = {
  unreadCount: 0,
  loadUnreadCount: jest.fn(),
};

const mockNotificationStore = {
  unreadCount: 0,
  loadNotifications: jest.fn(),
};

// Mock stores before importing them
jest.mock('stores/AuthStore', () => ({
  useAuthStore: () => mockAuthStore,
}));

jest.mock('stores/MessageStore', () => ({
  useMessageStore: () => mockMessageStore,
}));

jest.mock('stores/NotificationStore', () => ({
  useNotificationStore: () => mockNotificationStore,
}));

// Mock services
jest.mock('services/authService', () => ({
  authService: {
    login: jest.fn(),
    register: jest.fn(),
    logout: jest.fn(),
  },
}));

jest.mock('services/userService', () => ({
  userService: {
    getUserInfo: jest.fn(),
    getMe: jest.fn(),
  },
}));

jest.mock('services/websocketService', () => ({
  websocketService: {
    connect: jest.fn(),
    disconnect: jest.fn(),
    subscribe: jest.fn(),
    unsubscribe: jest.fn(),
  },
}));

// Mock react-router-dom navigation
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
  useLocation: () => ({ pathname: '/' }),
}));

const AllTheProviders = ({ children }) => {
  return (
    <BrowserRouter>
      <LanguageProvider>
        <AlertProvider>{children}</AlertProvider>
      </LanguageProvider>
    </BrowserRouter>
  );
};

const customRender = (ui, options) =>
  render(ui, { wrapper: AllTheProviders, ...options });

// Helper function to reset all mocks
const resetMocks = () => {
  mockAuthStore.token = null;
  mockAuthStore.user = {
    role: null,
    firstName: null,
    lastName: null,
    id: null,
    email: null,
  };
  mockAuthStore.setToken.mockClear();
  mockAuthStore.setUserInfo.mockClear();
  mockAuthStore.clearState.mockClear();
  mockMessageStore.unreadCount = 0;
  mockMessageStore.loadUnreadCount.mockClear();
  mockNotificationStore.unreadCount = 0;
  mockNotificationStore.loadNotifications.mockClear();
  mockNavigate.mockClear();
};

// Helper function to set authenticated state
const setAuthenticatedUser = (userOverrides = {}) => {
  mockAuthStore.token = 'mock-token';
  mockAuthStore.user = {
    role: 'EMPLOYEE',
    firstName: 'John',
    lastName: 'Doe',
    id: 1,
    email: 'john.doe@example.com',
    ...userOverrides,
  };
};

export * from '@testing-library/react';
export {
  customRender as render,
  mockAuthStore,
  mockMessageStore,
  mockNotificationStore,
  mockNavigate,
  resetMocks,
  setAuthenticatedUser,
};
export const getMockAuthService = () => require('services/authService');
export const getMockUserService = () => require('services/userService');
