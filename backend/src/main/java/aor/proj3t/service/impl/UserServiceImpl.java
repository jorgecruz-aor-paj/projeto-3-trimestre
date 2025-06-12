package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.entities.*;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.util.EmailUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import io.jsonwebtoken.JwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl extends BaseService implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Inject
    JwtUtil jwtUtil;

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Inject
    PasswordUtil passwordUtil;

    @Inject
    private EmailUtil emailUtil;

    @Override
    public UserDTO getUser(Long userId) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        return converterFactory.forUser().toDTO(user);
    }

    @Override
    public void registerUser(LoginDTO loginDTO, String ipAddress) {
        Instant now = Instant.now();
        String email = loginDTO.getEmail();
        String password = passwordUtil.hashPassword(loginDTO.getPassword());
        Integer confirmationExpirationTime = getSettings().getConfirmationExpirationTime();
        validateEmail(email);
        PendingRegistration registration = new PendingRegistration(email, password);
        registration.setConfirmationToken(
                jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime));
        registration.setExpirationDate(now.plus(confirmationExpirationTime, ChronoUnit.MINUTES));
        daoFactory.forPendingRegistration().create(registration);
        sendConfirmationEmail(email, ipAddress, confirmationExpirationTime);
    }

    private void validateEmail(String email) {
        daoFactory.forPendingRegistration().deleteExpired();
        Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
        if (userOpt.isPresent()) {
            throw new ConflictException("User with email " + email + " already exists");
        }
        Optional<PendingRegistration> pendingOpt = daoFactory.forPendingRegistration().findByEmail(email);
        if (pendingOpt.isPresent()) {
            throw new ConflictException("User with email " + email + " already exists");
        }
    }
    private void sendConfirmationEmail(String email, String ipAddress, Integer confirmationExpirationTime) {
        try {
            String token = jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime);
            String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
            emailUtil.sendConfirmationEmail(email, token, baseUrl);
        } catch (Exception e) {
            logger.error("Error sending confirmation email to {}", email, e);
            throw new RuntimeException("Failed to send confirmation email to " + email);
        }
    }


    @Override
    public void confirmUser(String email, UserRegistrationDTO userRegistrationDto) {
        PendingRegistration registration = getPendingRegistration(email);
        if (registration == null) {
            logger.warn("No pending registration found for email {}", email);
            throw new NotFoundException("No pending registration found for this email");
        }

        Workplace workplace = getEntity(
                daoFactory.forWorkplace().findById(userRegistrationDto.getWorkplaceId()),
                userRegistrationDto.getWorkplaceId(), "Workplace"
        );
        if (workplace == null) {
            logger.warn("Workplace ID {} does not exist", userRegistrationDto.getWorkplaceId());
            throw new NotFoundException("Selected workplace does not exist");
        }

        User newUser = createNewUser(userRegistrationDto, registration, workplace);
        daoFactory.forUser().create(newUser);
        daoFactory.forPendingRegistration().deleteById(registration.getId());
        logger.info("User with email {} successfully registered and confirmed.", registration.getEmail());
    }



    @Override
    public void changePassword(Long userId, String newRawPassword) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        user.setPassword(passwordUtil.hashPassword(newRawPassword));
        daoFactory.forUser().update(user);
    }


    @Override
    public Boolean assignManager(Long userId, Long managerId) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            User manager = getEntity(daoFactory.forUser().findById(managerId), managerId, "User");
            user.setManager(manager);
            daoFactory.forUser().update(user);
            if (!manager.getRole().equals(Role.MANAGER)) {
                manager.setRole(Role.MANAGER);
                daoFactory.forUser().update(manager);
            }
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} or manager ID {} while assigning manager. ", userId, managerId, e);
            return false;

        } catch (Exception e) {
            logger.error("Error assigning manager. ", e);
            return false;
        }
    }

    @Override
    public Boolean assignManagerBulk(Long managerId, Long[] userIds) {
        if (userIds.length == 0) {
            logger.warn("No user IDs provided for assigning manager");
            return true;
        }
        try {
            User manager = getEntity(daoFactory.forUser().findById(managerId), managerId, "User");
            List<User> users = getUserListFromIds(userIds);
            if (users == null || users.isEmpty()) {
                logger.error("No valid users Id's assigning manager in bulk. ");
                return false;
            }
            for (User user : users) {
                user.setManager(manager);
                daoFactory.forUser().update(user);
            }
            if (!manager.getRole().equals(Role.MANAGER)) {
                manager.setRole(Role.MANAGER);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error assigning manager with id {} in bulk. ", e);
            return false;
        }
    }

    @Override
    public void updateUser(Long requesterId, UserDTO userDto) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        User targetUser = getEntity(daoFactory.forUser().findById(userDto.getId()), userDto.getId(), "User");
        if (!requester.getRole().equals(Role.ADMIN) && !Objects.equals(requesterId, userDto.getId())) {
            throw new ForbiddenException("Permission denied: cannot update user with ID " + userDto.getId());
        }
        updateUserFields(targetUser, userDto);
        daoFactory.forUser().update(targetUser);
    }


    @Override
    public List<UserDTO> getUsersByFilters(UserFilter filter) {
        try {
            return converterFactory.forUser().toDTOList(daoFactory.forUser().getUsersByFilters(filter));
        } catch (Exception e) {
            logger.error("Error getting users list. ", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void sendResetPasswordLink(String email, String ipAddress) {
        Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.warn("User with email {} not found for password reset", email);
            return;
        }
        User user = userOpt.get();
        Settings settings = getSettings();
        sendResetPasswordEmail(
                user.getEmail(),
                user.getId(),
                ipAddress,
                settings.getResetPasswordExpirationTime()
        );
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

    private User getUserByEmail(String email) {
        try {
            Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
            return userOpt.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting user by email {}. ", email, e);
            throw new NoSuchElementException("No user found with email " + email);
        }
    }

    private List<User> getUserListFromIds(Long[] userIds) {
        try {
            List<User> users = new ArrayList<>(userIds.length);
            for (Long userId : userIds) {
                User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
                if (user != null) {
                    users.add(user);
                } else {
                    logger.error("User not found with ID: " + userId);
                }
            }
            return users;
        } catch (NoSuchElementException e) {
            logger.error("User not found with ID: " + userIds[0]);
            return null;
        } catch (Exception e) {
            logger.error("Error getting user list. ", e);
            return null;
        }
    }


    private PendingRegistration getPendingRegistration(String email) {
        try {
            Optional<PendingRegistration> pendingRegistrationOpt = daoFactory.forPendingRegistration().findByEmail(email);
            return pendingRegistrationOpt.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting pending registration. ", e);
            return null;
        }
    }

    private static User createNewUser(UserRegistrationDTO userRegistrationDto, PendingRegistration registration, Workplace workplace) {
        User newUser = new User(
                registration.getEmail(),
                registration.getPassword(),
                userRegistrationDto.getFirstName(),
                userRegistrationDto.getLastName(),
                userRegistrationDto.getPhoneNumber(),
                userRegistrationDto.getAddress(),
                userRegistrationDto.getBirthDate(),
                Role.USER,
                workplace);

        if (userRegistrationDto.getBiography() != null) {
            newUser.setBiography(userRegistrationDto.getBiography());
        }
        if (userRegistrationDto.getPhotograph() != null) {
            newUser.setPhotograph(userRegistrationDto.getPhotograph());
        } else {
            newUser.setPhotograph("photo");
        }
        //todo remove photograph from hardcoded string to path
        return newUser;
    }

    private void sendResetPasswordEmail(String email, Long userId, String ipAddress, Integer resetPasswordExpirationTime) {
        String resetPasswordToken = jwtUtil.generatePasswordResetToken(email, userId, ipAddress, resetPasswordExpirationTime);
        String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
        emailUtil.sendPasswordResetEmail(email, resetPasswordToken, baseUrl);
    }

    private User updateUserFields(User user, UserDTO userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());
        user.setBiography(userDto.getBiography());
        user.setBirthDate(userDto.getBirthDate());
        user.setPhotograph(userDto.getPhotograph());

        Workplace workplace = getEntity(
                daoFactory.forWorkplace().findById(userDto.getWorkplaceId()),
                userDto.getWorkplaceId(), "Workplace");
        user.setWorkplace(workplace);
        return user;
    }

}

