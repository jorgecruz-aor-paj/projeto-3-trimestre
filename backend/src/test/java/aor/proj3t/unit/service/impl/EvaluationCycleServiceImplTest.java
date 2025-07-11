package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.impl.EvaluationCycleDAOimpl;
import aor.proj3t.daos.impl.EvaluationDAOimpl;
import aor.proj3t.daos.impl.UserDAOimpl;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.filters.DateRange;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.EvaluationCycleServiceImpl;
import aor.proj3t.service.interfaces.AsyncEmailService;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.EvaluationCycleConverter;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationCycleServiceImplTest {

    // Test constants
    private final Long TEST_ADMIN_ID = 1L;
    private final Long TEST_USER_ID = 2L;
    private final Long TEST_MANAGER_ID = 3L;
    private final Long TEST_CYCLE_ID = 1L;
    private final String TEST_ADMIN_EMAIL = "admin@test.com";
    private final String TEST_USER_EMAIL = "user@test.com";
    private final String TEST_MANAGER_EMAIL = "manager@test.com";

    @Mock
    private DAOFactory daoFactory;
    @Mock
    private ConverterFactory converterFactory;
    @Mock
    private EmailUtil emailUtil;
    @Mock
    private ServiceFactory serviceFactory;
    @Mock
    private AsyncEmailService asyncEmailService;
    @Mock
    private UserDAOimpl userDAO;
    @Mock
    private EvaluationDAOimpl evaluationDAO;
    @Mock
    private EvaluationCycleDAOimpl evaluationCycleDAO;
    @Mock
    private EvaluationCycleConverter evaluationCycleConverter;

    @InjectMocks
    private EvaluationCycleServiceImpl evaluationCycleService;

    private User adminUser;
    private User regularUser;
    private User managerUser;
    private EvaluationCycle cycle;
    private EvaluationCycleDTO cycleDTO;
    private DateRange dateRange;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();

        // Configure common mocks
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forEvaluation()).thenReturn(evaluationDAO);
        lenient().when(daoFactory.forEvaluationCycle()).thenReturn(evaluationCycleDAO);
        lenient().when(converterFactory.forEvaluationCycle()).thenReturn(evaluationCycleConverter);
        lenient().when(serviceFactory.forAsyncEmail()).thenReturn(asyncEmailService);

        // Set up admin user
        adminUser = new User();
        adminUser.setId(TEST_ADMIN_ID);
        adminUser.setRole(Role.ADMIN);
        adminUser.setEmail(TEST_ADMIN_EMAIL);

        // Set up regular user
        regularUser = new User();
        regularUser.setId(TEST_USER_ID);
        regularUser.setRole(Role.USER);
        regularUser.setEmail(TEST_USER_EMAIL);

        // Set up manager user
        managerUser = new User();
        managerUser.setId(TEST_MANAGER_ID);
        managerUser.setRole(Role.MANAGER);
        managerUser.setEmail(TEST_MANAGER_EMAIL);

        // Set up date range
        dateRange = new DateRange();
        dateRange.setStartDate(now);
        dateRange.setEndDate(now.plus(30, ChronoUnit.DAYS));

        // Set up evaluation cycle
        cycle = new EvaluationCycle();
        cycle.setId(TEST_CYCLE_ID);
        cycle.setCreatedBy(adminUser);
        cycle.setStartDate(dateRange.getStartDate());
        cycle.setEndDate(dateRange.getEndDate());
        cycle.setStatus(EvaluationCycleStatus.ACTIVE);
        cycle.setCreatedAt(now);

        // Set up cycle DTO
        cycleDTO = new EvaluationCycleDTO();
        cycleDTO.setId(cycle.getId());
        cycleDTO.setCreatedById(adminUser.getId());
        cycleDTO.setStartDate(cycle.getStartDate());
        cycleDTO.setEndDate(cycle.getEndDate());
        cycleDTO.setStatus(cycle.getStatus());
    }

    @Test
    @DisplayName("startCycle should create cycle and send notifications when admin creates cycle with valid data")
    void startCycle_Success() throws MessagingException {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(userDAO.countUsersWithoutManager()).thenReturn(0L);
        when(evaluationCycleDAO.countActiveEvaluationCycles()).thenReturn(0L);

        // Mock the cycle creation to return a cycle with an ID
        EvaluationCycle createdCycle = new EvaluationCycle();
        createdCycle.setId(TEST_CYCLE_ID);
        when(evaluationCycleDAO.create(any(EvaluationCycle.class))).thenReturn(createdCycle);

        List<User> allUsers = new ArrayList<>();
        allUsers.add(regularUser);
        when(userDAO.findAll()).thenReturn(allUsers);

        // Create a mock Evaluation for the return value
        Evaluation mockEvaluation = new Evaluation();
        mockEvaluation.setId(1L);
        when(evaluationDAO.create(any(Evaluation.class))).thenReturn(mockEvaluation);

        // Execute
        evaluationCycleService.startCycle(adminUser.getId(), dateRange);

        // Verify
        verify(evaluationCycleDAO).create(any(EvaluationCycle.class));
        verify(evaluationDAO).create(any(Evaluation.class));
        verify(asyncEmailService).sendCycleStartEmailAsync(TEST_CYCLE_ID);
        verify(userDAO).findAll();
    }

    @Test
    @DisplayName("startCycle should throw ForbiddenException when non-admin user tries to create cycle")
    void startCycle_NotAdmin_ThrowsForbiddenException() {
        // Set up
        when(userDAO.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        // Execute & Verify
        assertThrows(ForbiddenException.class,
                () -> evaluationCycleService.startCycle(regularUser.getId(), dateRange));
    }

    @Test
    @DisplayName("startCycle should throw InvalidDataException when there are users without managers")
    void startCycle_UsersWithoutManager_ThrowsInvalidDataException() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(userDAO.countUsersWithoutManager()).thenReturn(1L);

        // Execute & Verify
        assertThrows(InvalidDataException.class,
                () -> evaluationCycleService.startCycle(adminUser.getId(), dateRange));
    }

    @Test
    @DisplayName("startCycle should throw InvalidDataException when there is already an active cycle")
    void startCycle_ActiveCycleExists_ThrowsInvalidDataException() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(userDAO.countUsersWithoutManager()).thenReturn(0L);
        when(evaluationCycleDAO.countActiveEvaluationCycles()).thenReturn(1L);

        // Execute & Verify
        assertThrows(InvalidDataException.class,
                () -> evaluationCycleService.startCycle(adminUser.getId(), dateRange));
    }

    @Test
    @DisplayName("getActiveCycle should return cycle when one exists and requester is valid")
    void getActiveCycle_Success() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(evaluationCycleDAO.findActive(true)).thenReturn(List.of(cycle));
        when(evaluationCycleConverter.toDTO(cycle)).thenReturn(cycleDTO);

        // Execute
        EvaluationCycleDTO result = evaluationCycleService.getActiveCycle(adminUser.getId());

        // Verify
        assertNotNull(result);
        assertEquals(cycle.getId(), result.getId());
        assertEquals(cycle.getStartDate(), result.getStartDate());
        assertEquals(cycle.getEndDate(), result.getEndDate());
        assertEquals(cycle.getStatus(), result.getStatus());
        verify(evaluationCycleConverter).toDTO(cycle);
    }

    @Test
    @DisplayName("getActiveCycle should return empty DTO when no active cycle exists")
    void getActiveCycle_NoActiveCycle_ReturnsEmptyDTO() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(evaluationCycleDAO.findActive(true)).thenReturn(new ArrayList<>());

        // Execute
        EvaluationCycleDTO result = evaluationCycleService.getActiveCycle(adminUser.getId());

        // Verify
        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("getActiveCycle should throw ConflictException when multiple active cycles exist")
    void getActiveCycle_MultipleActiveCycles_ThrowsConflictException() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));

        EvaluationCycle cycle2 = new EvaluationCycle();
        cycle2.setId(2L);
        when(evaluationCycleDAO.findActive(true)).thenReturn(List.of(cycle, cycle2));

        // Execute & Verify
        assertThrows(ConflictException.class,
                () -> evaluationCycleService.getActiveCycle(adminUser.getId()));
    }

    @Test
    @DisplayName("getAllCycles should return list of cycles when requester is admin")
    void getAllCycles_Success() {
        // Set up
        when(userDAO.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(evaluationCycleDAO.findAll(true)).thenReturn(List.of(cycle));
        when(evaluationCycleConverter.toDTOList(anyList())).thenReturn(List.of(cycleDTO));

        // Execute
        List<EvaluationCycleDTO> result = evaluationCycleService.getAllCycles(adminUser.getId(), true);

        // Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(cycle.getId(), result.get(0).getId());
        verify(evaluationCycleConverter).toDTOList(anyList());
    }

    @Test
    @DisplayName("getAllCycles should throw ForbiddenException when requester is not admin")
    void getAllCycles_NotAdmin_ThrowsForbiddenException() {
        // Set up
        when(userDAO.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        // Execute & Verify
        assertThrows(ForbiddenException.class,
                () -> evaluationCycleService.getAllCycles(regularUser.getId(), true));
    }

    @Test
    @DisplayName("sendCycleClosureEmail should send emails to all users when cycle is valid")
    void sendCycleClosureEmail_Success() throws MessagingException {
        // Set up
        List<User> allUsers = List.of(adminUser, regularUser, managerUser);
        when(userDAO.findAll()).thenReturn(allUsers);
        when(emailUtil.createEmailTemplate(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("<html>Mock email body</html>");
        when(emailUtil.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
        cycle.setClosureDate(now.plus(1, ChronoUnit.DAYS));

        // Execute
        evaluationCycleService.sendCycleClosureEmail(cycle);

        // Verify
        verify(emailUtil, times(allUsers.size())).sendEmail(anyString(), anyString(), anyString());
        verify(emailUtil, times(allUsers.size())).createEmailTemplate(anyString(), anyString(), anyString(),
                anyString(), anyString());
    }

    @Test
    @DisplayName("sendCycleClosureEmail should throw RuntimeException when email sending fails")
    void sendCycleClosureEmail_WithEmailError_ThrowsRuntimeException() {
        // Set up
        List<User> allUsers = List.of(adminUser, regularUser, managerUser);
        when(userDAO.findAll()).thenReturn(allUsers);

        // Ensure cycle has proper dates
        cycle.setStartDate(now);
        cycle.setEndDate(now.plus(30, ChronoUnit.DAYS));
        cycle.setClosureDate(now.plus(60, ChronoUnit.DAYS));
        cycle.setStatus(EvaluationCycleStatus.COMPLETED);

        // Mock email template creation to return a valid HTML body
        when(emailUtil.createEmailTemplate(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("<html>Mock email body</html>");

        // Mock email sending to throw RuntimeException
        doThrow(new RuntimeException("Email error"))
                .when(emailUtil)
                .sendEmail(anyString(), anyString(), anyString());

        // Execute & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> evaluationCycleService.sendCycleClosureEmail(cycle));

        assertEquals("Email error", exception.getMessage());

        // Verify that email template creation and sending were attempted
        verify(emailUtil).createEmailTemplate(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(emailUtil).sendEmail(anyString(), anyString(), anyString());
    }
}
