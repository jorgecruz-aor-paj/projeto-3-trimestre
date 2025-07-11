package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.NewTrainingParticipationDTO;
import aor.proj3t.dto.TrainingParticipationDTO;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.impl.TrainingParticipationServiceImpl;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.TrainingParticipationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingParticipationServiceImplTest {

    @InjectMocks
    private TrainingParticipationServiceImpl participationService;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private TrainingParticipationDAO participationDAO;

    @Mock
    private TrainingParticipationConverter participationConverter;

    @Mock
    private NotificationService notificationService;

    private final Long TEST_USER_ID = 1L;
    private final Long TEST_MANAGER_ID = 2L;
    private final Long TEST_TRAINING_ID = 1L;

    @BeforeEach
    public void setup() {
        lenient().when(daoFactory.forTraining()).thenReturn(trainingDAO);
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(daoFactory.forTrainingParticipation()).thenReturn(participationDAO);
        lenient().when(converterFactory.forTrainingParticipation()).thenReturn(participationConverter);
        lenient().when(serviceFactory.forNotification()).thenReturn(notificationService);
    }

    @Test
    @DisplayName("findTrainingParticipationsByUser should return participations for user's own data")
    public void testFindTrainingParticipationsByUserAsSelf() {
        // Arrange
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setRole(Role.USER);

        TrainingParticipation participation = new TrainingParticipation();
        List<TrainingParticipation> participations = Collections.singletonList(participation);

        TrainingParticipationDTO participationDto = new TrainingParticipationDTO();
        List<TrainingParticipationDTO> expectedDtos = Collections.singletonList(participationDto);

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(participationDAO.findByParticipantId(TEST_USER_ID)).thenReturn(participations);
        when(participationConverter.toDTOList(participations)).thenReturn(expectedDtos);

        // Act
        List<TrainingParticipationDTO> result = participationService.findTrainingParticipationsByUser(TEST_USER_ID, TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(userDAO, times(2)).findById(TEST_USER_ID);
        verify(participationDAO).findByParticipantId(TEST_USER_ID);
        verify(participationConverter).toDTOList(participations);
    }

    @Test
    @DisplayName("findTrainingParticipationsByUser should return participations for manager viewing their user")
    public void testFindTrainingParticipationsByUserAsManager() {
        // Arrange
        User manager = new User();
        manager.setId(TEST_MANAGER_ID);
        manager.setRole(Role.MANAGER);

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setRole(Role.USER);
        user.setManager(manager);

        TrainingParticipation participation = new TrainingParticipation();
        List<TrainingParticipation> participations = Collections.singletonList(participation);

        TrainingParticipationDTO participationDto = new TrainingParticipationDTO();
        List<TrainingParticipationDTO> expectedDtos = Collections.singletonList(participationDto);

        when(userDAO.findById(TEST_MANAGER_ID)).thenReturn(Optional.of(manager));
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(participationDAO.findByParticipantId(TEST_USER_ID)).thenReturn(participations);
        when(participationConverter.toDTOList(participations)).thenReturn(expectedDtos);

        // Act
        List<TrainingParticipationDTO> result = participationService.findTrainingParticipationsByUser(TEST_MANAGER_ID, TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(userDAO).findById(TEST_MANAGER_ID);
        verify(userDAO).findById(TEST_USER_ID);
        verify(participationDAO).findByParticipantId(TEST_USER_ID);
        verify(participationConverter).toDTOList(participations);
    }

    @Test
    @DisplayName("findTrainingParticipationsByUser should throw ForbiddenException for unauthorized access")
    public void testFindTrainingParticipationsByUserUnauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(TEST_MANAGER_ID);
        otherUser.setRole(Role.USER);

        // Create a different manager with proper ID
        User differentManager = new User();
        differentManager.setId(3L); // Different ID from TEST_MANAGER_ID
        differentManager.setRole(Role.MANAGER);

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setRole(Role.USER);
        user.setManager(differentManager); // Set the properly initialized manager

        when(userDAO.findById(TEST_MANAGER_ID)).thenReturn(Optional.of(otherUser));
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> participationService.findTrainingParticipationsByUser(TEST_MANAGER_ID, TEST_USER_ID));

        assertTrue(exception.getMessage().contains("invalid access to user"));

        // Verify
        verify(userDAO).findById(TEST_MANAGER_ID);
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(participationDAO, participationConverter);
    }

    @Test
    @DisplayName("registerTrainingParticipation should create participation and notify user")
    public void testRegisterTrainingParticipation() {
        // Arrange
        User manager = new User();
        manager.setId(TEST_MANAGER_ID);
        manager.setRole(Role.MANAGER);

        User participant = new User();
        participant.setId(TEST_USER_ID);
        participant.setRole(Role.USER);
        participant.setManager(manager);

        Training training = new Training();
        training.setId(TEST_TRAINING_ID);
        training.setTitle("Test Training");

        NewTrainingParticipationDTO newParticipationDto = new NewTrainingParticipationDTO();
        newParticipationDto.setParticipantId(TEST_USER_ID);
        newParticipationDto.setManagerId(TEST_MANAGER_ID);
        newParticipationDto.setTrainingId(TEST_TRAINING_ID);
        newParticipationDto.setParticipatedAt(Instant.now());

        when(userDAO.findById(TEST_MANAGER_ID)).thenReturn(Optional.of(manager));
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(participant));
        when(trainingDAO.findById(TEST_TRAINING_ID)).thenReturn(Optional.of(training));
        when(participationDAO.create(any(TrainingParticipation.class))).thenReturn(new TrainingParticipation());

        // Act
        participationService.registerTrainingParticipation(TEST_MANAGER_ID, newParticipationDto);

        // Verify
        verify(userDAO).findById(TEST_MANAGER_ID);
        verify(userDAO).findById(TEST_USER_ID);
        verify(trainingDAO).findById(TEST_TRAINING_ID);
        verify(participationDAO).create(any(TrainingParticipation.class));
        verify(notificationService).createNotification(any(Notification.class));
    }

    @Test
    @DisplayName("registerTrainingParticipation should throw ForbiddenException for unauthorized manager")
    public void testRegisterTrainingParticipationUnauthorized() {
        // Arrange
        User wrongManager = new User();
        wrongManager.setId(TEST_MANAGER_ID);
        wrongManager.setRole(Role.MANAGER);

        User participant = new User();
        participant.setId(TEST_USER_ID);
        participant.setRole(Role.USER);
        participant.setManager(new User()); // Different manager

        NewTrainingParticipationDTO newParticipationDto = new NewTrainingParticipationDTO();
        newParticipationDto.setParticipantId(TEST_USER_ID);
        newParticipationDto.setManagerId(TEST_MANAGER_ID);
        newParticipationDto.setTrainingId(TEST_TRAINING_ID);

        when(userDAO.findById(TEST_MANAGER_ID)).thenReturn(Optional.of(wrongManager));
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(participant));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> participationService.registerTrainingParticipation(TEST_MANAGER_ID, newParticipationDto));

        assertTrue(exception.getMessage().contains("adding participation to unmanaged user"));

        // Verify
        verify(userDAO).findById(TEST_MANAGER_ID);
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(trainingDAO, participationDAO, notificationService);
    }

    @Test
    @DisplayName("getTrainingParticipationTimeByYear should return data for user's own stats")
    public void testGetTrainingParticipationTimeByYearAsSelf() {
        // Arrange
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setRole(Role.USER);

        LinkedHashMap<Integer, Long> expectedStats = new LinkedHashMap<>();
        expectedStats.put(2025, 120L); // 2 hours
        expectedStats.put(2024, 60L);  // 1 hour

        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(participationDAO.trainingTimeByYearByParticipantId(TEST_USER_ID)).thenReturn(expectedStats);

        // Act
        LinkedHashMap<Integer, Long> result = participationService.getTrainingParticipationTimeByYearByUser(TEST_USER_ID, TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(120L, result.get(2025));
        assertEquals(60L, result.get(2024));

        // Verify
        verify(userDAO, times(2)).findById(TEST_USER_ID);
        verify(participationDAO).trainingTimeByYearByParticipantId(TEST_USER_ID);
    }

    @Test
    @DisplayName("getTrainingParticipationTimeByYear should throw ForbiddenException for unauthorized access")
    public void testGetTrainingParticipationTimeByYearUnauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(TEST_MANAGER_ID);
        otherUser.setRole(Role.USER);

        // Create manager with proper ID
        User manager = new User();
        manager.setId(3L); // Different manager ID
        manager.setRole(Role.MANAGER);

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setRole(Role.USER);
        user.setManager(manager); // Set manager with proper ID

        when(userDAO.findById(TEST_MANAGER_ID)).thenReturn(Optional.of(otherUser));
        when(userDAO.findById(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> participationService.getTrainingParticipationTimeByYearByUser(TEST_MANAGER_ID, TEST_USER_ID));

        assertTrue(exception.getMessage().contains("not authorized"));

        // Verify
        verify(userDAO).findById(TEST_MANAGER_ID);
        verify(userDAO).findById(TEST_USER_ID);
        verifyNoInteractions(participationDAO);
    }
}
