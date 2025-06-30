import { create } from 'zustand';
import { apiService } from 'services/apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const useManagersStore = create(set => ({
  managers: [],
  loading: false,
  error: null,
  fetchManagers: async () => {
    set({ loading: true, error: null });
    try {
      const data = await apiService.getAll(API_ENDPOINTS.user.getAllManagers);
      set({ managers: data, loading: false });
    } catch (err) {
      set({ error: err.message || 'Failed to fetch managers', loading: false });
    }
  },
}));
