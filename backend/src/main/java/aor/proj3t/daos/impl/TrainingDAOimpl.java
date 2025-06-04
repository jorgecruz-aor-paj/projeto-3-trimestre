package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.entities.Training;
import aor.proj3t.filters.TrainingFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrainingDAOimpl extends AbstractDAO<Training, Long> implements TrainingDAO {

    @Override
    public Boolean setAsInactive(Long trainingId) {
        Optional<Training> trainingOpt = findById(trainingId);
        if (trainingOpt.isPresent()) {
            Training training = trainingOpt.get();
            training.setActive(false);
            training.setEditedAt(LocalDateTime.now());
            update(training);
            return true;
        }
        return false;
    }

    @Override
    public List<Training> findTrainingsNotParticipatedByUser(Long userId) {
        String query = """
                    SELECT t FROM Training t WHERE t.id NOT IN (
                        SELECT tp.training.id FROM TrainingParticipation tp WHERE tp.participant.id = :userId
                    )
                """;
        return em.createQuery(query, Training.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    public List<Training> getTrainingsByFilters(TrainingFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filters.getTrainingId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getTrainingId()));
        }
        if (filters.getTrainingArea() != null) {
            predicates.add(cb.equal(root.get("trainingArea"), filters.getTrainingArea()));
        }
        if (filters.getLanguage() != null) {
            predicates.add(cb.equal(root.get("language"), filters.getLanguage()));
        }
        if (filters.getDuration() != null) {
            predicates.add(cb.equal(root.get("duration"), filters.getDuration()));
        }
        if (filters.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("created_at"), LocalDateTime.parse(filters.getStartDate())));
        }
        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            predicates.add(cb.like(root.get("title"), "%" + filters.getSearchText() + "%"));
            predicates.add(cb.like(root.get("description"), "%" + filters.getSearchText() + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (filters.getSortField() != null) {
            if (filters.getSortAscending()) {
                query.orderBy(cb.asc(root.get(filters.getSortField().getFieldName())));
            } else {
                query.orderBy(cb.desc(root.get(filters.getSortField().getFieldName())));
            }
        }

        return em.createQuery(query).getResultList();
    }

}
