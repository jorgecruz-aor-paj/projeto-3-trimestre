package aor.proj3t.daos.impl;


import aor.proj3t.daos.interfaces.EvaluationCycleDAO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.enums.EvaluationStatus;
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
        try {
            return executeNamedQuery("EvaluationCycle.findActive", EvaluationCycle.class);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<EvaluationCycle> findByStatus(EvaluationStatus status) {
        try {
            return executeNamedQuery("EvaluationCycle.findByStatus", EvaluationCycle.class, "status", status);
        } catch (NoResultException e) {
            return null;
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
            int updatedRows = em.createNamedQuery("EvaluationCycle.updateStatus")
                    .setParameter("id", cycleId)
                    .setParameter("status", status)
                    .executeUpdate();

            return updatedRows > 0;
        } catch (NoResultException e) {
            return false;
        }
    }


}
