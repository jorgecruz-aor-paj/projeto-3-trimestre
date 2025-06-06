package aor.proj3t.util;

import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Password validation pattern: at least 8 chars, 1 uppercase, 1 lowercase, 1
    // digit, 1 special char
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$");

    /**
     * Hash a password using BCrypt.
     * 
     * @param password The plain text password
     * @return The hashed password
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verify a password against a hash.
     * 
     * @param password       The plain text password
     * @param hashedPassword The hashed password
     * @return true if the password matches the hash, false otherwise
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
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
