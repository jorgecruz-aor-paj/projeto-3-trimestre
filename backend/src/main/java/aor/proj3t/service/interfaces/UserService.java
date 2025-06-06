package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;

import java.util.List;

public interface UserService {

    Boolean registerUser(LoginDTO loginDTO, String ipAddress);

    Boolean confirmUser(Long registrationId, UserDTO newUserDto);

    Boolean changePassword(Long userId, String newPassword);

    Boolean assignManager(Long userId, Long managerId);

    Boolean assignManagerBulk(Long managerId, Long[] userIds);

    Boolean updateUser(UserDTO userDto);

    List<UserDTO> getUsersByFilters(UserFilter userFilter);
}
