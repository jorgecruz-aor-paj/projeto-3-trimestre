import { axiosInstance } from 'api/axiosInstance';
import { API_ENDPOINTS } from 'api/apiConfig';

/**
 * Service for notification operations
 */
export const notificationService = {
  /**
   * Get all notifications for the current user
   * @returns {Promise<Array>} Array of notification objects
   */
  async getUserNotifications() {
    try {
      const response = await axiosInstance.get(
        API_ENDPOINTS.notifications.getUserNotifications.url
      );
      return response || [];
    } catch (error) {
      console.error('Error fetching user notifications:', error);
      throw new Error('Falha ao carregar notificações');
    }
  },

  /**
   * Mark a notification as read
   * @param {number} notificationId - The notification ID
   * @returns {Promise<boolean>} Success status
   */
  async markAsRead(notificationId) {
    try {
      await axiosInstance.put(
        API_ENDPOINTS.notifications.markAsRead.url(notificationId)
      );
      return true;
    } catch (error) {
      console.error('Error marking notification as read:', error);
      throw new Error('Falha ao marcar notificação como lida');
    }
  },

  /**
   * Mark all notifications as read
   * @returns {Promise<boolean>} Success status
   */
  async markAllAsRead() {
    try {
      await axiosInstance.put(API_ENDPOINTS.notifications.markAllAsRead.url);
      return true;
    } catch (error) {
      console.error('Error marking all notifications as read:', error);
      throw new Error('Falha ao marcar todas as notificações como lidas');
    }
  },

  /**
   * Delete a notification
   * @param {number} notificationId - The notification ID
   * @returns {Promise<boolean>} Success status
   */
  async deleteNotification(notificationId) {
    try {
      await axiosInstance.delete(
        API_ENDPOINTS.notifications.delete.url(notificationId)
      );
      return true;
    } catch (error) {
      console.error('Error deleting notification:', error);
      throw new Error('Falha ao eliminar notificação');
    }
  },
};
