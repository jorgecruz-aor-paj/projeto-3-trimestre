package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import aor.proj3t.config.AppConstants;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
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
    ConverterFactory converterFactory;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    PasswordUtil passwordUtil;

    @Override
    public String login(LoginDTO loginDto, String ipAddress, String userAgent) throws Exception {
        try {
            User user = getUserByEmail(loginDto.getEmail());
            Integer confirmationExpirationTime = getSettings().getConfirmationExpirationTime();
            if (verifyCredentials(user, loginDto)) {
                String token = jwtUtil.generateSessionToken(user.getId(), user.getEmail(), user.getRole().getName(), ipAddress,
                        confirmationExpirationTime);
                Instant tokenLimit = jwtUtil.extractExpirationDate(token);
                Session newSession = new Session(token,ipAddress,userAgent,user,tokenLimit);
                daoFactory.forSession().create(newSession);
                return token;
            }else{
                logger.warn("Invalid credentials on login for email {} ", loginDto.getEmail());
                return "";
            }
        } catch (NoSuchElementException e) {
            logger.error("User with email {} not found", loginDto.getEmail(), e);
            throw new NoSuchElementException("User with  email " + loginDto.getEmail() + " not found", e);
        } catch (Exception e) {
            logger.error("Error logging in with email {}.", loginDto.getEmail(), e);
            throw new Exception("Unknown Error");
        }
    }

    @Override
    public Boolean logout (Long userId, String token) {
        try{
            Session session = getSessionByToken(token);
            session.setActive(false);
            daoFactory.forSession().update(session);
            return true;
        } catch (Exception e) {
            logger.error("Error logging out for user with id {} ",userId, e);
            return false;
        }
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
        try {
            return daoFactory.forSession()
                    .findByToken(token)
                    .orElseThrow(() ->
                            new NoSuchElementException("No session found with token: " + token));
        } catch (NoSuchElementException e) {
            logger.warn("Session not found for token: {}", token);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while fetching session with token {}.", token, e);
            throw new RuntimeException("Failed to retrieve session for token: " + token, e);
        }
    }

    private User getUserByEmail(String email) {
        try {
            Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
            return userOpt.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting user by email {}. ",email, e);
            throw new NoSuchElementException("No user found with email " + email);
        }
    }

    private Settings getSettings() {
        Settings settings = new Settings(
                AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME,
                AppConstants.Jwt.DEFAULT_TOKEN_EXPIRATION_TIME,
                AppConstants.Jwt.DEFAULT_RESET_PASSWORD_TOKEN_EXPIRATION_TIME);
        try {
            Optional<Settings> settingsOpt = daoFactory.forSettings().getLatestSettings();
            return settingsOpt.orElse(settings);
        } catch (Exception e) {
            logger.error("Error getting confirmation expiration window. ", e);
            return settings;
        }
    }

    private Boolean verifyCredentials(User user, LoginDTO loginDto) {
        return passwordUtil.verifyPassword(loginDto.getPassword(), user.getPassword()) &&
                user.getEmail().equals(loginDto.getEmail());

    }



}
