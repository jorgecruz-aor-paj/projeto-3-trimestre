package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.FileStorageUtil;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.MiniUserConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementation of the UserService interface.
 * This comprehensive service handles all user-related operations including
 * registration, authentication,
 * profile management, role assignment, and user data retrieval with filtering
 * and pagination.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>User registration with email confirmation</li>
 * <li>User profile management and updates</li>
 * <li>Password management and reset functionality</li>
 * <li>Manager assignment and role management</li>
 * <li>User search and filtering capabilities</li>
 * <li>Profile picture upload and management</li>
 * <li>User statistics and reporting</li>
 * </ul>
 * 
 * <p>
 * This service integrates with multiple external services including email
 * utilities,
 * file storage, JWT token management, and password hashing utilities.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
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

    @Inject
    SettingsProvider settingsProvider;

    @Inject
    MiniUserConverter miniUserConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUser(Long userId) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        return converterFactory.forUser().toDTO(user);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: Creates a pending registration with expiration token
     * and sends confirmation email asynchronously.
     */
    @Override
    public void registerUser(LoginDTO loginDTO, String ipAddress) {
        Instant now = Instant.now();
        String email = loginDTO.getEmail();
        String password = passwordUtil.hashPassword(loginDTO.getPassword());
        Integer confirmationExpirationTime = settingsProvider.getSettings().getConfirmationExpirationTime();
        validateEmail(email);
        PendingRegistration registration = new PendingRegistration(email, password);
        registration
                .setConfirmationToken(jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime));
        registration.setExpirationDate(now.plus(confirmationExpirationTime, ChronoUnit.MINUTES));
        daoFactory.forPendingRegistration().create(registration);
        sendConfirmationEmail(email, ipAddress, confirmationExpirationTime);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: Validates pending registration, creates user account,
     * and cleans up the pending registration record from database.
     */
    @Override
    public void confirmUser(String email, UserRegistrationDTO userRegistrationDto) {
        PendingRegistration registration = getPendingRegistration(email);
        Workplace workplace = getEntity(daoFactory.forWorkplace().findById(userRegistrationDto.getWorkplaceId()),
                userRegistrationDto.getWorkplaceId(), "Workplace");

        User newUser = createNewUser(userRegistrationDto, registration, workplace, fileStorageUtil);
        daoFactory.forUser().create(newUser);
        daoFactory.forPendingRegistration().deleteById(registration.getId());
        logger.info("User with email {} successfully registered and confirmed.", registration.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword(Long userId, String newRawPassword) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        user.setPassword(passwordUtil.hashPassword(newRawPassword));
        daoFactory.forUser().update(user);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Implementation note: Validates admin permissions, promotes manager role if
     * needed,
     * and handles evaluation reassignments for affected users during bulk
     * assignment.
     */
    @Override
    @Transactional
    public void assignManagerBulk(Long userId, ManagerAssignDTO managerAssignDto) {
        User requester = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        User manager = getEntity(daoFactory.forUser().findById(managerAssignDto.getManagerId()),
                managerAssignDto.getManagerId(), "User");
        List<User> users = getUserListFromIds(managerAssignDto.getUserIds());
        if (!requester.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "Manager assignment request by nonAdmin user with id " + requester.getId());
        }
        if (manager.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User management assigned to admin with id " + manager.getId() + " by Admin with id "
                            + requester.getId());
        }
        if (users.contains(manager)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + manager.getId() + " cannot manage himself");
        }
        for (User user : users) {
            user.setManager(manager);
            daoFactory.forUser().update(user);
            reassignEvaluatorForUser(user, manager);
        }
        if (manager.getRole().equals(Role.USER)) {
            manager.setRole(Role.MANAGER);
            daoFactory.forUser().update(manager);
        }
        demoteManagersWithoutUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(Long requesterId, UserDTO userDto) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        User targetUser = getEntity(daoFactory.forUser().findById(userDto.getId()), userDto.getId(), "User");
        if (!requester.getRole().equals(Role.ADMIN) && !Objects.equals(requesterId, userDto.getId())) {
            throw new ForbiddenException(
                    "Permission denied",
                    "Non admin user with ID " + requesterId + " cannot update user with ID " + userDto.getId());
        }
        updateUserFields(targetUser, userDto);
        daoFactory.forUser().update(targetUser);
        if (targetUser.getManager() != null) {
            sendProfileUpdateEmail(targetUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> getUsersByFilters(Long requesterId, UserFilter filters) {
        filters.hydrate(daoFactory);
        System.out.println(filters);
        return converterFactory.forUser().toDTOList(daoFactory.forUser().getUsersByFilters(filters));
    }

    /**
     * Sends a password reset link to the specified email address.
     * This method validates the email exists and sends a secure reset link.
     * 
     * @param email     the email address to send the reset link to
     * @param ipAddress the IP address of the client requesting the reset
     * @throws NotFoundException if no user exists with the given email
     */
    @Override
    public void sendResetPasswordLink(String email, String ipAddress) {
        Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.warn("User with email {} not found for password reset", email);
            return;
        }
        User user = userOpt.get();
        Integer resetPasswordExpirationTime = settingsProvider.getSettings().getResetPasswordExpirationTime();
        sendResetPasswordEmail(user.getEmail(), user.getId(), ipAddress, resetPasswordExpirationTime);
    }

    /**
     * Retrieves all users with manager role.
     * This method is restricted to admin users only.
     * 
     * @param requesterId the ID of the admin user making the request
     * @return a list of UserDTO objects representing all managers
     * @throws NotFoundException  if the requester doesn't exist
     * @throws ForbiddenException if the requester is not an admin
     */
    @Override
    public List<UserDTO> getAllManagers(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        return converterFactory.forUser().toDTOList(daoFactory.forUser().getManagers());
    }

    private List<User> getUserListFromIds(List<Long> userIds) {
        List<User> users = new ArrayList<>(userIds.size());
        for (Long userId : userIds) {
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            users.add(user);
        }
        return users;
    }

    private PendingRegistration getPendingRegistration(String email) {
        return daoFactory.forPendingRegistration()
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "No pending registration found for email " + email));
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
                userRegistrationDto.getBirthdate(),
                Role.USER,
                workplace);

        if (userRegistrationDto.getBiography() != null) {
            newUser.setBiography(userRegistrationDto.getBiography());
        }
        newUser.setPhotograph(fileStorageUtil.getDefaultProfilePicture());
        return newUser;
    }

    private void sendResetPasswordEmail(String email, Long userId, String ipAddress,
            Integer resetPasswordExpirationTime) {
        String resetPasswordToken = jwtUtil.generatePasswordResetToken(email, userId, ipAddress,
                resetPasswordExpirationTime);
        String baseUrl = AppConstants.Email.EMAIL_BASE_URL;
        emailUtil.sendPasswordResetEmail(email, resetPasswordToken, baseUrl);
    }

    private void updateUserFields(User user, UserDTO userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());
        user.setBiography(userDto.getBiography());
        user.setBirthdate(userDto.getBirthdate());
        // NOTE: Photograph field is intentionally excluded here
        // Profile pictures should only be updated through the dedicated upload endpoint
        // to prevent accidental overwrites during regular user updates
        // If photograph field is provided and not null, we ignore it for safety

        Workplace workplace = getEntity(daoFactory.forWorkplace().findById(userDto.getWorkplace().getId()),
                userDto.getWorkplace().getId(), "Workplace");
        user.setWorkplace(workplace);
    }

    private void demoteManagersWithoutUsers() {
        List<User> emptyManagers = daoFactory.forUser().findManagersWithoutUsers();
        for (User manager : emptyManagers) {
            manager.setRole(Role.USER);
            daoFactory.forUser().update(manager);
        }
    }

    private void validateEmail(String email) {
        daoFactory.forPendingRegistration().deleteExpired();
        Optional<User> userOpt = daoFactory.forUser().findByEmail(email);
        if (userOpt.isPresent()) {
            throw new ConflictException(
                    "Email already exists",
                    "User with email " + email + " already exists");
        }
        Optional<PendingRegistration> pendingOpt = daoFactory.forPendingRegistration().findByEmail(email);
        if (pendingOpt.isPresent()) {
            throw new ConflictException(
                    "Email already exists",
                    "User with email " + email + " already exists");
        }
    }

    private void sendConfirmationEmail(String email, String ipAddress, Integer confirmationExpirationTime) {
        String token = jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime);
        String baseUrl = AppConstants.Email.EMAIL_BASE_URL;
        emailUtil.sendConfirmationEmail(email, token, baseUrl);
    }

    /**
     * Retrieves statistics comparing total users versus unmanaged users.
     * This method is restricted to admin users and provides insights into user
     * management coverage.
     * 
     * @param requesterId the ID of the admin user making the request
     * @return a map containing "total" and "unmanaged" user counts
     * @throws NotFoundException  if the requester doesn't exist
     * @throws ForbiddenException if the requester is not an admin
     */
    @Override
    public Map<String, Long> getTotalVSUnmanagedUsers(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        Long totalUsers = daoFactory.forUser().countAllUsers();
        Long unmanagedUsers = daoFactory.forUser().countUsersWithoutManager();
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", totalUsers);
        stats.put("unmanaged", unmanagedUsers);
        return stats;
    }

    /**
     * Uploads and sets a new profile picture for a user.
     * This method handles file validation, storage, and updates the user's
     * photograph field.
     * Users can upload their own profile pictures, and admins can upload for any
     * user.
     * 
     * @param requesterId   the ID of the user making the upload request
     * @param userId        the ID of the user whose profile picture is being
     *                      updated
     * @param fileUploadDTO the file upload data containing the image information
     * @return true if the upload was successful, false otherwise
     * @throws NotFoundException  if either the requester or target user doesn't
     *                            exist
     * @throws ForbiddenException if the requester lacks permission to upload for
     *                            the target user
     */
    @Override
    @Transactional
    public Boolean uploadProfilePicture(Long requesterId, Long userId, FileUploadDTO fileUploadDTO) {
        logger.info("Starting profile picture upload service for user ID: {} by requester ID: {}", userId, requesterId);

        try {
            // Authorization check
            User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "Requester");
            User targetUser = getEntity(daoFactory.forUser().findById(userId), userId, "User");

            if (!requesterId.equals(userId) && !requester.getRole().equals(Role.ADMIN)) {
                logger.warn("Unauthorized attempt to upload profile picture - requester ID: {}, target user ID: {}",
                        requesterId, userId);
                throw new ForbiddenException(
                        "Permission denied",
                        "User with ID " + requesterId + " cannot upload profile picture for user with ID " + userId);
            }

            logger.debug("Found user: {} {} (ID: {}) for profile picture upload",
                    targetUser.getFirstName(), targetUser.getLastName(), userId);

            // Delete old profile picture ONLY if it's not the default one
            String oldPicture = targetUser.getPhotograph();
            if (oldPicture != null && !oldPicture.equals(fileStorageUtil.getDefaultProfilePicture())) {
                logger.debug("Deleting old profile picture: {} for user ID: {}", oldPicture, userId);
                fileStorageUtil.deleteFile(oldPicture);
            }

            // Store the new picture with resizing
            String fileName = fileStorageUtil.storeProfilePicture(fileUploadDTO.getFileContent(),
                    fileUploadDTO.getFileName(), fileUploadDTO.getFileExtension(), fileUploadDTO.getFileMimeType());
            logger.debug("New profile picture stored with filename: {} for user ID: {}", fileName, userId);

            // Update user entity
            targetUser.setPhotograph(fileName);
            daoFactory.forUser().update(targetUser);
            logger.debug("User entity updated with new photograph filename: {} for user ID: {}", fileName, userId);

            logger.info("Profile picture updated successfully for user ID: {}, new filename: {}", userId, fileName);
            return true;
        } catch (ForbiddenException e) {
            logger.error("Error uploading profile picture for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Resets a user's profile picture to the default image.
     * This method removes the current profile picture (if not default) and sets the
     * default one.
     * Users can reset their own profile pictures, and admins can reset for any
     * user.
     * 
     * @param requesterId the ID of the user making the reset request
     * @param userId      the ID of the user whose profile picture is being reset
     * @return true if the reset was successful, false otherwise
     * @throws NotFoundException  if either the requester or target user doesn't
     *                            exist
     * @throws ForbiddenException if the requester lacks permission to reset for the
     *                            target user
     */
    @Override
    public Boolean resetProfilePicture(Long requesterId, Long userId) {
        logger.info("Starting profile picture reset for user ID: {} by requester ID: {}", userId, requesterId);

        try {
            // Authorization check
            User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "Requester");
            User targetUser = getEntity(daoFactory.forUser().findById(userId), userId, "User");

            if (!requesterId.equals(userId) && !requester.getRole().equals(Role.ADMIN)) {
                logger.warn("Unauthorized attempt to reset profile picture - requester ID: {}, target user ID: {}",
                        requesterId, userId);
                throw new ForbiddenException(
                        "Permission denied",
                        "User with ID " + requesterId + " cannot reset profile picture for user with ID " + userId);
            }

            // Delete old profile picture if it's not the default one
            String oldPicture = targetUser.getPhotograph();
            if (oldPicture != null && !oldPicture.equals(fileStorageUtil.getDefaultProfilePicture())) {
                logger.debug("Deleting old profile picture: {} for user ID: {}", oldPicture, userId);
                fileStorageUtil.deleteFile(oldPicture);
            }

            // Set default picture
            targetUser.setPhotograph(fileStorageUtil.getDefaultProfilePicture());
            daoFactory.forUser().update(targetUser);

            logger.info("Profile picture reset to default for user ID: {} by requester ID: {}", userId, requesterId);
            return true;
        } catch (ForbiddenException e) {
            logger.error("Error resetting profile picture for user ID: {}", userId, e);
            return false;
        }
    }

    /**
     * Retrieves all users in a minimal format (MiniUserDTO) excluding admins by
     * default.
     * This method provides a lightweight user list for UI components like
     * dropdowns.
     * 
     * @return a list of MiniUserDTO objects representing all non-admin users
     */
    @Override
    public List<MiniUserDTO> getAllMiniUsers() {
        return getAllMiniUsers(false);
    }

    /**
     * Retrieves all users in a minimal format (MiniUserDTO) with option to include
     * or exclude admins.
     * This method provides a lightweight user list for UI components like
     * dropdowns.
     * 
     * @param includeAdmins whether to include admin users in the result set
     * @return a list of MiniUserDTO objects representing users based on the admin
     *         inclusion flag
     */
    @Override
    public List<MiniUserDTO> getAllMiniUsers(boolean includeAdmins) {
        List<User> users = daoFactory.forUser().findAll();
        if (!includeAdmins) {
            users.removeIf(user -> user.getRole() == Role.ADMIN);
        }
        return miniUserConverter.toMiniUserDTOList(users);
    }

    private void sendProfileUpdateEmail(User user) {

        String managerEmail = user.getManager().getEmail();
        String userFullName = user.getFirstName() + " " + user.getLastName();
        String profileUrl = AppConstants.Email.EMAIL_BASE_URL + "/profile?id=" + user.getId();

        String title = "Team Member Profile Updated";
        String message = "Your team member <strong>" + userFullName + "</strong> has updated their profile.";
        String buttonText = "View Profile";
        String footerText = "You are receiving this email because you're listed as a manager for this user.";

        String emailBody = emailUtil.createEmailTemplate(title, message, buttonText, profileUrl, footerText);
        String subject = "Profile Update Notification: " + userFullName;

        emailUtil.sendEmail(managerEmail, subject, emailBody);
    }

    private void reassignEvaluatorForUser(User evaluatedUser, User newEvaluator) {
        List<Evaluation> evaluations = daoFactory.forEvaluation()
                .findByEvaluatedAndStatusIn(evaluatedUser,
                        List.of(EvaluationStatus.ONGOING, EvaluationStatus.COMPLETED));

        for (Evaluation evaluation : evaluations) {
            evaluation.setEvaluator(newEvaluator);
            daoFactory.forEvaluation().update(evaluation);
        }
    }

}
