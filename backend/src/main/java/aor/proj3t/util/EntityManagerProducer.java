package aor.proj3t.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
/**
 * CDI producer class for {@link EntityManager} instances.
 * <p>
 * This utility enables injection of JPA-managed {@link EntityManager} across the application
 * via the `@Inject` annotation. It ties into the defined persistence unit in `persistence.xml`
 * and ensures consistent transactional access to database operations.
 * </p>
 *
 * Best practices:
 * - Keep the persistence unit name aligned with configuration
 * - Use with `@Transactional` methods for automatic transaction management
 * - Avoid manually closing the injected EntityManager
 *
 * Example usage:
 * <pre>{@code
 *     @Inject
 *     private EntityManager em;
 * }</pre>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class EntityManagerProducer {

    /**
     * Injects the EntityManager tied to the "jorgeosorio-proj3t" persistence unit.
     * The name must match the one declared in `persistence.xml`.
     */
    @PersistenceContext(unitName = "jorgeosorio-proj3t")
    private EntityManager entityManager;

    /**
     * Exposes the injected {@link EntityManager} as a CDI producer.
     *
     * @return the active EntityManager
     */
    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
