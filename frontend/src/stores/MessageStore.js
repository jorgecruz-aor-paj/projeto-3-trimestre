import { create } from 'zustand';
import { axiosInstance } from 'api/axiosInstance';
import { API_ENDPOINTS } from 'api/apiConfig';
import { useAuthStore } from './AuthStore';
import { websocketService } from 'services/websocketService';

export const useMessageStore = create((set, get) => ({
  // State
  conversations: [],
  activeConversation: null,
  messages: [],
  unreadCount: 0,
  unreadByConversation: {}, // Track unread count per conversation
  isLoading: false,
  error: null,

  // Actions
  setActiveConversation: conversation => {
    set({ activeConversation: conversation, messages: [] });
    if (conversation) {
      get().loadConversationMessages(conversation.id);
    }
  },

  loadConversations: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(
        API_ENDPOINTS.messages.conversations.url
      );

      // Load unread counts for each conversation from backend
      const unreadByConversation = {};
      const currentUserId = useAuthStore.getState().user?.id;

      if (currentUserId) {
        for (const conversation of response) {
          try {
            const unreadResponse = await axiosInstance.get(
              API_ENDPOINTS.messages.unreadCountFromSender.url(conversation.id)
            );
            unreadByConversation[conversation.id] = unreadResponse.count || 0;
          } catch (error) {
            console.warn(
              `Failed to load unread count for conversation ${conversation.id}:`,
              error
            );
            unreadByConversation[conversation.id] = 0;
          }
        }
      }

      set({
        conversations: response,
        unreadByConversation,
        isLoading: false,
      });
    } catch (error) {
      console.error('Error loading conversations:', error);
      set({
        error: error.message || 'Failed to load conversations',
        isLoading: false,
      });
    }
  },

  // Helper function to calculate unread messages from loaded conversation
  calculateUnreadFromMessages: (messages, currentUserId) => {
    if (!messages || !currentUserId) return 0;
    return messages.filter(
      msg => !msg.read && msg.recipientId === currentUserId
    ).length;
  },

  // Enhanced function to get unread count for a specific conversation
  getUnreadCountForConversation: conversationId => {
    const state = get();
    const currentUserId = useAuthStore.getState().user?.id;

    // If we have unread count tracked, use it
    if (state.unreadByConversation[conversationId] !== undefined) {
      return state.unreadByConversation[conversationId];
    }

    // If this is the active conversation, calculate from loaded messages
    if (state.activeConversation?.id === conversationId && state.messages) {
      return state.calculateUnreadFromMessages(state.messages, currentUserId);
    }

    // Default to 0 if no data available
    return 0;
  },

  loadConversationMessages: async (otherUserId, page = 0, pageSize = 50) => {
    set({ isLoading: true, error: null });
    try {
      const url = `${API_ENDPOINTS.messages.conversation.url(
        otherUserId
      )}?page=${page}&pageSize=${pageSize}`;
      const response = await axiosInstance.get(url);

      // Sort messages by timestamp (oldest first)
      const sortedMessages = response.sort(
        (a, b) => new Date(a.timestampSent) - new Date(b.timestampSent)
      );

      // Calculate unread count before marking as read (for potential future use)
      const currentUserId = useAuthStore.getState().user?.id;
      // const unreadCount = get().calculateUnreadFromMessages(sortedMessages, currentUserId);

      set({ messages: sortedMessages, isLoading: false });

      // Mark unread messages as read
      const unreadMessages = sortedMessages.filter(
        msg => !msg.read && msg.recipientId === currentUserId
      );

      for (const message of unreadMessages) {
        await get().markMessageAsRead(message.id);
      }

      // Clear unread count for this conversation after marking as read
      set(state => ({
        unreadByConversation: {
          ...state.unreadByConversation,
          [otherUserId]: 0,
        },
      }));
    } catch (error) {
      console.error('Error loading conversation messages:', error);
      set({
        error: error.message || 'Failed to load messages',
        isLoading: false,
      });
    }
  },

  sendMessage: async (recipientId, content) => {
    if (!content.trim()) return;

    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.post(
        API_ENDPOINTS.messages.send.url(recipientId),
        content,
        {
          headers: {
            'Content-Type': 'text/plain',
          },
        }
      );

      // Add message to current conversation immediately
      const { activeConversation } = get();
      if (activeConversation && activeConversation.id === recipientId) {
        set(state => ({
          messages: [...state.messages, response],
          isLoading: false,
        }));
      } else {
        set({ isLoading: false });
      }

      return response;
    } catch (error) {
      console.error('Error sending message:', error);
      set({
        error: error.message || 'Failed to send message',
        isLoading: false,
      });
      throw error;
    }
  },

  markMessageAsRead: async messageId => {
    try {
      await axiosInstance.put(API_ENDPOINTS.messages.markAsRead.url(messageId));

      // Update the message in the store
      set(state => ({
        messages: state.messages.map(msg => {
          if (msg.id === messageId) {
            // Send read receipt via WebSocket if connected
            if (websocketService.isConnected()) {
              websocketService.sendReadReceipt(messageId);
            }
            return { ...msg, read: true };
          }
          return msg;
        }),
      }));

      // Update unread count
      get().loadUnreadCount();
    } catch (error) {
      console.error('Error marking message as read:', error);
    }
  },

  loadUnreadCount: async () => {
    try {
      const response = await axiosInstance.get(
        API_ENDPOINTS.messages.unreadCount.url
      );
      set({ unreadCount: response.count || 0 });
    } catch (error) {
      console.error('Error loading unread count:', error);
    }
  },

  // Function to preload unread counts for all conversations
  preloadUnreadCounts: () => {
    const { conversations } = get();
    const currentUserId = useAuthStore.getState().user?.id;

    if (!conversations || !currentUserId) return;

    const unreadByConversation = {};

    // For each conversation, we could potentially load the first few messages
    // to check for unread status, but for now we'll rely on WebSocket updates
    for (const conversation of conversations) {
      unreadByConversation[conversation.id] = 0;
    }

    set(state => ({
      unreadByConversation: {
        ...state.unreadByConversation,
        ...unreadByConversation,
      },
    }));
  },

  // Function to refresh unread counts for all conversations
  refreshUnreadCounts: async () => {
    const { conversations } = get();
    const currentUserId = useAuthStore.getState().user?.id;

    if (!conversations || !currentUserId) return;

    const unreadByConversation = {};

    for (const conversation of conversations) {
      try {
        const unreadResponse = await axiosInstance.get(
          API_ENDPOINTS.messages.unreadCountFromSender.url(conversation.id)
        );
        unreadByConversation[conversation.id] = unreadResponse.count || 0;
      } catch (error) {
        console.warn(
          `Failed to refresh unread count for conversation ${conversation.id}:`,
          error
        );
        unreadByConversation[conversation.id] = 0;
      }
    }

    set(state => ({
      unreadByConversation: {
        ...state.unreadByConversation,
        ...unreadByConversation,
      },
    }));
  },

  // Handle WebSocket notifications
  handleNotification: notificationData => {
    console.log('Received notification:', notificationData);

    if (notificationData.type === 'new_message') {
      const { senderId } = notificationData;

      // Refresh current conversation if it's active
      const { activeConversation } = get();
      if (activeConversation && activeConversation.id === senderId) {
        get().loadConversationMessages(activeConversation.id);
      } else {
        // Refresh unread counts for all conversations
        get().refreshUnreadCounts();
      }

      // Update conversations list and global unread count
      get().loadConversations();
      get().loadUnreadCount();
    } else if (notificationData.type === 'message_read') {
      // Update read status for the message
      const messageId = notificationData.messageId;
      set(state => ({
        messages: state.messages.map(msg =>
          msg.id === messageId ? { ...msg, read: true } : msg
        ),
      }));
    }
  },

  // Clear store state
  clearStore: () => {
    set({
      conversations: [],
      activeConversation: null,
      messages: [],
      unreadCount: 0,
      unreadByConversation: {},
      isLoading: false,
      error: null,
    });
  },
}));
