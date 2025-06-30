import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { useUserStore } from './UserStore';

// State-only store
export const useAuthStore = create(
  persist(
    set => ({
      token: null,
      user: {
        role: null,
        firstName: null,
        lastName: null,
        id: null,
        email: null,
      },

      setToken: token => {
        sessionStorage.setItem('token', token);
        set({ token });
      },

      setUserInfo: info => set({ user: info }),

      clearState: () => {
        sessionStorage.removeItem('token');
        set({
          token: null,
          user: {
            role: null,
            firstName: null,
            lastName: null,
            id: null,
            email: null,
          },
        });
        //useUserStore.getState().clearState();
      },
    }),
    {
      name: 'useAuthStore',
      storage: createJSONStorage(() => sessionStorage),
    }
  )
);
