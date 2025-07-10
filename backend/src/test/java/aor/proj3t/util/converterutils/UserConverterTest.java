package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.entities.User;
import aor.proj3t.entities.Workplace;
import aor.proj3t.enums.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserConverter Tests")
public class UserConverterTest extends BaseConverterTest<User, UserDTO, UserConverter> {

    @Mock
    private EntityManager entityManager;

    @Mock
    private WorkplaceConverter workplaceConverter;

    @Mock
    private MiniUserConverter miniUserConverter;

    @InjectMocks
    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing to avoid strict stubbing issues
        lenient().when(entityManager.find(eq(User.class), any(Long.class))).thenReturn(null);
        lenient().when(entityManager.find(eq(Workplace.class), any(Long.class))).thenReturn(null);
    }

    @Override
    protected UserConverter getConverter() {
        return userConverter;
    }

    @Override
    protected User createValidEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress("123 Main St");
        user.setPhoneNumber("123-456-7890");
        user.setRole(Role.USER);
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setCreatedAt(Instant.now());
        user.setBiography("Test biography");
        user.setPhotograph("profile.jpg");
        return user;
    }

    @Override
    protected UserDTO createValidDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 Main St");
        dto.setPhoneNumber("123-456-7890");
        dto.setRole(Role.USER);
        dto.setBirthdate(LocalDate.of(1990, 1, 1));
        dto.setCreatedAt(Instant.now());
        dto.setBiography("Test biography");
        dto.setPhotograph("profile.jpg");
        return dto;
    }

    @Override
    protected void assertEntityEqualsDTO(User entity, UserDTO dto) {
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getAddress(), dto.getAddress());
        assertEquals(entity.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(entity.getRole(), dto.getRole());
        assertEquals(entity.getBirthdate(), dto.getBirthdate());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getBiography(), dto.getBiography());
        assertEquals(entity.getPhotograph(), dto.getPhotograph());
    }

    @Override
    protected void assertDTOEqualsEntity(UserDTO dto, User entity) {
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getAddress(), entity.getAddress());
        assertEquals(dto.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(dto.getRole(), entity.getRole());
        assertEquals(dto.getBirthdate(), entity.getBirthdate());
        assertEquals(dto.getBiography(), entity.getBiography());
        assertEquals(dto.getPhotograph(), entity.getPhotograph());
    }

    @Test
    @DisplayName("Should convert user entity with manager to DTO")
    public void testUserWithManagerToDTO() {
        // Given
        User manager = createValidEntity();
        manager.setId(2L);
        manager.setEmail("manager@example.com");
        manager.setFirstName("Jane");
        manager.setLastName("Smith");
        
        User user = createValidEntity();
        user.setManager(manager);
        
        // When
        UserDTO dto = userConverter.toDTO(user);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getManager());
        assertEquals(manager.getId(), dto.getManager().getId());
        assertEquals(manager.getFirstName(), dto.getManager().getFirstName());
        assertEquals(manager.getLastName(), dto.getManager().getLastName());
        assertEquals(manager.getEmail(), dto.getManager().getEmail());
        assertEquals(manager.getPhotograph(), dto.getManager().getPhotograph());
    }

    @Test
    @DisplayName("Should convert user entity with workplace to DTO")
    public void testUserWithWorkplaceToDTO() {
        // Given
        User user = createValidEntity();
        Workplace workplace = new Workplace();
        workplace.setId(1L);
        workplace.setName("Test Workplace");
        user.setWorkplace(workplace);
        
        WorkplaceDTO workplaceDTO = new WorkplaceDTO();
        workplaceDTO.setId(1L);
        workplaceDTO.setName("Test Workplace");
        
        when(workplaceConverter.toDTO(workplace)).thenReturn(workplaceDTO);
        
        // When
        UserDTO dto = userConverter.toDTO(user);
        
        // Then
        assertNotNull(dto);
        assertNotNull(dto.getWorkplace());
        assertEquals(workplace.getId(), dto.getWorkplace().getId());
        assertEquals(workplace.getName(), dto.getWorkplace().getName());
        verify(workplaceConverter).toDTO(workplace);
    }

    @Test
    @DisplayName("Should convert user entity without manager to DTO")
    public void testUserWithoutManagerToDTO() {
        // Given
        User user = createValidEntity();
        user.setManager(null);
        
        // When
        UserDTO dto = userConverter.toDTO(user);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getManager());
    }

    @Test
    @DisplayName("Should convert user entity without workplace to DTO")
    public void testUserWithoutWorkplaceToDTO() {
        // Given
        User user = createValidEntity();
        user.setWorkplace(null);
        
        // When
        UserDTO dto = userConverter.toDTO(user);
        
        // Then
        assertNotNull(dto);
        assertNull(dto.getWorkplace());
    }

    @Test
    @DisplayName("Should convert DTO with manager to entity")
    public void testDTOWithManagerToEntity() {
        // Given
        UserDTO dto = createValidDTO();
        MiniUserDTO managerDTO = new MiniUserDTO();
        managerDTO.setId(2L);
        dto.setManager(managerDTO);
        
        User manager = createValidEntity();
        manager.setId(2L);
        
        when(entityManager.find(User.class, 2L)).thenReturn(manager);
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getManager());
        assertEquals(manager.getId(), entity.getManager().getId());
    }

    @Test
    @DisplayName("Should convert DTO with workplace to entity")
    public void testDTOWithWorkplaceToEntity() {
        // Given
        UserDTO dto = createValidDTO();
        WorkplaceDTO workplaceDTO = new WorkplaceDTO();
        workplaceDTO.setId(1L);
        dto.setWorkplace(workplaceDTO);
        
        Workplace workplace = new Workplace();
        workplace.setId(1L);
        
        when(entityManager.find(Workplace.class, 1L)).thenReturn(workplace);
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNotNull(entity.getWorkplace());
        assertEquals(workplace.getId(), entity.getWorkplace().getId());
    }

    @Test
    @DisplayName("Should handle DTO with new ID (0) correctly")
    public void testDTOWithNewIdToEntity() {
        // Given
        UserDTO dto = createValidDTO();
        dto.setId(0L); // New entity
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        // Should create new entity, not look up existing one
        verify(entityManager, never()).find(eq(User.class), any());
    }

    @Test
    @DisplayName("Should handle DTO with existing ID correctly")
    public void testDTOWithExistingIdToEntity() {
        // Given
        UserDTO dto = createValidDTO();
        dto.setId(1L); // Existing entity
        
        User existingUser = createValidEntity();
        when(entityManager.find(User.class, 1L)).thenReturn(existingUser);
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        verify(entityManager).find(User.class, 1L);
    }

    @Test
    @DisplayName("Should handle manager not found gracefully")
    public void testManagerNotFoundInEntity() {
        // Given
        UserDTO dto = createValidDTO();
        MiniUserDTO managerDTO = new MiniUserDTO();
        managerDTO.setId(999L); // Non-existent manager
        dto.setManager(managerDTO);
        
        when(entityManager.find(User.class, 999L)).thenReturn(null);
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getManager());
    }

    @Test
    @DisplayName("Should handle workplace not found gracefully")
    public void testWorkplaceNotFoundInEntity() {
        // Given
        UserDTO dto = createValidDTO();
        WorkplaceDTO workplaceDTO = new WorkplaceDTO();
        workplaceDTO.setId(999L); // Non-existent workplace
        dto.setWorkplace(workplaceDTO);
        
        when(entityManager.find(Workplace.class, 999L)).thenReturn(null);
        
        // When
        User entity = userConverter.toEntity(dto);
        
        // Then
        assertNotNull(entity);
        assertNull(entity.getWorkplace());
    }
}
