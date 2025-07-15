package aor.proj3t.util.authutils;

import aor.proj3t.enums.Role;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

/**
 * Custom implementation of {@link SecurityContext} for JWT-based authentication.
 * <p>
 * This class wraps user identity and role into a simplified security context.
 * It supports role-based access control checks, assumes secure transmission,
 * and identifies JWT as the authentication scheme.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class CustomSecurityContext implements SecurityContext {

    private final String userId;
    private final String role;

    /**
     * Constructs a custom security context using user ID and role.
     *
     * @param userId the authenticated user's ID
     * @param role the user's role (e.g. ADMIN, USER)
     */
    public CustomSecurityContext(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    /**
     * Returns the user's principal, representing the user identity.
     *
     * @return a {@link Principal} object using the user ID
     */
    @Override
    public Principal getUserPrincipal() {
        return () -> userId;
    }

    /**
     * Checks if the user is assigned the specified role.
     *
     * @param role the role to check against
     * @return true if the user's role matches, false otherwise
     */
    @Override
    public boolean isUserInRole(String role) {
        try {
            Role expectedRole = Role.valueOf(role.toUpperCase());
            return expectedRole.name().equals(this.role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Indicates whether the current request is transmitted over a secure channel.
     *
     * @return true (assumes HTTPS is enforced)
     */
    @Override
    public boolean isSecure() {
        return true;
    }

    /**
     * Returns the authentication scheme used for the request.
     *
     * @return "JWT" indicating token-based authentication
     */
    @Override
    public String getAuthenticationScheme() {
        return "JWT";
    }
}


