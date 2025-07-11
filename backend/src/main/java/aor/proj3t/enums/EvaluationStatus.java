package aor.proj3t.enums;

import aor.proj3t.exceptions.NotFoundException;

/**
 * Enum representing the different statuses of an individual evaluation.
 * Each status has an associated ID that corresponds to database values.
 * An evaluation progresses through these statuses during its lifecycle.
 * 
 * @author Project Team
 * @version 1.0
 * @since 1.0
 */
public enum EvaluationStatus {
    /** The evaluation is currently in progress */
    ONGOING(1L),

    /** The evaluation has been completed */
    COMPLETED(2L),

    /** The evaluation is closed and cannot be modified */
    CLOSED(3L);

    /** The unique identifier for this status in the database */
    private final Long id;

    /**
     * Constructor for EvaluationStatus enum values.
     * 
     * @param id The database ID for this status
     */
    EvaluationStatus(Long id) {
        this.id = id;
    }

    /**
     * Gets the database ID for this evaluation status.
     * 
     * @return The unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Finds an EvaluationStatus by its database ID.
     * 
     * @param id The database ID to search for
     * @return The EvaluationStatus with the matching ID
     * @throws NotFoundException if no status is found for the given ID
     */
    public static EvaluationStatus fromId(Long id) {
        for (EvaluationStatus status : EvaluationStatus.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new NotFoundException("EvaluationStatus with id " + id + " not found");
    }
}
