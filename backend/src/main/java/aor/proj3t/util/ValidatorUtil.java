package aor.proj3t.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.enums.Role;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidatorUtil {

    public ValidatorUtil() {
        // Default constructor
    }

    public Boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public Boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&.-_])\\S{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public Boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (name.length() < 2 || name.length() > 30) {
            return false;
        }
        return true;
    }

    public Boolean isValidPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        String emailRegex = "^\\d{9}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(phoneNumber).matches();

    }

    public Boolean isValidAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        if (address.length() < 2 || address.length() > 100) {
            return false;
        }
        return true;
    }

    public Boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        return birthDate.isBefore(LocalDate.now());
    }

    public Boolean isValidBiography(String biography) {
        if (biography.length() < 2 || biography.length() > 250) {
            return false;
        }
        return true;
    }

    public Boolean isValidWorkplaceId(Long workplaceId) {
        if (workplaceId == null || workplaceId <= 0) {
            return false;
        }
        return true;
    }

    public Boolean isValidPhotograph(String photograph) {
        // Accepts null (optional field), or valid base64 string
        if (photograph == null || photograph.isEmpty()) {
            return true;
        }
        //String base64Regex = "^[A-Za-z0-9+/=]+$";
        //return photograph.matches(base64Regex);
        return true;
    }

    public Boolean isValidManagerId(Long managerId) {
        // Optional field; if present must be positive
        return managerId == null || managerId > 0;
    }

    public Boolean isValidRole(String role) {
        if (role == null || role.isEmpty()) {
            return false;
        }
        for (Role roleEnum : Role.values()) {
            if (roleEnum.name().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isValidLoginDTO(LoginDTO loginDTO) {
        return isValidEmail(loginDTO.getEmail()) && isValidPassword(loginDTO.getPassword());
    }

    public Boolean isValidUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO) {
        return isValidName(userRegistrationDTO.getFirstName()) &&
                isValidName(userRegistrationDTO.getLastName()) &&
                isValidPhone(userRegistrationDTO.getPhoneNumber()) &&
                isValidBirthDate(userRegistrationDTO.getBirthDate()) &&
                isValidBiography(userRegistrationDTO.getBiography()) &&
                isValidAddress(userRegistrationDTO.getAddress()) &&
                isValidWorkplaceId(userRegistrationDTO.getWorkplaceId());
    }

    public Boolean isValidUserDTO(UserDTO userDTO) {
        System.out.println("here");
        return isValidName(userDTO.getFirstName()) &&
                isValidName(userDTO.getLastName()) &&
                isValidPhone(userDTO.getPhoneNumber()) &&
                isValidAddress(userDTO.getAddress()) &&
                isValidBirthDate(userDTO.getBirthDate()) &&
                isValidBiography(userDTO.getBiography()) &&
                isValidWorkplaceId(userDTO.getWorkplaceId()) &&
                isValidPhotograph(userDTO.getPhotograph()) &&
                isValidManagerId(userDTO.getManagerId()) &&
                isValidRole(userDTO.getRole().getName());
    }


}
