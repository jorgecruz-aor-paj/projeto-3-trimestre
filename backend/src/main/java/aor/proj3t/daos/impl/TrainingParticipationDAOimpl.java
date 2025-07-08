package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.TrainingParticipationDAO;
import aor.proj3t.entities.TrainingParticipation;
import jakarta.ejb.Stateless;

import java.util.LinkedHashMap;
import java.util.List;

@Stateless
public class TrainingParticipationDAOimpl extends AbstractDAO<TrainingParticipation, Long>
        implements TrainingParticipationDAO {

    @Override
    protected Class<TrainingParticipation> getEntityClass() {
        return TrainingParticipation.class;
    }

    @Override
    public List<TrainingParticipation> findByParticipantId(Long participantId) {
        String query = "SELECT tp FROM TrainingParticipation tp WHERE tp.participant.id = :participantId";
        return em.createQuery(query, TrainingParticipation.class)
                .setParameter("participantId", participantId)
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
