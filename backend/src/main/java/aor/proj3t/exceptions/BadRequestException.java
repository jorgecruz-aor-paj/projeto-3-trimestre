package aor.proj3t.exceptions;

public class BadRequestException extends RuntimeException {
    private final String userMessage;

    public BadRequestException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public BadRequestException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}