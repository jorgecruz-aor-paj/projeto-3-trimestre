package aor.proj3t.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import aor.proj3t.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a user in the system.
 * Users are the main actors in the application and can have different roles
 * such as employees, managers, or administrators. They have hierarchical
 * relationships through manager-employee associations and belong to workplaces.
 * 
 * @author Jorge Osório
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "users")

@NamedQueries({
        @NamedQuery(name = "users.allUsersHaveManager", query = "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL"),
        @NamedQuery(name = "User.findManagersWithoutUsers", query = "SELECT u FROM User u WHERE u.role = :role AND SIZE(u.managedUsers) = 0"),
        @NamedQuery(name = "User.findAllManagers", query = "SELECT u FROM User u WHERE u.role = :role"),
        @NamedQuery(name = "User.countNonAdmins", query = "SELECT COUNT(u) FROM User u WHERE u.role <> :adminRole"),
        @NamedQuery(name = "User.countWithoutManagerAndNotAdmin", query = "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL AND u.role <> :adminRole")

})

public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    /**
     * Email address of the user, used for authentication and communication.
     */
    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    /**
     * Hashed password for user authentication.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * First name of the user.
     */
    @Column(name = "firstname", nullable = false)
    private String firstName;

    /**
     * Last name of the user.
     */
    @Column(name = "lastname", nullable = false)
    private String lastName;

    /**
     * Phone number of the user.
     */
    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phoneNumber;

    /**
     * Physical address of the user.
     */
    @Column(name = "address", nullable = false)
    private String address;

    /**
     * Birth date of the user.
     */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthdate;

    /**
     * Biography or personal description of the user.
     */
    @Column(name = "biography")
    private String biography;

    /**
     * Timestamp when the user account was created.
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
     * URL or path to the user's profile photograph.
     */
    @Column(name = "photograph")
    private String photograph;

    /**
     * Role of the user in the system (EMPLOYEE, MANAGER, ADMIN).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * The manager of this user in the organizational hierarchy.
     */
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    /**
     * List of users that this user manages.
     */
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<User> managedUsers;

    /**
     * The workplace/office where this user is located.
     */
    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructor to create a user with all basic information.
     * 
     * @param email       the user's email address
     * @param password    the user's hashed password
     * @param firstName   the user's first name
     * @param lastName    the user's last name
     * @param phoneNumber the user's phone number
     * @param Address     the user's physical address
     * @param birthdate   the user's birth date
     * @param role        the user's role in the system
     * @param workplace   the user's workplace
     */
    public User(String email, String password, String firstName, String lastName, String phoneNumber, String Address,
            LocalDate birthdate, Role role, Workplace workplace) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = Address;
        this.birthdate = birthdate;
        this.role = role;
        this.workplace = workplace;
    }

    /**
     * Gets the unique identifier of this user.
     * 
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this user.
     * 
     * @param id the user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the email address of this user.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of this user.
     * 
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the hashed password of this user.
     * 
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the hashed password of this user.
     * 
     * @param password the hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the first name of this user.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this user.
     * 
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of this user.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this user.
     * 
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the phone number of this user.
     * 
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of this user.
     * 
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the address of this user.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this user.
     * 
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the birth date of this user.
     * 
     * @return the birth date
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the birth date of this user.
     * 
     * @param birthdate the birth date
     */
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Gets the biography of this user.
     * 
     * @return the biography
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the biography of this user.
     * 
     * @param biography the biography
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Gets the creation timestamp of this user.
     * 
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this user.
     * 
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the photograph URL/path of this user.
     * 
     * @return the photograph URL/path
     */
    public String getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph URL/path of this user.
     * 
     * @param photograph the photograph URL/path
     */
    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    /**
     * Gets the role of this user.
     * 
     * @return the user role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of this user.
     * 
     * @param role the user role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the manager of this user.
     * 
     * @return the manager user
     */
    public User getManager() {
        return manager;
    }

    /**
     * Sets the manager of this user.
     * 
     * @param manager the manager user
     */
    public void setManager(User manager) {
        this.manager = manager;
    }

    /**
     * Gets the list of users managed by this user.
     * 
     * @return the list of managed users
     */
    public List<User> getManagedUsers() {
        return managedUsers;
    }

    /**
     * Sets the list of users managed by this user.
     * 
     * @param managedUsers the list of managed users
     */
    public void setManagedUsers(List<User> managedUsers) {
        this.managedUsers = managedUsers;
    }

    /**
     * Gets the workplace of this user.
     * 
     * @return the workplace
     */
    public Workplace getWorkplace() {
        return workplace;
    }

    /**
     * Sets the workplace of this user.
     * 
     * @param workplace the workplace
     */
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

}
