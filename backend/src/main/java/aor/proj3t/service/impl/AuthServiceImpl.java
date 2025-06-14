package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import aor.proj3t.config.AppConstants;
import aor.proj3t.config.SettingsProvider;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.resource.ErrorResponse;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the AuthService interface.
 * This class is responsible for handling authentication-related operations.
 */
@ApplicationScoped
public class AuthServiceImpl extends BaseService implements AuthService {
    private static final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    SettingsProvider settingsProvider;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    PasswordUtil passwordUtil;

    @Override
    public String login(LoginDTO loginDto, String ipAddress, String userAgent) {
            User user = daoFactory.forUser()
                    .findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new UnauthorizedException(
                            "Invalid credentials",
                            "User with email " + loginDto.getEmail() + " not found on login"
                    ));
            if (!passwordUtil.verifyPassword(loginDto.getPassword(), user.getPassword())) {
                throw new UnauthorizedException(
                        "Invalid credentials",
                        "User with email " + loginDto.getEmail() + " provided wrong password on login"
                );
            }
            Integer sessionTimeoutMinutes = settingsProvider.getSettings().getSessionExpirationTime();
            String token = jwtUtil.generateSessionToken(user.getId(), user.getEmail(), user.getRole().getName(), ipAddress, sessionTimeoutMinutes);
            Instant tokenLimit = jwtUtil.extractExpirationDate(token);
            Session newSession = new Session(token,ipAddress,userAgent,user,tokenLimit);
            daoFactory.forSession().create(newSession);
            logger.info("Login successful for user with email {}", loginDto.getEmail());
            return token;
    }

    @Override
    public void logout(Long userId, String token) {
        Session session = getSessionByToken(token); // Throw a NotFoundException or UnauthorizedException here if not found
        session.setActive(false);
        daoFactory.forSession().update(session);
    }



    @Override
    public int logoutAll(Long userId) {
        logger.info("Logout all sessions for user ID: {}", userId);
        // Implement logout all logic
        return 0;
    }

    @Override
    public List<Session> getActiveSessions(Long userId) {
        // Implement get active sessions logic
        return List.of();
    }

    @Override
    public int cleanupExpiredSessions() {
        logger.info("Cleaning up expired sessions");
        // Implement cleanup logic
        return 0;
    }

    @Override
    public Boolean isValidToken(String token) {
        boolean valid = jwtUtil.validateToken(token);
        if (!valid) {
            logger.warn("Invalid token");
            return false;
        }
        boolean expired = jwtUtil.isTokenExpired(token);
        if(expired) {
            logger.warn("Expired session token {}", token);
            return false;
        }
        boolean active = isTokenActive(token);
        if(!active) {
            logger.warn("Inactive session token {}", token);
            return false;
        }
        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = Long.parseLong(claims.get("userId").toString());
        UserDTO user = serviceFactory.forUser().getUser(userId);
        boolean validUserId = user != null;
        if(!validUserId) {
            logger.warn("Invalid user id {} inside session token {}", userId,token);
            return false;
        }
        return true;
    }


    public Boolean isTokenActive(String token) {
        Session session = getSessionByToken(token);
        return session.getActive();
    }

    private Session getSessionByToken(String token) {
            return daoFactory.forSession()
                    .findByToken(token)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Invalid or expired token",
                                    "No session found for token: " + token));
    }

    private Claims extractClaims(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed token");
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        if (!isValidToken(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        return jwtUtil.extractAllClaims(token);
    }


    @Override
    public Long getUserIdFromToken(String token) {
        if (!serviceFactory.forAuth().isValidToken(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        Claims claims = jwtUtil.extractAllClaims(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    @Override
    public Long getUserIdFromAuthHeader(String authHeader) {
        Claims claims = extractClaims(authHeader);
        return Long.parseLong(claims.get("userId").toString());
    }

    @Override
    public Role getRoleFromAuthHeader(String authHeader) {
        Claims claims = extractClaims(authHeader);
        try {
            return Role.valueOf(claims.get("role", String.class).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired token","Invalid role in token");
        }
    }





}
