package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@ApplicationScoped
public class TokenService {
    private static final Logger logger = LogManager.getLogger(TokenService.class);

    @Inject
    JwtUtil jwtUtil;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    DAOFactory daoFactory;

    public Long getUserIdFromToken(String token) {
        return extractUserId(getValidClaims(token));
    }

    public Long getUserIdFromAuthHeader(String authHeader) {
        String token = TokenUtil.extractToken(authHeader);  // Reuse your utility
        return extractUserId(getValidClaims(token));
    }

    public Role getRoleFromAuthHeader(String authHeader) {
        String token = TokenUtil.extractToken(authHeader);
        return extractRole(getValidClaims(token));
    }

    private Long extractUserId(Claims claims) {
        return Long.parseLong(claims.get("userId").toString());
    }

    private Role extractRole(Claims claims) {
        try {
            return Role.valueOf(claims.get("role", String.class).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired token", "Invalid role in token");
        }
    }

    public boolean isTokenActive(String token) {
        try {
            Session session = getSessionByToken(token);
            return session.getActive();
        } catch (NotFoundException e) {
            logger.warn("Token is not associated with any session: {}", token);
            return false;
        }
    }

    public Session getSessionByToken(String token) {
        return daoFactory.forSession()
                .findByToken(token)
                .orElseThrow(() ->
                        new NotFoundException(
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

