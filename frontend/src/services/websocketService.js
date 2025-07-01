// src/services/websocketService.js
import { WEBSOCKET_BASE_URL } from 'api/apiConfig';
import { useAuthStore } from 'stores/AuthStore';

class WebSocketService {
  constructor() {
    this.socket = null;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectDelay = 1000;
    this.messageHandlers = [];
    this.notificationHandlers = [];
    this.connected = false;
  }

  connect(userId) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      return this.socket;
    }

    try {
      const token =
        useAuthStore.getState()?.token || sessionStorage.getItem('token');
      const wsUrl = `${WEBSOCKET_BASE_URL}/websocket/messages/${userId}${
        token ? `?token=${token}` : ''
      }`;

      console.log('Connecting to WebSocket:', wsUrl);
      this.socket = new WebSocket(wsUrl);

      this.socket.onopen = () => {
        console.log('WebSocket connected');
        this.connected = true;
        this.reconnectAttempts = 0;
      };

      this.socket.onmessage = event => {
        try {
          const data = JSON.parse(event.data);
          console.log('WebSocket message received:', data);

          if (data.type === 'message') {
            this.messageHandlers.forEach(handler => handler(data));
          } else if (data.type === 'notification') {
            this.notificationHandlers.forEach(handler => handler(data));
          }
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.socket.onclose = event => {
        console.log('WebSocket disconnected:', event.code, event.reason);
        this.connected = false;
        if (event.code !== 1000) {
          // Not a normal closure
          this.handleReconnect(userId);
        }
      };

      this.socket.onerror = error => {
        console.error('WebSocket error:', error);
        this.connected = false;
      };

      return this.socket;
    } catch (error) {
      console.error('Failed to create WebSocket connection:', error);
      throw error;
    }
  }

  handleReconnect(userId) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      setTimeout(() => {
        console.log(
          `Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`
        );
        this.connect(userId);
      }, this.reconnectDelay * this.reconnectAttempts);
    }
  }

  disconnect() {
    if (this.socket) {
      this.socket.close(1000, 'Manual disconnect');
      this.socket = null;
      this.connected = false;
    }
  }

  send(data) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(data));
    } else {
      console.warn('WebSocket is not connected');
    }
  }

  // Subscribe to message events
  onMessage(handler) {
    this.messageHandlers.push(handler);
    return () => {
      this.messageHandlers = this.messageHandlers.filter(h => h !== handler);
    };
  }

  // Subscribe to notification events
  onNotification(handler) {
    this.notificationHandlers.push(handler);
    return () => {
      this.notificationHandlers = this.notificationHandlers.filter(
        h => h !== handler
      );
    };
  }

  // Check if connected
  isConnected() {
    return (
      this.connected && this.socket && this.socket.readyState === WebSocket.OPEN
    );
  }
}

export const websocketService = new WebSocketService();
