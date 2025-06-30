import { create } from 'zustand';
import { trainingParticipationService } from '../services/trainingParticipationService';

export const useParticipationStore = create(set => ({
  participations: [],
  loading: false,
  error: null,

  setParticipations: participations => set({ participations }),
  setLoading: loading => set({ loading }),
  setError: error => set({ error }),

  fetchParticipations: async (filters = {}) => {
    set({ loading: true, error: null });
    try {
      const data = await trainingParticipationService.getByParticipantId(
        filters.participantId
      );
      console.log('Fetched participations:', data); // Debugging log
      set({ participations: data, loading: false });
    } catch (error) {
      set({ error, loading: false });
    }
  },

  clear: () => set({ participations: [], loading: false, error: null }),
}));
