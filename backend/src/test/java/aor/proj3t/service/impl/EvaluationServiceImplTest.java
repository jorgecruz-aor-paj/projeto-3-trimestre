package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.MiniEvaluationCycleDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.ai.SentimentResponseDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationScale;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.service.interfaces.AuthorizationService;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.AIService;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.EvaluationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceImplTest {

    // Test constants
    private final Long TEST_EVALUATOR_ID = 1L;
    private final Long TEST_EVALUATED_ID = 2L;
    private final Long TEST_EVALUATION_ID = 1L;
    private final EvaluationScale TEST_SCALE = EvaluationScale.EXCEED_CONTRIBUTION;
    private final String TEST_FEEDBACK = "Great performance and dedication";

    @Mock
    private DAOFactory daoFactory;
    
    @Mock
    private ConverterFactory converterFactory;
    
    @Mock
    private ServiceFactory serviceFactory;
    
    @Mock
    private EvaluationDAO evaluationDAO;
    
    @Mock
    private UserDAO userDAO;
    
    @Mock
    private EvaluationConverter evaluationConverter;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private AIService aiService;

    @InjectMocks
    private EvaluationServiceImpl evaluationService;

    @InjectMocks
    private EvaluationCycleServiceImpl evaluationCycleService;

    private User evaluator;
    private User evaluated;
    private Evaluation evaluation;
    private EvaluationDTO evaluationDTO;
    private EvaluationCycle cycle;

    @BeforeEach
    void setUp() {
        // Configure common mocks
        lenient().when(daoFactory.forEvaluation()).thenReturn(evaluationDAO);
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(converterFactory.forEvaluation()).thenReturn(evaluationConverter);
        lenient().when(serviceFactory.forNotification()).thenReturn(notificationService);
        lenient().when(serviceFactory.forAI()).thenReturn(aiService);
        lenient().when(serviceFactory.forEvaluationCycle()).thenReturn(evaluationCycleService);  // Add this line



        // Set up evaluator (manager)
        evaluator = new User();
        evaluator.setId(TEST_EVALUATOR_ID);
        evaluator.setRole(Role.MANAGER);
        evaluator.setFirstName("John");
        evaluator.setLastName("Doe");

        // Set up evaluated (user)
        evaluated = new User();
        evaluated.setId(TEST_EVALUATED_ID);
        evaluated.setRole(Role.USER);
        evaluated.setFirstName("Jane");
        evaluated.setLastName("Smith");
        evaluated.setManager(evaluator);

        // Set up evaluation
        evaluation = new Evaluation();
        evaluation.setId(TEST_EVALUATION_ID);
        evaluation.setEvaluator(evaluator);
        evaluation.setEvaluated(evaluated);
        evaluation.setScale(TEST_SCALE);
        evaluation.setFeedback(TEST_FEEDBACK);
        evaluation.setStatus(EvaluationStatus.ONGOING);
        evaluation.setCreatedAt(Instant.now());

        // Set up evaluation cycle
        cycle = new EvaluationCycle();
        cycle.setId(1L);
        cycle.setStatus(EvaluationCycleStatus.ACTIVE);
        cycle.setStartDate(Instant.now());
        cycle.setEndDate(Instant.now().plusSeconds(86400)); // 24 hours later
        evaluation.setEvaluationCycle(cycle);

        lenient().when(daoFactory.forEvaluationCycle()).thenReturn(evaluationCycleDAO);
        lenient().when(evaluationCycleDAO.findById(cycle.getId())).thenReturn(Optional.of(cycle));

        // Set up mini DTOs
        MiniUserDTO evaluatorDTO = new MiniUserDTO(evaluator.getId(), evaluator.getFirstName(), 
            evaluator.getLastName(), evaluator.getEmail(), null);
        MiniUserDTO evaluatedDTO = new MiniUserDTO(evaluated.getId(), evaluated.getFirstName(), 
            evaluated.getLastName(), evaluated.getEmail(), null);
        MiniEvaluationCycleDTO cycleDTO = new MiniEvaluationCycleDTO();
        cycleDTO.setId(cycle.getId());

        // Set up evaluation DTO
        evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(TEST_EVALUATION_ID);
        evaluationDTO.setEvaluator(evaluatorDTO);
        evaluationDTO.setEvaluatee(evaluatedDTO);
        evaluationDTO.setScale(TEST_SCALE);
        evaluationDTO.setFeedback(TEST_FEEDBACK);
        evaluationDTO.setStatus(EvaluationStatus.ONGOING);
        evaluationDTO.setEvaluationCycle(cycleDTO);
    }

    @Mock
    private EvaluationCycleDAO evaluationCycleDAO;  // Add this mock

    @Test
    @DisplayName("updateEvaluation should update evaluation and notify user when status is completed")
    void updateEvaluation_WhenStatusCompleted_ShouldUpdateAndNotify() {
        // Given
        evaluationDTO.setStatus(EvaluationStatus.CLOSED);
        when(evaluationDAO.findById(TEST_EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        when(evaluationConverter.toDTO(any(Evaluation.class))).thenReturn(evaluationDTO);

        // Mock sentiment analysis
        SentimentResponseDTO sentimentResponse = new SentimentResponseDTO();
        sentimentResponse.setSentiment("POSITIVE");
        sentimentResponse.setPolarity(0.8f);
        when(aiService.analyzeSentiment(TEST_FEEDBACK)).thenReturn(sentimentResponse);

        // When
        EvaluationDTO result = evaluationService.updateEvaluation(TEST_EVALUATOR_ID, evaluationDTO);

        // Then
        assertNotNull(result);
        assertEquals(TEST_EVALUATION_ID, result.getId());
        assertEquals(EvaluationStatus.CLOSED, result.getStatus());

        // Verify all interactions in order
        InOrder inOrder = inOrder(evaluationDAO, evaluationCycleDAO, notificationService, aiService);

        verify(evaluationDAO).findById(TEST_EVALUATION_ID);
        verify(aiService).analyzeSentiment(TEST_FEEDBACK);
        verify(evaluationDAO).update(any(Evaluation.class));
        verify(evaluationCycleDAO).updateStatus(eq(cycle.getId()), eq(EvaluationCycleStatus.COMPLETED));
        verify(evaluationCycleDAO,times(2)).findById(cycle.getId());
        verify(evaluationCycleDAO).update(any(EvaluationCycle.class));
    }

    @Test
    @DisplayName("updateEvaluation should proceed even if sentiment analysis fails")
    void updateEvaluation_WhenSentimentAnalysisFails_ShouldStillUpdate() {
        // Given
        evaluationDTO.setStatus(EvaluationStatus.COMPLETED);
        when(evaluationDAO.findById(TEST_EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        when(evaluationConverter.toDTO(any(Evaluation.class))).thenReturn(evaluationDTO);
        when(aiService.analyzeSentiment(anyString())).thenThrow(new RuntimeException("API Error"));

        // When
        EvaluationDTO result = evaluationService.updateEvaluation(TEST_EVALUATOR_ID, evaluationDTO);

        // Then
        assertNotNull(result);
        assertEquals(TEST_EVALUATION_ID, result.getId());
        assertEquals(EvaluationStatus.COMPLETED, result.getStatus());
        
        verify(evaluationDAO).findById(TEST_EVALUATION_ID);
        verify(evaluationDAO).update(any(Evaluation.class));
        verify(notificationService).createNotification(any(Notification.class));
    }

    @Test
    @DisplayName("updateEvaluation should throw NotFoundException when evaluation doesn't exist")
    void updateEvaluation_WhenEvaluationNotFound_ShouldThrowNotFoundException() {
        // Given
        when(evaluationDAO.findById(TEST_EVALUATION_ID)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class,
                () -> evaluationService.updateEvaluation(TEST_EVALUATOR_ID, evaluationDTO));
        
        verify(evaluationDAO).findById(TEST_EVALUATION_ID);
        verifyNoMoreInteractions(evaluationDAO);
        verifyNoInteractions(notificationService, aiService);
    }

    @Test
    @DisplayName("closeEvaluationsBulk should close evaluations when admin request is valid")
    void closeEvaluationsBulk_WhenValidAdminRequest_ShouldCloseEvaluations() {
        // Given
        User admin = new User();
        admin.setId(3L);
        admin.setRole(Role.ADMIN);

        EvaluationCycle activeCycle = new EvaluationCycle();
        activeCycle.setId(1L);
        activeCycle.setStatus(EvaluationCycleStatus.ACTIVE);

        evaluation.setEvaluationCycle(activeCycle);
        evaluation.setStatus(EvaluationStatus.COMPLETED);

        EvaluationCloseDTO closeDto = new EvaluationCloseDTO();
        closeDto.setCycleId(activeCycle.getId());
        closeDto.setEvaluationIds(List.of(TEST_EVALUATION_ID));

        when(userDAO.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(evaluationDAO.findById(TEST_EVALUATION_ID)).thenReturn(Optional.of(evaluation));
        when(daoFactory.forEvaluationCycle().findById(activeCycle.getId())).thenReturn(Optional.of(activeCycle));
        when(evaluationDAO.countUnclosedEvaluationsByCycleId(activeCycle.getId())).thenReturn(0);

        // Mock authorization
        AuthorizationService authService = mock(AuthorizationService.class);

        // When
        evaluationService.closeEvaluationsBulk(admin.getId(), closeDto);

        // Then
        verify(evaluationDAO).update(any(Evaluation.class));
        assertEquals(EvaluationStatus.CLOSED, evaluation.getStatus());
        verify(daoFactory.forEvaluationCycle()).update(activeCycle);
        assertEquals(EvaluationCycleStatus.CLOSED, activeCycle.getStatus());
    }

    @Test
    @DisplayName("closeEvaluationsBulk should throw ForbiddenException when non-admin tries to close")
    void closeEvaluationsBulk_WhenNotAdmin_ShouldThrowForbiddenException() {
        // Given
        EvaluationCloseDTO closeDto = new EvaluationCloseDTO();
        closeDto.setCycleId(1L);
        closeDto.setEvaluationIds(List.of(TEST_EVALUATION_ID));

        when(userDAO.findById(TEST_EVALUATOR_ID)).thenReturn(Optional.of(evaluator)); // evaluator is MANAGER role

        // When/Then
        assertThrows(ForbiddenException.class,
                () -> evaluationService.closeEvaluationsBulk(TEST_EVALUATOR_ID, closeDto));
        
        verifyNoInteractions(daoFactory.forEvaluationCycle());
        verify(evaluationDAO, never()).update(any());
    }

    @Test
    @DisplayName("closeEvaluationsBulk should throw BadRequestException when cycle is already closed")
    void closeEvaluationsBulk_WhenCycleAlreadyClosed_ShouldThrowBadRequestException() {
        // Given
        User admin = new User();
        admin.setId(3L);
        admin.setRole(Role.ADMIN);

        EvaluationCycle closedCycle = new EvaluationCycle();
        closedCycle.setId(1L);
        closedCycle.setStatus(EvaluationCycleStatus.CLOSED);

        EvaluationCloseDTO closeDto = new EvaluationCloseDTO();
        closeDto.setCycleId(closedCycle.getId());
        closeDto.setEvaluationIds(List.of(TEST_EVALUATION_ID));

        when(userDAO.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(daoFactory.forEvaluationCycle().findById(closedCycle.getId())).thenReturn(Optional.of(closedCycle));

        // When/Then
        assertThrows(BadRequestException.class,
                () -> evaluationService.closeEvaluationsBulk(admin.getId(), closeDto));
        
        verify(evaluationDAO, never()).update(any());
    }

    @Test
    @DisplayName("getEvaluationsByFilter should return filtered evaluations for admin")
    void getEvaluationsByFilter_WhenAdmin_ShouldReturnAllMatchingEvaluations() {
        // Given
        User admin = new User();
        admin.setId(3L);
        admin.setRole(Role.ADMIN);

        EvaluationFilter filter = new EvaluationFilter();
        filter.setEvaluatorId(TEST_EVALUATOR_ID);
        
        List<Evaluation> evaluations = List.of(evaluation);
        List<EvaluationDTO> expectedDtos = List.of(evaluationDTO);

        when(userDAO.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(evaluationDAO.getEvaluationsByFilters(filter)).thenReturn(evaluations);
        when(evaluationConverter.toDTOList(evaluations)).thenReturn(expectedDtos);
        // Mock authorization
        AuthorizationService authService = mock(AuthorizationService.class);
        when(serviceFactory.forAuthorization()).thenReturn(authService);
        doNothing().when(authService).authorizeEvaluationAccess(any(), any(), any());

        // When
        List<EvaluationDTO> result = evaluationService.getEvaluationsByFilter(admin.getId(), filter);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TEST_EVALUATION_ID, result.get(0).getId());
    }

    @Test
    @DisplayName("getEvaluationsByFilter should limit user to their own evaluations")
    void getEvaluationsByFilter_WhenUser_ShouldOnlyReturnOwnEvaluations() {
        // Given
        User user = new User();
        user.setId(TEST_EVALUATED_ID);
        user.setRole(Role.USER);

        EvaluationFilter filter = new EvaluationFilter();
        List<Evaluation> evaluations = List.of(evaluation);
        List<EvaluationDTO> expectedDtos = List.of(evaluationDTO);

        when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
        when(evaluationDAO.getEvaluationsByFilters(any(EvaluationFilter.class))).thenReturn(evaluations);
        when(evaluationConverter.toDTOList(evaluations)).thenReturn(expectedDtos);
        // Mock authorization
        AuthorizationService authService = mock(AuthorizationService.class);
        when(serviceFactory.forAuthorization()).thenReturn(authService);
        doNothing().when(authService).authorizeEvaluationAccess(any(), any(), any());

        // When
        List<EvaluationDTO> result = evaluationService.getEvaluationsByFilter(user.getId(), filter);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify filter was modified to only show user's evaluations
        verify(evaluationDAO).getEvaluationsByFilters(argThat(f -> 
            f.getEvaluatedId().equals(user.getId()) && f.getCycleClosed()
        ));
    }

    @Test
    @DisplayName("getEvaluationsByFilter should default to evaluator's evaluations for manager with no filters")
    void getEvaluationsByFilter_WhenManagerWithNoFilters_ShouldDefaultToOwnEvaluations() {
        // Given
        EvaluationFilter filter = new EvaluationFilter();
        List<Evaluation> evaluations = List.of(evaluation);
        List<EvaluationDTO> expectedDtos = List.of(evaluationDTO);

        when(userDAO.findById(TEST_EVALUATOR_ID)).thenReturn(Optional.of(evaluator)); // evaluator is MANAGER role
        when(evaluationDAO.getEvaluationsByFilters(any(EvaluationFilter.class))).thenReturn(evaluations);
        when(evaluationConverter.toDTOList(evaluations)).thenReturn(expectedDtos);
        // Mock authorization
        AuthorizationService authService = mock(AuthorizationService.class);
        when(serviceFactory.forAuthorization()).thenReturn(authService);
        doNothing().when(authService).authorizeEvaluationAccess(any(), any(), any());

        // When
        List<EvaluationDTO> result = evaluationService.getEvaluationsByFilter(TEST_EVALUATOR_ID, filter);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify filter was modified to show manager's evaluations
        verify(evaluationDAO).getEvaluationsByFilters(argThat(f -> 
            f.getEvaluatorId().equals(TEST_EVALUATOR_ID)
        ));
    }

    @Test
    @DisplayName("getEvaluationsByFilter should throw ForbiddenException when unauthorized access")
    void getEvaluationsByFilter_WhenUnauthorizedAccess_ShouldThrowForbiddenException() {
        // Given
        EvaluationFilter filter = new EvaluationFilter();
        filter.setEvaluatedId(TEST_EVALUATED_ID);

        when(userDAO.findById(TEST_EVALUATOR_ID)).thenReturn(Optional.of(evaluator));
        // Mock authorization to throw exception
        AuthorizationService authService = mock(AuthorizationService.class);
        when(serviceFactory.forAuthorization()).thenReturn(authService);
        doThrow(new ForbiddenException("Unauthorized access", "Test"))
            .when(authService).authorizeEvaluationAccess(any(), any(), any());

        // When/Then
        assertThrows(ForbiddenException.class,
                () -> evaluationService.getEvaluationsByFilter(TEST_EVALUATOR_ID, filter));
        
        verifyNoInteractions(evaluationConverter);
        verify(evaluationDAO, never()).getEvaluationsByFilters(any());
    }
}
