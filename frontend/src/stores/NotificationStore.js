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
  // Pagination state
  currentPage: 0,
  pageSize: 10,
  totalCount: 0,
  totalPages: 0,
  hasMore: false,
  loadingMore: false,

  // Actions
  loadNotifications: async (page = 0, size = 10, append = false) => {
    const loadingKey = append ? 'loadingMore' : 'loading';
    set({ [loadingKey]: true, error: null });

    try {
      const response = await notificationService.getUserNotificationsPaginated(
        page,
        size
      );

      const newNotifications = response.entities || [];
      const unreadCount = newNotifications.filter(n => !n.read).length;

      // Calculate if there are more pages
      const hasMore = page + 1 < (response.totalPages || 0);

      set(state => ({
        notifications: append
          ? [...state.notifications, ...newNotifications]
          : newNotifications,
        unreadCount: append ? state.unreadCount + unreadCount : unreadCount,
        currentPage: page,
        pageSize: size,
        totalCount: response.totalCount || 0,
        totalPages: response.totalPages || 0,
        hasMore,
        [loadingKey]: false,
      }));
    } catch (error) {
      console.error('Error loading notifications:', error);
      set({
        error: error.message || 'Falha ao carregar notificações',
        [loadingKey]: false,
      });
    }
  },

  // Load more notifications (for infinite scroll or pagination)
  loadMoreNotifications: async () => {
    const { currentPage, pageSize, hasMore, loadingMore } = get();

    if (!hasMore || loadingMore) return;

    await get().loadNotifications(currentPage + 1, pageSize, true);
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
      totalCount: get().totalCount + 1,
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
    set({
      notifications: [],
      unreadCount: 0,
      error: null,
      currentPage: 0,
      totalCount: 0,
      totalPages: 0,
      hasMore: false,
    });
  },

  // Clear error
  clearError: () => {
    set({ error: null });
  },
}));
