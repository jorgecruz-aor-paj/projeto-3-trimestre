package aor.proj3t.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;

/**
 * DTO specifically designed for CSV export of User data
 * Uses OpenCSV annotations for automatic CSV mapping
 */
public class UserCsvDTO {

    @CsvBindByName(column = "ID")
    private Long id;

    @CsvBindByName(column = "Email")
    private String email;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "Phone Number")
    private String phoneNumber;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "Birth Date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate birthdate;

    @CsvBindByName(column = "Role")
    private String role;

    @CsvBindByName(column = "Manager ID")
    private Long managerId;

    @CsvBindByName (column = "manager")
    private String manager;

    @CsvBindByName(column = "Workplace")
    private String workplace;

    // Default constructor required by OpenCSV
    public UserCsvDTO() {
    }

    // Constructor to convert from UserDTO
    public UserCsvDTO(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.address = userDTO.getAddress();
        this.birthdate = userDTO.getBirthdate();
        this.role = userDTO.getRole() != null ? userDTO.getRole().toString() : null;
        if(userDTO.getManager() != null) {
            this.managerId = userDTO.getManager().getId();
            this.manager = userDTO.getManager().getFirstName() + " " + userDTO.getManager().getLastName();
        }else{
            this.managerId = null;
            this.manager = null;
        }
        this.workplace = userDTO.getWorkplace().getName();
    }

    // Getters and setters
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}