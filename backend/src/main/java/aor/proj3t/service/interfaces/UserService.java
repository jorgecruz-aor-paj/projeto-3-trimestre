package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.PendingRegistrationDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.filters.UserFilter;

import java.util.List;

public interface UserService {

    Boolean registerUser(LoginDTO loginDTO, String ipAddress);

    UserDTO confirmUser(PendingRegistrationDTO pendingRegistration);

    boolean changePassword(Long userId, String newPassword);

    boolean assignManager(Long userId, Long managerId);

    List<UserDTO> getUsersByFilters(UserFilter userFilter);
}
