package aor.proj3t.service.interfaces;

import aor.proj3t.dto.*;
import aor.proj3t.dto.paginated.UserPaginationDTO;
import aor.proj3t.filters.UserFilter;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDTO getUser(Long userId);

    void registerUser(LoginDTO loginDTO, String ipAddress);

    void confirmUser(String email, UserRegistrationDTO userRegistrationDTO);

    void changePassword(Long userId, String newPassword);

    void assignManagerBulk(Long userId, ManagerAssignDTO managerAssignDto);

    void updateUser(Long userId, UserDTO userDto);

    List<UserDTO> getUsersByFilters(Long requesterId, UserFilter userFilter);

    void sendResetPasswordLink(String email, String ipAddress);

    Boolean uploadProfilePicture(Long userId, FileUploadDTO fileUploadDTO);

    Boolean resetProfilePicture(Long userId);

    List<UserDTO> getAllManagers(Long requesterId);

    Map<String, Long> getTotalVSUnmanagedUsers(Long requesterId);

    List<MiniUserDTO> getAllMiniUsers();
}
