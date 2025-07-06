package aor.proj3t.config;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.Settings;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class SettingsProvider {
    private static final Logger logger = LogManager.getLogger(SettingsProvider.class);


    @Inject
    DAOFactory daoFactory;

    private static final Settings DEFAULT_SETTINGS = new Settings(
            AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME,
            AppConstants.Jwt.DEFAULT_TOKEN_EXPIRATION_TIME,
            AppConstants.Jwt.DEFAULT_RESET_PASSWORD_TOKEN_EXPIRATION_TIME
    );

    public Settings getSettings() {
        try {
            return daoFactory.forSettings().getLatestSettings().orElse(DEFAULT_SETTINGS);
        } catch (Exception e) {
            logger.error("Error retrieving application settings", e);
            return DEFAULT_SETTINGS;
        }
    }
}

