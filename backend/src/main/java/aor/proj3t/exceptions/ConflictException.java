package aor.proj3t.exceptions;

public class ConflictException extends RuntimeException {
    private final String userMessage;

    public ConflictException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public ConflictException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}

