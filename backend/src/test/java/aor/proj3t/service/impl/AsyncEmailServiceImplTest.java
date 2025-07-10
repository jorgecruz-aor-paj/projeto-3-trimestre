package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.util.EmailUtil;

@ExtendWith(MockitoExtension.class)
class AsyncEmailServiceImplTest {

    @Mock
    private DAOFactory daoFactory;

    @Mock
    private EmailUtil emailUtil;

    @InjectMocks
    private AsyncEmailServiceImpl asyncEmailService;

    private EvaluationCycle testCycle;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        testCycle = new EvaluationCycle();
        testCycle.setId(1L);
        testCycle.setClosureDate(Instant.now());

        testUsers = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("John");
        testUsers.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        testUsers.add(user2);
    }

    @Test
    void testSendCycleClosureEmailAsync_Success() {
        // Arrange
        when(daoFactory.forEvaluationCycle()).thenReturn(mock(aor.proj3t.daos.interfaces.EvaluationCycleDAO.class));
        when(daoFactory.forEvaluationCycle().findById(1L)).thenReturn(Optional.of(testCycle));
        when(daoFactory.forUser()).thenReturn(mock(aor.proj3t.daos.interfaces.UserDAO.class));
        when(daoFactory.forUser().findAll()).thenReturn(testUsers);

        // Act
        asyncEmailService.sendCycleClosureEmailAsync(1L);

        // Assert
        verify(emailUtil, times(2)).sendEmail(anyString(), eq("Evaluation Cycle Closed"), anyString());
        verify(emailUtil).sendEmail(eq("user1@example.com"), eq("Evaluation Cycle Closed"), anyString());
        verify(emailUtil).sendEmail(eq("user2@example.com"), eq("Evaluation Cycle Closed"), anyString());
    }

    @Test
    void testSendCycleClosureEmailAsync_CycleNotFound() {
        // Arrange
        when(daoFactory.forEvaluationCycle()).thenReturn(mock(aor.proj3t.daos.interfaces.EvaluationCycleDAO.class));
        when(daoFactory.forEvaluationCycle().findById(1L)).thenReturn(Optional.empty());

        // Act
        asyncEmailService.sendCycleClosureEmailAsync(1L);

        // Assert
        verify(emailUtil, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
