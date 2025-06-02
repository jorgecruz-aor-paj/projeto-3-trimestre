package aor.proj3t.service.interfaces;

import aor.proj3t.Entities.SessionEntity;
import aor.proj3t.dto.LoginDTO;

public interface AuthService {

    /**
     * Authenticates a user and creates a session.
     *
     * @param loginDTO  the login credentials
     * @param userAgent the user agent of the client
     * @param ipAddress the IP address of the client
     * @return a SessionEntity representing the authenticated session
     */
    SessionEntity login(LoginDTO loginDTO, String userAgent, String ipAddress);

    /**
     * Logs out a user by invalidating their session.
     *
     * @param sessionToken the session token of the user to log out
     * @return true if the logout was successful, false otherwise
     */
    boolean logout(String sessionToken);
}
