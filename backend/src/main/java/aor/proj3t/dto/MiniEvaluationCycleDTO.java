package aor.proj3t.dto;


import java.time.Instant;

public class MiniEvaluationCycleDTO {
    private Long id;
    private Instant startDate;
    private Instant closureDate;

    public MiniEvaluationCycleDTO() {
    }

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

