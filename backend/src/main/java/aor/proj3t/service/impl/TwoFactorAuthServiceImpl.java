package aor.proj3t.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.daos.interfaces.TwoFactorAuthDAO;
import aor.proj3t.daos.interfaces.UserDAO;
import aor.proj3t.entities.TwoFactorAuth;
import aor.proj3t.entities.User;
import aor.proj3t.exceptions.TwoFactorAuthException;
import aor.proj3t.service.interfaces.TwoFactorAuthService;
import aor.proj3t.util.authutils.PasswordUtil;
import aor.proj3t.util.authutils.TotpUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the TwoFactorAuthService interface.
 * This service manages Two-Factor Authentication operations including TOTP
 * setup,
 * verification, backup codes, and 2FA management within the security framework.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>TOTP secret key generation and QR code URL creation for authenticator
 * apps</li>
 * <li>Time-based one-time password verification with replay attack
 * prevention</li>
 * <li>Backup code generation, verification, and one-time usage management</li>
 * <li>2FA enablement/disablement with proper security validations</li>
 * <li>User 2FA status checking and management</li>
 * </ul>
 * 
 * <p>
 * This implementation follows RFC 6238 standards for TOTP and provides
 * compatibility with popular authenticator applications like Google
 * Authenticator,
 * Authy, and Microsoft Authenticator.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class TwoFactorAuthServiceImpl extends BaseService implements TwoFactorAuthService {

    private static final Logger logger = LogManager.getLogger(TwoFactorAuthServiceImpl.class);

    @Inject
    private DAOFactory daoFactory;

    @Inject
    private TotpUtil totpUtil;

    @Inject
    private PasswordUtil passwordUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public TwoFactorSetupData setupTwoFactorAuth(Long userId) {
        try {
            UserDAO userDAO = daoFactory.forUser();
            Optional<User> userOpt = userDAO.findById(userId);

            if (userOpt.isEmpty()) {
                throw new TwoFactorAuthException("User not found", "Invalid user");
            }

            User user = userOpt.get();
            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();

            // Check if user already has 2FA enabled
            Optional<TwoFactorAuth> existingTwoFactor = twoFactorDAO.findByUser(user);
            if (existingTwoFactor.isPresent() && existingTwoFactor.get().getIsEnabled()) {
                throw new TwoFactorAuthException("2FA already enabled",
                        "Two-factor authentication is already enabled for user with id: " + userId);
            }

            // Generate secret key and backup codes
            String secretKey = totpUtil.generateSecretKey();
            List<String> backupCodes = generateBackupCodes();

            // Create or update 2FA record
            TwoFactorAuth twoFactor;
            if (existingTwoFactor.isPresent()) {
                twoFactor = existingTwoFactor.get();
                twoFactor.setSecretKey(secretKey);
                twoFactor.setBackupCodes(String.join(",", backupCodes));
                twoFactor.setIsEnabled(false);
                twoFactor.setIsVerified(false);
                twoFactor.setLastUsedAt(null);
                twoFactorDAO.update(twoFactor);
            } else {
                twoFactor = new TwoFactorAuth();
                twoFactor.setUser(user);
                twoFactor.setSecretKey(secretKey);
                twoFactor.setBackupCodes(String.join(",", backupCodes));
                twoFactor.setIsEnabled(false);
                twoFactor.setIsVerified(false);
                twoFactorDAO.create(twoFactor);
            }

            // Generate QR code URL for authenticator apps
            String qrCodeUrl = totpUtil.generateQrCodeUrl(
                    user.getEmail(),
                    secretKey,
                    "Proj3T-AoR" // Application name
            );

            logger.info("2FA setup initiated for user: {}", userId);

            return new TwoFactorSetupData(secretKey, qrCodeUrl, backupCodes);

        } catch (Exception e) {
            logger.error("Failed to setup 2FA for user: {}", userId, e);
            throw new TwoFactorAuthException("Failed to setup 2FA", "Error setting up two-factor authentication", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyAndEnableTwoFactorAuth(Long userId, String code) {
        try {
            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();
            Optional<TwoFactorAuth> twoFactorOpt = twoFactorDAO.findByUserId(userId);

            if (twoFactorOpt.isEmpty()) {
                throw new TwoFactorAuthException("2FA not set up",
                        "Two-factor authentication is not set up for this user");
            }

            TwoFactorAuth twoFactor = twoFactorOpt.get();

            if (twoFactor.getIsEnabled()) {
                throw new TwoFactorAuthException("2FA already enabled", "Two-factor authentication is already enabled");
            }

            // Verify the TOTP code (no replay protection during setup)
            logger.debug("Verifying setup code for user {}: code={}, secretKey={}...{}",
                    userId, code,
                    twoFactor.getSecretKey().substring(0, Math.min(8, twoFactor.getSecretKey().length())),
                    twoFactor.getSecretKey().substring(Math.max(0, twoFactor.getSecretKey().length() - 8)));

            boolean isValid = totpUtil.verifyCode(code, twoFactor.getSecretKey(), null);

            if (!isValid) {
                logger.warn("Invalid 2FA verification code for user: {}", userId);
                throw new TwoFactorAuthException("Invalid verification code",
                        "The verification code is invalid or expired");
            }

            // Enable 2FA
            twoFactor.setIsEnabled(true);
            twoFactor.setIsVerified(true);
            twoFactor.setLastUsedAt(totpUtil.getCurrentTimeStepAsInstant());
            twoFactorDAO.update(twoFactor);

            logger.info("2FA enabled successfully for user: {}", userId);
            return true;

        } catch (TwoFactorAuthException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to verify and enable 2FA for user: {}", userId, e);
            throw new TwoFactorAuthException("Failed to enable 2FA", "Error enabling two-factor authentication", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyTwoFactorCode(Long userId, String code) {
        try {
            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();
            Optional<TwoFactorAuth> twoFactorOpt = twoFactorDAO.findByUserId(userId);

            if (twoFactorOpt.isEmpty()) {
                throw new TwoFactorAuthException("2FA not enabled",
                        "Two-factor authentication is not enabled for this user");
            }

            TwoFactorAuth twoFactor = twoFactorOpt.get();

            if (!twoFactor.getIsEnabled()) {
                throw new TwoFactorAuthException("2FA not enabled",
                        "Two-factor authentication is not enabled for this user");
            }

            // First try TOTP verification
            boolean isValid = totpUtil.verifyCode(code, twoFactor.getSecretKey(), twoFactor.getLastUsedAt());

            if (isValid) {
                // Update last used time to prevent replay attacks
                twoFactor.setLastUsedAt(totpUtil.getCurrentTimeStepAsInstant());
                twoFactorDAO.update(twoFactor);
                logger.info("2FA TOTP verification successful for user: {}", userId);
                return true;
            }

            // If TOTP fails, try backup codes
            if (code.length() == AppConstants.TwoFactorAuth.BACKUP_CODE_LENGTH && verifyBackupCode(twoFactor, code)) {
                twoFactorDAO.update(twoFactor);
                logger.info("2FA backup code verification successful for user: {}", userId);
                return true;
            }

            logger.warn("2FA verification failed for user: {}", userId);
            return false;

        } catch (TwoFactorAuthException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to verify 2FA code for user: {}", userId, e);
            throw new TwoFactorAuthException("Failed to verify 2FA", "Error verifying two-factor authentication", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean disableTwoFactorAuth(Long userId, String password) {
        try {
            UserDAO userDAO = daoFactory.forUser();
            Optional<User> userOpt = userDAO.findById(userId);

            if (userOpt.isEmpty()) {
                throw new TwoFactorAuthException("User not found", "Invalid user");
            }

            User user = userOpt.get();

            // Verify password for security
            if (!passwordUtil.verifyPassword(password, user.getPassword())) {
                logger.warn("Invalid password provided for 2FA disable attempt by user: {}", userId);
                throw new TwoFactorAuthException("Invalid password", "Password verification failed");
            }

            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();
            Optional<TwoFactorAuth> twoFactorOpt = twoFactorDAO.findByUser(user);

            if (twoFactorOpt.isEmpty()) {
                throw new TwoFactorAuthException("2FA not enabled",
                        "Two-factor authentication is not enabled for this user");
            }

            // Delete the 2FA record
            twoFactorDAO.deleteById(twoFactorOpt.get().getId());

            logger.info("2FA disabled successfully for user: {}", userId);
            return true;

        } catch (Exception e) {
            logger.error("Failed to disable 2FA for user: {}", userId, e);
            throw new TwoFactorAuthException("Failed to disable 2FA", "Error disabling two-factor authentication", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> regenerateBackupCodes(Long userId) {
        try {
            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();
            Optional<TwoFactorAuth> twoFactorOpt = twoFactorDAO.findByUserId(userId);

            if (twoFactorOpt.isEmpty() || !twoFactorOpt.get().getIsEnabled()) {
                throw new TwoFactorAuthException("2FA not enabled",
                        "Two-factor authentication is not enabled for this user");
            }

            TwoFactorAuth twoFactor = twoFactorOpt.get();
            List<String> newBackupCodes = generateBackupCodes();

            twoFactor.setBackupCodes(String.join(",", newBackupCodes));
            twoFactorDAO.update(twoFactor);

            logger.info("Backup codes regenerated for user: {}", userId);
            return newBackupCodes;

        } catch (Exception e) {
            logger.error("Failed to regenerate backup codes for user: {}", userId, e);
            throw new TwoFactorAuthException("Failed to regenerate backup codes", "Error generating new backup codes",
                    e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTwoFactorEnabled(Long userId) {
        try {
            TwoFactorAuthDAO twoFactorDAO = daoFactory.forTwoFactorAuth();
            return twoFactorDAO.isUserTwoFactorEnabled(userId);
        } catch (Exception e) {
            logger.error("Failed to check 2FA status for user: {}", userId, e);
            return false;
        }
    }

    /**
     * Generates backup codes for 2FA.
     * 
     * @return list of backup codes
     */
    private List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < AppConstants.TwoFactorAuth.BACKUP_CODES_COUNT; i++) {
            codes.add(generateBackupCode());
        }
        return codes;
    }

    /**
     * Generates a single backup code.
     * 
     * @return a backup code
     */
    private String generateBackupCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < AppConstants.TwoFactorAuth.BACKUP_CODE_LENGTH; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }

    /**
     * Verifies a backup code and removes it from the list if valid.
     * 
     * @param twoFactor the 2FA entity
     * @param code      the backup code to verify
     * @return true if the backup code is valid
     */
    private boolean verifyBackupCode(TwoFactorAuth twoFactor, String code) {
        String backupCodesStr = twoFactor.getBackupCodes();
        if (backupCodesStr == null || backupCodesStr.isEmpty()) {
            return false;
        }

        List<String> backupCodes = new ArrayList<>(Arrays.asList(backupCodesStr.split(",")));
        boolean removed = backupCodes.remove(code.toUpperCase());

        if (removed) {
            // Update the backup codes list (one-time use)
            twoFactor.setBackupCodes(String.join(",", backupCodes));
        }

        return removed;
    }
}
