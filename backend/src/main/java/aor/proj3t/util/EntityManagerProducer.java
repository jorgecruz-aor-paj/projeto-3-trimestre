package aor.proj3t.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Produces EntityManager instances for CDI injection.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@ApplicationScoped
public class EntityManagerProducer {

    /**
     * The persistence unit name must match the one in persistence.xml
     */
    @PersistenceContext(unitName = "jorgeosorio-proj3t")
    private EntityManager entityManager;

    /**
     * Produces an EntityManager for CDI injection.
     * 
     * @return The entity manager
     */
    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
}