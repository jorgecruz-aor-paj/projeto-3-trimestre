package aor.proj3t.daos.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import aor.proj3t.daos.interfaces.TrainingDAO;
import aor.proj3t.entities.Training;
import aor.proj3t.filters.TrainingFilter;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Stateless EJB implementation of the {@link TrainingDAO} interface.
 * <p>
 * This class provides concrete implementation for managing training programs
 * within the application. It extends {@link AbstractDAO} to leverage common
 * CRUD operations and implements specific business logic for training catalog
 * management, availability tracking, and advanced filtering capabilities.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Complex JPQL subqueries for participation-based filtering</li>
 * <li>Criteria API implementation for dynamic filtering</li>
 * <li>Training availability detection for user recommendations</li>
 * <li>Multi-field search capabilities across training properties</li>
 * <li>Enum handling for language and status filtering</li>
 * <li>Dynamic sorting with configurable field and direction</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see TrainingDAO
 * @see AbstractDAO
 * @see Training
 * @see TrainingFilter
 */
@Stateless
public class TrainingDAOimpl extends AbstractDAO<Training, Long> implements TrainingDAO {

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * This method implements concrete entity type resolution by returning the
     * Training class reference. This enables the AbstractDAO parent class to
     * perform type-safe CRUD operations on Training entities.
     * </p>
     *
     * @return the Training.class for entity type resolution
     */
    @Override
    protected Class<Training> getEntityClass() {
        return Training.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Utilizes a JPQL subquery to identify trainings where the specified user
     * is not already registered as a participant. This prevents duplicate
     * registrations and ensures clean training catalog filtering for user
     * recommendations. Only active trainings are returned to maintain data
     * integrity.
     * </p>
     * <p>
     * Query structure:
     * </p>
     * 
     * <pre>
     * SELECT t FROM Training t 
     * WHERE t.isActive = true AND t.id NOT IN (
     *   SELECT tp.training.id FROM TrainingParticipation tp 
     *   WHERE tp.participant.id = :userId
     * )
     * </pre>
     */
    @Override
    public List<Training> findTrainingsNotParticipatedByUser(Long userId) {
        String query = """
                    SELECT t FROM Training t WHERE t.isActive = true AND t.id NOT IN (
                        SELECT tp.training.id FROM TrainingParticipation tp WHERE tp.participant.id = :userId
                    )
                """;
        return em.createQuery(query, Training.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Implementation notes:</strong>
     * Implements sophisticated multi-criteria filtering using JPA Criteria API
     * for dynamic query construction. This method provides extensive filtering
     * capabilities including:
     * </p>
     * <ul>
     * <li><strong>Entity filtering:</strong> By ID and training area
     * relationships</li>
     * <li><strong>Enum handling:</strong> Language filtering with case-insensitive
     * matching</li>
     * <li><strong>Date filtering:</strong> Start date comparison using Instant
     * parsing</li>
     * <li><strong>Status filtering:</strong> Active-only training visibility
     * control</li>
     * <li><strong>Text search:</strong> Multi-field text matching across title and
     * description</li>
     * <li><strong>Dynamic sorting:</strong> Configurable field and direction-based
     * ordering</li>
     * </ul>
     * <p>
     * The method constructs predicates dynamically based on non-null filter values,
     * ensuring optimal query performance by only including necessary conditions.
     * Text search utilizes SQL LIKE operations with wildcard matching for flexible
     * discovery capabilities.
     * </p>
     * <p>
     * <strong>Performance considerations:</strong>
     * </p>
     * <ul>
     * <li>Predicate lists are pre-allocated for efficient memory usage</li>
     * <li>Enum comparisons use string conversion for database compatibility</li>
     * <li>Conditional query building reduces unnecessary database load</li>
     * </ul>
     */
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
            // Special handling for language enum
            predicates.add(cb.equal(cb.upper(root.get("language").as(String.class)),
                    filters.getLanguage().name()));
        }
        if (filters.getDuration() != null) {
            predicates.add(cb.equal(root.get("duration"), filters.getDuration()));
        }
        if (filters.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), Instant.parse(filters.getStartDate())));
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
