package aor.proj3t.service.interfaces;

import aor.proj3t.entities.Session;
import aor.proj3t.enums.Role;

public interface TokenService {

    Long getUserIdFromToken(String token);

    Long getUserIdFromAuthHeader(String authHeader);

    Role getRoleFromAuthHeader(String authHeader);

    boolean isTokenActive(String token);

    Session getSessionByToken(String token);

}
