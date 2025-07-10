package aor.proj3t.util.authutils;

import aor.proj3t.config.AppConstants;
import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

@ApplicationScoped
public class PasswordUtil {

    private final int gensalt = AppConstants.Password.BCRYPT_WORK_FACTOR;

    // Password validation pattern: at least 8 chars, 1 uppercase, 1 lowercase, 1
    // digit, 1 special char, no spaces/tabs/newlines/null characters
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?!.*[ \\t\\n\\r\\f\\u0000\\u00A0]).{8,}$");

    /**
     * Hash a password using BCrypt.
     * 
     * @param password The plain text password
     * @return The hashed password
     * @throws IllegalArgumentException if password is null
     */
    public String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(gensalt));
    }

    /**
     * Verify a password against a hash.
     * 
     * @param password       The plain text password
     * @param hashedPassword The hashed password
     * @return true if the password matches the hash, false otherwise
     * @throws IllegalArgumentException if hashedPassword is null
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword == null) {
            throw new IllegalArgumentException("Hashed password cannot be null");
        }
        if (password == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw IllegalArgumentException for invalid hash format
        }
    }

    /**
     * Validate that a password meets strength requirements.
     * 
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
