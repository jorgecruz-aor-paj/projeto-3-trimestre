package aor.proj3t.daos.impl;

import java.util.List;

import org.hibernate.Hibernate;

import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link EvaluationCycleDAO} interface.
 * <p>
 * This class provides concrete implementation for managing evaluation cycles
 * in the performance evaluation system. It extends {@link AbstractDAO} to
 * leverage common CRUD operations and implements specific business logic
 * for evaluation cycle management.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Uses named queries for optimized database operations</li>
 * <li>Provides optional lazy loading control for evaluation collections</li>
 * <li>Implements efficient status update operations</li>
 * <li>Supports both active and all cycle retrieval with performance
 * optimization</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see EvaluationCycleDAO
 * @see AbstractDAO
 * @see EvaluationCycle
 */
@Stateless
public class EvaluationCycleDAOimpl extends AbstractDAO<EvaluationCycle, Long> implements EvaluationCycleDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code EvaluationCycle.class} to enable type-safe
     *           operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<EvaluationCycle> getEntityClass() {
        return EvaluationCycle.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the named query "EvaluationCycle.findActive" for
     * optimized
     * performance. When evaluation data is requested, uses Hibernate's lazy loading
     * initialization to fetch the evaluation collections.
     * </p>
     *
     * @implNote The method uses {@link Hibernate#initialize(Object)} to force
     *           loading
     *           of lazy collections when requested, which helps avoid
     *           LazyInitializationException
     *           when the evaluation data is accessed outside the persistence
     *           context.
     */
    @Override
    public List<EvaluationCycle> findActive(boolean getEvaluations) {
        List<EvaluationCycle> cycles = executeNamedQuery("EvaluationCycle.findActive", EvaluationCycle.class);
        if (getEvaluations) {
            // Initialize evaluations list for each cycle (if needed)
            for (EvaluationCycle cycle : cycles) {
                Hibernate.initialize(cycle.getEvaluations());
            }
        }
        return cycles;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the named query "EvaluationCycle.countActive" which
     * provides an optimized count operation without loading full entity objects.
     * </p>
     *
     * @implNote Uses direct EntityManager query execution for maximum performance
     *           as counting operations don't require entity materialization.
     */
    @Override
    public Long countActiveEvaluationCycles() {
        return em
                .createNamedQuery("EvaluationCycle.countActive", Long.class)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses the named query "EvaluationCycle.findAll" with optional
     * evaluation data loading. This provides flexibility for different use cases
     * where evaluation details may or may not be needed.
     * </p>
     *
     * @implNote Similar to {@link #findActive(boolean)}, uses Hibernate
     *           initialization
     *           to control lazy loading behavior based on the loadEvaluations
     *           parameter.
     */
    @Override
    public List<EvaluationCycle> findAll(boolean loadEvaluations) {
        List<EvaluationCycle> cycles = executeNamedQuery("EvaluationCycle.findAll", EvaluationCycle.class);
        if (loadEvaluations) {
            for (EvaluationCycle cycle : cycles) {
                Hibernate.initialize(cycle.getEvaluations());
            }
        }
        return cycles;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation uses a direct JPQL UPDATE query for efficient status updates
     * without requiring entity loading. This approach provides better performance
     * for bulk update operations.
     * </p>
     *
     * @implNote Uses {@link jakarta.persistence.Query#executeUpdate()} which
     *           returns
     *           the number of affected rows. The method doesn't verify if the cycle
     *           exists before updating, following the fire-and-forget pattern for
     *           efficiency.
     */
    @Override
    public void updateStatus(Long cycleId, EvaluationCycleStatus status) {
        em.createQuery(
                "UPDATE EvaluationCycle ec SET ec.status = :status WHERE ec.id = :id")
                .setParameter("id", cycleId)
                .setParameter("status", status)
                .executeUpdate();

    }

}
