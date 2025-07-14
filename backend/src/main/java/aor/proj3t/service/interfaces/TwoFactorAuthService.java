package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.exceptions.TwoFactorAuthException;

/**
 * Service interface for Two-Factor Authentication operations.
 * This interface defines the contract for TOTP setup, verification, backup
 * codes,
 * and 2FA management within the security framework of the application.
 * 
 * <p>
 * Key 2FA services include:
 * <ul>
 * <li><b>TOTP Setup:</b> Generate secret keys and QR codes for authenticator
 * apps</li>
 * <li><b>Code Verification:</b> Validate TOTP codes and backup codes during
 * login</li>
 * <li><b>Backup Code Management:</b> Generate and regenerate backup codes for
 * account recovery</li>
 * <li><b>2FA State Management:</b> Enable, disable, and check 2FA status for
 * users</li>
 * <li><b>Security Controls:</b> Replay attack prevention and time-based
 * validation</li>
 * </ul>
 * 
 * <p>
 * This service integrates with the authentication system to provide an
 * additional
 * layer of security through time-based one-time passwords (TOTP) following RFC
 * 6238
 * standards, ensuring compatibility with popular authenticator applications.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public interface TwoFactorAuthService {

    /**
     * Sets up 2FA for a user by generating a secret key and backup codes.
     * 
     * @param userId the user ID to set up 2FA for
     * @return the setup data including secret key and QR code URL
     * @throws TwoFactorAuthException if setup fails
     */
    TwoFactorSetupData setupTwoFactorAuth(Long userId);

    /**
     * Verifies a TOTP code and enables 2FA if verification is successful.
     * 
     * @param userId the user ID
     * @param code   the 6-digit TOTP code
     * @return true if verification successful and 2FA is now enabled
     * @throws TwoFactorAuthException if verification fails
     */
    boolean verifyAndEnableTwoFactorAuth(Long userId, String code);

    /**
     * Verifies a TOTP code for login purposes.
     * 
     * @param userId the user ID
     * @param code   the 6-digit TOTP code or backup code
     * @return true if verification successful
     * @throws TwoFactorAuthException if verification fails
     */
    boolean verifyTwoFactorCode(Long userId, String code);

    /**
     * Disables 2FA for a user.
     * 
     * @param userId   the user ID
     * @param password the user's password for confirmation
     * @return true if 2FA was successfully disabled
     * @throws TwoFactorAuthException if disabling fails
     */
    boolean disableTwoFactorAuth(Long userId, String password);

    /**
     * Generates new backup codes for a user.
     * 
     * @param userId the user ID
     * @return list of new backup codes
     * @throws TwoFactorAuthException if generation fails
     */
    List<String> regenerateBackupCodes(Long userId);

    /**
     * Checks if 2FA is enabled for a user.
     * 
     * @param userId the user ID
     * @return true if 2FA is enabled
     */
    boolean isTwoFactorEnabled(Long userId);

    /**
     * Data class for 2FA setup information.
     */
    public static class TwoFactorSetupData {
        private final String secretKey;
        private final String qrCodeUrl;
        private final List<String> backupCodes;

        public TwoFactorSetupData(String secretKey, String qrCodeUrl, List<String> backupCodes) {
            this.secretKey = secretKey;
            this.qrCodeUrl = qrCodeUrl;
            this.backupCodes = backupCodes;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public List<String> getBackupCodes() {
            return backupCodes;
        }
    }
}
