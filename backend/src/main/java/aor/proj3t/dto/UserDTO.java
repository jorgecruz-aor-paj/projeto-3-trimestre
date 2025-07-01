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

    @XmlElement(name = "birthdate")
    private LocalDate birthdate;

    @XmlElement(name = "biography")
    private String biography;

    @XmlElement(name = "createdAt")
    private Instant createdAt;
    
    @XmlElement(name = "photograph")
    private String photograph;

    @XmlElement(name = "role")
    private Role role;

    @XmlElement(name = "manager")
    private MiniUserDTO manager; // Only the ID is passed to avoid recursive references

    @XmlElement(name = "workplace")
    private WorkplaceDTO workplace;

    public UserDTO() {
    }

    public UserDTO(long id, String email, String firstName, String lastName, String phoneNumber, String address,
                   LocalDate birthdate, String biography, Instant createdAt, String photograph, Role role,
                   MiniUserDTO manager, WorkplaceDTO workplace) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthdate = birthdate;
        this.biography = biography;
        this.createdAt = createdAt;
        this.photograph = photograph;
        this.role = role;
        this.manager = manager;
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
