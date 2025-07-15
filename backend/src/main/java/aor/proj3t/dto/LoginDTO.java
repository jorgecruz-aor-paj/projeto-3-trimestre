package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for login requests.
 * <p>
 * Represents the authentication payload typically sent to login endpoints,
 * including user credentials such as email and password. JAXB annotations
 * enable XML-based integration for systems using XML request formats.
 * </p>
 *
 * Attributes:
 * - {@code email}: User's login identifier; must be a valid email format
 * - {@code password}: Secret credential used for authentication
 *
 * Common use cases include:
 * - Submitting login forms in RESTful authentication services
 * - Serializing user input for authentication-related service calls
 *
 * Example:
 * <pre>{@code
 *     LoginDTO dto = new LoginDTO("user@example.com", "securePassword123");
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "login")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginDTO {

    /** The email address used for login */
    @XmlElement(name = "email")
    private String email;

    /** The password associated with the account */
    @XmlElement(name = "password")
    private String password;

    public LoginDTO() {}

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
}

