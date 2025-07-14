package aor.proj3t.unit.service.impl;

import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.*;
import aor.proj3t.dto.*;
import aor.proj3t.entities.*;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.service.impl.UserServiceImpl;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.FileStorageUtil;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.MiniUserConverter;
import aor.proj3t.util.converterutils.UserConverter;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private UserDAO userDAO;

    @Mock
    private EvaluationDAO evaluationDAO;

    @Mock
    private UserValidator userValidator;


    @Mock
    private PendingRegistrationDAO pendingRegistrationDAO;

    @Mock
    private WorkplaceDAO workplaceDAO;

    @Mock
    private ResetPasswordDAO resetPasswordDAO;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private UserConverter userConverter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private FileStorageUtil fileStorageUtil;

    @Mock
    private SettingsProvider settingsProvider;

    @Mock
    private MiniUserConverter miniUserConverter;

    @Mock
    private Settings settings;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "Password123!";
    private final String TEST_HASHED_PASSWORD = "hashedPassword";
    private final String TEST_TOKEN = "test.jwt.token";
    private final String TEST_IP = "127.0.0.1";
    private final Long TEST_USER_ID = 1L;
    private final Integer CONFIRMATION_EXPIRATION_TIME = 60;
    private static final Long TEST_WORKPLACE_ID = 1L;

    @BeforeEach
    public void setup() {
        // Configure mocks that are used across multiple tests
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forPendingRegistration()).thenReturn(pendingRegistrationDAO);
        lenient().when(converterFactory.forUser()).thenReturn(userConverter);
        lenient().when(settingsProvider.getSettings()).thenReturn(settings);
        lenient().when(settings.getConfirmationExpirationTime()).thenReturn(CONFIRMATION_EXPIRATION_TIME);
        lenient().when(daoFactory.forWorkplace()).thenReturn(workplaceDAO);
        lenient().when(daoFactory.forEvaluation()).thenReturn(evaluationDAO);
        lenient().when(evaluationDAO.findByEvaluatedAndStatusIn(any(User.class), anyList()))
                .thenReturn(Collections.emptyList()); // or a list of mock evaluations
        lenient().when(daoFactory.forResetPassword()).thenReturn(resetPasswordDAO);
        lenient().doNothing().when(userValidator).validateEmail(TEST_EMAIL);


    }

    @Test
    @DisplayName("getUser should return user DTO when user exists")
    public void testGetUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(TEST_USER_ID);
        mockUser.setEmail(TEST_EMAIL);

        UserDTO mockUserDto = new UserDTO();
        mockUserDto.setId(TEST_USER_ID);
        mockUserDto.setEmail(TEST_EMAIL);

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(mockUser));
        when(userConverter.toDTO(mockUser)).thenReturn(mockUserDto);

        // Act
        UserDTO result = userService.getUser(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getId());
        assertEquals(TEST_EMAIL, result.getEmail());

        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verify(userConverter).toDTO(mockUser);
    }

    @Test
    @DisplayName("getUser should throw NotFoundException when user doesn't exist")
    public void testGetUserNotFound() {
        // Arrange
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUser(TEST_USER_ID));

        assertEquals("User not found with ID: 1", exception.getMessage());

        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(userConverter);
    }

    @Test
    @DisplayName("registerUser should create pending registration and send confirmation email")
    public void testRegisterUser() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);

        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(pendingRegistrationDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(passwordUtil.hashPassword(TEST_PASSWORD)).thenReturn(TEST_HASHED_PASSWORD);
        when(jwtUtil.generateConfirmationToken(eq(TEST_EMAIL), eq(TEST_IP), eq(CONFIRMATION_EXPIRATION_TIME)))
                .thenReturn(TEST_TOKEN);
        when(pendingRegistrationDAO.create(any(PendingRegistration.class)))
                .thenReturn(new PendingRegistration());
        when(emailUtil.sendConfirmationEmail(anyString(), anyString(), anyString()))
                .thenReturn(true);

        // Act
        userService.registerUser(loginDTO, TEST_IP);

        // Verify
        verify(userDAO).findByEmail(TEST_EMAIL);
        verify(pendingRegistrationDAO).findByEmail(TEST_EMAIL);
        verify(passwordUtil).hashPassword(TEST_PASSWORD);
        verify(jwtUtil, times(2)).generateConfirmationToken(eq(TEST_EMAIL), eq(TEST_IP), eq(CONFIRMATION_EXPIRATION_TIME));
        verify(pendingRegistrationDAO).create(any(PendingRegistration.class));
        verify(emailUtil).sendConfirmationEmail(eq(TEST_EMAIL), eq(TEST_TOKEN), anyString());
    }

    @Test
    @DisplayName("registerUser should throw ConflictException when email is already in use")
    public void testRegisterUserEmailAlreadyInUse() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);

        User existingUser = new User();
        existingUser.setEmail(TEST_EMAIL);

        when(userDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class,
                () -> userService.registerUser(loginDTO, TEST_IP));

        assertTrue(exception.getMessage().toLowerCase().contains("email"));

        // Verify only the interactions that actually happen
        verify(userDAO).findByEmail(TEST_EMAIL);
        verify(pendingRegistrationDAO).deleteExpired(); // this is the only interaction with this DAO
        verifyNoMoreInteractions(userDAO);
        verifyNoMoreInteractions(pendingRegistrationDAO);
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(emailUtil);
    }

    @Test
    @DisplayName("confirmUser should create user from pending registration")
    public void testConfirmUser() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setFirstName("Test");
        registrationDTO.setLastName("User");
        registrationDTO.setWorkplaceId(1L); // Add required workplaceId

        PendingRegistration pendingRegistration = new PendingRegistration();
        pendingRegistration.setId(1L); // Set ID for deleteById
        pendingRegistration.setEmail(TEST_EMAIL);
        pendingRegistration.setPassword(TEST_HASHED_PASSWORD);
        pendingRegistration.setExpirationDate(Instant.now().plusSeconds(3600));

        when(pendingRegistrationDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(pendingRegistration));
        when(daoFactory.forWorkplace().findById(anyLong())).thenReturn(Optional.of(mock(aor.proj3t.entities.Workplace.class)));

        // Act
        userService.confirmUser(TEST_EMAIL, registrationDTO);

        // Verify
        verify(pendingRegistrationDAO).findByEmail(TEST_EMAIL);
        verify(userDAO).create(any(User.class));
        verify(pendingRegistrationDAO).deleteById(pendingRegistration.getId());
    }

    @Test
    @DisplayName("confirmUser should throw NotFoundException when pending registration doesn't exist")
    public void testConfirmUserPendingRegistrationNotFound() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setFirstName("Test");
        registrationDTO.setLastName("User");

        when(pendingRegistrationDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.confirmUser(TEST_EMAIL, registrationDTO));

        assertTrue(exception.getMessage().contains("No pending registration found"));

        // Verify
        verify(pendingRegistrationDAO).findByEmail(TEST_EMAIL);
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("changePassword should update password and delete reset token")
    public void testChangePassword() {
        // Arrange
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setId(123L);
        resetPassword.setResetPasswordToken("validToken");

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail(TEST_EMAIL);
        user.setPassword("oldHashedPassword");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword(TEST_PASSWORD);

        when(jwtUtil.validateToken("validToken")).thenReturn(true);
        when(jwtUtil.extractAllClaims("validToken")).thenReturn(new DefaultClaims(Map.of(
                "userId", String.valueOf(TEST_USER_ID)
        )));
        when(daoFactory.forResetPassword().findByToken("validToken")).thenReturn(Optional.of(resetPassword));
        when(daoFactory.forUser().findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(passwordUtil.hashPassword(TEST_PASSWORD)).thenReturn(TEST_HASHED_PASSWORD);

        // Act
        userService.changePassword("validToken", loginDTO);

        // Assert
        assertEquals(TEST_HASHED_PASSWORD, user.getPassword());
        verify(daoFactory.forUser()).update(user);
        verify(daoFactory.forResetPassword()).deleteById(resetPassword.getId());
    }

    @Test
    @DisplayName("updateUser should update user fields and return updated user")
    public void testUpdateUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(TEST_USER_ID);
        existingUser.setEmail(TEST_EMAIL);
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");
        existingUser.setRole(Role.USER);

        Workplace workplace = new Workplace();
        workplace.setId(TEST_WORKPLACE_ID);
        existingUser.setWorkplace(workplace);

        // Mock workplaceDAO to return the workplace when looked up by ID
        when(workplaceDAO.findById(TEST_WORKPLACE_ID)).thenReturn(Optional.of(workplace));

        UserDTO updateDto = new UserDTO();
        updateDto.setId(TEST_USER_ID);
        updateDto.setEmail(TEST_EMAIL);
        updateDto.setFirstName("New");
        updateDto.setLastName("Name");

        // Set a non-null WorkplaceDTO on the DTO
        WorkplaceDTO workplaceDto = new WorkplaceDTO();
        workplaceDto.setId(TEST_WORKPLACE_ID);
        updateDto.setWorkplace(workplaceDto);

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(existingUser));
        when(userDAO.update(existingUser)).thenReturn(existingUser);

        // Act
        userService.updateUser(TEST_USER_ID, updateDto);

        // Verify
        verify(userDAO, times(2)).findById(TEST_USER_ID); // Adjust if your service calls it more/less
        verify(userDAO).update(existingUser);
    }

    @Test
    @DisplayName("updateUser should throw NotFoundException when user doesn't exist")
    public void testUpdateUserNotFound() {
        // Arrange
        UserDTO updateDto = new UserDTO();
        updateDto.setId(TEST_USER_ID);
        updateDto.setEmail(TEST_EMAIL);

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(TEST_USER_ID, updateDto));

        assertTrue(exception.getMessage().contains("not found"));

        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(userConverter);
    }

    @Test
    @DisplayName("getAllManagers should return list of manager users")
    public void testGetAllManagers() {
        // Arrange
        User manager1 = new User();
        manager1.setId(1L);
        manager1.setEmail("manager1@example.com");
        manager1.setRole(Role.MANAGER);

        User manager2 = new User();
        manager2.setId(2L);
        manager2.setEmail("manager2@example.com");
        manager2.setRole(Role.MANAGER);

        List<User> managers = Arrays.asList(manager1, manager2);
        List<UserDTO> managerDtos = Arrays.asList(
                createManagerDTO(1L, "manager1@example.com"),
                createManagerDTO(2L, "manager2@example.com")
        );

        // Mock the requesting user with ADMIN role
        User requestingUser = new User();
        requestingUser.setId(TEST_USER_ID);
        requestingUser.setEmail("requestinguser@example.com");
        requestingUser.setRole(Role.ADMIN);

        // Setup mocks with lenient() to avoid UnnecessaryStubbingException
        lenient().when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(requestingUser));
        lenient().when(userDAO.getManagers()).thenReturn(managers);
        lenient().when(userConverter.toDTOList(managers)).thenReturn(managerDtos);

        // Act
        List<UserDTO> result = userService.getAllManagers(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("manager1@example.com", result.get(0).getEmail());
        assertEquals("manager2@example.com", result.get(1).getEmail());
        assertEquals(Role.MANAGER, result.get(0).getRole());
        assertEquals(Role.MANAGER, result.get(1).getRole());

        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verify(userDAO).getManagers();
        verify(userConverter).toDTOList(managers);
    }

    private UserDTO createManagerDTO(Long id, String email) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setEmail(email);
        dto.setRole(Role.MANAGER);
        return dto;
    }

    @Test
    @DisplayName("confirmUser should throw NotFoundException when workplace doesn't exist")
    public void testConfirmUserWorkplaceNotFound() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setFirstName("Test");
        registrationDTO.setLastName("User");
        registrationDTO.setWorkplaceId(999L);

        PendingRegistration pendingRegistration = new PendingRegistration();
        pendingRegistration.setId(1L);
        pendingRegistration.setEmail(TEST_EMAIL);
        pendingRegistration.setPassword(TEST_HASHED_PASSWORD);
        pendingRegistration.setExpirationDate(Instant.now().plusSeconds(3600));

        when(pendingRegistrationDAO.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(pendingRegistration));
        when(daoFactory.forWorkplace().findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.confirmUser(TEST_EMAIL, registrationDTO));

        assertTrue(exception.getMessage().contains("not found"));

        // Verify
        verify(pendingRegistrationDAO).findByEmail(TEST_EMAIL);
        verify(daoFactory.forWorkplace()).findById(999L);
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("getAllMiniUsers should return list of mini user DTOs")
    public void testGetAllMiniUsers() {
        // Arrange
        List<User> users = Arrays.asList(
                mockUser(1L, "First1", "Last1"),
                mockUser(2L, "First2", "Last2")
        );

        MiniUserDTO miniUserDto1 = new MiniUserDTO();
        miniUserDto1.setId(1L);
        miniUserDto1.setFirstName("First1");
        miniUserDto1.setLastName("Last1");

        MiniUserDTO miniUserDto2 = new MiniUserDTO();
        miniUserDto2.setId(2L);
        miniUserDto2.setFirstName("First2");
        miniUserDto2.setLastName("Last2");

        when(userDAO.findAll()).thenReturn(users);
        List<MiniUserDTO> miniUsers = Arrays.asList(miniUserDto1, miniUserDto2);
        when(miniUserConverter.toMiniUserDTOList(users)).thenReturn(miniUsers);

        // Act
        List<MiniUserDTO> result = userService.getAllMiniUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("First1", result.get(0).getFirstName());
        assertEquals("Last1", result.get(0).getLastName());
        assertEquals("First2", result.get(1).getFirstName());
        assertEquals("Last2", result.get(1).getLastName());

        // Verify
        verify(userDAO).findAll();
        verify(miniUserConverter).toMiniUserDTOList(users);
        verifyNoMoreInteractions(userDAO);
    }

    private User mockUser(Long id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    @Test
    @DisplayName("assignManagerBulk should assign manager to multiple users")
    public void testAssignManagerBulk() {
        // Arrange
        Long managerId = 2L;
        List<Long> userIds = Arrays.asList(3L, 4L);
        ManagerAssignDTO assignDTO = new ManagerAssignDTO();
        assignDTO.setManagerId(managerId);
        assignDTO.setUserIds(userIds);

        User admin = new User();
        admin.setId(TEST_USER_ID);
        admin.setRole(Role.ADMIN);

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(admin));

        User manager = new User();
        manager.setId(managerId);
        manager.setRole(Role.MANAGER);

        User user1 = new User();
        user1.setId(3L);
        User user2 = new User();
        user2.setId(4L);

        when(userDAO.findById(managerId)).thenReturn(Optional.of(manager));
        when(userDAO.findById(3L)).thenReturn(Optional.of(user1));
        when(userDAO.findById(4L)).thenReturn(Optional.of(user2));

        // Act
        userService.assignManagerBulk(TEST_USER_ID, assignDTO);

        // Verify
        verify(userDAO).findById(managerId);
        verify(userDAO).findById(3L);
        verify(userDAO).findById(4L);
        verify(userDAO).update(user1);
        verify(userDAO).update(user2);
        assertEquals(manager, user1.getManager());
        assertEquals(manager, user2.getManager());
    }

    @Test
    @DisplayName("assignManagerBulk should throw NotFoundException when manager doesn't exist")
    public void testAssignManagerBulkManagerNotFound() {
        // Arrange
        UserDTO updateDto = new UserDTO();
        updateDto.setId(TEST_USER_ID);
        updateDto.setEmail(TEST_EMAIL);
        updateDto.setFirstName("New");
        updateDto.setLastName("Name");

// ✅ Add this to prevent NullPointerException
        WorkplaceDTO workplaceDto = new WorkplaceDTO();
        workplaceDto.setId(TEST_WORKPLACE_ID);
        updateDto.setWorkplace(workplaceDto);


        Long managerId = 2L;
        List<Long> userIds = Arrays.asList(3L, 4L);
        ManagerAssignDTO assignDTO = new ManagerAssignDTO();
        assignDTO.setManagerId(managerId);
        assignDTO.setUserIds(userIds);

        when(userDAO.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.assignManagerBulk(TEST_USER_ID, assignDTO));

        assertTrue(exception.getMessage().contains("not found with ID"));

        // Verify
        verify(userDAO).findById(1L);
        verifyNoMoreInteractions(userDAO);
    }

}
