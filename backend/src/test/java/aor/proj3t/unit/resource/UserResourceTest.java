package aor.proj3t.unit.resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.resource.UserResource;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TokenService;
import aor.proj3t.service.interfaces.UserService;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
public class UserResourceTest {

    private UserResource userResource;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserValidator validator;

    @Mock
    private FilterValidatorFactory validatorFactory;

    private final String TEST_AUTH_HEADER = "Bearer test.jwt.token";
    private final Long TEST_USER_ID = 1L;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_FIRST_NAME = "Test";
    private final String TEST_LAST_NAME = "User";

    @BeforeEach
    public void setup() {
        // Create the resource instance
        userResource = new UserResource();

        // Set mocked dependencies using reflection
        try {
            setField(userResource, "serviceFactory", serviceFactory);
            setField(userResource, "validator", validator);
            setField(userResource, "validatorFactory", validatorFactory);
        } catch (Exception e) {
            fail("Failed to set up mocks: " + e.getMessage());
        }

        // Configure common mock behavior
        when(serviceFactory.forUser()).thenReturn(userService);
        lenient().when(serviceFactory.forToken()).thenReturn(tokenService);
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
    @DisplayName("getMe should return user DTO for authenticated user")
    public void testGetMe() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TEST_USER_ID);
        userDTO.setEmail(TEST_EMAIL);
        userDTO.setFirstName(TEST_FIRST_NAME);
        userDTO.setLastName(TEST_LAST_NAME);

        when(tokenService.getUserIdFromAuthHeader(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
        when(userService.getUser(TEST_USER_ID)).thenReturn(userDTO);

        // Act
        Response response = userResource.getMe(TEST_AUTH_HEADER);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(userDTO, response.getEntity());

        // Verify
        verify(tokenService).getUserIdFromAuthHeader(TEST_AUTH_HEADER);
        verify(userService).getUser(TEST_USER_ID);
    }

    @Test
    @DisplayName("getUsers should return filtered list of users")
    public void testGetUsers() {
        // Arrange
        UserFilter filter = new UserFilter();
        List<UserDTO> userList = new ArrayList<>();

        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setEmail("user1@example.com");

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        userList.add(user1);
        userList.add(user2);

        when(tokenService.getUserIdFromAuthHeader(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
        doNothing().when(validatorFactory).validate(any(UserFilter.class));
        when(userService.getUsersByFilters(TEST_USER_ID, filter)).thenReturn(userList);

        // Act
        Response response = userResource.getUsers(TEST_AUTH_HEADER, filter);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(userList, response.getEntity());

        // Verify
        verify(tokenService).getUserIdFromAuthHeader(TEST_AUTH_HEADER);
        verify(validatorFactory).validate(filter);
        verify(userService).getUsersByFilters(TEST_USER_ID, filter);
    }

    @Test
    @DisplayName("updateUser should update user and return the updated user in the response")
    public void testUpdateUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TEST_USER_ID);
        userDTO.setEmail(TEST_EMAIL);
        userDTO.setFirstName("Updated");
        userDTO.setLastName("Name");

        when(tokenService.getUserIdFromAuthHeader(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
        doNothing().when(validator).validateUserDTO(userDTO);
        doNothing().when(userService).updateUser(TEST_USER_ID, userDTO);

        // Act
        Response response = userResource.updateUser(TEST_AUTH_HEADER, userDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Object entity = response.getEntity();
        assertInstanceOf(UserDTO.class, entity);
        // Verify
        verify(tokenService).getUserIdFromAuthHeader(TEST_AUTH_HEADER);
        verify(validator).validateUserDTO(userDTO);
        verify(userService).updateUser(TEST_USER_ID, userDTO);
    }

    @Test
    @DisplayName("getManagers should return list of managers")
    public void testGetManagers() {
        // Arrange
        List<UserDTO> managerList = new ArrayList<>();

        UserDTO manager1 = new UserDTO();
        manager1.setId(1L);
        manager1.setEmail("manager1@example.com");

        UserDTO manager2 = new UserDTO();
        manager2.setId(2L);
        manager2.setEmail("manager2@example.com");

        managerList.add(manager1);
        managerList.add(manager2);

        when(tokenService.getUserIdFromAuthHeader(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
        when(userService.getAllManagers(TEST_USER_ID)).thenReturn(managerList);

        // Act
        Response response = userResource.getManagers(TEST_AUTH_HEADER);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(managerList, response.getEntity());

        // Verify
        verify(tokenService).getUserIdFromAuthHeader(TEST_AUTH_HEADER);
        verify(userService).getAllManagers(TEST_USER_ID);
    }

    @Test
    @DisplayName("getCompactUsers should return list of mini user DTOs")
    public void testGetCompactUsers() {
        // Arrange
        List<MiniUserDTO> miniUserList = new ArrayList<>();

        MiniUserDTO user1 = new MiniUserDTO();
        user1.setId(1L);
        user1.setFirstName("First1");
        user1.setLastName("Last1");

        MiniUserDTO user2 = new MiniUserDTO();
        user2.setId(2L);
        user2.setFirstName("First2");
        user2.setLastName("Last2");

        miniUserList.add(user1);
        miniUserList.add(user2);

        when(userService.getAllMiniUsers(false)).thenReturn(miniUserList);

        // Act
        Response response = userResource.getCompactUsers(false);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(miniUserList, response.getEntity());

        // Verify
        verify(userService).getAllMiniUsers(false);
    }

    @Test
    @DisplayName("assignManager should assign manager and return success response")
    public void testAssignManager() {
        // Arrange
        ManagerAssignDTO assignDTO = new ManagerAssignDTO();
        assignDTO.setManagerId(2L);
        List<Long> userIds = new ArrayList<>();
        userIds.add(3L);
        userIds.add(4L);
        assignDTO.setUserIds(userIds);

        when(tokenService.getUserIdFromAuthHeader(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
        doNothing().when(validator).validateManagerAssignDTO(assignDTO);
        doNothing().when(userService).assignManagerBulk(TEST_USER_ID, assignDTO);

        // Act
        Response response = userResource.assignManager(TEST_AUTH_HEADER, assignDTO);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Object entity = response.getEntity();
        assertTrue(entity instanceof APIResponseDTO);
        APIResponseDTO apiResponse = (APIResponseDTO) entity;
        assertEquals("Managers assigned successfully", apiResponse.getMessage());

        // Verify
        verify(tokenService).getUserIdFromAuthHeader(TEST_AUTH_HEADER);
        verify(validator).validateManagerAssignDTO(assignDTO);
        verify(userService).assignManagerBulk(TEST_USER_ID, assignDTO);
    }
}
