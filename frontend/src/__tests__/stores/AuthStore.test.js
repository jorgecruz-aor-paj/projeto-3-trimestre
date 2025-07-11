/**
 * @jest-environment jsdom
 */

// Mock all external dependencies first
jest.mock('stores/UserStore', () => ({
  useUserStore: jest.fn(() => ({
    clearState: jest.fn(),
  })),
}));

jest.mock('services/apiService', () => ({
  apiService: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  },
}));

jest.mock('api/apiConfig', () => ({
  API_ENDPOINTS: {},
  API_BASE_URL: 'http://localhost:8080',
}));

import { renderHook, act } from '@testing-library/react';

// Mock sessionStorage
const mockSessionStorage = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
};

Object.defineProperty(window, 'sessionStorage', {
  value: mockSessionStorage,
});

describe('AuthStore', () => {
  let useAuthStore;

  beforeAll(async () => {
    // Dynamically import the store after mocks are set up
    const authModule = await import('stores/AuthStore');
    useAuthStore = authModule.useAuthStore;
  });

  beforeEach(() => {
    // Clear all mocks and reset store state
    jest.clearAllMocks();
    mockSessionStorage.getItem.mockReturnValue(null);

    // Reset the store to initial state if available
    if (useAuthStore) {
      const { result } = renderHook(() => useAuthStore());
      act(() => {
        result.current.clearState();
      });
    }
  });

  describe('Initial State', () => {
    it('should have null token initially', () => {
      const { result } = renderHook(() => useAuthStore());
      expect(result.current.token).toBeNull();
    });

    it('should have empty user object initially', () => {
      const { result } = renderHook(() => useAuthStore());
      expect(result.current.user).toEqual({
        role: null,
        firstName: null,
        lastName: null,
        id: null,
        email: null,
      });
    });
  });

  describe('setToken', () => {
    it('should set token in store and sessionStorage', () => {
      const { result } = renderHook(() => useAuthStore());
      const testToken = 'test-jwt-token';

      act(() => {
        result.current.setToken(testToken);
      });

      expect(result.current.token).toBe(testToken);
      expect(mockSessionStorage.setItem).toHaveBeenCalledWith(
        'token',
        testToken
      );
    });

    it('should handle null token', () => {
      const { result } = renderHook(() => useAuthStore());

      act(() => {
        result.current.setToken(null);
      });

      expect(result.current.token).toBeNull();
      expect(mockSessionStorage.setItem).toHaveBeenCalledWith('token', null);
    });
  });

  describe('setUserInfo', () => {
    it('should set user information in store', () => {
      const { result } = renderHook(() => useAuthStore());
      const userInfo = {
        role: 'EMPLOYEE',
        firstName: 'John',
        lastName: 'Doe',
        id: 123,
        email: 'john.doe@example.com',
      };

      act(() => {
        result.current.setUserInfo(userInfo);
      });

      expect(result.current.user).toEqual(userInfo);
    });

    it('should handle partial user information', () => {
      const { result } = renderHook(() => useAuthStore());
      const partialUserInfo = {
        firstName: 'Jane',
        email: 'jane@example.com',
      };

      act(() => {
        result.current.setUserInfo(partialUserInfo);
      });

      expect(result.current.user).toEqual(partialUserInfo);
    });
  });

  describe('clearState', () => {
    it('should clear token and user information', () => {
      const { result } = renderHook(() => useAuthStore());

      // First set some data
      act(() => {
        result.current.setToken('test-token');
        result.current.setUserInfo({
          role: 'ADMIN',
          firstName: 'Admin',
          lastName: 'User',
          id: 1,
          email: 'admin@example.com',
        });
      });

      // Then clear state
      act(() => {
        result.current.clearState();
      });

      expect(result.current.token).toBeNull();
      expect(result.current.user).toEqual({
        role: null,
        firstName: null,
        lastName: null,
        id: null,
        email: null,
      });
      expect(mockSessionStorage.removeItem).toHaveBeenCalledWith('token');
    });
  });

  describe('Authentication State Checks', () => {
    it('should be unauthenticated when token is null', () => {
      const { result } = renderHook(() => useAuthStore());
      const isAuthenticated = !!result.current.token;
      expect(isAuthenticated).toBe(false);
    });

    it('should be authenticated when token exists', () => {
      const { result } = renderHook(() => useAuthStore());

      act(() => {
        result.current.setToken('valid-token');
      });

      const isAuthenticated = !!result.current.token;
      expect(isAuthenticated).toBe(true);
    });
  });

  describe('User Role Management', () => {
    it('should handle different user roles', () => {
      const { result } = renderHook(() => useAuthStore());

      const roles = ['EMPLOYEE', 'MANAGER', 'ADMIN'];

      roles.forEach(role => {
        act(() => {
          result.current.setUserInfo({ role });
        });

        expect(result.current.user.role).toBe(role);
      });
    });

    it('should handle role changes', () => {
      const { result } = renderHook(() => useAuthStore());

      act(() => {
        result.current.setUserInfo({ role: 'EMPLOYEE' });
      });
      expect(result.current.user.role).toBe('EMPLOYEE');

      act(() => {
        result.current.setUserInfo({ role: 'MANAGER' });
      });
      expect(result.current.user.role).toBe('MANAGER');
    });
  });

  describe('Store Methods Exist', () => {
    it('should have all required methods', () => {
      const { result } = renderHook(() => useAuthStore());

      expect(typeof result.current.setToken).toBe('function');
      expect(typeof result.current.setUserInfo).toBe('function');
      expect(typeof result.current.clearState).toBe('function');
    });
  });
});
