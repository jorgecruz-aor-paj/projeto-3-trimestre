package aor.proj3t.entities;

import aor.proj3t.enums.Role;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")

@NamedQueries({
        @NamedQuery(name = "users.allUsersHaveManager", query = "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL"),
        @NamedQuery(
                name = "User.findManagersWithoutUsers",
                query = "SELECT u FROM User u WHERE u.role = :role AND SIZE(u.managedUsers) = 0"
        ),
        @NamedQuery(
                name = "User.findAllManagers",
                query = "SELECT u FROM User u WHERE u.role = :role"
        ),
        @NamedQuery(
                name = "User.countNonAdmins",
                query = "SELECT COUNT(u) FROM User u WHERE u.role <> :adminRole"
        ),
        @NamedQuery(
                name = "User.countWithoutManagerAndNotAdmin",
                query = "SELECT COUNT(u) FROM User u WHERE u.manager IS NULL AND u.role <> :adminRole"
        )



})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthdate;

    @Column(name = "biography")
    private String biography;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @Column(name = "photograph")
    private String photograph;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<User> managedUsers;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, String phoneNumber, String Address, LocalDate birthdate, Role role, Workplace workplace) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<User> getManagedUsers() {
        return managedUsers;
    }

    public void setManagedUsers(List<User> managedUsers) {
        this.managedUsers = managedUsers;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

}
