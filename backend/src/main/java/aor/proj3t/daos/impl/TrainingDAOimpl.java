package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.entities.Training;
import aor.proj3t.filters.TrainingFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TrainingDAOimpl extends AbstractDAO<Training, Long> implements TrainingDAO {

    @Override
    protected Class<Training> getEntityClass() {
        return Training.class;
    }

    @Override
    public Boolean setAsInactive(Long trainingId) {
        Optional<Training> trainingOpt = findById(trainingId);
        if (trainingOpt.isPresent()) {
            Training training = trainingOpt.get();
            training.setActive(false);
            training.setEditedAt(Instant.now());
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

    @Override
    public List<Training> getTrainingsByFilters(TrainingFilter filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filters.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filters.getId()));
        }
        if (filters.getTrainingAreaId() != null) {
            predicates.add(cb.equal(root.get("trainingArea").get("id"), filters.getTrainingAreaId()));
        }
        if (filters.getLanguage() != null) {
            predicates.add(cb.equal(root.get("language"), filters.getLanguage()));
        }
        if (filters.getDuration() != null) {
            predicates.add(cb.equal(root.get("duration"), filters.getDuration()));
        }
        if (filters.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("created_at"), Instant.parse(filters.getStartDate())));
        }
        if (Boolean.TRUE.equals(filters.getActiveOnly())) {
            predicates.add(cb.isTrue(root.get("isActive")));
        }

        if (filters.getSearchText() != null && !filters.getSearchText().isEmpty()) {
            Predicate namePredicate = cb.or(
                    cb.like(root.get("title"), "%" + filters.getSearchText() + "%"),
                    cb.like(root.get("description"), "%" + filters.getSearchText() + "%"));
            predicates.add(namePredicate);
        }

        query.where(predicates.toArray(Predicate[]::new));

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
