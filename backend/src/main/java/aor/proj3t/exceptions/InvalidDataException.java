
/**
 * Exception thrown when invalid data is encountered, such as failed validation or parsing errors.
 * Used to signal data integrity issues in business logic or API layers.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
package aor.proj3t.exceptions;

public class InvalidDataException extends RuntimeException {
    private final String userMessage;


    /**
     * Constructs a new InvalidDataException with separate user and developer messages.
     * @param userMessage the message to show to end users
     * @param devMessage the detailed message for developers/logs
     */
    public InvalidDataException(String userMessage, String devMessage) {
        super(devMessage);
        this.userMessage = userMessage;
    }


    /**
     * Constructs a new InvalidDataException with the same message for both user and developer.
     * @param message the message for both user and developer
     */
    public InvalidDataException(String message) {
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


