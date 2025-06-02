package aor.proj3t.daos;


import aor.proj3t.entities.EvaluationCycle;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the EvaluationCycleRepository interface.
 */
@Stateless
public class EvaluationCycleDAOimpl extends AbstractDAO<EvaluationCycle, Long> implements EvaluationCycleDAO {

    @Override
    public List<EvaluationCycle> findActive() {
        return executeNamedQuery("EvaluationCycle.findActive");
    }

    @Override
    public List<EvaluationCycle> findByStatus(EvaluationStatus status) {
        TypedQuery<EvaluationCycle> query = em.createQuery(
                "SELECT ec FROM EvaluationCycle ec WHERE ec.status = :status", EvaluationCycle.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public Optional<EvaluationCycle> findLatest() {
        try {
            TypedQuery<EvaluationCycle> query = em.createNamedQuery("EvaluationCycle.findLatest", EvaluationCycle.class);
            query.setMaxResults(1);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean updateStatus(Long cycleId, EvaluationStatus status) {
        Query query = em.createQuery(
                "UPDATE EvaluationCycle ec SET ec.status = :status WHERE ec.id = :id");
        query.setParameter("status", status);
        query.setParameter("id", cycleId);
        return query.executeUpdate() > 0;
    }
}
