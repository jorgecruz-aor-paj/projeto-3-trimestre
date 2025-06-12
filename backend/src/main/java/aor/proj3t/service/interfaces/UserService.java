package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.filters.UserFilter;

import java.util.List;

public interface UserService {

    UserDTO getUser(Long userId);

    String login(LoginDTO loginDto, String ipAddress, String userAgent) throws Exception;

    Boolean registerUser(LoginDTO loginDTO, String ipAddress);

    Boolean confirmUser(String email, UserRegistrationDTO userRegistrationDTO);

    Boolean changePassword(Long userId, String newPassword);

    Boolean assignManager(Long userId, Long managerId);

    Boolean assignManagerBulk(Long managerId, Long[] userIds);

    Boolean updateUser(UserDTO userDto);

    List<UserDTO> getUsersByFilters(UserFilter userFilter);

    Boolean sendResetPasswordLink(String email, String ipAddress);

    }
