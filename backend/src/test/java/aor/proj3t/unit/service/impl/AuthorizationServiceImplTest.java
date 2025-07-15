package aor.proj3t.unit.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.service.impl.AuthorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {

    // Test constants
    private static final Long ADMIN_ID = 1L;
    private static final Long MANAGER_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final Long OTHER_USER_ID = 4L;
    private static final Long MANAGED_USER_ID = 5L;
    private static final Long NON_EXISTENT_USER_ID = 999L;

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private User admin;
    private User manager;
    private User user;
    private User otherUser;
    private User managedUser;

    @BeforeEach
    void setUp() {
        // Configure DAO mocks with lenient stubbing to avoid unnecessary stubbing exceptions
        lenient().when(daoFactory.forUser()).thenReturn(userDAO);

        // Set up admin user
        admin = new User();
        admin.setId(ADMIN_ID);
        admin.setRole(Role.ADMIN);
        admin.setFirstName("Admin");
        admin.setLastName("User");

        // Set up manager user
        manager = new User();
        manager.setId(MANAGER_ID);
        manager.setRole(Role.MANAGER);
        manager.setFirstName("Manager");
        manager.setLastName("User");

        // Set up regular user
        user = new User();
        user.setId(USER_ID);
        user.setRole(Role.USER);
        user.setFirstName("Regular");
        user.setLastName("User");
        user.setManager(manager); // User is managed by the manager

        // Set up another regular user (not managed by the manager)
        otherUser = new User();
        otherUser.setId(OTHER_USER_ID);
        otherUser.setRole(Role.USER);
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");

        // Set up user managed by the manager
        managedUser = new User();
        managedUser.setId(MANAGED_USER_ID);
        managedUser.setRole(Role.USER);
        managedUser.setFirstName("Managed");
        managedUser.setLastName("User");
        managedUser.setManager(manager);
    }

    // ===== ADMIN AUTHORIZATION TESTS =====

    @Test
    @DisplayName("Admin should have access to all evaluations regardless of parameters")
    void authorizeEvaluationAccess_WhenAdminUser_ShouldAllowAllAccess() {
        // Test with both evaluatedId and evaluatorId specified
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(admin, OTHER_USER_ID, USER_ID));

        // Test with only evaluatedId specified
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(admin, OTHER_USER_ID, null));

        // Test with only evaluatorId specified
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(admin, null, USER_ID));

        // Test with no parameters specified
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(admin, null, null));

        // Verify no DAO calls are made for admin authorization
        verifyNoInteractions(userDAO);
    }

    // ===== REGULAR USER AUTHORIZATION TESTS =====

    @Test
    @DisplayName("Regular user should be allowed to access their own evaluations")
    void authorizeEvaluationAccess_WhenUserAccessesOwnEvaluations_ShouldAllow() {
        // User accessing their own evaluations
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(user, USER_ID, null));

        // User accessing evaluations with no filter
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(user, null, null));
    }

    @Test
    @DisplayName("Regular user should be forbidden from accessing other users' evaluations")
    void authorizeEvaluationAccess_WhenUserAccessesOtherUserEvaluations_ShouldThrowForbidden() {
        // User trying to access another user's evaluations
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> 
            authorizationService.authorizeEvaluationAccess(user, OTHER_USER_ID, null));

        assertEquals("Permission denied", exception.getUserMessage());
        assertTrue(exception.getMessage().contains("User with id " + USER_ID + " cannot access evaluations for user " + OTHER_USER_ID));
    }

    // ===== MANAGER AUTHORIZATION TESTS =====

    @Test
    @DisplayName("Manager should be allowed to access their own evaluations")
    void authorizeEvaluationAccess_WhenManagerAccessesOwnEvaluations_ShouldAllow() {
        // Manager accessing their own evaluations as evaluated
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, MANAGER_ID, null));

        // Manager accessing evaluations they conducted
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, null, MANAGER_ID));

        // Manager accessing their own evaluations with both parameters
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, MANAGER_ID, MANAGER_ID));
    }

    @Test
    @DisplayName("Manager should be allowed to access evaluations of their direct reports")
    void authorizeEvaluationAccess_WhenManagerAccessesDirectReportEvaluations_ShouldAllow() {
        // Mock the user lookup
        when(userDAO.findById(MANAGED_USER_ID)).thenReturn(Optional.of(managedUser));

        // Manager accessing evaluations of their direct report
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, MANAGED_USER_ID, null));

        verify(userDAO).findById(MANAGED_USER_ID);
    }

    @Test
    @DisplayName("Manager should be forbidden from accessing evaluations of users they don't manage")
    void authorizeEvaluationAccess_WhenManagerAccessesNonDirectReportEvaluations_ShouldThrowForbidden() {
        // Mock the user lookup
        when(userDAO.findById(OTHER_USER_ID)).thenReturn(Optional.of(otherUser));

        // Manager trying to access evaluations of user they don't manage
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> 
            authorizationService.authorizeEvaluationAccess(manager, OTHER_USER_ID, null));

        assertEquals("Permission denied", exception.getUserMessage());
        assertTrue(exception.getMessage().contains("Manager with id " + MANAGER_ID + " cannot access evaluations for user " + OTHER_USER_ID));

        verify(userDAO).findById(OTHER_USER_ID);
    }


    @Test
    @DisplayName("Manager should be forbidden when user they're trying to access has no manager")
    void authorizeEvaluationAccess_WhenTargetUserHasNoManager_ShouldThrowForbidden() {
        // Create user with no manager
        User userWithoutManager = new User();
        userWithoutManager.setId(OTHER_USER_ID);
        userWithoutManager.setRole(Role.USER);
        userWithoutManager.setManager(null);

        when(userDAO.findById(OTHER_USER_ID)).thenReturn(Optional.of(userWithoutManager));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> 
            authorizationService.authorizeEvaluationAccess(manager, OTHER_USER_ID, null));

        assertEquals("Permission denied", exception.getUserMessage());
        assertTrue(exception.getMessage().contains("Manager with id " + MANAGER_ID + " cannot access evaluations for user " + OTHER_USER_ID));
    }

    // ===== ERROR HANDLING TESTS =====

    @Test
    @DisplayName("Should throw NotFoundException when evaluated user is not found")
    void authorizeEvaluationAccess_WhenEvaluatedUserNotFound_ShouldThrowNotFoundException() {
        // Mock user not found
        when(userDAO.findById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Manager trying to access evaluations for non-existent user
        NotFoundException exception = assertThrows(NotFoundException.class, () -> 
            authorizationService.authorizeEvaluationAccess(manager, NON_EXISTENT_USER_ID, null));

        assertTrue(exception.getMessage().contains("User not found with ID: " + NON_EXISTENT_USER_ID));

        verify(userDAO).findById(NON_EXISTENT_USER_ID);
    }

    // ===== COMPLEX SCENARIO TESTS =====

    @Test
    @DisplayName("Manager should be allowed complex access scenarios for their direct reports")
    void authorizeEvaluationAccess_ComplexManagerScenarios_ShouldAllow() {
        // Mock user lookups
        when(userDAO.findById(MANAGED_USER_ID)).thenReturn(Optional.of(managedUser));

        // Manager accessing evaluations where they evaluate their direct report
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, MANAGED_USER_ID, MANAGER_ID));

        verify(userDAO).findById(MANAGED_USER_ID);
    }

    @Test
    @DisplayName("Should handle null parameters gracefully")
    void authorizeEvaluationAccess_WithNullParameters_ShouldHandleGracefully() {
        // All users should be able to access with null parameters (no specific filters)
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(admin, null, null));

        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(manager, null, null));

        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(user, null, null));

        // No DAO calls should be made with null parameters
        verifyNoInteractions(userDAO);
    }


    // ===== EDGE CASES =====

    @Test
    @DisplayName("Should handle case where user manager relationship is circular")
    void authorizeEvaluationAccess_WithCircularManagerRelationship_ShouldHandleCorrectly() {
        // Create users with circular reference (though this shouldn't happen in real data)
        User userA = new User();
        userA.setId(10L);
        userA.setRole(Role.MANAGER);

        User userB = new User();
        userB.setId(11L);
        userB.setRole(Role.MANAGER);
        userB.setManager(userA);

        // Set circular reference
        userA.setManager(userB);

        when(userDAO.findById(11L)).thenReturn(Optional.of(userB));

        // This should still work correctly - userA can access evaluations of userB
        assertDoesNotThrow(() -> 
            authorizationService.authorizeEvaluationAccess(userA, 11L, null));

        verify(userDAO).findById(11L);
    }
}
