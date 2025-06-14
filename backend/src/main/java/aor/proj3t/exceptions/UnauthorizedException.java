package aor.proj3t.exceptions;

public class UnauthorizedException extends RuntimeException {
    private final String userMessage;

    public UnauthorizedException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public UnauthorizedException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}

