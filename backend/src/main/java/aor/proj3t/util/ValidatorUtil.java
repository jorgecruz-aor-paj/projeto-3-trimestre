package aor.proj3t.util;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserRegistrationDTO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;


public class ValidatorUtil {

    private ValidatorUtil() {
    }

    private Boolean isValidEmail(String email) {
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
        String emailRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])\\S{8,}$";
        Pattern pattern = Pattern.compile(emailRegex);
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

    public Boolean isValidBiography (String biography){
        if(biography.length() < 2 || biography.length() > 250){
            return false;
        }
        return true;
    }

    public Boolean isValidWorkplaceId(Long workplaceId) {
        if (workplaceId == null || workplaceId >= 0) {
            return false;
        }
        return true;
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


}
