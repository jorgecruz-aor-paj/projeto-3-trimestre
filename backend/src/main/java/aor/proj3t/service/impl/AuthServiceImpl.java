package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import io.jsonwebtoken.Claims;
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
        Session newSession = new Session(token, ipAddress, userAgent, user, tokenLimit);
        daoFactory.forSession().create(newSession);
        logger.info("Login successful for user with email {}", loginDto.getEmail());
        return token;
    }

    @Override
    public void logout(Long userId, String token) {
        Session session = serviceFactory.forToken().getSessionByToken(token);
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






}
