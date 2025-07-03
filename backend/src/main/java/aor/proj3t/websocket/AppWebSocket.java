package aor.proj3t.websocket;

import java.io.IOException;
import java.util.HashMap;
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
 * Simple WebSocket endpoint for real-time messaging between users.
 * 
 * This class handles WebSocket connections for real-time message delivery.
 * Messages are sent via REST API, WebSocket is only used for instant delivery.
 */
@ApplicationScoped
@ServerEndpoint("/websocket/{token}")
public class AppWebSocket {

    private static final Logger logger = LogManager.getLogger(AppWebSocket.class);
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    private ServiceFactory serviceFactory;

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

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Long userId = (Long) session.getUserProperties().get("userId");
        if (userId != null) {
            logger.info("WebSocket connection closed for user: {}. Reason: {}", userId, reason.getReasonPhrase());
            WebSocketSessionManager.removeSession(userId);
        }
    }

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
     * Public method to send a new message to a user via WebSocket
     * This is called from MessageResource after saving the message
     */
    public void notifyNewMessage(MessageDTO messageDTO) {
        logger.info("Notifying new message: from {} to {}", messageDTO.getSenderId(), messageDTO.getRecipientId());

        // Prepare message data
        Map<String, Object> messageObject = new HashMap<>();
        messageObject.put("id", messageDTO.getId());
        messageObject.put("senderId", messageDTO.getSenderId());
        messageObject.put("recipientId", messageDTO.getRecipientId());
        messageObject.put("content", messageDTO.getContent());
        messageObject.put("timestampSent", messageDTO.getTimestampSent().toString());
        messageObject.put("read", messageDTO.isRead());

        // Send to recipient
        Map<String, Object> recipientMessage = Map.of(
                "type", "message",
                "message", messageObject);

        // Send to sender as confirmation
        Map<String, Object> senderConfirmation = Map.of(
                "type", "message_sent",
                "message", messageObject);

        // Send to recipient if connected
        sendToUser(messageDTO.getRecipientId(), recipientMessage);

        // Send confirmation to sender if connected
        sendToUser(messageDTO.getSenderId(), senderConfirmation);
    }

    /**
     * Public method to notify message read status
     */
    public void notifyMessageRead(Long messageId, Long senderId) {
        Map<String, Object> readStatus = Map.of(
                "type", "message_read",
                "messageId", messageId);
        sendToUser(senderId, readStatus);
    }

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
        } catch (Exception e) {
            logger.error("Error sending message to user {}", userId, e);
            WebSocketSessionManager.removeSession(userId);
        }
    }

    private void sendMessage(Session session, Map<String, Object> data) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(jsonb.toJson(data));
            }
        } catch (IOException | JsonbException e) {
            logger.error("Error sending message to session", e);
        }
    }

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