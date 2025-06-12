package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.FileStorageUtil;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
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

    @Inject
    private FileStorageUtil fileStorageUtil;

    @Override
    public UserDTO getUser(Long userId) {
        try {
            return converterFactory.forUser().toDTO(getEntity(daoFactory.forUser().findById(userId), userId));
        } catch (NoSuchElementException e) {
            logger.error("User with id {} not found", userId, e);
            return null;
        } catch (Exception e) {
            logger.error("Error getting user with id {}", userId);
            return null;
        }
    }

    @Override
    public Boolean registerUser(LoginDTO loginDTO, String ipAddress) {
        Instant now = Instant.now();
        String email = loginDTO.getEmail();
        String password = passwordUtil.hashPassword(loginDTO.getPassword());
        Integer confirmationExpirationTime = getSettings().getConfirmationExpirationTime();
        try {
            if (!isEmailValid(email)) {
                logger.error("Error registering user with email {} due to pre-existing email.", email);
                return false;
            }

            PendingRegistration registration = new PendingRegistration(email, password);
            registration
                    .setConfirmationToken(
                            jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime));
            registration
                    .setExpirationDate(now.plus(confirmationExpirationTime, ChronoUnit.MINUTES));
            daoFactory.forPendingRegistration().create(registration);
            return sendConfirmationEmail(email, ipAddress, confirmationExpirationTime);
        } catch (Exception e) {
            logger.error("Error registering user with email {}.  {}", email, e);
            return false;
        }
    }

    @Override
    public Boolean confirmUser(String email, UserRegistrationDTO userRegistrationDto) {
        try {

            PendingRegistration registration = getPendingRegistration(email);
            if (registration == null) {
                logger.warn("Failed to confirm user: No pending registration found for email {}", email);
                return false;
            }
            Workplace workplace = getEntity(daoFactory.forWorkplace().findById(userRegistrationDto.getWorkplaceId()),
                    userRegistrationDto.getWorkplaceId());
            if (workplace == null) {
                logger.warn("Failed to confirm user: Workplace ID {} does not exist",
                        userRegistrationDto.getWorkplaceId());
                return false;
            }

            User newUser = createNewUser(userRegistrationDto, registration, workplace, fileStorageUtil);
            daoFactory.forUser().create(newUser);
            daoFactory.forPendingRegistration().deleteById(registration.getId());
            logger.info("User with email {} successfully registered and confirmed.", registration.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error confirming registration with email {}.", email, e);
            return false;
        }
    }

    @Override
    public Boolean changePassword(Long userId, String newRawPassword) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId);
            user.setPassword(passwordUtil.hashPassword(newRawPassword));
            daoFactory.forUser().update(user);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} while updating user. ", userId, e);
            return false;
        } catch (Exception e) {
            logger.error("Error changing password for user id {}. ", userId, e);
            return false;
        }
    }

    @Override
    public Boolean assignManager(Long userId, Long managerId) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId);
            User manager = getEntity(daoFactory.forUser().findById(managerId), managerId);
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
            User manager = getEntity(daoFactory.forUser().findById(managerId), managerId);
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
    public Boolean updateUser(UserDTO userDto) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userDto.getId()), userDto.getId());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setAddress(userDto.getAddress());
            user.setRole(userDto.getRole());
            user.setBiography(userDto.getBiography());
            user.setBirthDate(userDto.getBirthDate());
            user.setPhotograph(userDto.getPhotograph());
            Workplace workplace;
            try {
                workplace = getEntity(daoFactory.forWorkplace().findById(userDto.getWorkplaceId()),
                        userDto.getWorkplaceId());
            } catch (NoSuchElementException e) {
                logger.error("Workplace with ID {} not found while updating user.", userDto.getWorkplaceId());
                return false;
            }
            user.setWorkplace(workplace);

            daoFactory.forUser().update(user);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} while updating user. ", userDto.getId(), e);
            return false;

        } catch (Exception e) {
            logger.error("Error updating user.", e);
            return false;
        }
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
    public Boolean sendResetPasswordLink(String email, String ipAddress) {
        try {
            User user = getUserByEmail(email);
            if (user == null) {
                logger.warn("User with email {} not found for password reset", email);
                return false;
            }
            Settings settings = getSettings();
            return sendResetPasswordEmail(email, user.getId(), ipAddress, settings.getResetPasswordExpirationTime());
        } catch (Exception e) {
            logger.error("Error resetting password. ", e);
            return false;
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
                User user = getEntity(daoFactory.forUser().findById(userId), userId);
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

    private Boolean isEmailValid(String email) throws Exception {
        try {
            daoFactory.forPendingRegistration().deleteExpired();
            Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
            if (userOpt.isPresent()) {
                return false;
            }
            Optional<PendingRegistration> pendingRegistrationOpt = daoFactory.forPendingRegistration()
                    .findByEmail(email);
            if (pendingRegistrationOpt.isPresent()) {
                return false;
            }
            return true;
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e) {
            logger.error("Error finding email for pre-registering validation. ", e);
            throw new Exception("Unexpected error validating email");
        }
    }

    private PendingRegistration getPendingRegistration(String email) {
        try {
            Optional<PendingRegistration> pendingRegistrationOpt = daoFactory.forPendingRegistration()
                    .findByEmail(email);
            return pendingRegistrationOpt.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting pending registration. ", e);
            return null;
        }
    }

    private static User createNewUser(UserRegistrationDTO userRegistrationDto, PendingRegistration registration,
            Workplace workplace, FileStorageUtil fileStorageUtil) {
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

        newUser.setPhotograph(fileStorageUtil.getDefaultProfilePicture());

        return newUser;
    }

    private Boolean sendConfirmationEmail(String email, String ipAddress, Integer confirmationExpirationTime) {
        try {
            String confirmationToken = jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime);
            String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
            emailUtil.sendConfirmationEmail(email, confirmationToken, baseUrl);
            return true;
        } catch (Exception e) {
            logger.error("Error sending confirmation email to {}", email, e);
            return false;
        }
    }

    private Boolean sendResetPasswordEmail(String email, Long userId, String ipAddress,
            Integer resetPasswordExpirationTime) {
        try {
            String resetPasswordToken = jwtUtil.generatePasswordResetToken(email, userId, ipAddress,
                    resetPasswordExpirationTime);
            String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
            return emailUtil.sendPasswordResetEmail(email, resetPasswordToken, baseUrl);
        } catch (Exception e) {
            logger.error("Error sending confirmation email to {}", email, e);
            return false;
        }
    }

    @Override
    public Boolean uploadProfilePicture(Long userId, FileUploadDTO fileUploadDTO) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId);
            if (user == null) {
                logger.warn("User not found with ID: {} for profile picture upload", userId);
                return false;
            }

            // Delete old profile picture ONLY if it's not the default one
            String oldPicture = user.getPhotograph();
            if (oldPicture != null && !oldPicture.equals(fileStorageUtil.getDefaultProfilePicture())) {
                fileStorageUtil.deleteFile(oldPicture);
            }

            // Store the new picture
            String fileName = fileStorageUtil.storeFile(
                    fileUploadDTO.getFileContent(),
                    fileUploadDTO.getFileName(),
                    fileUploadDTO.getFileExtension());

            // Update user entity
            user.setPhotograph(fileName);
            daoFactory.forUser().update(user);

            logger.info("Profile picture updated for user ID: {}", userId);
            return true;
        } catch (Exception e) {
            logger.error("Error uploading profile picture for user ID: {}", userId, e);
            return false;
        }
    }

    @Override
    public Boolean resetProfilePicture(Long userId) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId);
            if (user == null) {
                logger.warn("User not found with ID: {} for profile picture reset", userId);
                return false;
            }

            // Delete old profile picture if it's not the default one
            String oldPicture = user.getPhotograph();
            if (oldPicture != null && !oldPicture.equals(fileStorageUtil.getDefaultProfilePicture())) {
                fileStorageUtil.deleteFile(oldPicture);
            }

            // Set default picture
            user.setPhotograph(fileStorageUtil.getDefaultProfilePicture());
            daoFactory.forUser().update(user);

            logger.info("Profile picture reset to default for user ID: {}", userId);
            return true;
        } catch (Exception e) {
            logger.error("Error resetting profile picture for user ID: {}", userId, e);
            return false;
        }
    }
}
