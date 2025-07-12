package aor.proj3t.exceptions;

/**
 * Exception thrown when a JWT token is invalid, expired, or malformed.
 * This is typically thrown during token validation or claim extraction
 * operations.
 */
public class JwtTokenException extends UnauthorizedException {

    public JwtTokenException(String userMessage, String devMessage) {
        super(userMessage, devMessage);
    }

    public JwtTokenException(String message) {
        super(message, message);
    }
}
