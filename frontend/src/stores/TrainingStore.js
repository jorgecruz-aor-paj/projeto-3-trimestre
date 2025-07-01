import { create } from 'zustand';
import { trainingService } from 'services/trainingService';

export const useTrainingStore = create(set => ({
  trainings: [],
  loading: false,
  error: null,

  setTrainings: trainings => set({ trainings }),
  setLoading: loading => set({ loading }),
  setError: error => set({ error }),

  fetchTrainings: async (filters = {}) => {
    set({ loading: true, error: null });
    try {
      const data = await trainingService.getAll(filters);
      set({ trainings: data, loading: false });
    } catch (error) {
      set({ error, loading: false });
    }
  },

  clear: () => set({ trainings: [], loading: false, error: null }),
}));
