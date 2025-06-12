package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.filters.UserFilter;

public interface UserService {

    UserDTO getUser(Long userId);

    Boolean registerUser(LoginDTO loginDTO, String ipAddress);

    Boolean confirmUser(String email, UserRegistrationDTO userRegistrationDTO);

    Boolean changePassword(Long userId, String newPassword);

    Boolean assignManager(Long userId, Long managerId);

    Boolean assignManagerBulk(Long managerId, Long[] userIds);

    Boolean updateUser(UserDTO userDto);

    List<UserDTO> getUsersByFilters(UserFilter userFilter);

    Boolean sendResetPasswordLink(String email, String ipAddress);

    Boolean uploadProfilePicture(Long userId, FileUploadDTO fileUploadDTO);

    Boolean resetProfilePicture(Long userId);

}
