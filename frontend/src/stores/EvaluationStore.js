import { create } from 'zustand';
import { evaluationService } from '../services/evaluationService';

export const useEvaluationStore = create(set => ({
  evaluations: [],
  loading: false,
  error: null,

  setEvaluations: evaluations => set({ evaluations }),
  setLoading: loading => set({ loading }),
  setError: error => set({ error }),

  fetchEvaluations: async (filters = {}) => {
    set({ loading: true, error: null });
    try {
      const data = await evaluationService.getAll(filters);
      console.log('Fetched evaluations:', data); // Debugging log
      set({ evaluations: data, loading: false });
    } catch (error) {
      set({ error, loading: false });
    }
  },

  clear: () => set({ evaluations: [], loading: false, error: null }),
}));
