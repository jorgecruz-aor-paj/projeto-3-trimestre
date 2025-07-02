package aor.proj3t.daos.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.entities.TrainingParticipation;
import jakarta.ejb.Stateless;

@Stateless
public class TrainingParticipationDAOimpl extends AbstractDAO<TrainingParticipation, Long>
        implements TrainingParticipationDAO {

    @Override
    protected Class<TrainingParticipation> getEntityClass() {
        return TrainingParticipation.class;
    }

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
    public LinkedHashMap<Integer, Long> trainingTimeByYearByParticipantId(Long participantId) {
        String query = """
                    SELECT YEAR(tp.participatedAt), SUM(tp.training.duration)
                    FROM TrainingParticipation tp
                    WHERE tp.participant.id = :participantId
                    GROUP BY YEAR(tp.participatedAt)
                    ORDER BY YEAR(tp.participatedAt) DESC
                """;


        List<Object[]> results = em.createQuery(query, Object[].class)
                .setParameter("participantId", participantId)
                .getResultList();

        LinkedHashMap<Integer, Long> trainingTimeByYear = new LinkedHashMap<>();
        for (Object[] row : results) {
            trainingTimeByYear.put((Integer) row[0], ((Long) row[1]));
        }

        return trainingTimeByYear;
    }

}
