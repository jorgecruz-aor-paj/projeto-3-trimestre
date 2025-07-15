// src/services/websocketService.js
import { WEBSOCKET_BASE_URL } from 'api/apiConfig';
import { useAuthStore } from 'stores/AuthStore';

class WebSocketService {
  constructor() {
    this.socket = null;
    this.notificationHandlers = [];
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 3;
    this.reconnectDelay = 2000; // 2 seconds
  }

  connect() {
    // If already connected, do nothing
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      return this.socket;
    }

    try {
      const token =
        useAuthStore.getState()?.token || sessionStorage.getItem('token');

      if (!token) {
        throw new Error('Authentication token required');
      }

      const wsUrl = `${WEBSOCKET_BASE_URL}/websocket/${token}`;
      //console.log('Connecting to WebSocket...');

      this.socket = new WebSocket(wsUrl);

      this.socket.onopen = () => {
        //console.log('WebSocket connected');
        this.reconnectAttempts = 0; // Reset on successful connection
      };

      this.socket.onmessage = event => {
        try {
          const data = JSON.parse(event.data);
          //console.log('WebSocket notification received:', data);

          switch (data.type) {
            case 'error':
              console.error('WebSocket error:', data.message);
              break;

            case 'new_message':
            case 'message_read':
            case 'new_notification':
              // Just pass notifications to handlers
              this.notificationHandlers.forEach(handler => handler(data));
              break;

            case 'connect':
             //console.log('WebSocket connection confirmed');
              break;

            default:
              //console.warn('Unknown notification type:', data.type);
          }
        } catch (error) {
          //console.error('Error parsing WebSocket notification:', error);
        }
      };

      this.socket.onclose = event => {
        //console.log('WebSocket disconnected:', event.code, event.reason);
        this.socket = null;

        // Only try to reconnect if user is still authenticated and it wasn't a manual disconnect
        if (event.reason !== 'User logout' && useAuthStore.getState()?.token) {
          this.handleReconnect();
        }
      };

      this.socket.onerror = error => {
        //console.error('WebSocket error:', error);
      };

      return this.socket;
    } catch (error) {
      //console.error('Failed to create WebSocket connection:', error);
      throw error;
    }
  }

  handleReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      //console.log(
      //  `Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`
      //);

      setTimeout(() => {
        // Only reconnect if still authenticated
        if (useAuthStore.getState()?.token) {
          this.connect();
        }
      }, this.reconnectDelay);
    } else {
      //console.error(
      //  'Max reconnection attempts reached. Please refresh the page.'
      //);
    }
  }

  disconnect() {
    if (this.socket) {
      //console.log('Disconnecting WebSocket');
      this.socket.close(1000, 'User logout');
      this.socket = null;
    }
  }

  send(data) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(data));
    } else {
      console.warn('WebSocket is not connected');
    }
  }

  sendReadReceipt(messageId) {
    this.send({
      type: 'mark_read',
      messageId: messageId,
    });
  }

  onNotification(handler) {
    this.notificationHandlers.push(handler);
    return () => {
      this.notificationHandlers = this.notificationHandlers.filter(
        h => h !== handler
      );
    };
  }

  isConnected() {
    return this.socket && this.socket.readyState === WebSocket.OPEN;
  }
}

export const websocketService = new WebSocketService();
