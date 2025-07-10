package aor.proj3t.service.interfaces;

public interface AsyncEmailService {
    void sendCycleClosureEmailAsync(Long cycleId);

    void sendCycleStartEmailAsync(Long cycleId);
}
