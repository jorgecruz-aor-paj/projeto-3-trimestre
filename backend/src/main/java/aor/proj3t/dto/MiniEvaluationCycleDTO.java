package aor.proj3t.dto;


import java.time.Instant;
/**
 * Lightweight Data Transfer Object for {@link EvaluationCycle} entity.
 * <p>
 * Serves as a compact representation of an evaluation cycle, typically used when
 * full cycle details are unnecessary. Ideal for nested references in other DTOs
 * such as {@link EvaluationDTO}, or for optimizing serialization in list views and summaries.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier of the cycle
 * - {@code startDate}: Date when the cycle officially begins
 * - {@code closureDate}: Date when the cycle is considered closed or archived
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class MiniEvaluationCycleDTO {

    /** Unique identifier of the evaluation cycle */
    private Long id;

    /** Date marking the start of the cycle */
    private Instant startDate;

    /** Date marking the closure of the cycle */
    private Instant closureDate;

    public MiniEvaluationCycleDTO() {}

    public MiniEvaluationCycleDTO(Long id, Instant startDate, Instant closureDate) {
        this.id = id;
        this.startDate = startDate;
        this.closureDate = closureDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(Instant closureDate) {
        this.closureDate = closureDate;
    }
}


