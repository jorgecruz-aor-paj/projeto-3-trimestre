package aor.proj3t.enums;

/**
 * Enum representing the different statuses of an evaluation cycle.
 * An evaluation cycle goes through different phases from being active
 * to completion and finally closure.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public enum EvaluationCycleStatus {
    /** The evaluation cycle is currently active and accepting evaluations */
    ACTIVE,

    /** The evaluation cycle has been completed but not yet closed */
    COMPLETED,

    /** The evaluation cycle is closed and no further modifications are allowed */
    CLOSED
}
