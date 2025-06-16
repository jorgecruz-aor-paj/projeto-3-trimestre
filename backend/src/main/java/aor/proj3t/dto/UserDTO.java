package aor.proj3t.dto;

import java.time.Instant;
import java.time.LocalDate;

import aor.proj3t.enums.Role;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for User entity
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO {
    @XmlElement(name = "id")
    private Long id;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement(name = "phoneNumber")
    private String phoneNumber;

    @XmlElement(name = "address")
    private String address;

    @XmlElement(name = "birthDate")
    private LocalDate birthDate;

    @XmlElement(name = "biography")
    private String biography;

    @XmlElement(name = "createdAt")
    private Instant createdAt;
    @XmlElement(name = "photograph")
    private String photograph;

    @XmlElement(name = "role")
    private Role role;

    @XmlElement(name = "managerId")
    private Long managerId; // Only the ID is passed to avoid recursive references

    @XmlElement(name = "workplaceId")
    private Long workplaceId;

    public UserDTO() {
    }

    public UserDTO(long id, String email, String firstName, String lastName, String phoneNumber, String address,
            LocalDate birthDate, String biography, Instant createdAt, String photograph, Role role,
            Long managerId, Long workplaceId) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDate = birthDate;
        this.biography = biography;
        this.createdAt = createdAt;
        this.photograph = photograph;
        this.role = role;
        this.managerId = managerId;
        this.workplaceId = workplaceId;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }
}
