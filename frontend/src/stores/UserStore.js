import { create } from 'zustand';
import { apiService } from 'services/apiService';
import { API_ENDPOINTS } from 'api/apiConfig';

export const useUserStore = create(set => ({
  users: [],
  loading: false,
  error: null,
  fetchUsers: async (filters = {}) => {
    set({ loading: true, error: null });
    try {
      const params = {};
      if (filters.workplaceId) params.workplaceId = filters.workplaceId;
      if (filters.managerId) params.managerId = filters.managerId;
      if (filters.id) params.id = filters.id;
      if (filters.searchText) params.searchText = filters.searchText;
      if (filters.noManager) params.noManager = filters.noManager;
      if (filters.sortField) params.sortField = filters.sortField;
      if (typeof filters.ascending === 'boolean') {
        params.ascending = filters.ascending;
      }

      const response = await apiService.getAll(API_ENDPOINTS.user.getAll, params);
      set({ users: response, loading: false });
    } catch (err) {
      set({ error: err.message || 'Failed to fetch users', loading: false });
    }
  },
}));

