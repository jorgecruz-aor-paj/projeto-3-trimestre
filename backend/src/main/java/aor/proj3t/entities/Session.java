package aor.proj3t.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a user session in the system.
 * Sessions track user authentication state with tokens, expiration dates,
 * and additional security information like IP address and user agent.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "session")
public class Session {

    /**
     * Unique identifier for the session.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the session was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * JPA lifecycle callback to set creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    /**
     * Expiration date for the session.
     */
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    /**
     * Unique session token for authentication.
     */
    @Column(name = "token", nullable = false, unique = true, length = 800)
    private String sessionToken;

    /**
     * Flag indicating whether the session is currently active.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * IP address from which the session was created.
     */
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    /**
     * User agent string from the client browser/application.
     */
    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    /**
     * The user this session belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Default constructor for JPA.
     */
    public Session() {
    }

    /**
     * Constructor to create a session with all required parameters.
     * 
     * @param sessionToken   the unique session token
     * @param ipAddress      the client IP address
     * @param userAgent      the client user agent
     * @param user           the user this session belongs to
     * @param expirationDate the session expiration date
     */
    public Session(String sessionToken, String ipAddress, String userAgent, User user, Instant expirationDate) {
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the unique identifier of this session.
     * 
     * @return the session ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this session.
     * 
     * @param id the session ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the creation timestamp of this session.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this session.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the expiration date of this session.
     * 
     * @return the expiration date
     */
    public Instant getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of this session.
     * 
     * @param expirationDate the expiration date
     */
    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the session token.
     * 
     * @return the session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Sets the session token.
     * 
     * @param sessionToken the session token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * Gets the user this session belongs to.
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user this session belongs to.
     * 
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Checks if this session is currently active.
     * 
     * @return true if the session is active, false otherwise
     */
    public Boolean getActive() {
        return isActive;
    }

    /**
     * Sets the active status of this session.
     * 
     * @param active the active status
     */
    public void setActive(Boolean active) {
        isActive = active;
    }

    /**
     * Gets the IP address from which this session was created.
     * 
     * @return the IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address from which this session was created.
     * 
     * @param ipAddress the IP address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the user agent string from the client.
     * 
     * @return the user agent string
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent string from the client.
     * 
     * @param userAgent the user agent string
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
