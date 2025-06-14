package aor.proj3t.util.authutils;

import aor.proj3t.config.AppConstants;
import aor.proj3t.exceptions.UnauthorizedException;

/**
 * Utility class for token-related operations
 */
public final class TokenUtil {

    private TokenUtil() {
        // Prevent instantiation
    }

    /**
     * Extracts token from Authorization header.
     *
     * @param authHeader Authorization header
     * @return The extracted token
     * @throws UnauthorizedException if the header is missing or invalid
     */
    public static String extractToken(String authHeader) {
        validateAuthHeader(authHeader);
        return authHeader.substring(AppConstants.Jwt.AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     * Validates that a token is present in the Authorization header.
     * Throws UnauthorizedException if not valid.
     *
     * @param authHeader Authorization header
     * @throws UnauthorizedException if header is null or does not match scheme
     */
    public static void validateAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(AppConstants.Jwt.AUTHENTICATION_SCHEME + " ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }
    }

    /**
     * Checks if a token is structurally present in the Authorization header.
     * Meant for conditional validation.
     *
     * @param authHeader Authorization header
     * @return true if token format is valid, false otherwise
     */
    public static boolean hasToken(String authHeader) {
        return authHeader != null && authHeader.startsWith(AppConstants.Jwt.AUTHENTICATION_SCHEME + " ");
    }

    /**
     * Gets the authentication scheme used.
     *
     * @return The authentication scheme (e.g., "Bearer")
     */
    public static String getAuthenticationScheme() {
        return AppConstants.Jwt.AUTHENTICATION_SCHEME;
    }
}




