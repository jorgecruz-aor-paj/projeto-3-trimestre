package aor.proj3t.service.impl;

import aor.proj3t.daos.impl.*;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.*;
import aor.proj3t.enums.Role;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserServiceImpl {
    private static final int DEFAULT_CONFIRMATION_WINDOW = 60;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Inject
    JwtUtil jwtUtil;

    @Inject
    SettingsDAOimpl settingsDAO;

    @Inject
    WorkPlaceDAOimpl workPlaceDAO;

    @Inject
    PendingRegistrationDAOimpl pendingRegistrationDAO;

    @Inject
    UserDAOimpl userDao;

    @Inject
    SessionDAOimpl sessionDAO;

    public Boolean registerUser(LoginDTO loginDTO, String ipAddress) {
        try {
            PendingRegistration registration = new PendingRegistration(loginDTO.getEmail(), loginDTO.getPassword());
            registration.setConfirmationToken(jwtUtil.generateConfirmationToken(loginDTO.getEmail(), ipAddress, getConfirmationExpirationTime()));
            registration.setExpirationDate(Instant.now().plus(getConfirmationExpirationTime(), ChronoUnit.MINUTES));
            pendingRegistrationDAO.create(registration);
            return true;
        } catch (Exception e) {
            logger.error("Error registering user with email {}. Error: {}", loginDTO.getEmail(), e.getMessage());
            return false;
        }
    }

    public Boolean confirmUser(String confirmationToken, UserDTO newUserDto) {
        try {
            PendingRegistration registration = getPendingRegistration(confirmationToken);
            if (registration == null) {
                return false;
            }
            Workplace workplace = getWorkplace(newUserDto.getWorkplaceId());
            if (workplace == null) {
                return false;
            }
            User newUser = new User(
                    registration.getEmail(),
                    registration.getPassword(),
                    newUserDto.getFirstName(),
                    newUserDto.getLastName(),
                    newUserDto.getPhoneNumber(),
                    newUserDto.getAddress(),
                    newUserDto.getBirthDate(),
                    newUserDto.getRole(),
                    workplace);
            userDao.create(newUser);
            return true;
        } catch (Exception e) {
            logger.error("Error confirming user with email {}. Error: {}", confirmationToken, e.getMessage());
            return false;
        }
    }

    public Boolean changePassword(String sessionToken, String newPassword) {
        try {
            Session session = getSession(sessionToken);
            if (session == null) {
                return false;
            }
            User user = session.getUser();
            user.setPassword(PasswordUtil.hashPassword(newPassword));
            userDao.update(user);
            return true;
        } catch (NoSuchElementException e) {
            logger.info("Invalid or expired session token {}", sessionToken);
            return false;
        } catch (Exception e) {
            logger.error("Error changing password with session token {}. Error: {}", sessionToken, e.getMessage());
            return false;
        }
    }

    public Boolean assignManager(Long userId, Long managerId) {
       try {
           User user = getUser(userId);
           User manager = getUser(managerId);
           user.setManager(manager);
           userDao.update(user);
           if (!manager.getRole().equals(Role.MANAGER)) {
               manager.setRole(Role.MANAGER);
               userDao.update(manager);
           }
           return true;
       }catch (NoSuchElementException e) {
               logger.error("Invalid user ID {} or manager ID {} while assigning manager. Error: {}", userId, managerId, e.getMessage());
               return false;

       }catch (Exception e) {
           logger.error("Error assigning manager. Error: {}", e.getMessage());
           return false;
       }
    }

    private Integer getConfirmationExpirationTime() {
        try {
            Optional<Settings> settingsOpt = settingsDAO.getLatestSettings();
            Settings settings;
            if (settingsOpt.isPresent()) {
                settings = settingsOpt.get();
                return settings.getConfirmationExpirationTime();
            } else {
                return DEFAULT_CONFIRMATION_WINDOW;
            }
        } catch (Exception e) {
            logger.error("Error getting confirmation expiration window. Error: {}", e.getMessage());
            return DEFAULT_CONFIRMATION_WINDOW;
        }
    }

    private PendingRegistration getPendingRegistration(String token) {
        Optional<PendingRegistration> pendingRegistrationOpt = pendingRegistrationDAO.findByToken(token);
        return pendingRegistrationOpt.orElse(null);
    }

    private Workplace getWorkplace(Long workplaceId) {
        return workPlaceDAO.findById(workplaceId)
                .orElseThrow(() -> new NoSuchElementException("Workplace not found with ID: " + workplaceId));
    }

    private Session getSession(String sessionToken) {
        try {
            if (!sessionDAO.isValidToken(sessionToken)) {
                logger.info("Invalid or expired session token: {} ", sessionToken);
                return null;
            } else {
                Optional<Session> sessionOpt = sessionDAO.findByToken(sessionToken);
                return sessionOpt.orElseThrow(() -> new NoSuchElementException("Session not found for token: " + sessionToken));
            }
        } catch (Exception e) {
            logger.error("Error getting session with token {}. Error: {}", sessionToken, e.getMessage());
            return null;
        }
    }

    private User getUser(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }

}
