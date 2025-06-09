package aor.proj3t.daos.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.entities.TrainingParticipation;
import jakarta.ejb.Stateless;

@Stateless
public class TrainingParticipationDAOimpl extends AbstractDAO<TrainingParticipation, Long>
        implements TrainingParticipationDAO {

    @Override
    public List<TrainingParticipation> findByTrainingId(Long trainingId) {
        String query = "SELECT tp FROM TrainingParticipation tp WHERE tp.training.id = :trainingId";
        return em.createQuery(query, TrainingParticipation.class)
                .setParameter("trainingId", trainingId)
                .getResultList();
    }

    @Override
    public List<TrainingParticipation> findByParticipantId(Long participantId) {
        String query = "SELECT tp FROM TrainingParticipation tp WHERE tp.participant.id = :participantId";
        return em.createQuery(query, TrainingParticipation.class)
                .setParameter("participantId", participantId)
                .getResultList();
    }

    @Override
    public List<TrainingParticipation> findByManagerId(Long managerId) {
        String query = "SELECT tp FROM TrainingParticipation tp WHERE tp.manager.id = :managerId";
        return em.createQuery(query, TrainingParticipation.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }

    @Override
    public Map<Integer, Integer> trainingTimeByYearByParticipantId(Long participantId) {
        String query = """
                    SELECT YEAR(tp.participatedAt), SUM(tp.training.duration)
                    FROM TrainingParticipation tp
                    WHERE tp.participant.id = :participantId
                    GROUP BY YEAR(tp.participatedAt)
                """;

        List<Object[]> results = em.createQuery(query, Object[].class)
                .setParameter("participantId", participantId)
                .getResultList();

        Map<Integer, Integer> trainingTimeByYear = new HashMap<>();
        for (Object[] row : results) {
            trainingTimeByYear.put((Integer) row[0], ((Integer) row[1]));
        }

        return trainingTimeByYear;
    }

}
