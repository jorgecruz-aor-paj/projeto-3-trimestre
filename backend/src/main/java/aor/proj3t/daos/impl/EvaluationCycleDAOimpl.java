package aor.proj3t.daos.impl;

import java.util.Collections;
import java.util.List;

import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

/**
 * Implementation of the EvaluationCycleRepository interface.
 */
@Stateless
public class EvaluationCycleDAOimpl extends AbstractDAO<EvaluationCycle, Long> implements EvaluationCycleDAO {

    @Override
    protected Class<EvaluationCycle> getEntityClass() {
        return EvaluationCycle.class;
    }

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

    @Override
    public Long countActiveEvaluationCycles() {
        return em
                .createNamedQuery("EvaluationCycle.countActive", Long.class)
                .getSingleResult();
    }


    @Override
    public List<EvaluationCycle> findByStatus(EvaluationStatus status) {
        try {
            TypedQuery<EvaluationCycle> query = em.createQuery(
                    "SELECT ec FROM EvaluationCycle ec WHERE ec.status = :status", EvaluationCycle.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<EvaluationCycle> findByStatus(EvaluationStatus status, int page, int size) {
        TypedQuery<EvaluationCycle> query = em.createNamedQuery("EvaluationCycle.findActive", EvaluationCycle.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public void updateStatus(Long cycleId, EvaluationCycleStatus status) {
        em.createQuery(
                        "UPDATE EvaluationCycle ec SET ec.status = :status WHERE ec.id = :id")
                .setParameter("id", cycleId)
                .setParameter("status", status)
                .executeUpdate();

    }

}
