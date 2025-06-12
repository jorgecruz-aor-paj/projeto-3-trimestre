package aor.proj3t.config;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    public static final class Jwt {
        private Jwt() {
            // Prevent instantiation
        }

        public static final int DEFAULT_TOKEN_EXPIRATION_TIME = 60; // value in minutes
        public static final int DEFAULT_RESET_PASSWORD_TOKEN_EXPIRATION_TIME = 60; // value in minutes
        public static final int DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME = 60; // value in minutes
    }

    /**
     * Constants for email configuration.
     * 
     * This class provides constants related to email functionality,
     * such as default template values and configuration settings.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class Email {
        private Email() {
            // Prevent instantiation
        }

        /**
         * Default subject for account confirmation emails
         */
        public static final String DEFAULT_CONFIRMATION_SUBJECT = "Confirm Your Account";

        /**
         * Default subject for password reset emails
         */
        public static final String DEFAULT_PASSWORD_RESET_SUBJECT = "Password Reset Request";

        /**
         * Default expiration time for confirmation links in minutes
         * (This is the same value as Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME)
         */
        public static final int DEFAULT_CONFIRMATION_EXPIRATION_MINUTES = 60;

        /**
         * Default expiration time for password reset links in minutes
         */
        public static final int DEFAULT_PASSWORD_RESET_EXPIRATION_MINUTES = 60;
    }

    public static final class FileStorage {
        private FileStorage() {
            // Prevent instantiation
        }

        /**
         * Default directory for storing profile pictures.
         * This directory is relative to the JBoss server's data directory.
         */
        public static final String DEFAULT_PROFILE_PICTURE_DIR = "profile-pictures";

        /**
         * Default profile picture file name.
         */
        public static final String DEFAULT_PROFILE_PICTURE = "default-profile-picture.png";

        /**
         * Maximum file size for uploads in bytes.
         * This is set to 5 MB (5,000,000 bytes).
         */
        public static final int MAX_FILE_SIZE = 5_000_000;
    }
}
