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

        /** Two-factor authentication login token expiration time in minutes */
        public static final int TWO_FACTOR_LOGIN_EXPIRATION_TIME = 5;

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

    /**
     * Constants for Two-Factor Authentication configuration.
     * 
     * This class provides constants related to 2FA functionality,
     * including backup code settings and configuration parameters.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class TwoFactorAuth {
        private TwoFactorAuth() {
            // Prevent instantiation
        }

        /**
         * Number of backup codes to generate for 2FA.
         * This is set to 8 backup codes per user.
         */
        public static final int BACKUP_CODES_COUNT = 8;

        /**
         * Length of each backup code in characters.
         * This is set to 8 characters per backup code.
         */
        public static final int BACKUP_CODE_LENGTH = 8;
    }

    /**
     * Constants for input validation.
     * 
     * This class provides regex patterns and validation rules used throughout
     * the application for validating user input data.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class Validation {
        private Validation() {
            // Prevent instantiation
        }

        /**
         * Regular expression for email validation.
         * Validates email format with domain length restrictions.
         */
        public static final String EMAIL_REGEX = "^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

        /**
         * Regular expression for password validation.
         * Requires at least 8 characters, 1 uppercase, 1 lowercase, 1 digit,
         * 1 special character, and no whitespace/null characters.
         */
        public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*.])(?!.*[ \\t\\n\\r\\f\\u0000\\u00A0]).{8,}$";

        /**
         * Regular expression for phone number validation.
         * Validates Portuguese phone numbers with 9 digits.
         */
        public static final String PHONE_REGEX = "^\\d{9}$";

        /**
         * Regular expression for URL validation.
         * Validates HTTP, HTTPS, and FTP URLs.
         */
        public static final String URL_REGEX = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w.-]+)+(?:[/?#]\\S*)?$";

        /**
         * Minimum length for name fields.
         */
        public static final int MIN_NAME_LENGTH = 2;

        /**
         * Maximum length for name fields.
         */
        public static final int MAX_NAME_LENGTH = 30;

        /**
         * Minimum length for address fields.
         */
        public static final int MIN_ADDRESS_LENGTH = 2;

        /**
         * Maximum length for address fields.
         */
        public static final int MAX_ADDRESS_LENGTH = 100;

        /**
         * Minimum length for biography fields.
         */
        public static final int MIN_BIOGRAPHY_LENGTH = 2;

        /**
         * Maximum length for biography fields.
         */
        public static final int MAX_BIOGRAPHY_LENGTH = 250;

        /**
         * Maximum length for URL/link fields.
         */
        public static final int MAX_URL_LENGTH = 500;
    }

    /**
     * Constants for export file types and MIME types.
     * 
     * This class provides constants related to file export functionality,
     * including MIME types for various export formats.
     * 
     * @author Jorge Cruz & João Osório - Gr4
     * @version 1.0
     */
    public static final class Export {
        private Export() {
            // Prevent instantiation
        }

        /**
         * MIME type for CSV files
         */
        public static final String CSV_MIME_TYPE = "text/csv";

        /**
         * MIME type for XLSX files
         */
        public static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        /**
         * MIME type for PDF files
         */
        public static final String PDF_MIME_TYPE = "application/pdf";

        /**
         * File extension for CSV files
         */
        public static final String CSV_EXTENSION = ".csv";

        /**
         * File extension for XLSX files
         */
        public static final String XLSX_EXTENSION = ".xlsx";

        /**
         * File extension for PDF files
         */
        public static final String PDF_EXTENSION = ".pdf";
    }

}
