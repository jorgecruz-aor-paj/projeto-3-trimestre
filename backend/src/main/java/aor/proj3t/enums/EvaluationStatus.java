package aor.proj3t.enums;

import aor.proj3t.exceptions.NotFoundException;

public enum EvaluationStatus {
    ONGOING(1L),
    COMPLETED(2L),
    CLOSED(3L);
    private final Long id;
    EvaluationStatus(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public static EvaluationStatus fromId(Long id) {
        for (EvaluationStatus status : EvaluationStatus.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new NotFoundException("EvaluationStatus with id " + id + " not found");
    }
}
