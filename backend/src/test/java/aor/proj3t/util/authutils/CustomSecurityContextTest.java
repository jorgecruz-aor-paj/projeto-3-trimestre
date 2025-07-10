package aor.proj3t.util.authutils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomSecurityContextTest {

    private CustomSecurityContext securityContext;

    private final String USER_ID = "42";
    private final String ROLE = "ADMIN";

    @BeforeEach
    public void setup() {
        securityContext = new CustomSecurityContext(USER_ID, ROLE);
    }

    @Test
    @DisplayName("Should return correct user principal")
    public void testGetUserPrincipal() {
        Principal principal = securityContext.getUserPrincipal();
        assertNotNull(principal);
        assertEquals(USER_ID, principal.getName());
    }

    @Test
    @DisplayName("Should confirm user is in correct role")
    public void testIsUserInRoleValid() {
        assertTrue(securityContext.isUserInRole("ADMIN"));
    }

    @Test
    @DisplayName("Should reject incorrect role")
    public void testIsUserInRoleInvalid() {
        assertFalse(securityContext.isUserInRole("USER"));
    }

    @Test
    @DisplayName("Should handle unknown role gracefully")
    public void testIsUserInRoleMalformed() {
        assertFalse(securityContext.isUserInRole("NON_EXISTENT_ROLE"));
    }

    @Test
    @DisplayName("Should report as secure")
    public void testIsSecure() {
        assertTrue(securityContext.isSecure());
    }

    @Test
    @DisplayName("Should return JWT as authentication scheme")
    public void testGetAuthenticationScheme() {
        assertEquals("JWT", securityContext.getAuthenticationScheme());
    }
}

