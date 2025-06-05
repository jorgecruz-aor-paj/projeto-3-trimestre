package aor.proj3t.service.impl;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.impl.*;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.*;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.ConverterUtil;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.PasswordUtil;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserServiceImpl implements UserService {
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

    public Boolean registerUser(LoginDTO loginDTO, String ipAddress) {
        try {
            PendingRegistration registration = new PendingRegistration(loginDTO.getEmail(), loginDTO.getPassword());
            registration.setConfirmationToken(jwtUtil.generateConfirmationToken(loginDTO.getEmail(), ipAddress, getConfirmationExpirationTime()));
            registration.setExpirationDate(Instant.now().plus(getConfirmationExpirationTime(), ChronoUnit.MINUTES));
            pendingRegistrationDAO.create(registration);
            return true;
        } catch (Exception e) {
            logger.error("Error registering user with email {}.  {}", loginDTO.getEmail(), e);
            return false;
        }
    }

    public Boolean confirmUser(Long registrationId, UserDTO newUserDto) {
        try {
            PendingRegistration registration = getPendingRegistration(registrationId);
            if (registration == null) {
                logger.warn("Failed to confirm user: No pending registration found with id {}", registrationId);
                return false;
            }
            Workplace workplace = getWorkplace(newUserDto.getWorkplaceId());
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
            if(newUserDto.getBiography() != null) {
                newUser.setBiography(newUserDto.getBiography());
            }
            userDao.create(newUser);
            return true;
        } catch (Exception e) {
            logger.error("Error confirming registration with id {}. ", registrationId, e);
            return false;
        }
    }

    public Boolean changePassword(Long userId, String newRawPassword) {
        try {
            User user = getUser(userId);
            user.setPassword(PasswordUtil.hashPassword(newRawPassword));
            userDao.update(user);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} while updating user. ", userId, e);
            return false;
        } catch (Exception e) {
            logger.error("Error changing password for user id {}. ", userId, e);
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
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} or manager ID {} while assigning manager. ", userId, managerId, e);
            return false;

        } catch (Exception e) {
            logger.error("Error assigning manager. ", e);
            return false;
        }
    }

    public Boolean updateUser(UserDTO userDto) {
        try {
            User user = getUser(userDto.getId());
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
                workplace = getWorkplace(userDto.getWorkplaceId());
            } catch (NoSuchElementException e) {
                logger.error("Workplace with ID {} not found while updating user.", userDto.getWorkplaceId());
                return false;
            }
            user.setWorkplace(workplace);

            userDao.update(user);
        return true;
        } catch (NoSuchElementException e) {
            logger.error("Invalid user ID {} while updating user. ", userDto.getId(), e);
            return false;

        } catch (Exception e) {
            logger.error("Error updating user.", e);
            return false;
        }
    }

    public List<UserDTO> getUsersByFilters(UserFilter filter) {
        try {
            return ConverterUtil.userEntityListToUserDTOList(userDao.getUsersByFilters(filter));
        } catch (Exception e) {
            logger.error("Error getting users list. ", e);
            return new ArrayList<>();
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
                return AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME;
            }
        } catch (Exception e) {
            logger.error("Error getting confirmation expiration window. ", e);
            return AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME;
        }
    }

    private PendingRegistration getPendingRegistration(Long id) {
        Optional<PendingRegistration> pendingRegistrationOpt = pendingRegistrationDAO.findById(id);
        return pendingRegistrationOpt.orElse(null);
    }

    private Workplace getWorkplace(Long workplaceId) {
        return workPlaceDAO.findById(workplaceId)
                .orElseThrow(() -> new NoSuchElementException("Workplace not found with ID: " + workplaceId));
    }

    private User getUser(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }

}
