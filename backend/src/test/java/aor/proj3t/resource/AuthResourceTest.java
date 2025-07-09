package aor.proj3t.resource;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.TokenUtil;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for the AuthResource class.
 * Tests the authentication-related REST endpoints.
 */
@ExtendWith(MockitoExtension.class)
public class AuthResourceTest {

    private AuthResource authResource;
    
    @Mock
    private ServiceFactory serviceFactory;
    
    @Mock
    private UserValidator validator;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private AuthService authService;
    
    @Mock
    private TokenService tokenService;
    
    @Mock
    private HttpHeaders headers;

    private final String TEST_TOKEN = "test.jwt.token";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "Password123!";
    private final String TEST_USER_AGENT = "Mozilla/5.0";

    @BeforeEach
    public void setup() {
        // Create the resource instance
        authResource = new AuthResource();
        
        // Set mocked dependencies using reflection
        try {
            setField(authResource, "serviceFactory", serviceFactory);
            setField(authResource, "validator", validator);
            setField(authResource, "jwtUtil", jwtUtil);
            setField(authResource, "headers", headers);
        } catch (Exception e) {
            fail("Failed to set up mocks: " + e.getMessage());
        }
        
        // Only configure the common mock behavior needed by all tests
        when(serviceFactory.forAuth()).thenReturn(authService);
    }

    /**
     * Helper method to set a field value using reflection
     */
    private void setField(Object object, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    @DisplayName("Login endpoint should return token on successful login")
    public void testLogin() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);
        
        // Specific test setup
        when(headers.getHeaderString("User-Agent")).thenReturn(TEST_USER_AGENT);
        
        // Use lenient() and doReturn() for more flexible stubbing
        lenient().doReturn(TEST_TOKEN).when(authService).login(any(LoginDTO.class), any(), any());
        doNothing().when(validator).validateLoginDTO(any());
        
        // Act
        Response response = authResource.login(loginDTO);
        
        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Response status should be 200 OK");
        assertEquals(TEST_TOKEN, response.getEntity(), "Response entity should be the token");
    }

    @Test
    @DisplayName("Logout endpoint should return success response")
    public void testLogout() {
        // Create a test authHeader
        String authHeader = "Bearer " + TEST_TOKEN;
        
        // Setup for logout test
        when(serviceFactory.forToken()).thenReturn(tokenService);
        
        try (MockedStatic<TokenUtil> tokenUtilMock = mockStatic(TokenUtil.class)) {
            // Mock the static TokenUtil.extractToken method
            tokenUtilMock.when(() -> TokenUtil.extractToken(anyString())).thenReturn(TEST_TOKEN);
            
            // Act
            Response response = authResource.logout(authHeader);
            
            // Assert
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "Response status should be 200 OK");
            
            Object entity = response.getEntity();
            assertTrue(entity instanceof APIResponseDTO, "Response entity should be APIResponseDTO");
            
            APIResponseDTO responseDTO = (APIResponseDTO) entity;
            assertEquals("Logout successful", responseDTO.getMessage(), "Response message should indicate successful logout");
        }
    }
}
