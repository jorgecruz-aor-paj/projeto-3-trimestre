import { create } from 'zustand';
import { notificationService } from 'services/notificationService';

/**
 * Zustand store for notification management
 */
export const useNotificationStore = create((set, get) => ({
  // State
  notifications: [],
  unreadCount: 0,
  loading: false,
  error: null,

  // Actions
  loadNotifications: async () => {
    set({ loading: true, error: null });
    try {
      const notifications = await notificationService.getUserNotifications();

      // Sort by timestamp (newest first)
      const sortedNotifications = notifications.sort(
        (a, b) => new Date(b.timestampSent) - new Date(a.timestampSent)
      );

      const unreadCount = sortedNotifications.filter(n => !n.read).length;

      set({
        notifications: sortedNotifications,
        unreadCount,
        loading: false,
      });
    } catch (error) {
      console.error('Error loading notifications:', error);
      set({
        error: error.message || 'Falha ao carregar notificações',
        loading: false,
      });
    }
  },

  // Mark notification as read
  markAsRead: async notificationId => {
    try {
      await notificationService.markAsRead(notificationId);

      const notifications = get().notifications.map(n =>
        n.id === notificationId ? { ...n, read: true } : n
      );

      set({
        notifications,
        unreadCount: notifications.filter(n => !n.read).length,
      });
    } catch (error) {
      console.error('Failed to mark notification as read:', error);
      set({ error: error.message || 'Falha ao marcar notificação como lida' });
    }
  },

  // Mark all notifications as read
  markAllAsRead: async () => {
    try {
      await notificationService.markAllAsRead();

      const notifications = get().notifications.map(n => ({
        ...n,
        read: true,
      }));
      set({ notifications, unreadCount: 0 });
    } catch (error) {
      console.error('Failed to mark all notifications as read:', error);
      set({
        error:
          error.message || 'Falha ao marcar todas as notificações como lidas',
      });
    }
  },

  // Delete notification
  deleteNotification: async notificationId => {
    try {
      await notificationService.deleteNotification(notificationId);

      const notifications = get().notifications.filter(
        n => n.id !== notificationId
      );
      set({
        notifications,
        unreadCount: notifications.filter(n => !n.read).length,
      });
    } catch (error) {
      console.error('Failed to delete notification:', error);
      set({ error: error.message || 'Falha ao eliminar notificação' });
    }
  },

  // Add new notification (for real-time updates)
  addNotification: notification => {
    const notifications = [notification, ...get().notifications];
    set({
      notifications,
      unreadCount: notifications.filter(n => !n.read).length,
    });
  },

  // Update unread count (for compatibility with existing message system)
  updateUnreadCount: () => {
    const { notifications } = get();
    const unreadCount = notifications.filter(n => !n.read).length;
    set({ unreadCount });
  },

  // Clear all notifications
  clearNotifications: () => {
    set({ notifications: [], unreadCount: 0, error: null });
  },

  // Clear error
  clearError: () => {
    set({ error: null });
  },
}));
