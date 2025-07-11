package aor.proj3t.unit.util.authutils;

import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.impl.TokenServiceImpl;
import aor.proj3t.util.authutils.JwtAuthFilter;
import aor.proj3t.util.authutils.Secured;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private TokenServiceImpl tokenServiceImpl;

    @Mock
    private ResourceInfo resourceInfo;

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private Method resourceMethod;

    @Mock
    private UriInfo uriInfo;

    private static final String AUTH_HEADER = "Bearer mock.jwt.token";
    private static final Long USER_ID = 1L;
    private static final Role USER_ROLE = Role.USER;

    @BeforeEach
    public void setup() {
        lenient().when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(AUTH_HEADER);
        lenient().when(tokenServiceImpl.getRoleFromAuthHeader(AUTH_HEADER)).thenReturn(USER_ROLE);
        lenient().when(tokenServiceImpl.getUserIdFromAuthHeader(AUTH_HEADER)).thenReturn(USER_ID);
        lenient().when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
        lenient().when(requestContext.getUriInfo()).thenReturn(uriInfo);
        lenient().when(requestContext.getMethod()).thenReturn("GET");
        lenient().when(uriInfo.getPath()).thenReturn("/secure-endpoint");
    }

    @Test
    @DisplayName("Should allow access when role is permitted by @Secured")
    public void testAllowedAccess() {
        Secured securedMock = mock(Secured.class);
        when(securedMock.value()).thenReturn(new Role[]{Role.USER}); // Changed to USER to match USER_ROLE
        when(resourceMethod.getAnnotation(Secured.class)).thenReturn(securedMock);

        jwtAuthFilter.filter(requestContext);

        verify(requestContext).setSecurityContext(any(SecurityContext.class));
    }

    @Test
    @DisplayName("Should deny access when role is not permitted")
    public void testForbiddenAccess() {
        Secured securedMock = mock(Secured.class);
        when(securedMock.value()).thenReturn(new Role[]{Role.ADMIN}); // USER not allowed
        when(resourceMethod.getAnnotation(Secured.class)).thenReturn(securedMock);

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> jwtAuthFilter.filter(requestContext));

        assertTrue(exception.getMessage().contains("Unauthorized access attempt"));
    }

    @Test
    @DisplayName("Should allow access when no @Secured annotation is present")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void testNoSecuredAnnotation() {
        when(resourceMethod.getAnnotation(Secured.class)).thenReturn(null);
        
        // Use a real class that doesn't have @Secured annotation
        when(resourceInfo.getResourceClass()).thenReturn((Class) String.class);
        
        jwtAuthFilter.filter(requestContext);

        verify(requestContext).setSecurityContext(any(SecurityContext.class));
    }
}

