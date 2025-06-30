package aor.proj3t.util.validatorutils.fordto;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserValidator {

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidDataException("Email cannot be null or empty");
        }
        String emailRegex = "^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new InvalidDataException("Email format is invalid");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Password cannot be null or empty");
        }
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&.-_])\\S{8,}$";
        if (!Pattern.compile(passwordRegex).matcher(password).matches()) {
            throw new InvalidDataException("Password does not meet complexity requirements");
        }
    }

    public void validateName(String name) {
        if (name == null || name.length() < 2 || name.length() > 30) {
            throw new InvalidDataException("Name must be between 2 and 30 characters");
        }
    }

    public void validatePhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new InvalidDataException("Phone number cannot be empty");
        }
        String phoneRegex = "^\\d{9}$";
        if (!Pattern.compile(phoneRegex).matcher(phoneNumber).matches()) {
            throw new InvalidDataException("Phone number must be 9 digits");
        }
    }

    public void validateAddress(String address) {
        if (address == null || address.isEmpty() || address.length() < 2 || address.length() > 100) {
            throw new InvalidDataException("Address must be between 2 and 100 characters");
        }
    }

    public void validatebirthdate(LocalDate birthdate) {
        if (birthdate == null || !birthdate.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Birth date must be in the past");
        }
    }

    public void validateBiography(String biography) {
        if (biography == null || biography.length() < 2 || biography.length() > 250) {
            throw new InvalidDataException("Biography must be between 2 and 250 characters");
        }
    }

    public void validateId(Long id, String entity) {
        if (id == null || id <= 0) {
            throw new InvalidDataException(entity + " ID must be a positive number");
        }
    }

    public void validatePhotograph(String photograph) {
        if (photograph != null && !photograph.isEmpty()) {
            // Optional: add base64 format check if needed
            // String base64Regex = "^[A-Za-z0-9+/=]+$";
            // if (!photograph.matches(base64Regex)) {
            // throw new InvalidDataException("Invalid base64 format for photograph");
            // }
            // TODO check validation logic
        }
    }

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

    public void validateUserIdList(List<Long> userIdList) {
        if (userIdList == null || userIdList.isEmpty()) {
            throw new InvalidDataException("User list must not be empty");
        }
    }

    public void validateLoginDTO(LoginDTO loginDTO) {
        validateEmail(loginDTO.getEmail());
        validatePassword(loginDTO.getPassword());
    }

    public void validateUserRegistrationDTO(UserRegistrationDTO dto) {
        validateName(dto.getFirstName());
        validateName(dto.getLastName());
        validatePhone(dto.getPhoneNumber());
        validatebirthdate(dto.getBirthdate());
        validateBiography(dto.getBiography());
        validateAddress(dto.getAddress());
        validateId(dto.getWorkplaceId(), "Workplace");
    }

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
        validateId(dto.getManagerId(), "Manager");
        validateRole(dto.getRole().getName());
    }

    public void validateManagerAssignDTO(ManagerAssignDTO dto) {
        validateId(dto.getManagerId(), "Manager");
        validateUserIdList(dto.getUserIds());
    }
}
