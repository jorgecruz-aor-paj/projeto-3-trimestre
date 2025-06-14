package aor.proj3t.exceptions;

public class ForbiddenException extends RuntimeException {
    private final String userMessage;

    public ForbiddenException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public ForbiddenException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}


