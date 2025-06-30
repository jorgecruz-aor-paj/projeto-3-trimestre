import { create } from 'zustand';

export const useEvaluationStore = create(set => ({
  evaluations: [],
  loading: false,
  error: null,

  setEvaluations: evaluations => set({ evaluations }),
  setLoading: loading => set({ loading }),
  setError: error => set({ error }),

  clear: () => set({ evaluations: [], loading: false, error: null }),
}));
