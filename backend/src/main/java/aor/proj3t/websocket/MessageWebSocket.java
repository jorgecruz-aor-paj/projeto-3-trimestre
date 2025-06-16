package aor.proj3t.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.MessageDTO;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Message;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.daos.DAOFactory;
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
 * WebSocket endpoint for real-time messaging and notifications between users.
 * 
 * This class handles WebSocket connections for real-time communication in the
 * application.
 * It supports direct messaging between users and system notifications.
 * The endpoint path includes the user's ID for session management.
 */
@ApplicationScoped
@ServerEndpoint("/websocket/messages/{userId}")
public class MessageWebSocket {

    private static final Logger logger = LogManager.getLogger(MessageWebSocket.class);

    /** JSON binding utility for serialization and deserialization of messages. */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private DAOFactory daoFactory;

    /**
     * Performs cleanup operations when the endpoint is destroyed.
     */
    @PreDestroy
    public void cleanup() {
        try {
            // Close the JSON-B instance to free resources
            if (jsonb != null) {
                jsonb.close();
            }
        } catch (Exception e) {
            logger.error("Error closing JSON-B instance: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles new WebSocket connections
     * 
     * @param session WebSocket session that was opened
     * @param userId  User ID from the path parameter
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        logger.info("WebSocket connection opened for user: {}", userId);
        WebSocketSessionManager.addSession(userId, session);

        // Store user ID as a session property for future reference
        session.getUserProperties().put("userId", userId);

        try {
            // Send connection confirmation
            Map<String, Object> response = Map.of(
                    "type", "connect",
                    "status", "success",
                    "userId", userId);
            session.getBasicRemote().sendText(jsonb.toJson(response));

            // Send any pending notifications or messages
            sendPendingNotifications(userId);
        } catch (IOException | JsonbException e) {
            logger.error("Error sending connection confirmation to user {}", userId, e);
        }
    }

    /**
     * Handles WebSocket connection closures
     * 
     * @param session WebSocket session that was closed
     * @param userId  User ID from the path parameter
     * @param reason  Reason for closure
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") Long userId, CloseReason reason) {
        logger.info("WebSocket connection closed for user: {}. Reason: {}", userId, reason.getReasonPhrase());
        WebSocketSessionManager.removeSession(userId);
    }

    /**
     * Handles WebSocket errors
     * 
     * @param session   WebSocket session where the error occurred
     * @param userId    User ID from the path parameter
     * @param throwable The error that occurred
     */
    @OnError
    public void onError(Session session, @PathParam("userId") Long userId, Throwable throwable) {
        logger.error("WebSocket error for user: {}", userId, throwable);
    }

    /**
     * Handles incoming WebSocket messages
     * 
     * @param session WebSocket session where the message was received
     * @param userId  User ID from the path parameter
     * @param message The message content received
     */
    @OnMessage
    public void onMessage(Session session, @PathParam("userId") Long userId, String message) {
        try {
            logger.info("Received message from user {}: {}", userId, message);

            // Parse the incoming WebSocket message using JSON-B
            @SuppressWarnings("unchecked")
            Map<String, Object> messageData = jsonb.fromJson(message, Map.class);

            String type = (String) messageData.get("type");

            // Verify user ID matches sender ID for security
            Long senderId = Long.valueOf(messageData.get("senderId").toString());
            if (!userId.equals(senderId)) {
                sendErrorMessage(session, "User ID mismatch");
                return;
            }

            Long recipientId = Long.valueOf(messageData.get("recipientId").toString());
            String content = messageData.get("content").toString();

            // Process based on message type
            switch (type) {
                case "message":
                    handleDirectMessage(senderId, recipientId, content, session);
                    break;
                case "notification":
                    handleNotification(senderId, recipientId, content);
                    break;
                default:
                    logger.warn("Unknown message type: {}", type);
                    sendErrorMessage(session, "Unknown message type: " + type);
            }

        } catch (Exception e) {
            logger.error("Error processing WebSocket message", e);
            sendErrorMessage(session, "Error processing message: " + e.getMessage());
        }
    }

    /**
     * Sends an error message to a client session
     * 
     * @param session      The WebSocket session to receive the error
     * @param errorMessage The error message to send
     */
    private void sendErrorMessage(Session session, String errorMessage) {
        try {
            Map<String, Object> error = Map.of(
                    "type", "error",
                    "message", errorMessage);
            session.getBasicRemote().sendText(jsonb.toJson(error));
        } catch (IOException | JsonbException e) {
            logger.error("Error sending error message", e);
        }
    }

    /**
     * Handles direct messages between users
     * 
     * @param senderId    The ID of the sender
     * @param recipientId The ID of the recipient
     * @param content     The message content
     * @param session     The sender's session for confirmation
     */
    private void handleDirectMessage(Long senderId, Long recipientId, String content, Session session) {
        try {
            // Create message entity
            Message newMessage = new Message();
            newMessage.setContent(content);

            // Get User entities from DAO directly
            User sender = daoFactory.forUser().findById(senderId).orElse(null);
            User recipient = daoFactory.forUser().findById(recipientId).orElse(null);

            if (sender == null || recipient == null) {
                logger.error("Sender or recipient not found: senderId={}, recipientId={}", senderId, recipientId);
                sendErrorMessage(session, "Sender or recipient not found");
                return;
            }

            newMessage.setSender(sender);
            newMessage.setRecipient(recipient);

            // Save message using service
            Message savedMessage = serviceFactory.forMessage().createMessage(newMessage);
            MessageDTO messageDTO = serviceFactory.forMessage().getMessage(savedMessage.getId());

            // Prepare message data for sending
            Map<String, Object> messageObject = new HashMap<>();
            messageObject.put("id", messageDTO.getId());
            messageObject.put("senderId", messageDTO.getSenderId());
            messageObject.put("recipientId", messageDTO.getRecipientId());
            messageObject.put("content", messageDTO.getContent());
            messageObject.put("timestamp", messageDTO.getTimestampSent().toString());
            messageObject.put("read", messageDTO.isRead());

            // Message to recipient
            Map<String, Object> recipientMessage = Map.of(
                    "type", "message",
                    "message", messageObject);

            // Message confirmation to sender
            Map<String, Object> senderConfirmation = Map.of(
                    "type", "message_sent",
                    "message", messageObject);

            // Send to recipient if they are connected
            if (WebSocketSessionManager.isUserConnected(recipientId)) {
                sendToUser(recipientId, jsonb.toJson(recipientMessage));
            } else {
                logger.info("User {} is not connected. Message saved for later delivery.", recipientId);
                // Create a notification for offline user
                createOfflineMessageNotification(messageDTO);
            }

            // Send confirmation to sender
            session.getBasicRemote().sendText(jsonb.toJson(senderConfirmation));

        } catch (Exception e) {
            logger.error("Error handling direct message", e);
            sendErrorMessage(session, "Error sending message: " + e.getMessage());
        }
    }

    /**
     * Handles notifications
     * 
     * @param senderId    The ID of the sender
     * @param recipientId The ID of the recipient
     * @param content     The notification content
     */
    private void handleNotification(Long senderId, Long recipientId, String content) {
        try {
            // Get user entities from DAO directly
            User sender = daoFactory.forUser().findById(senderId).orElse(null);
            User recipient = daoFactory.forUser().findById(recipientId).orElse(null);

            if (sender == null || recipient == null) {
                logger.error("Sender or recipient not found: senderId={}, recipientId={}", senderId, recipientId);
                return;
            }

            // Create notification entity
            Notification notification = new Notification();
            notification.setContent(content);
            notification.setType(NotificationType.MESSAGE); // Using MESSAGE type for general notifications
            notification.setSender(sender);
            notification.setRecipient(recipient);

            // Save notification
            Notification savedNotification = serviceFactory.forNotification().createNotification(notification);
            NotificationDTO notificationDTO = serviceFactory.forNotification()
                    .getNotification(savedNotification.getId());

            // Prepare notification data for sending
            Map<String, Object> notificationObject = new HashMap<>();
            notificationObject.put("id", notificationDTO.getId());
            notificationObject.put("senderId", notificationDTO.getSenderId());
            notificationObject.put("recipientId", notificationDTO.getRecipientId());
            notificationObject.put("content", notificationDTO.getContent());
            notificationObject.put("type", notificationDTO.getType().toString());
            notificationObject.put("timestamp", notificationDTO.getTimestampSent().toString());
            notificationObject.put("read", notificationDTO.isRead());

            Map<String, Object> message = Map.of(
                    "type", "notification",
                    "notification", notificationObject);

            // Send to recipient if they are connected
            if (WebSocketSessionManager.isUserConnected(recipientId)) {
                sendToUser(recipientId, jsonb.toJson(message));
            }
        } catch (Exception e) {
            logger.error("Error handling notification", e);
        }
    }

    /**
     * Creates a notification for an offline user that received a message
     * 
     * @param messageDTO The message that was sent to an offline user
     */
    private void createOfflineMessageNotification(MessageDTO messageDTO) {
        try {
            // Get user entities from DAO directly
            User sender = daoFactory.forUser().findById(messageDTO.getSenderId()).orElse(null);
            User recipient = daoFactory.forUser().findById(messageDTO.getRecipientId()).orElse(null);

            if (sender == null || recipient == null) {
                logger.error("Sender or recipient not found for offline notification");
                return;
            }

            UserDTO senderDTO = serviceFactory.forUser().getUser(messageDTO.getSenderId());

            // Create notification
            Notification notification = new Notification();
            notification.setContent("New message from " + senderDTO.getFirstName() + " " + senderDTO.getLastName()
                    + ": " + messageDTO.getContent());
            notification.setType(NotificationType.MESSAGE);
            notification.setSender(sender);
            notification.setRecipient(recipient);

            serviceFactory.forNotification().createNotification(notification);

            // Send an email notification - this would integrate with an email service
            // sendEmailNotification(recipient.getEmail(), "New Message",
            // notification.getContent());

        } catch (Exception e) {
            logger.error("Error creating offline message notification", e);
        }
    }

    /**
     * Sends pending notifications to a user who just connected
     * 
     * @param userId The user who just connected
     */
    private void sendPendingNotifications(Long userId) {
        try {
            // Get unread notifications
            serviceFactory.forNotification().getUserNotifications(userId).stream()
                    .filter(n -> !n.isRead())
                    .forEach(notification -> {
                        try {
                            // Create notification data
                            Map<String, Object> notificationObject = new HashMap<>();
                            notificationObject.put("id", notification.getId());
                            notificationObject.put("senderId", notification.getSenderId());
                            notificationObject.put("recipientId", notification.getRecipientId());
                            notificationObject.put("content", notification.getContent());
                            notificationObject.put("type", notification.getType().toString());
                            notificationObject.put("timestamp", Objects.toString(notification.getTimestampSent(), ""));
                            notificationObject.put("read", notification.isRead());

                            Map<String, Object> message = Map.of(
                                    "type", "notification",
                                    "notification", notificationObject);

                            // Send to the user
                            sendToUser(userId, jsonb.toJson(message));

                        } catch (Exception e) {
                            logger.error("Error sending notification", e);
                        }
                    });

        } catch (Exception e) {
            logger.error("Error sending pending notifications", e);
        }
    }

    /**
     * Sends a message to a specific user
     * 
     * @param userId  The recipient's user ID
     * @param message The message to send
     */
    private void sendToUser(Long userId, String message) {
        try {
            Session session = WebSocketSessionManager.getSession(userId);
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(message);
                logger.debug("Message sent to user {}: {}", userId, message);
            }
        } catch (Exception e) {
            logger.error("Error sending message to user {}", userId, e);
        }
    }

    /**
     * Broadcasts a message to all connected users
     * 
     * @param message The message to broadcast
     */
    public void broadcast(String message) {
        WebSocketSessionManager.getAllSessions().forEach((userId, session) -> {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                logger.error("Error broadcasting message to user {}", userId, e);
            }
        });
    }
}