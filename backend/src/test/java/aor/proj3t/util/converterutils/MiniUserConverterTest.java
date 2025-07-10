package aor.proj3t.util.converterutils;

import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MiniUserConverter Tests")
public class MiniUserConverterTest {

    private MiniUserConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new MiniUserConverter();
    }

    @Test
    @DisplayName("Should convert User to MiniUserDTO successfully")
    public void testUserToMiniUserDTO() {
        // Given
        User user = createValidUser();
        
        // When
        MiniUserDTO dto = converter.toMiniUserDTO(user);
        
        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPhotograph(), dto.getPhotograph());
    }

    @Test
    @DisplayName("Should handle null user conversion gracefully")
    public void testNullUserToMiniUserDTO() {
        // When
        MiniUserDTO dto = converter.toMiniUserDTO(null);
        
        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should convert user list to MiniUserDTO list successfully")
    public void testUserListToMiniUserDTOList() {
        // Given
        User user1 = createValidUser();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        
        User user2 = createValidUser();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        
        List<User> userList = Arrays.asList(user1, user2);
        
        // When
        List<MiniUserDTO> dtoList = converter.toMiniUserDTOList(userList);
        
        // Then
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        
        MiniUserDTO dto1 = dtoList.get(0);
        assertNotNull(dto1);
        assertEquals(user1.getId(), dto1.getId());
        assertEquals(user1.getFirstName(), dto1.getFirstName());
        assertEquals(user1.getLastName(), dto1.getLastName());
        assertEquals(user1.getEmail(), dto1.getEmail());
        assertEquals(user1.getPhotograph(), dto1.getPhotograph());
        
        MiniUserDTO dto2 = dtoList.get(1);
        assertNotNull(dto2);
        assertEquals(user2.getId(), dto2.getId());
        assertEquals(user2.getFirstName(), dto2.getFirstName());
        assertEquals(user2.getLastName(), dto2.getLastName());
        assertEquals(user2.getEmail(), dto2.getEmail());
        assertEquals(user2.getPhotograph(), dto2.getPhotograph());
    }

    @Test
    @DisplayName("Should handle empty user list conversion")
    public void testEmptyUserListToMiniUserDTOList() {
        // Given
        List<User> userList = Arrays.asList();
        
        // When
        List<MiniUserDTO> dtoList = converter.toMiniUserDTOList(userList);
        
        // Then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    @DisplayName("Should handle user with null photograph")
    public void testUserWithNullPhotograph() {
        // Given
        User user = createValidUser();
        user.setPhotograph(null);
        
        // When
        MiniUserDTO dto = converter.toMiniUserDTO(user);
        
        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertNull(dto.getPhotograph());
    }

    @Test
    @DisplayName("Should handle user with empty photograph")
    public void testUserWithEmptyPhotograph() {
        // Given
        User user = createValidUser();
        user.setPhotograph("");
        
        // When
        MiniUserDTO dto = converter.toMiniUserDTO(user);
        
        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals("", dto.getPhotograph());
    }

    private User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setPhotograph("profile.jpg");
        return user;
    }
}
