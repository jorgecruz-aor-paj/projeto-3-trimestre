package aor.proj3t.unit.service.impl;

import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.service.impl.BaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BaseServiceTest {

    // Test implementation of BaseService for testing abstract class
    private static class TestBaseService extends BaseService {
        public <T> T testGetEntity(Optional<T> entityOpt, Long id, String fieldName) {
            return getEntity(entityOpt, id, fieldName);
        }

        public void testCheckIfRequesterIsAdmin(User user) {
            checkIfRequesterIsAdmin(user);
        }
    }

    private TestBaseService baseService;
    private User user;
    private static final Long USER_ID = 1L;
    private static final String FIELD_NAME = "User";

    @BeforeEach
    void setUp() {
        baseService = new TestBaseService();
        user = new User();
        user.setId(USER_ID);
    }

    @Test
    @DisplayName("getEntity should return entity when present")
    void getEntity_WhenEntityPresent_ShouldReturnEntity() {
        // Given
        Optional<User> userOpt = Optional.of(user);

        // When
        User result = baseService.testGetEntity(userOpt, USER_ID, FIELD_NAME);

        // Then
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("getEntity should throw NotFoundException when entity not present")
    void getEntity_WhenEntityNotPresent_ShouldThrowNotFoundException() {
        // Given
        Optional<User> userOpt = Optional.empty();

        // When/Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> baseService.testGetEntity(userOpt, USER_ID, FIELD_NAME));
        
        assertTrue(exception.getMessage().contains(FIELD_NAME));
        assertTrue(exception.getMessage().contains(USER_ID.toString()));
    }

    @Test
    @DisplayName("checkIfRequesterIsAdmin should not throw exception for admin user")
    void checkIfRequesterIsAdmin_WhenUserIsAdmin_ShouldNotThrow() {
        // Given
        user.setRole(Role.ADMIN);

        // When/Then
        assertDoesNotThrow(() -> baseService.testCheckIfRequesterIsAdmin(user));
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN"}, mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("checkIfRequesterIsAdmin should throw ForbiddenException for non-admin roles")
    void checkIfRequesterIsAdmin_WhenUserIsNotAdmin_ShouldThrowForbiddenException(Role role) {
        // Given
        user.setRole(role);

        // When/Then
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> baseService.testCheckIfRequesterIsAdmin(user));
        
        assertEquals("Permission denied", exception.getUserMessage());
        assertTrue(exception.getMessage().contains(USER_ID.toString()));
    }
}
