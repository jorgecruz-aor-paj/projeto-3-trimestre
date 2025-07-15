package aor.proj3t.dto;

/**
 * Data Transfer Object for real-time WebSocket messages.
 * <p>
 * Encapsulates core metadata for messages exchanged over WebSocket channels,
 * supporting dynamic interactions such as chat messages, system notifications,
 * or user alerts. Typically used by messaging brokers or frontend subscriptions
 * to identify message types and route data appropriately.
 * </p>
 *
 * Attributes:
 * - {@code type}: Message type identifier (e.g., "message", "notification")
 * - {@code senderId}: ID of the user who sent the message
 * - {@code recipientId}: ID of the target recipient for the message
 * - {@code content}: The textual content of the message
 * - {@code timestamp}: String-based timestamp of when the message was generated
 *
 * Use cases:
 * - Real-time chat applications
 * - Push notifications between backend and frontend
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class WebSocketMessageDTO {

    /** Type of WebSocket message, e.g. "message", "notification" */
    private String type;

    /** ID of the user who initiated the message */
    private Long senderId;

    /** ID of the user targeted to receive the message */
    private Long recipientId;

    /** Message content or body payload */
    private String content;

    /** Timestamp string representing when the message was sent */
    private String timestamp;

    // Default constructor
    public WebSocketMessageDTO() {
    }

    // Constructor with all fields
    public WebSocketMessageDTO(String type, Long senderId, Long recipientId, String content, String timestamp) {
        this.type = type;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
