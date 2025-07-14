package aor.proj3t.service.impl;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.auth.LoginResponseDTO;
import aor.proj3t.entities.Session;
import aor.proj3t.entities.User;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the AuthService interface.
 * This class provides comprehensive authentication services including user
 * login and logout operations.
 * It handles password verification, JWT token generation, session management,
 * and security logging.
 * 
 * <p>
 * Key responsibilities include:
 * <ul>
 * <li>User credential validation during login</li>
 * <li>JWT token generation and management</li>
 * <li>Session creation and tracking</li>
 * <li>Secure logout and session invalidation</li>
 * </ul>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
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
    public LoginResponseDTO login(LoginDTO loginDto, String ipAddress, String userAgent) {
        User user = daoFactory.forUser()
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException(
                        "Invalid credentials",
                        "User with email " + loginDto.getEmail() + " not found on login"));

        if (!passwordUtil.verifyPassword(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(
                    "Invalid credentials",
                    "User with email " + loginDto.getEmail() + " provided wrong password on login");
        }

        // Check if user has 2FA enabled
        boolean isTwoFactorEnabled = serviceFactory.forTwoFactorAuth().isTwoFactorEnabled(user.getId());

        if (isTwoFactorEnabled) {
            // Generate temporary token for 2FA verification
            String temporaryToken = jwtUtil.generateTwoFactorLoginToken(user.getId(), user.getEmail(), ipAddress);
            logger.info("Login successful for user with email {}, 2FA verification required", loginDto.getEmail());
            return new LoginResponseDTO(true, temporaryToken);
        } else {
            // Complete login without 2FA
            String sessionToken = createSessionToken(user, ipAddress, userAgent);
            logger.info("Login successful for user with email {}, no 2FA required", loginDto.getEmail());
            return new LoginResponseDTO(sessionToken);
        }
    }

    @Override
    public String completeTwoFactorLogin(String temporaryToken, String twoFactorCode, String ipAddress,
            String userAgent) {
        // Validate temporary token using dedicated 2FA token validation
        if (!serviceFactory.forToken().validateTwoFactorToken(temporaryToken)) {
            throw new UnauthorizedException("Invalid or expired temporary token", "Temporary token validation failed");
        }

        // Extract user ID using dedicated 2FA token method
        Long userId = serviceFactory.forToken().getUserIdFromTwoFactorToken(temporaryToken);

        // Extract user email using existing JWT utility
        String userEmail = jwtUtil.extractUserEmail(temporaryToken);

        // Verify 2FA code
        boolean isValid = serviceFactory.forTwoFactorAuth().verifyTwoFactorCode(userId, twoFactorCode);
        if (!isValid) {
            throw new UnauthorizedException("Invalid 2FA code", "Two-factor authentication failed");
        }

        // Get user and create full session
        User user = daoFactory.forUser()
                .findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found", "User not found during 2FA completion"));

        String sessionToken = createSessionToken(user, ipAddress, userAgent);
        logger.info("2FA login completed successfully for user with email {}", userEmail);
        return sessionToken;
    }

    /**
     * Helper method to create session token and record
     */
    private String createSessionToken(User user, String ipAddress, String userAgent) {
        Integer sessionTimeoutMinutes = settingsProvider.getSettings().getSessionExpirationTime();
        String token = jwtUtil.generateSessionToken(user.getId(), user.getEmail(), user.getRole().getName(), ipAddress,
                sessionTimeoutMinutes);
        Instant tokenLimit = jwtUtil.extractExpirationDate(token);
        Session newSession = new Session(token, ipAddress, userAgent, user, tokenLimit);
        daoFactory.forSession().create(newSession);
        return token;
    }

    @Override
    public void logout(Long userId, String token) {
        Session session = serviceFactory.forToken().getSessionByToken(token);
        session.setActive(false);
        daoFactory.forSession().update(session);
    }

}
