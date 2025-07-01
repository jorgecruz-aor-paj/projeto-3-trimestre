// src/services/websocketService.js
import { WEBSOCKET_BASE_URL } from 'api/apiConfig';
import { useAuthStore } from 'stores/AuthStore';

class WebSocketService {
  constructor() {
    this.socket = null;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectDelay = 1000;
  }

  connect(endpoint) {
    try {
      const token =
        useAuthStore.getState()?.token || sessionStorage.getItem('token');
      const wsUrl = `${WEBSOCKET_BASE_URL}${endpoint}${
        token ? `?token=${token}` : ''
      }`;

      this.socket = new WebSocket(wsUrl);

      this.socket.onopen = () => {
        console.log('WebSocket connected');
        this.reconnectAttempts = 0;
      };

      this.socket.onclose = event => {
        console.log('WebSocket disconnected:', event.code, event.reason);
        this.handleReconnect();
      };

      this.socket.onerror = error => {
        console.error('WebSocket error:', error);
      };

      return this.socket;
    } catch (error) {
      console.error('Failed to create WebSocket connection:', error);
      throw error;
    }
  }

  handleReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      setTimeout(() => {
        console.log(
          `Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`
        );
        this.connect();
      }, this.reconnectDelay * this.reconnectAttempts);
    }
  }

  disconnect() {
    if (this.socket) {
      this.socket.close();
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
}

export const websocketService = new WebSocketService();
