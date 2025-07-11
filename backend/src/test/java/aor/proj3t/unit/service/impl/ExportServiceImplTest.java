package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.EvaluationDAO;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.*;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.impl.ExportServiceImpl;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.EvaluationConverter;
import aor.proj3t.util.converterutils.TrainingConverter;
import aor.proj3t.util.converterutils.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceImplTest {

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private UserDAO userDAO;

    @Mock
    private EvaluationDAO evaluationDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserConverter userConverter;

    @Mock
    private EvaluationConverter evaluationConverter;

    @Mock
    private TrainingConverter trainingConverter;

    @InjectMocks
    private ExportServiceImpl exportService;

    // Test data
    private final Long REQUESTER_ID = 1L;
    private User requester;
    private User user1;
    private User user2;
    private Evaluation evaluation;
    private Training training;
    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private EvaluationDTO evaluationDTO;
    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        // Setup common mocks
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forEvaluation()).thenReturn(evaluationDAO);
        lenient().when(daoFactory.forTraining()).thenReturn(trainingDAO);
        lenient().when(converterFactory.forUser()).thenReturn(userConverter);
        lenient().when(converterFactory.forEvaluation()).thenReturn(evaluationConverter);
        lenient().when(converterFactory.forTraining()).thenReturn(trainingConverter);

        // Setup test data
        requester = new User();
        requester.setId(REQUESTER_ID);
        requester.setRole(Role.ADMIN);
        
        // Setup WorkplaceDTO
        WorkplaceDTO workplaceDTO = new WorkplaceDTO();
        workplaceDTO.setId(1L);
        workplaceDTO.setName("Test Workplace");

        // Setup Users and UserDTOs
        user1 = new User();
        user1.setId(2L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");

        user2 = new User();
        user2.setId(3L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");

        userDTO1 = new UserDTO();
        userDTO1.setId(user1.getId());
        userDTO1.setFirstName(user1.getFirstName());
        userDTO1.setLastName(user1.getLastName());
        userDTO1.setEmail(user1.getEmail());
        userDTO1.setWorkplace(workplaceDTO);

        userDTO2 = new UserDTO();
        userDTO2.setId(user2.getId());
        userDTO2.setFirstName(user2.getFirstName());
        userDTO2.setLastName(user2.getLastName());
        userDTO2.setEmail(user2.getEmail());
        userDTO2.setWorkplace(workplaceDTO);

        // Setup MiniUserDTO for Evaluation
        MiniUserDTO miniUser1DTO = new MiniUserDTO();
        miniUser1DTO.setId(user1.getId());
        miniUser1DTO.setFirstName(user1.getFirstName());
        miniUser1DTO.setLastName(user1.getLastName());

        MiniUserDTO miniUser2DTO = new MiniUserDTO();
        miniUser2DTO.setId(user2.getId());
        miniUser2DTO.setFirstName(user2.getFirstName());
        miniUser2DTO.setLastName(user2.getLastName());

        // Setup Evaluation and EvaluationDTO
        evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setEvaluator(user1);
        evaluation.setEvaluated(user2);
        evaluation.setStatus(EvaluationStatus.COMPLETED);
        evaluation.setCreatedAt(Instant.now());
        evaluation.setFeedback("Test feedback");

        MiniEvaluationCycleDTO evaluationCycleDTO = new MiniEvaluationCycleDTO();
        evaluationCycleDTO.setId(1L);
        evaluationCycleDTO.setStartDate(Instant.now());
        evaluationCycleDTO.setClosureDate(Instant.now().plusSeconds(3600));

        evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setStatus(evaluation.getStatus());
        evaluationDTO.setEvaluationCycle(evaluationCycleDTO);
        evaluationDTO.setEvaluator(miniUser1DTO);
        evaluationDTO.setEvaluatee(miniUser2DTO);
        evaluationDTO.setFeedback(evaluation.getFeedback());
        evaluationDTO.setCreatedAt(evaluation.getCreatedAt());

        // Setup Training Area and DTO
        TrainingArea trainingArea = new TrainingArea();
        trainingArea.setId(1L);
        trainingArea.setName("Test Area");

        TrainingAreaDTO trainingAreaDTO = new TrainingAreaDTO();
        trainingAreaDTO.setId(trainingArea.getId());
        trainingAreaDTO.setName(trainingArea.getName());

        // Setup Training and TrainingDTO
        training = new Training();
        training.setId(1L);
        training.setTitle("Test Training");
        training.setDescription("Test Description");
        training.setDuration(120);
        training.setLanguage(TrainingLanguage.ENGLISH);
        training.setActive(true);
        training.setCreatedAt(Instant.now());

        trainingDTO = new TrainingDTO();
        trainingDTO.setId(training.getId());
        trainingDTO.setTitle(training.getTitle());
        trainingDTO.setDescription(training.getDescription());
        trainingDTO.setDuration(training.getDuration());
        trainingDTO.setLanguage(training.getLanguage());
        trainingDTO.setActive(training.isActive());
        trainingDTO.setCreatedAt(training.getCreatedAt());
        trainingDTO.setTrainingArea(trainingAreaDTO);
    }

    @Test
    @DisplayName("exportUsersToCSV should return CSV with all users")
    void exportUsersToCSV_ShouldReturnCSVWithAllUsers() {
        // Given
        List<User> users = Arrays.asList(user1, user2);
        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(userDAO.findAll()).thenReturn(users);
        when(userConverter.toDTOList(users)).thenReturn(userDTOs);

        // When
        String result = exportService.exportUsersToCSV(REQUESTER_ID);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(user1.getFirstName()));
        assertTrue(result.contains(user1.getLastName()));
        assertTrue(result.contains(user1.getEmail()));
        assertTrue(result.contains(user2.getFirstName()));
        assertTrue(result.contains(user2.getLastName()));
        assertTrue(result.contains(user2.getEmail()));
        assertTrue(result.contains(userDTO1.getWorkplace().getName()));
        verify(userDAO).findAll();
        verify(userConverter).toDTOList(users);
    }

    @Test
    @DisplayName("exportFilteredUsersToCSV should return CSV with filtered users")
    void exportFilteredUsersToCSV_ShouldReturnCSVWithFilteredUsers() {
        // Given
        UserFilter filter = new UserFilter();
        List<User> filteredUsers = Arrays.asList(user1);
        List<UserDTO> filteredUserDTOs = Arrays.asList(userDTO1);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(userDAO.getUsersByFilters(filter)).thenReturn(filteredUsers);
        when(userConverter.toDTOList(filteredUsers)).thenReturn(filteredUserDTOs);

        // When
        String result = exportService.exportFilteredUsersToCSV(REQUESTER_ID, filter);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(user1.getFirstName()));
        assertTrue(result.contains(user1.getLastName()));
        assertTrue(result.contains(user1.getEmail()));
        assertTrue(result.contains(userDTO1.getWorkplace().getName()));
        assertFalse(result.contains(user2.getFirstName()));
        verify(userDAO).getUsersByFilters(filter);
        verify(userConverter).toDTOList(filteredUsers);
    }

    @Test
    @DisplayName("exportEvaluationsToCSV should return CSV with all evaluations")
    void exportEvaluationsToCSV_ShouldReturnCSVWithAllEvaluations() {
        // Given
        List<Evaluation> evaluations = Arrays.asList(evaluation);
        List<EvaluationDTO> evaluationDTOs = Arrays.asList(evaluationDTO);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(evaluationDAO.findAll()).thenReturn(evaluations);
        when(evaluationConverter.toDTOList(evaluations)).thenReturn(evaluationDTOs);

        // When
        String result = exportService.exportEvaluationsToCSV(REQUESTER_ID);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(evaluationDTO.getEvaluator().getFirstName()));
        assertTrue(result.contains(evaluationDTO.getEvaluator().getLastName()));
        assertTrue(result.contains(evaluationDTO.getEvaluatee().getFirstName()));
        assertTrue(result.contains(evaluationDTO.getEvaluatee().getLastName()));
        assertTrue(result.contains(evaluationDTO.getStatus().toString()));
        assertTrue(result.contains(evaluationDTO.getFeedback()));
        verify(evaluationDAO).findAll();
        verify(evaluationConverter).toDTOList(evaluations);
    }

    @Test
    @DisplayName("exportFilteredEvaluationsToCSV should return CSV with filtered evaluations")
    void exportFilteredEvaluationsToCSV_ShouldReturnCSVWithFilteredEvaluations() {
        // Given
        EvaluationFilter filter = new EvaluationFilter();
        List<Evaluation> filteredEvaluations = Arrays.asList(evaluation);
        List<EvaluationDTO> filteredEvaluationDTOs = Arrays.asList(evaluationDTO);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(evaluationDAO.getEvaluationsByFilters(filter)).thenReturn(filteredEvaluations);
        when(evaluationConverter.toDTOList(filteredEvaluations)).thenReturn(filteredEvaluationDTOs);

        // When
        String result = exportService.exportFilteredEvaluationsToCSV(REQUESTER_ID, filter);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(evaluationDTO.getEvaluator().getFirstName()));
        assertTrue(result.contains(evaluationDTO.getEvaluator().getLastName()));
        assertTrue(result.contains(evaluationDTO.getEvaluatee().getFirstName()));
        assertTrue(result.contains(evaluationDTO.getEvaluatee().getLastName()));
        assertTrue(result.contains(evaluationDTO.getStatus().toString()));
        assertTrue(result.contains(evaluationDTO.getFeedback()));
        verify(evaluationDAO).getEvaluationsByFilters(filter);
        verify(evaluationConverter).toDTOList(filteredEvaluations);
    }

    @Test
    @DisplayName("exportTrainingsToCSV should return CSV with all trainings")
    void exportTrainingsToCSV_ShouldReturnCSVWithAllTrainings() {
        // Given
        List<Training> trainings = Arrays.asList(training);
        List<TrainingDTO> trainingDTOs = Arrays.asList(trainingDTO);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(trainingDAO.findAll()).thenReturn(trainings);
        when(trainingConverter.toDTOList(trainings)).thenReturn(trainingDTOs);

        // When
        String result = exportService.exportTrainingsToCSV(REQUESTER_ID);

        // Then
        assertNotNull(result);
        assertTrue(result.contains(trainingDTO.getTitle()));
        assertTrue(result.contains(trainingDTO.getDescription()));
        assertTrue(result.contains(String.valueOf(trainingDTO.getDuration())));
        assertTrue(result.contains(trainingDTO.getLanguage().toString()));
        assertTrue(result.contains(trainingDTO.getTrainingArea().getName()));
        verify(trainingDAO).findAll();
        verify(trainingConverter).toDTOList(trainings);
    }

    @Test
    @DisplayName("exportFilteredTrainingsToCSV should return CSV with filtered trainings")
    void exportFilteredTrainingsToCSV_ShouldReturnCSVWithFilteredTrainings() {
        // Given
        TrainingFilter filter = new TrainingFilter();
        List<Training> filteredTrainings = Arrays.asList(training);
        List<TrainingDTO> filteredTrainingDTOs = Arrays.asList(trainingDTO);

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(trainingDAO.getTrainingsByFilters(filter)).thenReturn(filteredTrainings);
        when(trainingConverter.toDTOList(filteredTrainings)).thenReturn(filteredTrainingDTOs);

        // When
        String result = exportService.exportFilteredTrainingsToCSV(REQUESTER_ID, filter);

        // Then
        assertNotNull(result);
        verify(trainingDAO).getTrainingsByFilters(filter);
        verify(trainingConverter).toDTOList(filteredTrainings);
    }

    @Test
    @DisplayName("exportEvaluationToPDF should return PDF bytes for evaluation")
    void exportEvaluationToPDF_ShouldReturnPDFBytes() {
        // Given
        Long evaluationId = evaluation.getId();

        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(evaluationDAO.findById(evaluationId)).thenReturn(Optional.of(evaluation));
        when(userDAO.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userDAO.findById(user2.getId())).thenReturn(Optional.of(user2));

        // When
        byte[] result = exportService.exportEvaluationToPDF(REQUESTER_ID, evaluationId);

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(evaluationDAO).findById(evaluationId);
        verify(userDAO, times(3)).findById(anyLong()); // Once for requester, twice for evaluator/evaluated
    }

    @Test
    @DisplayName("Export methods should throw NotFoundException when requester not found")
    void exportMethods_ShouldThrowNotFoundException_WhenRequesterNotFound() {
        // Given
        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> exportService.exportUsersToCSV(REQUESTER_ID));
        assertThrows(NotFoundException.class, () -> exportService.exportEvaluationsToCSV(REQUESTER_ID));
        assertThrows(NotFoundException.class, () -> exportService.exportTrainingsToCSV(REQUESTER_ID));
        assertThrows(NotFoundException.class, () -> 
            exportService.exportFilteredUsersToCSV(REQUESTER_ID, new UserFilter()));
        assertThrows(NotFoundException.class, () -> 
            exportService.exportFilteredEvaluationsToCSV(REQUESTER_ID, new EvaluationFilter()));
        assertThrows(NotFoundException.class, () -> 
            exportService.exportFilteredTrainingsToCSV(REQUESTER_ID, new TrainingFilter()));
        assertThrows(NotFoundException.class, () -> 
            exportService.exportEvaluationToPDF(REQUESTER_ID, 1L));
    }

    @Test
    @DisplayName("exportEvaluationToPDF should throw NotFoundException when evaluation not found")
    void exportEvaluationToPDF_ShouldThrowNotFoundException_WhenEvaluationNotFound() {
        // Given
        Long evaluationId = 999L;
        when(userDAO.findById(REQUESTER_ID)).thenReturn(Optional.of(requester));
        when(evaluationDAO.findById(evaluationId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> 
            exportService.exportEvaluationToPDF(REQUESTER_ID, evaluationId));
    }
}
