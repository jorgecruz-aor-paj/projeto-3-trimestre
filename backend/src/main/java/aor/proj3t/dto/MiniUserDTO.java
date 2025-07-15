package aor.proj3t.dto;

/**
 * Compact Data Transfer Object for {@code User} entity.
 * <p>
 * Represents a minimal subset of user information, typically used for lightweight
 * references or summaries within other DTOs such as evaluations, notifications,
 * or dashboards. Avoids the overhead of full user profile details while retaining
 * key identity attributes.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the user
 * - {@code firstName}: User's first name
 * - {@code lastName}: User's last name
 * - {@code email}: User's primary email address
 * - {@code photograph}: Path or identifier for the user's profile image
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class MiniUserDTO {

    /** Unique identifier of the user */
    private Long id;

    /** First name of the user */
    private String firstName;

    /** Last name of the user */
    private String lastName;

    /** Email address associated with the user */
    private String email;

    /** Profile picture reference (e.g., filename or URL) */
    private String photograph;

    public MiniUserDTO() {}

    public MiniUserDTO(Long id, String firstName, String lastName, String email, String photograph) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photograph = photograph;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }
}

