package aor.proj3t.util.authutils;

import aor.proj3t.enums.Role;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class CustomSecurityContext implements SecurityContext {

    private final String userId;
    private final String role;

    public CustomSecurityContext(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> userId; // Returns user identity
    }

    @Override
    public boolean isUserInRole(String role) {
        try {
            Role expectedRole = Role.valueOf(role.toUpperCase());
            return expectedRole.name().equals(this.role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false; // Role isn't valid
        }
    }

    @Override
    public boolean isSecure() {
        return true; // Assume HTTPS is enforced
    }

    @Override
    public String getAuthenticationScheme() {
        return "JWT"; // Uses JWT for authentication
    }
}

