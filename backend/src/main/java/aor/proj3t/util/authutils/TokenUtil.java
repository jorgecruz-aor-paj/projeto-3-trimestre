package aor.proj3t.util.authutils;

import aor.proj3t.config.AppConstants;
import aor.proj3t.exceptions.UnauthorizedException;

/**
 * Utility class for token-related operations.
 * <p>
 * This class provides helper methods for handling JWT-based authentication tokens,
 * specifically within Authorization headers. It allows extraction, validation, and
 * format checking of tokens, and exposes the configured authentication scheme.
 * It is marked as final and includes a private constructor to prevent instantiation.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public final class TokenUtil {

    private TokenUtil() {
        // Prevent instantiation
    }

    /**
     * Extracts the token value from the Authorization header.
     * Assumes the header starts with the authentication scheme (e.g. "Bearer").
     *
     * @param authHeader the Authorization header from the request
     * @return the trimmed token string
     * @throws UnauthorizedException if the header is missing or invalid
     */
    public static String extractToken(String authHeader) {
        validateAuthHeader(authHeader);
        return authHeader.substring(AppConstants.Jwt.AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     * Validates that the Authorization header contains a correctly formatted token.
     * Throws an exception if the scheme is missing or malformed.
     *
     * @param authHeader the Authorization header from the request
     * @throws UnauthorizedException if header is null or invalid
     */
    public static void validateAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(AppConstants.Jwt.AUTHENTICATION_SCHEME + " ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }
    }

    /**
     * Performs a lightweight check to determine if a token is present in the Authorization header.
     * This method does not throw an exception and is suitable for pre-validation.
     *
     * @param authHeader the Authorization header from the request
     * @return true if a valid token prefix is detected, false otherwise
     */
    public static boolean hasToken(String authHeader) {
        return authHeader != null && authHeader.startsWith(AppConstants.Jwt.AUTHENTICATION_SCHEME + " ");
    }

    /**
     * Retrieves the authentication scheme configured for token parsing (e.g. "Bearer").
     *
     * @return the authentication scheme string
     */
    public static String getAuthenticationScheme() {
        return AppConstants.Jwt.AUTHENTICATION_SCHEME;
    }
}




