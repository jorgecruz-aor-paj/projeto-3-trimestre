package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import aor.proj3t.config.SettingsProvider;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.dto.paginated.UserPaginationDTO;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.angus.mail.imap.protocol.ID;

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
        Integer confirmationExpirationTime = settingsProvider.getSettings().getConfirmationExpirationTime();
        validateEmail(email);
        PendingRegistration registration = new PendingRegistration(email, password);
        registration.setConfirmationToken(jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime));
        registration.setExpirationDate(now.plus(confirmationExpirationTime, ChronoUnit.MINUTES));
        daoFactory.forPendingRegistration().create(registration);
        sendConfirmationEmail(email, ipAddress, confirmationExpirationTime);
    }

    @Override
    public void confirmUser(String email, UserRegistrationDTO userRegistrationDto) {
        PendingRegistration registration = getPendingRegistration(email);
        Workplace workplace = getEntity(daoFactory.forWorkplace().findById(userRegistrationDto.getWorkplaceId()), userRegistrationDto.getWorkplaceId(), "Workplace");

        User newUser = createNewUser(userRegistrationDto, registration, workplace, fileStorageUtil);
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
    @Transactional
    public void assignManagerBulk(Long userId, ManagerAssignDTO managerAssignDto) {
        User requester = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        User manager = getEntity(daoFactory.forUser().findById(managerAssignDto.getManagerId()), managerAssignDto.getManagerId(), "User");
        List<User> users = getUserListFromIds(managerAssignDto.getUserIds());
        if (!requester.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "Manager assignment request by nonAdmin user with id " + requester.getId());
        }
        if (manager.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User management assigned to admin with id " +manager.getId() + " by Admin with id " + requester.getId());
        }
        if (users.contains(manager)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "User with id " + manager.getId() + " cannot manage himself");
        }
        for (User user : users) {
            user.setManager(manager);
            daoFactory.forUser().update(user);
        }
        if (manager.getRole().equals(Role.USER)) {
            manager.setRole(Role.MANAGER);
            daoFactory.forUser().update(manager);
        }
        demoteManagersWithoutUsers();
    }

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
    }

    @Override
    public List<UserDTO> getUsersByFilters(Long requesterId, UserFilter filters) {
            User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
            //TODO remove if no filter limits are needed for users
            if (requester.getRole() == Role.USER && filters.getManager() != null) {
                throw new ForbiddenException(
                        "Permission denied",
                        "User with ID " + requesterId + " accessed forbidden user filters");
            }
            return converterFactory.forUser().toDTOList(daoFactory.forUser().getUsersByFilters(filters));
    }

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
                userRegistrationDto.getBirthDate(),
                Role.USER,
                workplace);

        if (userRegistrationDto.getBiography() != null) {
            newUser.setBiography(userRegistrationDto.getBiography());
        }
        newUser.setPhotograph(fileStorageUtil.getDefaultProfilePicture());
        return newUser;
    }

    private void sendResetPasswordEmail(String email, Long userId, String ipAddress, Integer resetPasswordExpirationTime) {
        String resetPasswordToken = jwtUtil.generatePasswordResetToken(email, userId, ipAddress, resetPasswordExpirationTime);
        String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
        emailUtil.sendPasswordResetEmail(email, resetPasswordToken, baseUrl);
    }

    private void updateUserFields(User user, UserDTO userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());
        user.setBiography(userDto.getBiography());
        user.setBirthDate(userDto.getBirthDate());
        user.setPhotograph(userDto.getPhotograph());

        Workplace workplace = getEntity(daoFactory.forWorkplace().findById(userDto.getWorkplaceId()), userDto.getWorkplaceId(), "Workplace");
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
        String baseUrl = "https://localhost:8443/jorgeosorio-proj3t/api/auth";
        emailUtil.sendConfirmationEmail(email, token, baseUrl);
    }

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


    @Override
    public Boolean uploadProfilePicture(Long userId, FileUploadDTO fileUploadDTO) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
            if (user == null) {
                logger.warn("User not found with ID: {} for profile picture upload", userId);
                return false;
            }

            // Delete old profile picture ONLY if it's not the default one
            String oldPicture = user.getPhotograph();
            if (oldPicture != null && !oldPicture.equals(fileStorageUtil.getDefaultProfilePicture())) {
                fileStorageUtil.deleteFile(oldPicture);
            } // Store the new picture with resizing
            String fileName = fileStorageUtil.storeProfilePicture(fileUploadDTO.getFileContent(), fileUploadDTO.getFileName(), fileUploadDTO.getFileExtension(), fileUploadDTO.getFileMimeType());

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
            User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
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

    /**
     * Get users with pagination and sorting
     */
    @Override
    public UserPaginationDTO getPaginatedUsers(UserFilter filter, int page, int size, String sortString) {
        // TODO implement pagination and sorting logic
        return new UserPaginationDTO();
    }
}
