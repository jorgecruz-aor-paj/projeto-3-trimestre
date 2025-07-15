
/**
 * Exception thrown when an operation is forbidden, such as insufficient permissions.
 * Used to signal HTTP 403 Forbidden errors in REST APIs or similar scenarios.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
package aor.proj3t.exceptions;

public class ForbiddenException extends RuntimeException {
    private final String userMessage;


    /**
     * Constructs a new ForbiddenException with separate user and developer messages.
     * @param userMessage the message to show to end users
     * @param devMessage the detailed message for developers/logs
     */
    public ForbiddenException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }


    /**
     * Constructs a new ForbiddenException with the same message for both user and developer.
     * @param message the message for both user and developer
     */
    public ForbiddenException(String message) {
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


