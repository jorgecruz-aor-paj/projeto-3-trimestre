package aor.proj3t.dto;

import aor.proj3t.enums.Role;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Data Transfer Object for {@code User} entity.
 * <p>
 * Represents a comprehensive user profile containing personal, contact, and employment-related data.
 * This DTO is commonly used across authentication flows, administrative dashboards, and user directories.
 * JAXB annotations support XML serialization for compatibility with XML-based APIs and integrations.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the user
 * - {@code email}: Primary contact email address
 * - {@code firstName}: User's given name
 * - {@code lastName}: User's family name or surname
 * - {@code phoneNumber}: Contact number (optional)
 * - {@code address}: User’s residence or mailing address
 * - {@code birthdate}: Date of birth in ISO format
 * - {@code biography}: Optional user-provided short bio or summary
 * - {@code createdAt}: Timestamp when the account was created
 * - {@code photograph}: Reference to the user’s profile image (e.g., filename or URL)
 * - {@code role}: Enum indicating the user's role (e.g., EMPLOYEE, MANAGER, ADMIN)
 * - {@code manager}: Lightweight representation of the user's assigned manager
 * - {@code workplace}: Workplace or department assigned to the user
 *
 * Common use cases:
 * - Registration and profile editing
 * - Admin-level user management
 * - Generating profile exports and audits
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO {

    /** Unique identifier of the user */
    @XmlElement(name = "id")
    private Long id;

    /** User’s email used for communication and login */
    @XmlElement(name = "email")
    private String email;

    /** First name or given name of the user */
    @XmlElement(name = "firstName")
    private String firstName;

    /** Last name or family name of the user */
    @XmlElement(name = "lastName")
    private String lastName;

    /** User’s phone number (optional) */
    @XmlElement(name = "phoneNumber")
    private String phoneNumber;

    /** User’s residential or mailing address */
    @XmlElement(name = "address")
    private String address;

    /** User’s date of birth */
    @XmlElement(name = "birthdate")
    private LocalDate birthdate;

    /** Optional biography text or personal summary */
    @XmlElement(name = "biography")
    private String biography;

    /** Timestamp of user account creation */
    @XmlElement(name = "createdAt")
    private Instant createdAt;

    /** Path or identifier for the user’s profile photograph */
    @XmlElement(name = "photograph")
    private String photograph;

    /** Role assigned to the user (e.g., ADMIN, MANAGER) */
    @XmlElement(name = "role")
    private Role role;

    /** Lightweight reference to the user’s assigned manager */
    @XmlElement(name = "manager")
    private MiniUserDTO manager; // Only the ID is passed to avoid recursive references

    /** Assigned workplace or department for the user */
    @XmlElement(name = "workplace")
    private WorkplaceDTO workplace;

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

    public MiniUserDTO getManager() {
        return manager;
    }

    public void setManager(MiniUserDTO manager) {
        this.manager = manager;
    }

    public WorkplaceDTO getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceDTO workplace) {
        this.workplace = workplace;
    }


}
