package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;

public interface AuthService {

    String login(LoginDTO loginDto, String ipAddress, String userAgent);

    void logout(Long userId, String token);


}
