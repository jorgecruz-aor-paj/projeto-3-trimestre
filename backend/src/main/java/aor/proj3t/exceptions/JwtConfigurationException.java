package aor.proj3t.exceptions;

/**
 * Exception thrown when JWT configuration is invalid or missing.
 * This typically occurs during application startup when JWT settings are not
 * properly configured.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class JwtConfigurationException extends RuntimeException {
    private final String userMessage;

    /**
     * Constructs a new JwtConfigurationException with separate user and developer messages.
     * @param userMessage the message to show to end users
     * @param devMessage the detailed message for developers/logs
     */
    public JwtConfigurationException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }

    /**
     * Constructs a new JwtConfigurationException with the same message for both user and developer.
     * @param message the message for both user and developer
     */
    public JwtConfigurationException(String message) {
        this(message, message);
    }

    /**
     * Gets the user-friendly message.
     * @return the user message
     */
    public String getUserMessage() {
        return userMessage;
    }
}
