package aor.proj3t.enums;

/**
 * Enum representing the different types of JWT tokens used in the application.
 * Each token type serves a specific purpose and may have different validation
 * rules, expiration times, or claim requirements.
 */
public enum TokenType {
    /**
     * Token used for user authentication sessions.
     * These tokens are issued after successful login and allow the user
     * to access protected resources.
     */
    SESSION,

    /**
     * Token used for account confirmation processes.
     * These tokens are sent to users' email addresses to verify ownership
     * during registration or email change procedures.
     */
    ACCOUNT_CONFIRMATION,

    /**
     * Token used for password reset procedures.
     * These tokens are issued when a user requests a password reset
     * and are typically sent via email.
     */
    PASSWORD_RESET
}
