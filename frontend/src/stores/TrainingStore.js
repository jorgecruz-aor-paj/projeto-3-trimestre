import { create } from 'zustand';

export const useTrainingStore = create(set => ({
  trainings: [],
  loading: false,
  error: null,

  setTrainings: trainings => set({ trainings }),
  setLoading: loading => set({ loading }),
  setError: error => set({ error }),

  clear: () => set({ trainings: [], loading: false, error: null }),
}));
