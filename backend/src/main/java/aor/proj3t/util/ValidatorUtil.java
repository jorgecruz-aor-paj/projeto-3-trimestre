package aor.proj3t.util;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import aor.proj3t.dto.*;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidatorUtil {

    public ValidatorUtil() {
        // Default constructor
    }

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

    public void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null || !birthDate.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Birth date must be in the past");
        }
    }

    public void validateBiography(String biography) {
        if (biography == null || biography.length() < 2 || biography.length() > 250) {
            throw new InvalidDataException("Biography must be between 2 and 250 characters");
        }
    }

    public void validateWorkplaceId(Long workplaceId) {
        if (workplaceId == null || workplaceId <= 0) {
            throw new InvalidDataException("Workplace ID must be a positive number");
        }
    }

    public void validatePhotograph(String photograph) {
        if (photograph != null && !photograph.isEmpty()) {
            // Optional: add base64 format check if needed
            // String base64Regex = "^[A-Za-z0-9+/=]+$";
            // if (!photograph.matches(base64Regex)) {
            //     throw new InvalidDataException("Invalid base64 format for photograph");
            // }
            //TODO check validation logic
        }
    }

    public void validateManagerId(Long managerId) {
        if (managerId != null && managerId <= 0) {
            throw new InvalidDataException("Manager ID, if provided, must be positive");
        }
    }

    public void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidDataException("User ID must be a positive number");
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


    public void validateTrainingId(Long id) {
        if (id == null) {
            throw new InvalidDataException("Training ID is required");
        }
        if (id <= 0) {
            throw new InvalidDataException("Training ID must be positive");
        }
    }

    public void validateTrainingTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("Title is required");
        }
        if (title.length() > 100) {
            throw new InvalidDataException("Title must not exceed 100 characters");
        }
    }

    public void validateTrainingDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new InvalidDataException("Duration must be a positive number of minutes");
        }
    }

    public void validateTrainingDescription(String description) {
        if (description == null || description.length() < 10 || description.length() > 1000) {
            throw new InvalidDataException("Description must be between 10 and 1000 characters");
        }
    }

    public void validateTrainingLink(String link) {
        if (link != null && !link.isEmpty()) {
            if (link.length() > 500) {
                throw new InvalidDataException("Link must not exceed 500 characters");
            }
            String urlRegex = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w\\.-]+)+(?:[/?#][^\\s]*)?$";
            if (!Pattern.matches(urlRegex, link)) {
                throw new InvalidDataException("Link is not a valid URL format");
            }
        }
    }

    public void validateTrainingLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new InvalidDataException("Language is required");
        }
    }

    public void validateTrainingAreaId(Long trainingAreaId) {
        if (trainingAreaId == null || trainingAreaId <= 0) {
            throw new InvalidDataException("Training Area ID must be a positive number");
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
        validateBirthDate(dto.getBirthDate());
        validateBiography(dto.getBiography());
        validateAddress(dto.getAddress());
        validateWorkplaceId(dto.getWorkplaceId());
    }

    public void validateUserDTO(UserDTO dto) {
        validateUserId(dto.getId());
        validateName(dto.getFirstName());
        validateName(dto.getLastName());
        validatePhone(dto.getPhoneNumber());
        validateAddress(dto.getAddress());
        validateBirthDate(dto.getBirthDate());
        validateBiography(dto.getBiography());
        validateWorkplaceId(dto.getWorkplaceId());
        validatePhotograph(dto.getPhotograph());
        validateManagerId(dto.getManagerId());
        validateRole(dto.getRole().getName());
    }

    public void validateManagerAssignDTO(ManagerAssignDTO dto) {
        validateUserId(dto.getManagerId());
        validateUserIdList(dto.getUserIds());
    }

    public void validateNewTrainingDTO(NewTrainingDTO newTrainingDto) {
        validateTrainingId(newTrainingDto.getId());
        validateTrainingTitle(newTrainingDto.getTitle());
        validateTrainingDuration(newTrainingDto.getDuration());
        validateTrainingDescription(newTrainingDto.getDescription());
        validateTrainingLink(newTrainingDto.getLink());
        validateTrainingLanguage(newTrainingDto.getLanguage());
        validateTrainingAreaId(newTrainingDto.getTrainingAreaId());
    }

}

