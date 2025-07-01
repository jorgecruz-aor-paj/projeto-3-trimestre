package aor.proj3t.dto;

/**
 * Data transfer object for WebSocket messages
 */
public class WebSocketMessageDTO {
    private String type; // 'message', 'notification', etc
    private Long senderId;
    private Long recipientId;
    private String content;
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
