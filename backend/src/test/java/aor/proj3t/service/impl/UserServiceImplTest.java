package aor.proj3t.service.impl;

import aor.proj3t.config.SettingsProvider;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.PendingRegistrationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.daos.interfaces.WorkplaceDAO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Settings;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.FileStorageUtil;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.MiniUserConverter;
import aor.proj3t.util.converterutils.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

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
    private PendingRegistrationDAO pendingRegistrationDAO;

    @Mock
    private WorkplaceDAO workplaceDAO;

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

    @BeforeEach
    public void setup() {
        // Configure mocks that are used across multiple tests
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forPendingRegistration()).thenReturn(pendingRegistrationDAO);
        lenient().when(converterFactory.forUser()).thenReturn(userConverter);
        lenient().when(settingsProvider.getSettings()).thenReturn(settings);
        lenient().when(settings.getConfirmationExpirationTime()).thenReturn(CONFIRMATION_EXPIRATION_TIME);
        lenient().when(daoFactory.forWorkplace()).thenReturn(workplaceDAO);
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
        verify(passwordUtil).hashPassword(TEST_PASSWORD);
        verifyNoMoreInteractions(passwordUtil);
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
    @DisplayName("changePassword should update user password")
    public void testChangePassword() {
        // Arrange
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail(TEST_EMAIL);
        user.setPassword("oldHashedPassword");
        
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(passwordUtil.hashPassword(TEST_PASSWORD)).thenReturn(TEST_HASHED_PASSWORD);
        
        // Act
        userService.changePassword(TEST_USER_ID, TEST_PASSWORD);
        
        // Assert
        assertEquals(TEST_HASHED_PASSWORD, user.getPassword());
        
        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verify(passwordUtil).hashPassword(TEST_PASSWORD);
        verify(userDAO).update(user);
    }

    @Test
    @DisplayName("changePassword should throw NotFoundException when user doesn't exist")
    public void testChangePasswordUserNotFound() {
        // Arrange
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.empty());
        
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> userService.changePassword(TEST_USER_ID, TEST_PASSWORD));
        
        assertTrue(exception.getMessage().contains("not found"));
        
        // Verify
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(passwordUtil);
    }
}
