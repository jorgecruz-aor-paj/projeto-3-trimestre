package aor.proj3t.exceptions;

public class InvalidDataException extends RuntimeException {
    private final String userMessage;

    public InvalidDataException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public InvalidDataException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}


