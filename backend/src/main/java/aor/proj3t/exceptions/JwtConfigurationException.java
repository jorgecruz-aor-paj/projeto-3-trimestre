package aor.proj3t.exceptions;

/**
 * Exception thrown when JWT configuration is invalid or missing.
 * This typically occurs during application startup when JWT settings are not
 * properly configured.
 */
public class JwtConfigurationException extends RuntimeException {
    private final String userMessage;

    public JwtConfigurationException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    public JwtConfigurationException(String message) {
        this(message, message);
    }

    public String getUserMessage() {
        return userMessage;
    }
}
