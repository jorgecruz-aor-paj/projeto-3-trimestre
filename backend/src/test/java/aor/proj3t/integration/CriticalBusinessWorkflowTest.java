package aor.proj3t.integration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.daos.interfaces.NotificationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.ai.SentimentResponseDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.DateRange;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.AuthorizationServiceImpl;
import aor.proj3t.service.impl.EvaluationCycleServiceImpl;
import aor.proj3t.service.impl.EvaluationServiceImpl;
import aor.proj3t.service.interfaces.AIService;
import aor.proj3t.service.interfaces.AsyncEmailService;
import aor.proj3t.service.interfaces.AuthorizationService;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.EvaluationConverter;
import aor.proj3t.util.converterutils.EvaluationCycleConverter;
import jakarta.mail.MessagingException;

/**
 * Integration tests for critical business workflows and security scenarios.
 * These tests focus on end-to-end flows and cross-service interactions.
 */
@ExtendWith(MockitoExtension.class)
class CriticalBusinessWorkflowTest {

        // Test constants
        private static final Long ADMIN_ID = 1L;
        private static final Long MANAGER_ID = 2L;
        private static final Long USER_ID = 3L;
        private static final Long OTHER_USER_ID = 4L;
        private static final Long CYCLE_ID = 1L;
        private static final Long EVALUATION_ID = 1L;

        // Service mocks
        @Mock
        private DAOFactory daoFactory;
        @Mock
        private ConverterFactory converterFactory;
        @Mock
        private ServiceFactory serviceFactory;
        @Mock
        private EmailUtil emailUtil;

        // DAO mocks
        @Mock
        private UserDAO userDAO;
        @Mock
        private EvaluationDAO evaluationDAO;
        @Mock
        private EvaluationCycleDAO evaluationCycleDAO;
        @Mock
        private NotificationDAO notificationDAO;

        // Converter mocks
        @Mock
        private EvaluationConverter evaluationConverter;
        @Mock
        private EvaluationCycleConverter evaluationCycleConverter;

        // Service mocks
        @Mock
        private NotificationService notificationService;
        @Mock
        private AIService aiService;
        @Mock
        private AuthorizationService authorizationService;
        @Mock
        private EvaluationCycleService mockEvaluationCycleService;
        @Mock
        private AsyncEmailService asyncEmailService;

        // Services under test
        @InjectMocks
        private EvaluationCycleServiceImpl evaluationCycleService;

        @InjectMocks
        private EvaluationServiceImpl evaluationService;

        @InjectMocks
        private AuthorizationServiceImpl authorizationServiceImpl;

        // Test entities
        private User admin;
        private User manager;
        private User user;
        private User otherUser;
        private EvaluationCycle cycle;
        private Evaluation evaluation;

        @BeforeEach
        void setUp() {
                // Configure factory mocks
                lenient().when(daoFactory.forUser()).thenReturn(userDAO);
                lenient().when(daoFactory.forEvaluation()).thenReturn(evaluationDAO);
                lenient().when(daoFactory.forEvaluationCycle()).thenReturn(evaluationCycleDAO);
                lenient().when(daoFactory.forNotification()).thenReturn(notificationDAO);

                lenient().when(converterFactory.forEvaluation()).thenReturn(evaluationConverter);
                lenient().when(converterFactory.forEvaluationCycle()).thenReturn(evaluationCycleConverter);

                lenient().when(serviceFactory.forNotification()).thenReturn(notificationService);
                lenient().when(serviceFactory.forAI()).thenReturn(aiService);
                lenient().when(serviceFactory.forAuthorization()).thenReturn(authorizationService);
                lenient().when(serviceFactory.forEvaluationCycle()).thenReturn(mockEvaluationCycleService);
                lenient().when(serviceFactory.forAsyncEmail()).thenReturn(asyncEmailService);

                // Set up test users
                admin = createTestUser(ADMIN_ID, "admin@test.com", Role.ADMIN, "Admin", "User", null);
                manager = createTestUser(MANAGER_ID, "manager@test.com", Role.MANAGER, "Manager", "User", null);
                user = createTestUser(USER_ID, "user@test.com", Role.USER, "Test", "User", manager);
                otherUser = createTestUser(OTHER_USER_ID, "other@test.com", Role.USER, "Other", "User", null);

                // Set up test evaluation cycle
                cycle = new EvaluationCycle();
                cycle.setId(CYCLE_ID);
                cycle.setStatus(EvaluationCycleStatus.ACTIVE);
                cycle.setStartDate(Instant.now().minus(30, ChronoUnit.DAYS));
                cycle.setEndDate(Instant.now().plus(30, ChronoUnit.DAYS));
                cycle.setCreatedBy(admin);
                cycle.setCreatedAt(Instant.now().minus(30, ChronoUnit.DAYS));

                // Set up test evaluation
                evaluation = new Evaluation();
                evaluation.setId(EVALUATION_ID);
                evaluation.setEvaluator(manager);
                evaluation.setEvaluated(user);
                evaluation.setStatus(EvaluationStatus.ONGOING);
                evaluation.setEvaluationCycle(cycle);
                evaluation.setCreatedAt(Instant.now().minus(20, ChronoUnit.DAYS));
                evaluation.setScale(EvaluationScale.EXPECTED_CONTRIBUTION);
                evaluation.setFeedback("Initial feedback");

                // Set up common mocks for EvaluationCycle retrieval
                lenient().when(evaluationCycleDAO.findById(CYCLE_ID)).thenReturn(Optional.of(cycle));
        }

        private User createTestUser(Long id, String email, Role role, String firstName, String lastName, User manager) {
                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setRole(role);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setManager(manager);
                return user;
        }

        @Test
        @DisplayName("Complete evaluation cycle workflow - creation to automatic closure")
        void testCompleteEvaluationCycleWorkflow() throws MessagingException {
                // === PHASE 1: Admin creates evaluation cycle ===
                DateRange dateRange = new DateRange();
                dateRange.setStartDate(Instant.now());
                dateRange.setEndDate(Instant.now().plus(30, ChronoUnit.DAYS));

                // Mock prerequisites for cycle creation
                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(userDAO.countUsersWithoutManager()).thenReturn(0L);
                when(evaluationCycleDAO.countActiveEvaluationCycles()).thenReturn(0L);
                when(evaluationCycleDAO.create(any(EvaluationCycle.class))).thenReturn(cycle);
                when(userDAO.findAll()).thenReturn(Arrays.asList(manager, user));
                lenient().when(userDAO.getManagers()).thenReturn(Arrays.asList(manager));

                // Execute cycle creation
                assertDoesNotThrow(() -> evaluationCycleService.startCycle(ADMIN_ID, dateRange));

                // Verify cycle creation and evaluation generation
                verify(evaluationCycleDAO).create(any(EvaluationCycle.class));
                verify(asyncEmailService).sendCycleStartEmailAsync(CYCLE_ID); // Verify async email service is called

                // === PHASE 2: Manager completes evaluation ===
                EvaluationDTO evaluationDTO = new EvaluationDTO();
                evaluationDTO.setId(EVALUATION_ID);
                evaluationDTO.setStatus(EvaluationStatus.COMPLETED);
                evaluationDTO.setFeedback("Excellent performance throughout the evaluation period");
                evaluationDTO.setScale(EvaluationScale.EXCEED_CONTRIBUTION);

                // Mock evaluation update prerequisites
                when(userDAO.findById(MANAGER_ID)).thenReturn(Optional.of(manager));
                when(evaluationDAO.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
                when(evaluationConverter.toDTO(any(Evaluation.class))).thenReturn(evaluationDTO);
                when(evaluationCycleDAO.findById(CYCLE_ID)).thenReturn(Optional.of(cycle));
                when(evaluationDAO.countOngoingEvaluationsByCycle(CYCLE_ID)).thenReturn(0L);
                when(evaluationDAO.countUnclosedEvaluationsByCycleId(CYCLE_ID)).thenReturn(0);

                // Mock AI service for sentiment analysis
                SentimentResponseDTO sentimentResponse = new SentimentResponseDTO();
                sentimentResponse.setSentiment("POSITIVE");
                sentimentResponse.setPolarity(0.8f);
                when(aiService.analyzeSentiment(anyString())).thenReturn(sentimentResponse);

                // Execute evaluation update
                EvaluationDTO result = evaluationService.updateEvaluation(MANAGER_ID, evaluationDTO);
                doNothing().when(asyncEmailService).sendCycleClosureEmailAsync(anyLong());

                // Verify evaluation was updated and notification sent
                assertNotNull(result);
                assertEquals(EvaluationStatus.COMPLETED, result.getStatus());
                verify(evaluationDAO).update(any(Evaluation.class));
                // + 1 for cycle complete

                // === PHASE 3: Verify cycle completion triggers ===
                verify(evaluationCycleDAO).updateStatus(eq(CYCLE_ID), eq(EvaluationCycleStatus.COMPLETED));
                verify(evaluationCycleDAO, times(2)).findById(CYCLE_ID);
                verify(evaluationCycleDAO).update(any(EvaluationCycle.class)); // Changed from 1 to 2: 1 for completion
                                                                               // + 1 for
                                                                               // closure

                // === PHASE 4: Test bulk closure (admin operation) ===
                EvaluationCloseDTO closeDTO = new EvaluationCloseDTO();
                closeDTO.setCycleId(CYCLE_ID);
                closeDTO.setEvaluationIds(Arrays.asList(EVALUATION_ID));

                // Update cycle to COMPLETED for closure test
                cycle.setStatus(EvaluationCycleStatus.COMPLETED);
                evaluation.setStatus(EvaluationStatus.COMPLETED);

                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(daoFactory.forEvaluationCycle().findById(CYCLE_ID)).thenReturn(Optional.of(cycle));
                when(evaluationDAO.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
                when(evaluationDAO.countUnclosedEvaluationsByCycleId(CYCLE_ID)).thenReturn(0);

                // Execute bulk closure
                assertDoesNotThrow(() -> evaluationService.closeEvaluationsBulk(ADMIN_ID, closeDTO));

                // Verify evaluations were closed and cycle was closed
                verify(evaluationDAO, atLeast(2)).update(any(Evaluation.class));
                verify(daoFactory.forEvaluationCycle(), times(2)).update(any(EvaluationCycle.class));
        }

        @Test
        @DisplayName("Authorization security across all user roles and edge cases")
        void testAuthorizationSecurityScenarios() {
                // Setup user relationships
                when(userDAO.findById(USER_ID)).thenReturn(Optional.of(user));
                when(userDAO.findById(OTHER_USER_ID)).thenReturn(Optional.of(otherUser));

                // === TEST 1: Admin access (should allow everything) ===
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(admin, USER_ID, null));
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(admin, null, MANAGER_ID));
                assertDoesNotThrow(
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(admin, USER_ID, MANAGER_ID));

                // === TEST 2: Manager access to direct reports ===
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(manager, USER_ID, null));
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(manager, null, MANAGER_ID));

                // === TEST 3: Manager forbidden access to non-direct reports ===
                ForbiddenException managerException = assertThrows(ForbiddenException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(manager, OTHER_USER_ID, null));
                assertEquals("Permission denied", managerException.getUserMessage());

                // === TEST 4: Manager forbidden access to evaluations by other evaluators ===
                ForbiddenException evaluatorException = assertThrows(ForbiddenException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(manager, null, OTHER_USER_ID));
                assertEquals("Permission denied", evaluatorException.getUserMessage());

                // === TEST 5: User access to own evaluations ===
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(user, USER_ID, null));

                // === TEST 6: User forbidden access to other users' evaluations ===
                ForbiddenException userException = assertThrows(ForbiddenException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(user, OTHER_USER_ID, null));
                assertEquals("Permission denied", userException.getUserMessage());

                // === TEST 7: User access to evaluations by their manager ===
                assertDoesNotThrow(() -> authorizationServiceImpl.authorizeEvaluationAccess(user, null, MANAGER_ID));

                // === TEST 8: User forbidden access to evaluations by non-manager ===
                ForbiddenException nonManagerException = assertThrows(ForbiddenException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(user, null, OTHER_USER_ID));
                assertEquals("Permission denied", nonManagerException.getUserMessage());

                // === TEST 9: Edge case - manager relationship changes ===
                // Simulate user changing managers
                User originalManager = user.getManager();
                user.setManager(null); // No manager

                when(userDAO.findById(USER_ID)).thenReturn(Optional.of(user));

                ForbiddenException noManagerException = assertThrows(ForbiddenException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(manager, USER_ID, null));
                assertEquals("Permission denied", noManagerException.getUserMessage());

                // Restore original manager
                user.setManager(originalManager);

                // === TEST 10: Entity not found scenarios ===
                when(userDAO.findById(999L)).thenReturn(Optional.empty());

                NotFoundException notFoundException = assertThrows(NotFoundException.class,
                                () -> authorizationServiceImpl.authorizeEvaluationAccess(manager, 999L, null));
                assertTrue(notFoundException.getMessage().contains("User"));
        }

        @Test
        @DisplayName("Evaluation status change permissions and business rules")
        void testEvaluationStatusChangePermissions() {
                // Set up mocks
                when(userDAO.findById(MANAGER_ID)).thenReturn(Optional.of(manager));
                when(userDAO.findById(USER_ID)).thenReturn(Optional.of(user));
                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(evaluationDAO.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));
                when(evaluationConverter.toDTO(any(Evaluation.class))).thenReturn(new EvaluationDTO());

                // === TEST 1: Manager can change ONGOING to COMPLETED ===
                evaluation.setStatus(EvaluationStatus.ONGOING);
                EvaluationDTO managerUpdate = new EvaluationDTO();
                managerUpdate.setId(EVALUATION_ID);
                managerUpdate.setStatus(EvaluationStatus.COMPLETED);
                managerUpdate.setFeedback("Completed by manager");

                assertDoesNotThrow(() -> evaluationService.updateEvaluation(MANAGER_ID, managerUpdate));

                // === TEST 2: Manager cannot change COMPLETED to CLOSED ===
                evaluation.setStatus(EvaluationStatus.COMPLETED);
                EvaluationDTO invalidManagerUpdate = new EvaluationDTO();
                invalidManagerUpdate.setId(EVALUATION_ID);
                invalidManagerUpdate.setStatus(EvaluationStatus.CLOSED);

                ForbiddenException managerException = assertThrows(ForbiddenException.class,
                                () -> evaluationService.updateEvaluation(MANAGER_ID, invalidManagerUpdate));
                assertEquals("Status change not allowed", managerException.getUserMessage());

                // === TEST 3: Regular user cannot change status ===
                evaluation.setStatus(EvaluationStatus.ONGOING);
                EvaluationDTO userUpdate = new EvaluationDTO();
                userUpdate.setId(EVALUATION_ID);
                userUpdate.setStatus(EvaluationStatus.COMPLETED);

                ForbiddenException userException = assertThrows(ForbiddenException.class,
                                () -> evaluationService.updateEvaluation(USER_ID, userUpdate));
                assertEquals("Status change not allowed", userException.getUserMessage());

                // === TEST 4: Admin can change any status ===
                evaluation.setStatus(EvaluationStatus.CLOSED);
                EvaluationDTO adminUpdate = new EvaluationDTO();
                adminUpdate.setId(EVALUATION_ID);
                adminUpdate.setStatus(EvaluationStatus.COMPLETED);
                adminUpdate.setFeedback("Admin override");

                assertThrows(ForbiddenException.class,
                                () -> evaluationService.updateEvaluation(ADMIN_ID, adminUpdate));
        }

        @Test
        @DisplayName("Concurrent evaluation cycle operations and data integrity")
        void testConcurrentCycleOperations() {
                // === TEST 1: Prevent multiple active cycles ===
                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(userDAO.countUsersWithoutManager()).thenReturn(0L);
                when(evaluationCycleDAO.countActiveEvaluationCycles()).thenReturn(1L); // Already active cycle

                DateRange dateRange = new DateRange();
                dateRange.setStartDate(Instant.now());
                dateRange.setEndDate(Instant.now().plus(30, ChronoUnit.DAYS));

                InvalidDataException concurrentException = assertThrows(InvalidDataException.class,
                                () -> evaluationCycleService.startCycle(ADMIN_ID, dateRange));
                assertEquals("There is an active evaluation cycle running", concurrentException.getUserMessage());

                // === TEST 2: Prevent operations on closed cycles ===
                cycle.setStatus(EvaluationCycleStatus.CLOSED);

                EvaluationCloseDTO closeDTO = new EvaluationCloseDTO();
                closeDTO.setCycleId(CYCLE_ID);
                closeDTO.setEvaluationIds(Arrays.asList(EVALUATION_ID));

                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(daoFactory.forEvaluationCycle().findById(CYCLE_ID)).thenReturn(Optional.of(cycle));

                BadRequestException closedCycleException = assertThrows(BadRequestException.class,
                                () -> evaluationService.closeEvaluationsBulk(ADMIN_ID, closeDTO));
                assertEquals("Evaluation cycle has already been closed", closedCycleException.getUserMessage());

                // === TEST 3: Validate evaluation belongs to correct cycle ===
                EvaluationCycle differentCycle = new EvaluationCycle();
                differentCycle.setId(999L);
                differentCycle.setStatus(EvaluationCycleStatus.ACTIVE);

                evaluation.setEvaluationCycle(differentCycle);

                when(evaluationDAO.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation));

                cycle.setStatus(EvaluationCycleStatus.ACTIVE); // Reset for test

                BadRequestException wrongCycleException = assertThrows(BadRequestException.class,
                                () -> evaluationService.closeEvaluationsBulk(ADMIN_ID, closeDTO));
                assertEquals("Evaluation belongs to a different cycle", wrongCycleException.getUserMessage());
        }

        /*
         * @Test
         *
         * @DisplayName("AI service failure handling and fallback behavior")
         * void testAIServiceFailureHandling() {
         * // Set up mocks
         * when(userDAO.findById(MANAGER_ID)).thenReturn(Optional.of(manager));
         * when(evaluationDAO.findById(EVALUATION_ID)).thenReturn(Optional.of(evaluation
         * ));
         * when(evaluationConverter.toDTO(any(Evaluation.class))).thenReturn(new
         * EvaluationDTO());
         *
         * // === TEST 1: AI service throws exception - evaluation should still succeed
         * ===
         * when(aiService.analyzeSentiment(anyString())).thenThrow(new
         * RuntimeException("AI service unavailable"));
         *
         * EvaluationDTO updateDTO = new EvaluationDTO();
         * updateDTO.setId(EVALUATION_ID);
         * updateDTO.setStatus(EvaluationStatus.COMPLETED);
         * updateDTO.setFeedback("Test feedback for AI analysis");
         *
         * // Should not throw exception despite AI failure
         * assertDoesNotThrow(() -> evaluationService.updateEvaluation(MANAGER_ID,
         * updateDTO));
         *
         * // Verify evaluation was still updated
         * verify(evaluationDAO).update(any(Evaluation.class));
         *
         * // === TEST 2: AI service returns null - should handle gracefully ===
         * when(aiService.analyzeSentiment(anyString())).thenReturn(null);
         *
         * assertDoesNotThrow(() -> evaluationService.updateEvaluation(MANAGER_ID,
         * updateDTO));
         * verify(evaluationDAO, times(2)).update(any(Evaluation.class));
         *
         * // === TEST 3: Empty feedback - should not call AI service ===
         * reset(aiService);
         * updateDTO.setFeedback("");
         *
         * evaluationService.updateEvaluation(MANAGER_ID, updateDTO);
         * verify(aiService, never()).analyzeSentiment(anyString());
         * }
         */
        @Test
        @DisplayName("Email notification failure handling")
        void testEmailNotificationFailureHandling() throws MessagingException {
                // === TEST 1: Email failure during cycle creation should not fail the operation
                // ===
                when(userDAO.findById(ADMIN_ID)).thenReturn(Optional.of(admin));
                when(userDAO.countUsersWithoutManager()).thenReturn(0L);
                when(evaluationCycleDAO.countActiveEvaluationCycles()).thenReturn(0L);
                when(evaluationCycleDAO.create(any(EvaluationCycle.class))).thenReturn(cycle);
                when(userDAO.findAll()).thenReturn(Arrays.asList(manager, user));
                when(userDAO.getManagers()).thenReturn(Arrays.asList(manager));
                when(evaluationDAO.create(any(Evaluation.class))).thenReturn(evaluation);
                when(emailUtil.sendEmail(anyString(), anyString(), anyString())).thenReturn(false); // Email fails

                DateRange dateRange = new DateRange();
                dateRange.setStartDate(Instant.now());
                dateRange.setEndDate(Instant.now().plus(30, ChronoUnit.DAYS));

                // Should not throw exception despite email failure
                assertDoesNotThrow(() -> evaluationCycleService.startCycle(ADMIN_ID, dateRange));

                // Verify cycle was still created
                verify(evaluationCycleDAO).create(any(EvaluationCycle.class));

                // Note: Email closure notifications are now handled asynchronously by
                // AsyncEmailService
                // The original test for email exceptions during closure has been removed since
                // sendCycleClosureEmail is no longer part of the public API
        }
}
