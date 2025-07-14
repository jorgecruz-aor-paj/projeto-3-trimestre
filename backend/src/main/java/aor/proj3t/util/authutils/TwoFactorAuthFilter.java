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
 * This filter validates temporary 2FA tokens rather than requiring full session
 * tokens.
 * 
 * @author Jorge Cruz
 */
@Provider
@TwoFactorAuth
public class TwoFactorAuthFilter implements ContainerRequestFilter {

    private static final Logger logger = LogManager.getLogger(TwoFactorAuthFilter.class);

    @Inject
    private TokenServiceImpl tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Use existing validation utility
        try {
            TokenUtil.validateAuthHeader(authHeader);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Authorization header required", "Missing or invalid authorization header");
        }

        String token = TokenUtil.extractToken(authHeader);

        // Validate that this is a temporary 2FA token
        if (!tokenService.validateTwoFactorToken(token)) {
            throw new UnauthorizedException("Invalid 2FA token", "Temporary 2FA token validation failed");
        }

        // Extract user information for context
        try {
            Long userId = tokenService.getUserIdFromTwoFactorToken(token);
            // Set a minimal security context for 2FA operations
            requestContext.setSecurityContext(new CustomSecurityContext(userId.toString(), "2FA_USER"));
            logger.debug("2FA token validated for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error processing 2FA token: {}", e.getMessage());
            throw new UnauthorizedException("Invalid 2FA token", "Failed to process 2FA token");
        }
    }
}
