package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.SettingsDAO;
import aor.proj3t.entities.Settings;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

/**
 * Stateless EJB implementation of the {@link SettingsDAO} interface.
 * <p>
 * This class provides concrete implementation for managing application-wide
 * configuration settings. It extends {@link AbstractDAO} to leverage common
 * CRUD operations and implements specific business logic for configuration
 * management and versioning support.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Named query usage for optimized latest settings retrieval</li>
 * <li>Safe handling of settings lookup with Optional return types</li>
 * <li>Support for configuration versioning and history</li>
 * <li>Efficient single-result queries with proper limit handling</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see SettingsDAO
 * @see AbstractDAO
 * @see Settings
 */
@Stateless
public class SettingsDAOimpl extends AbstractDAO<Settings, Long> implements SettingsDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code Settings.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<Settings> getEntityClass() {
        return Settings.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the named query "Settings.getLatestSettings" for
     * optimized
     * retrieval of the most recent configuration. Uses result limit and safe
     * Optional handling to ensure only one result is returned.
     * </p>
     *
     * @implNote Uses {@link TypedQuery#setMaxResults(int)} with value 1 to ensure
     *           only the most recent settings record is retrieved, then processes
     *           the result list with stream operations for safe Optional handling.
     *           The named query should include appropriate ordering (e.g., by
     *           timestamp)
     *           to ensure the latest record is returned.
     */
    @Override
    public Optional<Settings> getLatestSettings() {
        TypedQuery<Settings> query = em.createNamedQuery("Settings.getLatestSettings", Settings.class);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

}
