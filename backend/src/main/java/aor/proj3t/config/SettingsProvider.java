package aor.proj3t.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.Settings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Application Settings Provider.
 * 
 * This class provides centralized access to application settings that can be
 * configured dynamically. It handles retrieval of settings from the database
 * and provides fallback to default values when settings are not available.
 * 
 * <p>
 * The provider is application-scoped, ensuring a single instance exists
 * throughout the application lifecycle and settings are cached appropriately.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class SettingsProvider {
    private static final Logger logger = LogManager.getLogger(SettingsProvider.class);

    /**
     * Data Access Object factory for database operations.
     */
    @Inject
    DAOFactory daoFactory;

    /**
     * Default settings used as fallback when database settings are unavailable.
     * These settings use the default values defined in {@link AppConstants}.
     */
    private static final Settings DEFAULT_SETTINGS = new Settings(
            AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME,
            AppConstants.Jwt.DEFAULT_TOKEN_EXPIRATION_TIME,
            AppConstants.Jwt.DEFAULT_RESET_PASSWORD_TOKEN_EXPIRATION_TIME);

    /**
     * Retrieves the current application settings.
     * 
     * <p>
     * This method attempts to fetch the latest settings from the database.
     * If the retrieval fails or no settings are found, it returns the default
     * settings defined in {@link #DEFAULT_SETTINGS}.
     * </p>
     * 
     * @return the current application settings, never null
     * @throws RuntimeException if there's an unexpected error during settings
     *                          retrieval
     */
    public Settings getSettings() {
        try {
            return daoFactory.forSettings().getLatestSettings().orElse(DEFAULT_SETTINGS);
        } catch (Exception e) {
            logger.error("Error retrieving application settings", e);
            return DEFAULT_SETTINGS;
        }
    }
}
