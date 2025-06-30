import { create } from 'zustand';
import { apiService } from 'services/apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const useUserStore = create(set => ({
  users: [],
  loading: false,
  error: null,
  fetchUsers: async () => {
    set({ loading: true, error: null });
    try {
      const data = await apiService.getAll(API_ENDPOINTS.user.getAll);
      set({ users: data, loading: false });
    } catch (err) {
      set({ error: err.message || 'Failed to fetch users', loading: false });
    }
  },
}));
