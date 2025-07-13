package aor.proj3t.exceptions;

/**
 * Exception thrown when two-factor authentication operations fail.
 * This exception is used to handle errors related to TOTP generation,
 * verification, secret key management, and other 2FA operations.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class TwoFactorAuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String userMessage;
    private final String developerMessage;

    /**
     * Constructs a new TwoFactorAuthException with the specified detail message.
     * 
     * @param message the detail message
     */
    public TwoFactorAuthException(String message) {
        super(message);
        this.userMessage = message;
        this.developerMessage = message;
    }

    /**
     * Constructs a new TwoFactorAuthException with the specified detail message and
     * cause.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public TwoFactorAuthException(String message, Throwable cause) {
        super(message, cause);
        this.userMessage = message;
        this.developerMessage = message;
    }

    /**
     * Constructs a new TwoFactorAuthException with separate user and developer
     * messages.
     * 
     * @param userMessage      the message to show to end users
     * @param developerMessage the detailed message for developers/logs
     */
    public TwoFactorAuthException(String userMessage, String developerMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
    }

    /**
     * Constructs a new TwoFactorAuthException with separate messages and a cause.
     * 
     * @param userMessage      the message to show to end users
     * @param developerMessage the detailed message for developers/logs
     * @param cause            the cause of the exception
     */
    public TwoFactorAuthException(String userMessage, String developerMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
    }

    /**
     * Gets the user-friendly message.
     * 
     * @return the user message
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Gets the detailed message for developers.
     * 
     * @return the developer message
     */
    public String getDeveloperMessage() {
        return developerMessage;
    }
}
