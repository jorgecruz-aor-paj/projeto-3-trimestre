package aor.proj3t.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the TokenService interface.
 * This service handles JWT token validation, extraction of user information
 * from tokens,
 * session management, and password reset token operations.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Extracting user ID and role information from JWT tokens</li>
 * <li>Validating token authenticity and session status</li>
 * <li>Managing password reset tokens with expiration</li>
 * <li>Session verification and retrieval</li>
 * <li>Authorization header parsing and validation</li>
 * </ul>
 * 
 * <p>
 * This service integrates with JWT utilities and session management
 * to provide secure token-based authentication and authorization.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class TokenServiceImpl extends BaseService implements TokenService {
    private static final Logger logger = LogManager.getLogger(TokenServiceImpl.class);

    @Inject
    JwtUtil jwtUtil;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    DAOFactory daoFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getUserIdFromToken(String token) {
        return extractUserId(getValidClaims(token));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getUserIdFromAuthHeader(String authHeader) {
        String token = TokenUtil.extractToken(authHeader); // Reuse your utility
        return extractUserId(getValidClaims(token));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRoleFromAuthHeader(String authHeader) {
        String token = TokenUtil.extractToken(authHeader);
        return extractRole(getValidClaims(token));
    }

    private Long extractUserId(Claims claims) {
        return Long.valueOf(claims.get("userId").toString());
    }

    private Role extractRole(Claims claims) {
        try {
            return Role.valueOf(claims.get("role", String.class).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired token", "Invalid role in token");
        }
    }

    /**
     * Checks if a JWT token is associated with an active session.
     * This method validates the token against the session database to ensure
     * the session hasn't been terminated or invalidated.
     * 
     * @param token the JWT token to check for active session status
     * @return true if the token is associated with an active session, false
     *         otherwise
     */
    @Override
    public boolean isTokenActive(String token) {
        try {
            Session session = getSessionByToken(token);
            return session.getActive();
        } catch (NotFoundException e) {
            logger.warn("Token is not associated with any session: {}", token);
            return false;
        }
    }

    /**
     * Retrieves the session entity associated with a JWT token.
     * This method looks up the session in the database using the token value.
     * 
     * @param token the JWT token to look up the session for
     * @return the Session entity associated with the token
     * @throws NotFoundException if no session is found for the given token
     */
    @Override
    public Session getSessionByToken(String token) {
        return daoFactory.forSession()
                .findByToken(token)
                .orElseThrow(() -> new NotFoundException(
                        "Invalid or expired token",
                        "No session found for token: " + token));
    }

    private Claims getValidClaims(String token) {
        if (!jwtUtil.validateToken(token)) {
            logger.warn("Invalid token");
            throw new UnauthorizedException(
                    "Invalid or expired token",
                    "Invalid token");
        }
        if (jwtUtil.isTokenExpired(token)) {
            logger.warn("Expired session token {}", token);
            throw new UnauthorizedException(
                    "Invalid or expired token",
                    "Token has expired");
        }
        if (!isTokenActive(token)) {
            logger.warn("Inactive session token {}", token);
            throw new UnauthorizedException(
                    "Invalid or expired token",
                    "Inactive session token " + token);
        }
        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = extractUserId(claims);
        UserDTO user = serviceFactory.forUser().getUser(userId);
        if (user == null) {
            throw new UnauthorizedException(
                    "Invalid token",
                    "Invalid user id " + userId + " inside session token " + token);
        }
        return claims;
    }

}
