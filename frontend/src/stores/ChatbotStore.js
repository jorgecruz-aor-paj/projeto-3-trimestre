import { create } from 'zustand';
import { chatbotService } from 'services/chatbotService';

/**
 * Zustand store for chatbot functionality
 */
export const useChatbotStore = create(set => ({
  // State
  messages: [],
  isLoading: false,
  error: null,
  isOpen: false,

  // Actions
  toggleChatbot: () => {
    set(state => ({ isOpen: !state.isOpen }));
  },

  openChatbot: () => {
    set({ isOpen: true });
  },

  closeChatbot: () => {
    set({ isOpen: false });
  },

  sendMessage: async userMessage => {
    const messageId = Date.now();

    // Add user message immediately
    set(state => ({
      messages: [
        ...state.messages,
        {
          id: messageId,
          content: userMessage,
          isBot: false,
          timestamp: new Date().toISOString(),
        },
      ],
      isLoading: true,
      error: null,
    }));

    try {
      // Get bot response
      const botReply = await chatbotService.sendMessage(userMessage);

      // Add bot response
      set(state => ({
        messages: [
          ...state.messages,
          {
            id: messageId + 1,
            content: botReply,
            isBot: true,
            timestamp: new Date().toISOString(),
          },
        ],
        isLoading: false,
      }));
    } catch (error) {
      set(state => ({
        error: error.message,
        isLoading: false,
        messages: [
          ...state.messages,
          {
            id: messageId + 1,
            content: 'Desculpe, ocorreu um erro. Tente novamente mais tarde.',
            isBot: true,
            timestamp: new Date().toISOString(),
            isError: true,
          },
        ],
      }));
    }
  },

  clearMessages: () => {
    set({ messages: [], error: null });
  },

  clearError: () => {
    set({ error: null });
  },
}));
