package aor.proj3t.config;

import java.util.Set;

/**
 * Application-wide constants used throughout the system.
 * 
 * This utility class provides centralized access to various configuration
 * constants
 * used across different parts of the application, including JWT settings, email
 * configuration,
 * file storage parameters, and pagination defaults.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    /**
     * Constants for JWT (JSON Web Token) configuration.
     * 
     * This inner class contains constants related to token expiration times
     * and authentication scheme used in the application's security framework.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class Jwt {
        private Jwt() {
            // Prevent instantiation
        }

        /** Default token expiration time in minutes */
        public static final int DEFAULT_TOKEN_EXPIRATION_TIME = 60;

        /** Default reset password token expiration time in minutes */
        public static final int DEFAULT_RESET_PASSWORD_TOKEN_EXPIRATION_TIME = 60;

        /** Default account confirmation token expiration time in minutes */
        public static final int DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME = 1440;

        /** Authentication scheme used for JWT tokens */
        public static final String AUTHENTICATION_SCHEME = "Bearer";
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
         * Default expiration time for password reset links in minutes
         */
        public static final int DEFAULT_PASSWORD_RESET_EXPIRATION_MINUTES = 60;

        /**
         * Default url for registering a new account
         */
        public static final String EMAIL_BASE_URL = "https://localhost:3000";
    }

    /**
     * Constants for file storage configuration.
     * 
     * This class provides constants related to file storage functionality,
     * including directory paths, file size limitations, and allowed file types.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
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

        /**
         * Allowed MIME types for image uploads.
         * This set includes JPEG, PNG, and GIF formats.
         */
        public static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif");
    }

    /**
     * Constants for pagination configuration.
     * 
     * This class provides default values and limits for paginated data retrieval
     * operations throughout the application, including page size, page numbering,
     * and sorting parameters.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class Pagination {
        private Pagination() {
            // Prevent instantiation
        }

        /**
         * Default page size for paginated results.
         * This is set to 10 items per page.
         */
        public static final int DEFAULT_PAGE_SIZE = 10;

        /**
         * Default page number for paginated results.
         * This is set to the first page (0).
         */
        public static final int DEFAULT_PAGE_NUMBER = 0;

        /**
         * Maximum page size allowed for paginated results.
         * This is set to 100 items per page.
         */
        public static final int MAX_PAGE_SIZE = 100;

        /**
         * Default sort order for paginated results.
         * This is set to sort by "createdAt" in descending order.
         */
        public static final String DEFAULT_SORT_ORDER = "createdAt,desc";

        /**
         * Default sort field for paginated results.
         * This is set to "createdAt".
         */
        public static final String DEFAULT_SORT_FIELD = "createdAt";
    }

    public static final class Password {
        private Password() {
            // Prevent instantiation
        }

        public static final int BCRYPT_WORK_FACTOR = 12;

        /**
         * Default password length for user accounts.
         * This is set to 8 characters.
         */
        public static final int MIN_PASSWORD_LENGTH = 8;
    }
}
