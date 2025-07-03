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
      set({ conversations: response, isLoading: false });
    } catch (error) {
      console.error('Error loading conversations:', error);
      set({
        error: error.message || 'Failed to load conversations',
        isLoading: false,
      });
    }
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

      set({ messages: sortedMessages, isLoading: false });

      // Mark unread messages as read
      const unreadMessages = sortedMessages.filter(
        msg => !msg.read && msg.recipientId === useAuthStore.getState().user?.id
      );

      for (const message of unreadMessages) {
        await get().markMessageAsRead(message.id);
      }
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

  // Handle WebSocket notifications (just refresh data)
  handleNotification: notificationData => {
    console.log('Received notification:', notificationData);

    if (notificationData.type === 'new_message') {
      // Refresh current conversation if it's active
      const { activeConversation } = get();
      if (activeConversation) {
        get().loadConversationMessages(activeConversation.id);
      }

      // Update conversations list and unread count
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
      isLoading: false,
      error: null,
    });
  },
}));
