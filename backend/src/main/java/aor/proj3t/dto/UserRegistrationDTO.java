package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

/**
 * Data Transfer Object for registering a new {@code User}.
 * <p>
 * Captures personal and demographic information required during user signup or onboarding.
 * Typically consumed by registration endpoints prior to account creation and role assignment.
 * JAXB annotations allow for XML payload serialization in service APIs.
 * </p>
 *
 * Attributes:
 * - {@code firstName}: User’s given name
 * - {@code lastName}: User’s family name or surname
 * - {@code phoneNumber}: Contact phone number (optional)
 * - {@code address}: Mailing or residential address
 * - {@code birthdate}: Date of birth in ISO format (e.g. yyyy-MM-dd)
 * - {@code biography}: Brief personal description or user summary
 * - {@code workplaceId}: Reference to the workplace entity the user will belong to
 *
 * Example usage:
 * <pre>{@code
 *     UserRegistrationDTO dto = new UserRegistrationDTO();
 *     dto.setFirstName("Ana");
 *     dto.setLastName("Silva");
 *     dto.setWorkplaceId(101L);
 * }</pre>
 *
 * Common in user portals, onboarding flows, and admin-assisted registration forms.
 *
 * Validation tip: {@code firstName}, {@code lastName}, and {@code workplaceId} should be mandatory.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "userRegistration")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserRegistrationDTO {

    /** User’s first name */
    @XmlElement(name = "firstName")
    private String firstName;

    /** User’s last name */
    @XmlElement(name = "lastName")
    private String lastName;

    /** Phone number for contact (optional) */
    @XmlElement(name = "phoneNumber")
    private String phoneNumber;

    /** Residential or mailing address */
    @XmlElement(name = "address")
    private String address;

    /** User’s date of birth */
    @XmlElement(name = "birthdate")
    private LocalDate birthdate;

    /** Brief biography or introductory note */
    @XmlElement(name = "biography")
    private String biography;

    /** ID of the workplace the user is associated with */
    @XmlElement(name = "workplaceId")
    private Long workplaceId;

    public UserRegistrationDTO() {
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

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }


}
