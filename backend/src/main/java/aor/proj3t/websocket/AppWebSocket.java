package aor.proj3t.websocket;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.service.ServiceFactory;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * WebSocket Endpoint for Real-time Communication.
 * 
 * This class provides a WebSocket server endpoint for real-time messaging and
 * notification
 * delivery between users. It handles connection authentication via JWT tokens
 * and manages
 * bidirectional communication for instant message delivery and status updates.
 * 
 * <p>
 * The WebSocket endpoint is designed to complement the REST API by providing
 * instant
 * delivery of messages and notifications. Messages are still stored and managed
 * via REST
 * endpoints, while WebSocket provides real-time delivery capabilities.
 * </p>
 * 
 * <p>
 * Key features:
 * </p>
 * <ul>
 * <li>Token-based authentication for WebSocket connections</li>
 * <li>Real-time message notifications</li>
 * <li>Message read status updates</li>
 * <li>General notification delivery</li>
 * <li>Automatic session cleanup on errors</li>
 * </ul>
 * 
 * <p>
 * WebSocket URL format: {@code /websocket/{token}}
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see WebSocketSessionManager
 * @see ServiceFactory
 */
@ApplicationScoped
@ServerEndpoint("/websocket/{token}")
public class AppWebSocket {

    private static final Logger logger = LogManager.getLogger(AppWebSocket.class);

    /**
     * JSON-B instance for serializing/deserializing WebSocket messages.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Service factory for accessing business logic services.
     */
    @Inject
    private ServiceFactory serviceFactory;

    /**
     * Cleanup method called before the application scope is destroyed.
     * 
     * <p>
     * Ensures proper cleanup of JSON-B resources to prevent memory leaks.
     * </p>
     */
    @PreDestroy
    public void cleanup() {
        try {
            if (jsonb != null) {
                jsonb.close();
            }
        } catch (Exception e) {
            logger.error("Error closing JSON-B instance: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles new WebSocket connections.
     * 
     * <p>
     * This method is called when a client establishes a WebSocket connection.
     * It validates the provided JWT token, extracts the user ID, and registers
     * the session for real-time communication.
     * </p>
     * 
     * <p>
     * If authentication fails, the connection is immediately closed with an
     * appropriate reason code.
     * </p>
     * 
     * @param session the WebSocket session for the new connection
     * @param token   the JWT token provided in the URL path for authentication
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            logger.info("WebSocket connection attempt");

            // Validate token and get user information
            Long userId = serviceFactory.forToken().getUserIdFromToken(token);

            if (userId == null) {
                logger.error("Invalid token provided for WebSocket connection");
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid token"));
                return;
            }

            logger.info("WebSocket connection opened for user: {}", userId);

            // Store the session and user ID
            WebSocketSessionManager.addSession(userId, session);
            session.getUserProperties().put("userId", userId);

            // Send connection confirmation
            sendMessage(session, Map.of(
                    "type", "connect",
                    "status", "success",
                    "userId", userId));

        } catch (IOException | JsonbException e) {
            logger.error("Error during WebSocket connection: {}", e.getMessage(), e);
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Authentication failed"));
            } catch (IOException ex) {
                logger.error("Error closing invalid session", ex);
            }
        }
    }

    /**
     * Handles WebSocket connection closures.
     * 
     * <p>
     * This method is called when a WebSocket connection is closed, either by
     * the client or due to an error. It removes the user's session from the
     * session manager to prevent memory leaks.
     * </p>
     * 
     * @param session the WebSocket session that was closed
     * @param reason  the reason for connection closure
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Long userId = (Long) session.getUserProperties().get("userId");
        if (userId != null) {
            logger.info("WebSocket connection closed for user: {}. Reason: {}", userId, reason.getReasonPhrase());
            WebSocketSessionManager.removeSession(userId);
        }
    }

    /**
     * Handles WebSocket errors.
     * 
     * <p>
     * This method is called when an error occurs on a WebSocket connection.
     * It logs the error and removes the user's session from the session manager.
     * </p>
     * 
     * @param session   the WebSocket session that encountered an error
     * @param throwable the error that occurred
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        Long userId = (Long) session.getUserProperties().get("userId");
        if (userId != null) {
            logger.error("WebSocket error for user: {}", userId, throwable);
            WebSocketSessionManager.removeSession(userId);
        } else {
            logger.error("WebSocket error for unknown user", throwable);
        }
    }

    /**
     * Handles incoming messages from WebSocket clients.
     * 
     * <p>
     * This method processes various types of messages sent by clients through
     * the WebSocket connection. Currently supports:
     * </p>
     * <ul>
     * <li>"mark_read" - marks a message as read</li>
     * </ul>
     * 
     * <p>
     * Messages should be in JSON format with a "type" field indicating the
     * message type and additional fields as required by each type.
     * </p>
     * 
     * @param session the WebSocket session that sent the message
     * @param message the JSON message content sent by the client
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            Long userId = (Long) session.getUserProperties().get("userId");
            if (userId == null) {
                logger.error("No user ID found in session properties");
                sendErrorMessage(session, "User not authenticated");
                return;
            }

            logger.info("Received message from user {}: {}", userId, message);

            @SuppressWarnings("unchecked")
            Map<String, Object> messageData = jsonb.fromJson(message, Map.class);
            String type = (String) messageData.get("type");

            switch (type) {
                case "mark_read" -> {
                    Long messageId = Long.valueOf(messageData.get("messageId").toString());
                    handleMarkAsRead(messageId);
                }
                default -> {
                    logger.warn("Unknown message type: {}", type);
                    sendErrorMessage(session, "Unknown message type: " + type);
                }
            }

        } catch (JsonbException | NumberFormatException e) {
            logger.error("Error processing WebSocket message", e);
            sendErrorMessage(session, "Error processing message: " + e.getMessage());
        }
    }

    /**
     * Sends a new message notification to a specific user.
     * 
     * <p>
     * This method is called by other parts of the application (typically message
     * services) to notify a user when they receive a new message. The notification
     * is sent only if the user is currently connected via WebSocket.
     * </p>
     * 
     * @param recipientId the ID of the user who should receive the notification
     * @param messageId   the ID of the new message
     */
    public void notifyNewMessage(Long recipientId, Long messageId) {
        logger.info("Notifying new message {} to user {}", messageId, recipientId);

        Map<String, Object> notification = Map.of(
                "type", "new_message",
                "messageId", messageId);

        sendToUser(recipientId, notification);
    }

    /**
     * Notifies a user when their sent message has been read.
     * 
     * <p>
     * This method is called to inform the message sender that their message
     * has been marked as read by the recipient.
     * </p>
     * 
     * @param messageId the ID of the message that was read
     * @param senderId  the ID of the user who sent the message (recipient of this
     *                  notification)
     */
    public void notifyMessageRead(Long messageId, Long senderId) {
        Map<String, Object> readStatus = Map.of(
                "type", "message_read",
                "messageId", messageId);
        sendToUser(senderId, readStatus);
    }

    /**
     * Sends a general notification to a specific user.
     * 
     * <p>
     * This method is used to send various types of system notifications
     * to users in real-time, such as evaluation updates, training announcements,
     * or other application events.
     * </p>
     * 
     * @param recipientId      the ID of the user who should receive the
     *                         notification
     * @param notificationType the type/category of the notification
     * @param content          the notification content/message
     */
    public void notifyNewNotification(Long recipientId, String notificationType, String content) {
        logger.info("Notifying new notification to user {}: {}", recipientId, content);

        Map<String, Object> notification = Map.of(
                "type", "new_notification",
                "notificationType", notificationType,
                "content", content,
                "timestamp", java.time.Instant.now().toString());

        sendToUser(recipientId, notification);
    }

    /**
     * Handles the "mark_read" message type from clients.
     * 
     * <p>
     * This private method processes requests to mark a message as read.
     * It updates the message status in the database and notifies the original
     * sender that their message has been read.
     * </p>
     * 
     * @param messageId the ID of the message to mark as read
     */
    private void handleMarkAsRead(Long messageId) {
        try {
            Boolean success = serviceFactory.forMessage().markAsRead(messageId);
            if (success) {
                MessageDTO messageDTO = serviceFactory.forMessage().getMessage(messageId);
                notifyMessageRead(messageId, messageDTO.getSenderId());
            }
        } catch (RuntimeException e) {
            logger.error("Error handling read status update for message {}", messageId, e);
        }
    }

    /**
     * Sends a message to a specific user via WebSocket.
     * 
     * <p>
     * This private method handles the actual delivery of messages to users.
     * It checks if the user is connected and has an active session before
     * attempting to send the message. If the session is invalid, it's removed
     * from the session manager.
     * </p>
     * 
     * @param userId the ID of the user to send the message to
     * @param data   the message data to send (will be serialized to JSON)
     */
    private void sendToUser(Long userId, Map<String, Object> data) {
        try {
            Session session = WebSocketSessionManager.getSession(userId);
            if (session != null && session.isOpen()) {
                String jsonMessage = jsonb.toJson(data);
                session.getAsyncRemote().sendText(jsonMessage);
                logger.debug("Message sent to user {}", userId);
            } else {
                logger.debug("User {} is not connected", userId);
                WebSocketSessionManager.removeSession(userId);
            }
        } catch (JsonbException e) {
            logger.error("Error sending message to user {}", userId, e);
            WebSocketSessionManager.removeSession(userId);
        }
    }

    /**
     * Sends a message to a specific WebSocket session.
     * 
     * <p>
     * This private method is used for sending messages directly to a session,
     * typically for connection confirmations or immediate responses.
     * </p>
     * 
     * @param session the WebSocket session to send the message to
     * @param data    the message data to send (will be serialized to JSON)
     */
    private void sendMessage(Session session, Map<String, Object> data) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(jsonb.toJson(data));
            }
        } catch (IOException | JsonbException e) {
            logger.error("Error sending message to session", e);
        }
    }

    /**
     * Sends an error message to a specific WebSocket session.
     * 
     * <p>
     * This private method is used to send error responses to clients when
     * their requests cannot be processed or when validation fails.
     * </p>
     * 
     * @param session      the WebSocket session to send the error message to
     * @param errorMessage the error message to send
     */
    private void sendErrorMessage(Session session, String errorMessage) {
        try {
            if (session.isOpen()) {
                Map<String, Object> error = Map.of(
                        "type", "error",
                        "message", errorMessage);
                session.getBasicRemote().sendText(jsonb.toJson(error));
            }
        } catch (IOException | JsonbException e) {
            logger.error("Error sending error message", e);
        }
    }
}