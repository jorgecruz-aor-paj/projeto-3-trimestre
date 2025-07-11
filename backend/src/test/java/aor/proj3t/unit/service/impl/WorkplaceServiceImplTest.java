package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.WorkplaceDAO;
import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.Workplace;
import aor.proj3t.service.impl.WorkplaceServiceImpl;
import aor.proj3t.util.converterutils.ConverterFactory;
import aor.proj3t.util.converterutils.WorkplaceConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkplaceServiceImplTest {

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private ConverterFactory converterFactory;

    @Mock
    private WorkplaceDAO workplaceDAO;

    @Mock
    private WorkplaceConverter workplaceConverter;

    @InjectMocks
    private WorkplaceServiceImpl workplaceService;

    // Test data
    private final Workplace workplace1 = new Workplace();
    private final Workplace workplace2 = new Workplace();
    private final WorkplaceDTO workplaceDTO1 = new WorkplaceDTO();
    private final WorkplaceDTO workplaceDTO2 = new WorkplaceDTO();
    private final List<Workplace> workplaces = Arrays.asList(workplace1, workplace2);
    private final List<WorkplaceDTO> workplaceDTOs = Arrays.asList(workplaceDTO1, workplaceDTO2);

    @BeforeEach
    void setUp() {
        // Setup mock responses
        when(daoFactory.forWorkplace()).thenReturn(workplaceDAO);
        when(converterFactory.forWorkplace()).thenReturn(workplaceConverter);
    }

    @Test
    @DisplayName("getAllWorkplaces should return list of all workplace DTOs")
    void getAllWorkplaces_ShouldReturnAllWorkplaceDTOs() {
        // Given
        when(workplaceDAO.findAll()).thenReturn(workplaces);
        when(workplaceConverter.toDTOList(workplaces)).thenReturn(workplaceDTOs);

        // When
        List<WorkplaceDTO> result = workplaceService.getAllWorkplaces();

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(workplaceDTOs, result, "Should return the converted workplace DTOs");
        verify(daoFactory).forWorkplace();
        verify(workplaceDAO).findAll();
        verify(converterFactory).forWorkplace();
        verify(workplaceConverter).toDTOList(workplaces);
    }
}
