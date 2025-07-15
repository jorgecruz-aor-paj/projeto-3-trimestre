package aor.proj3t.exceptions;

/**
 * Exception thrown when a JWT token is invalid, expired, or malformed.
 * This is typically thrown during token validation or claim extraction
 * operations.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class JwtTokenException extends UnauthorizedException {

    /**
     * Constructs a new JwtTokenException with separate user and developer messages.
     * @param userMessage the message to show to end users
     * @param devMessage the detailed message for developers/logs
     */
    public JwtTokenException(String userMessage, String devMessage) {
        super(userMessage, devMessage);
    }

    /**
     * Constructs a new JwtTokenException with the same message for both user and developer.
     * @param message the message for both user and developer
     */
    public JwtTokenException(String message) {
        super(message, message);
    }
}
