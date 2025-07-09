package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Map;

import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.filters.UserFilter;

public interface UserService {

    UserDTO getUser(Long userId);

    void registerUser(LoginDTO loginDTO, String ipAddress);

    void confirmUser(String email, UserRegistrationDTO userRegistrationDTO);

    void changePassword(Long userId, String newPassword);

    void assignManagerBulk(Long userId, ManagerAssignDTO managerAssignDto);

    void updateUser(Long userId, UserDTO userDto);

    List<UserDTO> getUsersByFilters(Long requesterId, UserFilter userFilter);

    void sendResetPasswordLink(String email, String ipAddress);

    Boolean uploadProfilePicture(Long requesterId, Long userId, FileUploadDTO fileUploadDTO);

    Boolean resetProfilePicture(Long requesterId, Long userId);

    List<UserDTO> getAllManagers(Long requesterId);

    Map<String, Long> getTotalVSUnmanagedUsers(Long requesterId);

    List<MiniUserDTO> getAllMiniUsers();
}
