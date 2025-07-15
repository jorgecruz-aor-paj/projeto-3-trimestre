package aor.proj3t.util.validatorutils.fordto;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import aor.proj3t.config.AppConstants;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Validator class for user-related input and DTO validation.
 * <p>
 * This utility provides centralized methods for verifying the correctness of user
 * registration data, profiles, contact information, role assignment, and authentication credentials.
 * It ensures consistency and enforces constraints before the data reaches service or persistence layers.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class UserValidator {

    /**
     * Validates that the given email is present and matches the defined format.
     *
     * @param email the email to validate
     * @throws InvalidDataException if the email is null, empty, or invalid
     */
    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidDataException("Email cannot be null or empty");
        }
        if (!Pattern.compile(AppConstants.Validation.EMAIL_REGEX).matcher(email).matches()) {
            throw new InvalidDataException("Email format is invalid");
        }
    }

    /**
     * Validates that the given password is not empty and matches strength requirements.
     *
     * @param password the password to validate
     * @throws InvalidDataException if the password is null, empty, or weak
     */
    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Password cannot be null or empty");
        }
        if (!Pattern.compile(AppConstants.Validation.PASSWORD_REGEX).matcher(password).matches()) {
            throw new InvalidDataException("Password does not meet complexity requirements");
        }
    }

    /**
     * Validates that a name is within acceptable length bounds.
     *
     * @param name the name to validate
     * @throws InvalidDataException if the name is too short or too long
     */
    public void validateName(String name) {
        if (name == null || name.length() < AppConstants.Validation.MIN_NAME_LENGTH
                || name.length() > AppConstants.Validation.MAX_NAME_LENGTH) {
            throw new InvalidDataException("Name must be between " + AppConstants.Validation.MIN_NAME_LENGTH + " and "
                    + AppConstants.Validation.MAX_NAME_LENGTH + " characters");
        }
    }

    /**
     * Validates that the phone number matches the required pattern.
     *
     * @param phoneNumber the phone number to validate
     * @throws InvalidDataException if the phone number is blank or malformed
     */
    public void validatePhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new InvalidDataException("Phone number cannot be empty");
        }
        if (!Pattern.compile(AppConstants.Validation.PHONE_REGEX).matcher(phoneNumber).matches()) {
            throw new InvalidDataException("Phone number must be 9 digits");
        }
    }

    /**
     * Validates that the address is present and within allowed length.
     *
     * @param address the address string to validate
     * @throws InvalidDataException if the address is null, too short, or too long
     */
    public void validateAddress(String address) {
        if (address == null || address.isEmpty() || address.length() < AppConstants.Validation.MIN_ADDRESS_LENGTH
                || address.length() > AppConstants.Validation.MAX_ADDRESS_LENGTH) {
            throw new InvalidDataException("Address must be between " + AppConstants.Validation.MIN_ADDRESS_LENGTH
                    + " and " + AppConstants.Validation.MAX_ADDRESS_LENGTH + " characters");
        }
    }

    /**
     * Validates that the birthdate is in the past.
     *
     * @param birthdate the date of birth to validate
     * @throws InvalidDataException if birthdate is null or in the future
     */
    public void validatebirthdate(LocalDate birthdate) {
        if (birthdate == null || !birthdate.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Birth date must be in the past");
        }
    }

    /**
     * Validates that the biography meets the required character bounds.
     *
     * @param biography the bio text to validate
     * @throws InvalidDataException if the bio is too short or too long
     */
    public void validateBiography(String biography) {
        if (biography == null || biography.length() < AppConstants.Validation.MIN_BIOGRAPHY_LENGTH
                || biography.length() > AppConstants.Validation.MAX_BIOGRAPHY_LENGTH) {
            throw new InvalidDataException("Biography must be between " + AppConstants.Validation.MIN_BIOGRAPHY_LENGTH
                    + " and " + AppConstants.Validation.MAX_BIOGRAPHY_LENGTH + " characters");
        }
    }

    /**
     * Validates that a numeric ID is positive and non-null.
     *
     * @param id the ID value
     * @param entity the name of the entity for contextual error messages
     * @throws InvalidDataException if the ID is missing or invalid
     */
    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
        }
    }

    /**
     * Validates that no photograph is present in certain contexts.
     *
     * @param photograph the photograph filename or value
     * @throws InvalidDataException if photograph is not blank
     */
    public void validatePhotograph(String photograph) {
        if (photograph != null && !photograph.isEmpty()) {
            throw new InvalidDataException("Photograph must be empty");
        }
    }

    /**
     * Validates that a role string corresponds to a known enum value.
     *
     * @param role the role name to validate
     * @throws InvalidDataException if the role is unrecognized
     */
    public void validateRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new InvalidDataException("Role cannot be null or empty");
        }
        boolean matched = false;
        for (Role roleEnum : Role.values()) {
            if (roleEnum.name().equalsIgnoreCase(role)) {
                matched = true;
                break;
            }
        }
        if (!matched) {
            throw new InvalidDataException("Invalid role: " + role);
        }
    }

    /**
     * Validates that a list of user IDs is not empty.
     *
     * @param userIdList the list of IDs to validate
     * @throws InvalidDataException if the list is null or empty
     */
    public void validateUserIdList(List<Long> userIdList) {
        if (userIdList == null || userIdList.isEmpty()) {
            throw new InvalidDataException("User list must not be empty");
        }
    }

    /**
     * Validates credentials inside a {@link LoginDTO} object.
     *
     * @param loginDTO the DTO containing email and password
     */
    public void validateLoginDTO(LoginDTO loginDTO) {
        validateEmail(loginDTO.getEmail());
        validatePassword(loginDTO.getPassword());
    }

    /**
     * Validates fields within a {@link UserRegistrationDTO} object.
     *
     * @param dto the registration DTO
     */
    public void validateUserRegistrationDTO(UserRegistrationDTO dto) {
        validateName(dto.getFirstName());
        validateName(dto.getLastName());
        validatePhone(dto.getPhoneNumber());
        validatebirthdate(dto.getBirthdate());
        validateBiography(dto.getBiography());
        validateAddress(dto.getAddress());
        validateId(dto.getWorkplaceId(), "Workplace");
    }

    /**
     * Validates the full structure of a {@link UserDTO} including nested fields.
     *
     * @param dto the user profile DTO
     */
    public void validateUserDTO(UserDTO dto) {
        validateId(dto.getId(), "User");
        validateName(dto.getFirstName());
        validateName(dto.getLastName());
        validatePhone(dto.getPhoneNumber());
        validateAddress(dto.getAddress());
        validatebirthdate(dto.getBirthdate());
        validateBiography(dto.getBiography());
        validateId(dto.getWorkplace().getId(), "Workplace");
        validatePhotograph(dto.getPhotograph());
        if (dto.getManager() != null) {
            validateId(dto.getManager().getId(), "Manager");
        }
        validateRole(dto.getRole().getName());
    }

    /**
     * Validates a {@link ManagerAssignDTO}, ensuring a valid manager and user list.
     *
     * @param dto the DTO containing the assignment configuration
     */
    public void validateManagerAssignDTO(ManagerAssignDTO dto) {
        validateId(dto.getManagerId(), "Manager");
        validateUserIdList(dto.getUserIds());
    }
}

