package aor.proj3t.daos.interfaces;

import java.util.Optional;

import aor.proj3t.entities.Settings;

/**
 * Data Access Object interface for Settings entity operations.
 * <p>
 * This interface extends {@link BasicDAO} to provide operations for managing
 * application-wide configuration settings. Settings entities store system
 * configuration parameters that control application behavior and can be
 * modified at runtime without code changes.
 * </p>
 * <p>
 * Key features include:
 * </p>
 * <ul>
 * <li>Retrieving current/latest application settings</li>
 * <li>Configuration versioning support</li>
 * <li>Runtime configuration management</li>
 * <li>System parameter storage and retrieval</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see Settings
 * @see BasicDAO
 */
public interface SettingsDAO extends BasicDAO<Settings, Long> {

    /**
     * Retrieves the most recent settings configuration.
     * <p>
     * This method returns the latest version of application settings, which
     * represents the current active configuration. If multiple settings
     * records exist (for versioning purposes), this method returns the
     * most recently created or updated one.
     * </p>
     *
     * @return An {@link Optional} containing the latest settings if found,
     *         or {@link Optional#empty()} if no settings exist in the system
     * @throws javax.persistence.PersistenceException if a database access error
     *                                                occurs
     */
    Optional<Settings> getLatestSettings();
}
