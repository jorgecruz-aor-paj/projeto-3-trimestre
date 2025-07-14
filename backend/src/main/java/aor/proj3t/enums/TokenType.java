package aor.proj3t.enums;

/**
 * Enum representing the different types of JWT tokens used in the application.
 * Each token type serves a specific purpose and may have different validation
 * rules, expiration times, or claim requirements.
 * 
 * <p>
 * This enum helps the application distinguish between different token contexts
 * and apply appropriate security measures for each type.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public enum TokenType {
    /**
     * Token used for user authentication sessions.
     * These tokens are issued after successful login and allow the user
     * to access protected resources during their session.
     * 
     * <p>
     * Session tokens typically have longer expiration times and are
     * used for general API authentication.
     * </p>
     */
    SESSION,

    /**
     * Token used for account confirmation processes.
     * These tokens are sent to users' email addresses to verify ownership
     * during registration or email change procedures.
     * 
     * <p>
     * Account confirmation tokens are typically single-use and have
     * shorter expiration times for security purposes.
     * </p>
     */
    ACCOUNT_CONFIRMATION,

    /**
     * Token used for password reset procedures.
     * These tokens are issued when a user requests a password reset
     * and are typically sent via email with a secure link.
     * 
     * <p>
     * Password reset tokens are single-use and have very short
     * expiration times to minimize security risks.
     * </p>
     */
    PASSWORD_RESET,

    /**
     * Token used for two-factor authentication during login.
     * These tokens are issued after successful credential verification
     * but before 2FA verification is completed.
     * 
     * <p>
     * Two-factor login tokens have very short expiration times and
     * can only be used to complete the 2FA verification process.
     * They provide limited access and cannot be used for general API calls.
     * </p>
     */
    TWO_FACTOR_LOGIN
}
