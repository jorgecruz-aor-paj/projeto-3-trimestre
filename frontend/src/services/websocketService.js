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

      if (!token) {
        console.error(
          'No authentication token available for WebSocket connection'
        );
        throw new Error('Authentication token required');
      }

      const wsUrl = `${WEBSOCKET_BASE_URL}/websocket/${token}`;

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

          // Add type validation
          if (!data.type) {
            console.warn('Received message without type:', data);
            return;
          }

          // Handle different message types more robustly
          switch (data.type) {
            case 'error':
              console.error('WebSocket error from server:', data.message);
              if (
                data.message.includes('Authentication') ||
                data.message.includes('Unauthorized') ||
                data.message.includes('Invalid token')
              ) {
                this.disconnect();
                console.error('WebSocket authentication failed, disconnecting');
                return;
              }
              break;

            case 'message':
            case 'message_sent':
              this.messageHandlers.forEach(handler => {
                try {
                  handler(data);
                } catch (error) {
                  console.error('Error in message handler:', error);
                }
              });
              break;

            case 'notification':
              this.notificationHandlers.forEach(handler => {
                try {
                  handler(data);
                } catch (error) {
                  console.error('Error in notification handler:', error);
                }
              });
              break;

            case 'connect':
              console.log('WebSocket connection confirmed:', data);
              break;

            case 'message_read':
              // Handle read receipts
              this.messageHandlers.forEach(handler => {
                try {
                  handler(data);
                } catch (error) {
                  console.error('Error in read receipt handler:', error);
                }
              });
              break;

            default:
              console.warn('Unknown message type:', data.type, data);
          }
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      };

      this.socket.onclose = event => {
        console.log('WebSocket disconnected:', event.code, event.reason);
        this.connected = false;

        // Don't attempt to reconnect on authentication errors
        if (
          event.code === 1008 ||
          event.reason.includes('Authentication') ||
          event.reason.includes('Unauthorized') ||
          event.reason.includes('Invalid token')
        ) {
          console.error(
            'WebSocket closed due to authentication error:',
            event.reason
          );
          return;
        }

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

  // Send read receipt via WebSocket
  sendReadReceipt(messageId, senderId) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      const readReceiptData = {
        type: 'mark_read',
        senderId: useAuthStore.getState().user?.id,
        recipientId: senderId, // The sender becomes the recipient of the read receipt
        messageId: messageId,
      };
      this.socket.send(JSON.stringify(readReceiptData));
    } else {
      console.warn('WebSocket is not connected, cannot send read receipt');
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
