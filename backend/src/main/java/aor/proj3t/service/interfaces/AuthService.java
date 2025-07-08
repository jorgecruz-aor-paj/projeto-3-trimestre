package aor.proj3t.service.interfaces;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.Session;

import java.util.List;

public interface AuthService {

    String login(LoginDTO loginDto, String ipAddress, String userAgent);

    void logout(Long userId, String token);


}
