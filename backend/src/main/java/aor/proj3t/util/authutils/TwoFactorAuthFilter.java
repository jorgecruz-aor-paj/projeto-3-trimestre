package aor.proj3t.util.authutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.impl.TokenServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

/**
 * Authentication filter for Two-Factor Authentication endpoints.
 * <p>
 * This filter intercepts requests targeting endpoints marked with {@link TwoFactorAuth}
 * and validates temporary 2FA tokens from the authorization header.
 * Unlike full session tokens, 2FA tokens are short-lived and used during the second step
 * of the login process. The filter parses and verifies the token, and injects a simplified
 * security context for downstream operations.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Provider
@TwoFactorAuth
public class TwoFactorAuthFilter implements ContainerRequestFilter {

    private static final Logger logger = LogManager.getLogger(TwoFactorAuthFilter.class);

    @Inject
    private TokenServiceImpl tokenService;

    /**
     * Performs authentication filtering for endpoints requiring 2FA validation.
     * Extracts and verifies the temporary token from the Authorization header.
     * Sets a minimal security context representing the authenticated user.
     *
     * @param requestContext the container request context for the incoming request
     * @throws UnauthorizedException if the token is missing, invalid, or expired
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            TokenUtil.validateAuthHeader(authHeader);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Authorization header required", "Missing or invalid authorization header");
        }

        String token = TokenUtil.extractToken(authHeader);

        if (!tokenService.validateTwoFactorToken(token)) {
            throw new UnauthorizedException("Invalid 2FA token", "Temporary 2FA token validation failed");
        }

        try {
            Long userId = tokenService.getUserIdFromTwoFactorToken(token);
            requestContext.setSecurityContext(new CustomSecurityContext(userId.toString(), "2FA_USER"));
            logger.debug("2FA token validated for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error processing 2FA token: {}", e.getMessage());
            throw new UnauthorizedException("Invalid 2FA token", "Failed to process 2FA token");
        }
    }
}

