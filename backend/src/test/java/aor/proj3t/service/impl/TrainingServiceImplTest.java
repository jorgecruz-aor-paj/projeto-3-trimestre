package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.TrainingAreaDAO;
import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.Training;
import aor.proj3t.entities.TrainingArea;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Notification;
import aor.proj3t.enums.Role;
import aor.proj3t.enums.TrainingLanguage;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.NotificationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.TrainingAreaConverter;
import aor.proj3t.util.converterutils.TrainingConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingAreaDAO trainingAreaDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private TrainingConverter trainingConverter;

    @Mock
    private TrainingAreaConverter trainingAreaConverter;

    @Mock
    private NotificationService notificationService;

    private final Long TEST_ID = 1L;
    private final String TEST_TITLE = "Test Training";
    private final Integer TEST_DURATION = 60;
    private final String TEST_DESCRIPTION = "Test Description";
    private final String TEST_LINK = "http://test.com";
    private final TrainingLanguage TEST_LANGUAGE = TrainingLanguage.ENGLISH;

    @BeforeEach
    public void setup() {
        // Configure common mocks
        lenient().when(daoFactory.forTraining()).thenReturn(trainingDAO);
        lenient().when(daoFactory.forTrainingArea()).thenReturn(trainingAreaDAO);
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);
        lenient().when(converterFactory.forTraining()).thenReturn(trainingConverter);
        lenient().when(converterFactory.forTrainingArea()).thenReturn(trainingAreaConverter);
        lenient().when(serviceFactory.forNotification()).thenReturn(notificationService);
    }

    @Test
    @DisplayName("createTraining should create and return a new training")
    public void testCreateTraining() {
        // Arrange
        NewTrainingDTO newTrainingDto = new NewTrainingDTO();
        newTrainingDto.setTitle(TEST_TITLE);
        newTrainingDto.setDuration(TEST_DURATION);
        newTrainingDto.setDescription(TEST_DESCRIPTION);
        newTrainingDto.setLink(TEST_LINK);
        newTrainingDto.setLanguage(TEST_LANGUAGE);
        newTrainingDto.setTrainingAreaId(TEST_ID);

        TrainingArea trainingArea = new TrainingArea("Test Area");
        trainingArea.setId(TEST_ID);

        Training expectedTraining = new Training(TEST_TITLE, TEST_DURATION, TEST_DESCRIPTION, TEST_LINK, TEST_LANGUAGE, trainingArea);
        expectedTraining.setId(TEST_ID);

        TrainingDTO expectedDto = new TrainingDTO();
        expectedDto.setId(TEST_ID);
        expectedDto.setTitle(TEST_TITLE);

        when(trainingAreaDAO.findById(TEST_ID)).thenReturn(Optional.of(trainingArea));
        when(trainingDAO.create(any(Training.class))).thenReturn(expectedTraining);
        when(trainingConverter.toDTO(any(Training.class))).thenReturn(expectedDto);

        // Act
        TrainingDTO result = trainingService.createTraining(newTrainingDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_TITLE, result.getTitle());

        // Verify
        verify(trainingAreaDAO).findById(TEST_ID);
        verify(trainingDAO).create(any(Training.class));
        verify(trainingConverter).toDTO(any(Training.class));
    }

    @Test
    @DisplayName("createTraining should throw NotFoundException when training area doesn't exist")
    public void testCreateTrainingWithInvalidArea() {
        // Arrange
        NewTrainingDTO newTrainingDto = new NewTrainingDTO();
        newTrainingDto.setTrainingAreaId(TEST_ID);
        
        when(trainingAreaDAO.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> trainingService.createTraining(newTrainingDto));

        assertTrue(exception.getMessage().contains("Training area not found"));

        // Verify
        verify(trainingAreaDAO).findById(TEST_ID);
        verifyNoInteractions(trainingDAO, trainingConverter);
    }

    @Test
    @DisplayName("deactivateTraining should deactivate training and notify managers")
    public void testDeactivateTraining() {
        // Arrange
        Training training = new Training();
        training.setId(TEST_ID);
        training.setTitle(TEST_TITLE);
        training.setActive(true);

        User admin = new User();
        admin.setRole(Role.ADMIN);
        User manager = new User();
        manager.setRole(Role.MANAGER);
        List<User> users = Arrays.asList(admin, manager);

        when(trainingDAO.findById(TEST_ID)).thenReturn(Optional.of(training));
        when(userDAO.findAll()).thenReturn(users);

        // Mock createNotification to return a Notification object
        Notification mockNotification = new Notification();
        when(notificationService.createNotification(any(Notification.class))).thenReturn(mockNotification);

        // Act
        Boolean result = trainingService.deactivateTraining(TEST_ID);

        // Assert
        assertTrue(result);
        assertFalse(training.isActive());

        // Verify
        verify(trainingDAO).findById(TEST_ID);
        verify(trainingDAO).update(training);
        verify(userDAO).findAll();
        verify(notificationService, times(2)).createNotification(any(Notification.class));
    }

    @Test
    @DisplayName("addTrainingArea should create new training area")
    public void testAddTrainingArea() {
        // Arrange
        TrainingAreaDTO areaDto = new TrainingAreaDTO();
        areaDto.setName("New Area");

        TrainingArea area = new TrainingArea("New Area");
        area.setId(TEST_ID);

        when(trainingAreaDAO.findByName("new area")).thenReturn(Optional.empty());
        when(trainingAreaDAO.create(any(TrainingArea.class))).thenReturn(area);
        when(trainingAreaConverter.toDTO(any(TrainingArea.class))).thenReturn(areaDto);

        // Act
        TrainingAreaDTO result = trainingService.addTrainingArea(areaDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Area", result.getName());

        // Verify
        verify(trainingAreaDAO).findByName("new area");
        verify(trainingAreaDAO).create(any(TrainingArea.class));
        verify(trainingAreaConverter).toDTO(any(TrainingArea.class));
    }

    @Test
    @DisplayName("addTrainingArea should throw ConflictException when area name exists")
    public void testAddTrainingAreaDuplicate() {
        // Arrange
        TrainingAreaDTO areaDto = new TrainingAreaDTO();
        areaDto.setName("Existing Area");

        TrainingArea existingArea = new TrainingArea("Existing Area");
        when(trainingAreaDAO.findByName("existing area")).thenReturn(Optional.of(existingArea));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class,
                () -> trainingService.addTrainingArea(areaDto));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("duplicate"));

        // Verify
        verify(trainingAreaDAO).findByName("existing area");
        verifyNoMoreInteractions(trainingAreaDAO);
        verifyNoInteractions(trainingAreaConverter);
    }

    @Test
    @DisplayName("getTrainingsByFilters should return filtered trainings for admin")
    public void testGetTrainingsByFiltersAsAdmin() {
        // Arrange
        User admin = new User();
        admin.setId(TEST_ID);
        admin.setRole(Role.ADMIN);

        TrainingFilter filters = new TrainingFilter();
        filters.setActiveOnly(false);

        Training training = new Training();
        List<Training> trainings = Collections.singletonList(training);
        TrainingDTO trainingDto = new TrainingDTO();
        List<TrainingDTO> expected = Collections.singletonList(trainingDto);

        when(userDAO.findById(TEST_ID)).thenReturn(Optional.of(admin));
        when(trainingDAO.getTrainingsByFilters(filters)).thenReturn(trainings);
        when(trainingConverter.toDTOList(trainings)).thenReturn(expected);

        // Act
        List<TrainingDTO> result = trainingService.getTrainingsByFilters(TEST_ID, filters);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(userDAO).findById(TEST_ID);
        verify(trainingDAO).getTrainingsByFilters(filters);
        verify(trainingConverter).toDTOList(trainings);
    }

    @Test
    @DisplayName("getTrainingsByFilters should throw ForbiddenException for non-admin requesting inactive trainings")
    public void testGetTrainingsByFiltersNonAdminInactive() {
        // Arrange
        User user = new User();
        user.setId(TEST_ID);
        user.setRole(Role.USER);

        TrainingFilter filters = new TrainingFilter();
        filters.setActiveOnly(false);

        when(userDAO.findById(TEST_ID)).thenReturn(Optional.of(user));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> trainingService.getTrainingsByFilters(TEST_ID, filters));

        assertTrue(exception.getMessage().contains("requested deactivated trainings"));

        // Verify
        verify(userDAO).findById(TEST_ID);
        verifyNoInteractions(trainingDAO, trainingConverter);
    }

    @Test
    @DisplayName("getUnparticipatedTrainings should return trainings for manager's user")
    public void testGetUnparticipatedTrainingsByUserIdAsManager() {
        // Arrange
        User manager = new User();
        manager.setId(TEST_ID);
        manager.setRole(Role.MANAGER);

        User user = new User();
        user.setId(2L);
        user.setManager(manager);

        Training training = new Training();
        List<Training> trainings = Collections.singletonList(training);
        TrainingDTO trainingDto = new TrainingDTO();
        List<TrainingDTO> expected = Collections.singletonList(trainingDto);

        when(userDAO.findById(TEST_ID)).thenReturn(Optional.of(manager));
        when(userDAO.findById(2L)).thenReturn(Optional.of(user));
        when(trainingDAO.findTrainingsNotParticipatedByUser(2L)).thenReturn(trainings);
        when(trainingConverter.toDTOList(trainings)).thenReturn(expected);

        // Act
        List<TrainingDTO> result = trainingService.getUnparticipatedTrainingsByUserId(TEST_ID, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify
        verify(userDAO).findById(TEST_ID);
        verify(userDAO).findById(2L);
        verify(trainingDAO).findTrainingsNotParticipatedByUser(2L);
        verify(trainingConverter).toDTOList(trainings);
    }

    @Test
    @DisplayName("getUnparticipatedTrainings should throw ForbiddenException for unauthorized access")
    public void testGetUnparticipatedTrainingsByUserIdUnauthorized() {
        // Arrange
        User nonManager = new User();
        nonManager.setId(TEST_ID);
        nonManager.setRole(Role.USER);

        User manager = new User();
        manager.setId(999L); // Not TEST_ID

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setManager(manager);

        when(userDAO.findById(TEST_ID)).thenReturn(Optional.of(nonManager));
        when(userDAO.findById(2L)).thenReturn(Optional.of(otherUser));

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> trainingService.getUnparticipatedTrainingsByUserId(TEST_ID, 2L));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("not authorized"));

        // Verify
        verify(userDAO).findById(TEST_ID);
        verify(userDAO).findById(2L);
        verifyNoInteractions(trainingDAO, trainingConverter);
    }

}
