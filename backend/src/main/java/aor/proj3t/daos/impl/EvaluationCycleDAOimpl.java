package aor.proj3t.daos.impl;

import java.util.Collections;
import java.util.List;

import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * Implementation of the EvaluationCycleRepository interface.
 */
@Stateless
public class EvaluationCycleDAOimpl extends AbstractDAO<EvaluationCycle, Long> implements EvaluationCycleDAO {

    @Override
    public List<EvaluationCycle> findActive() {
        try {
            return executeNamedQuery("EvaluationCycle.findActive", EvaluationCycle.class);
        } catch (NoResultException e) {
            return null;
        }
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
    public boolean updateStatus(Long cycleId, EvaluationStatus status) {
        try {
            int updatedRows = em.createQuery(
                    "UPDATE EvaluationCycle ec SET ec.status = :status WHERE ec.id = :id")
                    .setParameter("id", cycleId)
                    .setParameter("status", status)
                    .executeUpdate();

            return updatedRows > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

}
