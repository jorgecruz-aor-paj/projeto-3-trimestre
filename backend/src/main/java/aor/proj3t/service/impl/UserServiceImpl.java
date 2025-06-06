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
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.inject.Inject;

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

    @Override
    public Boolean registerUser(LoginDTO loginDTO, String ipAddress) {
        Instant now = Instant.now();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        Integer confirmationExpirationTime = getConfirmationExpirationTime();
        try {
            PendingRegistration registration = new PendingRegistration(email, password);
            registration
                    .setConfirmationToken(
                            jwtUtil.generateConfirmationToken(email, ipAddress, confirmationExpirationTime));
            registration
                    .setExpirationDate(now.plus(confirmationExpirationTime, ChronoUnit.MINUTES));
            daoFactory.forPendingRegistration().create(registration);
            return true;
        } catch (Exception e) {
            logger.error("Error registering user with email {}.  {}", email, e);
            return false;
        }
    }

    @Override
    public Boolean confirmUser(Long registrationId, UserDTO newUserDto) {
        try {
            PendingRegistration registration = getEntity(daoFactory.forPendingRegistration().findById(registrationId),registrationId);
            if (registration == null) {
                logger.warn("Failed to confirm user: No pending registration found with id {}", registrationId);
                return false;
            }
            Workplace workplace = getEntity(daoFactory.forWorkplace().findById(newUserDto.getWorkplaceId()), newUserDto.getWorkplaceId());
            if (workplace == null) {
                logger.warn("Failed to confirm user: Workplace ID {} does not exist", newUserDto.getWorkplaceId());
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

            if (newUserDto.getBiography() != null) {
                newUser.setBiography(newUserDto.getBiography());
            }
            // Create the user
            daoFactory.forUser().create(newUser);

            // Delete the pending registration
            daoFactory.forPendingRegistration().deleteById(registration.getId());

            logger.info("User with email {} successfully registered and confirmed.", registration.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error confirming registration with id {}. ", registrationId, e);
            return false;
        }
    }

    @Override
    public Boolean changePassword(Long userId, String newRawPassword) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userId),userId);
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
            User user = getEntity(daoFactory.forUser().findById(userId),userId);
            User manager = getEntity(daoFactory.forUser().findById(managerId),managerId);
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
        try {
            User manager = getEntity(daoFactory.forUser().findById(managerId),managerId);
            List<User> users = getUserListFromIds(userIds);
            for (User user : users) {
                user.setManager(manager);
                daoFactory.forUser().update(user);
            }
            if (!manager.getRole().equals(Role.MANAGER)) {
                manager.setRole(Role.MANAGER);
            }
            return true;
        }catch (Exception e) {
            logger.error("Error assigning manager with id {} in bulk. ", e);
            return false;
        }
    }

    @Override
    public Boolean updateUser(UserDTO userDto) {
        try {
            User user = getEntity(daoFactory.forUser().findById(userDto.getId()),userDto.getId());
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
                workplace = getEntity(daoFactory.forWorkplace().findById(userDto.getWorkplaceId()), userDto.getWorkplaceId());
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

    private Integer getConfirmationExpirationTime() {
        try {
            Optional<Settings> settingsOpt = daoFactory.forSettings().getLatestSettings();
            Settings settings;
            if (settingsOpt.isPresent()) {
                settings = settingsOpt.get();
                return settings.getConfirmationExpirationTime();
            } else {
                return AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME;
            }
        } catch (Exception e) {
            logger.error("Error getting confirmation expiration window. ", e);
            return AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME;
        }
    }

    protected List<User> getUserListFromIds(Long[] userIds) {
        try {
            List<User> users = new ArrayList<>(userIds.length);
            for (Long userId : userIds) {
                User user = getEntity(daoFactory.forUser().findById(userId),userId);
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

}
