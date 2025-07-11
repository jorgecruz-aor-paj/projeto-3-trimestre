package aor.proj3t.unit.entities;

import aor.proj3t.entities.Session;
import aor.proj3t.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Session entity.
 */
public class SessionTest {

    @Test
    @DisplayName("Should create a session with correct values")
    public void testCreateSession() {
        // Arrange
        String sessionToken = "test.jwt.token";
        String ipAddress = "127.0.0.1";
        String userAgent = "Mozilla/5.0";
        User user = new User();
        user.setId(1L);
        Instant expirationDate = Instant.now().plusSeconds(3600);
        
        // Act
        Session session = new Session(sessionToken, ipAddress, userAgent, user, expirationDate);
        
        // Assert
        assertEquals(sessionToken, session.getSessionToken(), "Session token should match");
        assertEquals(ipAddress, session.getIpAddress(), "IP address should match");
        assertEquals(userAgent, session.getUserAgent(), "User agent should match");
        assertEquals(user, session.getUser(), "User should match");
        assertEquals(expirationDate, session.getExpirationDate(), "Expiration date should match");
        assertTrue(session.getActive(), "Session should be active by default");
    }

    @Test
    @DisplayName("Should set session as inactive")
    public void testSetInactiveSession() {
        // Arrange
        Session session = new Session();
        session.setActive(true);
        
        // Act
        session.setActive(false);
        
        // Assert
        assertFalse(session.getActive(), "Session should be inactive");
    }
    
    @Test
    @DisplayName("Should set and get all properties correctly")
    public void testSettersAndGetters() {
        // Arrange
        Session session = new Session();
        Long id = 1L;
        String sessionToken = "updated.jwt.token";
        String ipAddress = "192.168.1.1";
        String userAgent = "Chrome/91.0";
        User user = new User();
        user.setId(2L);
        Instant createdAt = Instant.now();
        Instant expirationDate = Instant.now().plusSeconds(7200);
        
        // Act
        session.setId(id);
        session.setSessionToken(sessionToken);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setUser(user);
        session.setCreatedAt(createdAt);
        session.setExpirationDate(expirationDate);
        session.setActive(false);
        
        // Assert
        assertEquals(id, session.getId(), "ID should match");
        assertEquals(sessionToken, session.getSessionToken(), "Session token should match");
        assertEquals(ipAddress, session.getIpAddress(), "IP address should match");
        assertEquals(userAgent, session.getUserAgent(), "User agent should match");
        assertEquals(user, session.getUser(), "User should match");
        assertEquals(createdAt, session.getCreatedAt(), "Created at should match");
        assertEquals(expirationDate, session.getExpirationDate(), "Expiration date should match");
        assertFalse(session.getActive(), "Session should be inactive");
    }
}
