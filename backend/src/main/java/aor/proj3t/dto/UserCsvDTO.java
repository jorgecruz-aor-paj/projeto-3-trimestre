package aor.proj3t.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;

/**
 * DTO specifically designed for CSV export of {@code User} data.
 * <p>
 * Uses OpenCSV annotations to map user attributes to structured CSV columns.
 * This DTO provides a flat representation of user profile metadata, optimized
 * for exporting data into spreadsheets, audits, or analytics pipelines.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the user
 * - {@code email}: Primary contact email of the user
 * - {@code firstName}: User's given name
 * - {@code lastName}: User's surname
 * - {@code phoneNumber}: Optional phone contact number
 * - {@code address}: User's residential or mailing address
 * - {@code birthdate}: User's date of birth, formatted as yyyy-MM-dd
 * - {@code role}: Current role or position held by the user
 * - {@code managerId}: ID of the user's assigned manager
 * - {@code manager}: Full name of the user's manager
 * - {@code workplace}: Name of the user's office or department
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class UserCsvDTO {

    /** Unique identifier for the user */
    @CsvBindByName(column = "ID")
    private Long id;

    /** Email address used for communication and login */
    @CsvBindByName(column = "Email")
    private String email;

    /** User's first name */
    @CsvBindByName(column = "First Name")
    private String firstName;

    /** User's last name or surname */
    @CsvBindByName(column = "Last Name")
    private String lastName;

    /** Phone number associated with the user */
    @CsvBindByName(column = "Phone Number")
    private String phoneNumber;

    /** Physical or mailing address of the user */
    @CsvBindByName(column = "Address")
    private String address;

    /** Birth date of the user in yyyy-MM-dd format */
    @CsvBindByName(column = "Birth Date")
    @CsvDate("yyyy-MM-dd")
    private LocalDate birthdate;

    /** Role or position of the user (e.g. ADMIN, EMPLOYEE) */
    @CsvBindByName(column = "Role")
    private String role;

    /** Unique identifier of the manager assigned to this user */
    @CsvBindByName(column = "Manager ID")
    private Long managerId;

    /** Full name of the manager */
    @CsvBindByName(column = "manager")
    private String manager;

    /** Name of the workplace or department the user is part of */
    @CsvBindByName(column = "Workplace")
    private String workplace;

    /** Default constructor required by OpenCSV */
    public UserCsvDTO() {}

    /**
     * Constructs a {@code UserCsvDTO} by extracting relevant fields from a {@link UserDTO}.
     *
     * @param userDTO the source user object
     */
    public UserCsvDTO(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.address = userDTO.getAddress();
        this.birthdate = userDTO.getBirthdate();
        this.role = userDTO.getRole() != null ? userDTO.getRole().toString() : null;
        if (userDTO.getManager() != null) {
            this.managerId = userDTO.getManager().getId();
            this.manager = userDTO.getManager().getFirstName() + " " + userDTO.getManager().getLastName();
        } else {
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