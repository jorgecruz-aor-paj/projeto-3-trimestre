package aor.proj3t.service.interfaces;

import java.util.List;
import java.util.Map;

import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.filters.UserFilter;

/**
 * Service interface for user management operations.
 * This interface defines the contract for comprehensive user lifecycle
 * management
 * including registration, authentication, profile management, role assignment,
 * and user data operations.
 * 
 * <p>
 * Key service areas include:
 * <ul>
 * <li><b>User Registration & Confirmation:</b> Email-based registration with
 * confirmation workflow</li>
 * <li><b>Profile Management:</b> User profile updates, password changes, and
 * profile picture management</li>
 * <li><b>Role & Manager Assignment:</b> Bulk manager assignments and role
 * management operations</li>
 * <li><b>User Retrieval & Filtering:</b> Advanced user search and filtering
 * capabilities</li>
 * <li><b>Password Management:</b> Secure password reset and change
 * functionality</li>
 * <li><b>Statistics & Reporting:</b> User management statistics and
 * analytics</li>
 * </ul>
 * 
 * <p>
 * This service integrates with email utilities for notifications, file storage
 * for profile pictures,
 * and authorization services to ensure proper access controls throughout user
 * operations.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface UserService {

    /**
     * Retrieves a user by their unique identifier.
     * 
     * @param userId the unique identifier of the user to retrieve
     * @return a UserDTO containing the user's information
     * @throws NotFoundException if no user exists with the given ID
     */
    UserDTO getUser(Long userId);

    /**
     * Initiates the user registration process by creating a pending registration.
     * Sends a confirmation email with a token for account activation.
     * 
     * @param loginDTO  the login data containing email and password for
     *                  registration
     * @param ipAddress the IP address of the client initiating registration
     * @throws ConflictException if the email is already registered or pending
     *                           confirmation
     */
    void registerUser(LoginDTO loginDTO, String ipAddress);

    /**
     * Completes the user registration process by confirming the email and creating
     * the user account.
     * 
     * @param email               the email address of the user to confirm
     * @param userRegistrationDTO the registration data containing user details and
     *                            workplace
     * @throws NotFoundException if no pending registration exists for the email
     */
    void confirmUser(String email, UserRegistrationDTO userRegistrationDTO);

    /**
     * Changes a user's password by updating it with a new hashed password.
     * 
     * @param userId      the ID of the user whose password should be changed
     * @param newPassword the new password in plain text (will be hashed)
     * @throws NotFoundException if no user exists with the given ID
     */
    void changePassword(Long userId, String newPassword);

    /**
     * Assigns a manager to multiple users in a bulk operation.
     * Validates permissions and promotes the manager role if necessary.
     * 
     * @param userId           the ID of the admin user performing the assignment
     * @param managerAssignDto the data containing manager ID and user IDs to assign
     * @throws ForbiddenException if the requester is not an admin
     */
    void assignManagerBulk(Long userId, ManagerAssignDTO managerAssignDto);

    /**
     * Updates user information based on the provided UserDTO.
     * Validates permissions before allowing updates.
     * 
     * @param userId  the ID of the user making the update request
     * @param userDto the data containing updated user information
     * @throws ForbiddenException if the requester lacks permission to update the
     *                            target user
     */
    void updateUser(Long userId, UserDTO userDto);

    /**
     * Retrieves users based on specified filtering criteria.
     * 
     * @param requesterId the ID of the user making the request
     * @param userFilter  the filter criteria for user search
     * @return a list of UserDTO objects matching the filter criteria
     */
    List<UserDTO> getUsersByFilters(Long requesterId, UserFilter userFilter);

    /**
     * Sends a password reset link to the specified email address.
     * 
     * @param email     the email address to send the reset link to
     * @param ipAddress the IP address of the client requesting the reset
     */
    void sendResetPasswordLink(String email, String ipAddress);

    /**
     * Uploads and sets a new profile picture for a user.
     * Handles file validation, storage, and updates the user's photograph field.
     * 
     * @param requesterId   the ID of the user making the upload request
     * @param userId        the ID of the user whose profile picture is being
     *                      updated
     * @param fileUploadDTO the file upload data containing the image information
     * @return true if the upload was successful, false otherwise
     * @throws ForbiddenException if the requester lacks permission to upload for
     *                            the target user
     */
    Boolean uploadProfilePicture(Long requesterId, Long userId, FileUploadDTO fileUploadDTO);

    /**
     * Resets a user's profile picture to the default image.
     * 
     * @param requesterId the ID of the user making the reset request
     * @param userId      the ID of the user whose profile picture is being reset
     * @return true if the reset was successful, false otherwise
     * @throws ForbiddenException if the requester lacks permission to reset for the
     *                            target user
     */
    Boolean resetProfilePicture(Long requesterId, Long userId);

    /**
     * Retrieves all users with manager role.
     * This operation is restricted to admin users only.
     * 
     * @param requesterId the ID of the admin user making the request
     * @return a list of UserDTO objects representing all managers
     * @throws ForbiddenException if the requester is not an admin
     */
    List<UserDTO> getAllManagers(Long requesterId);

    /**
     * Retrieves statistics comparing total users versus unmanaged users.
     * Provides insights into user management coverage.
     * 
     * @param requesterId the ID of the admin user making the request
     * @return a map containing "total" and "unmanaged" user counts
     * @throws ForbiddenException if the requester is not an admin
     */
    Map<String, Long> getTotalVSUnmanagedUsers(Long requesterId);

    /**
     * Retrieves all users in a minimal format excluding admins by default.
     * Provides a lightweight user list for UI components.
     * 
     * @return a list of MiniUserDTO objects representing all non-admin users
     */
    List<MiniUserDTO> getAllMiniUsers();

    /**
     * Retrieves all users in a minimal format with option to include or exclude
     * admins.
     * 
     * @param includeAdmins whether to include admin users in the result set
     * @return a list of MiniUserDTO objects based on the admin inclusion flag
     */
    List<MiniUserDTO> getAllMiniUsers(boolean includeAdmins);
}
