package aor.proj3t.service.interfaces;

/**
 * Service interface for asynchronous email operations.
 * This interface defines the contract for non-blocking email sending operations
 * that handle bulk email notifications without impacting application
 * performance.
 * 
 * <p>
 * Key asynchronous email services include:
 * <ul>
 * <li><b>Cycle Notifications:</b> Bulk email sending for evaluation cycle
 * events</li>
 * <li><b>Asynchronous Processing:</b> Non-blocking email operations
 * using @Asynchronous</li>
 * <li><b>Bulk Operations:</b> Efficient handling of mass email
 * notifications</li>
 * <li><b>Performance Optimization:</b> Background processing to maintain
 * application responsiveness</li>
 * </ul>
 * 
 * <p>
 * This service ensures that bulk email operations don't block the main
 * application thread,
 * providing better user experience during large-scale notification sending
 * operations.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface AsyncEmailService {

    /**
     * Asynchronously sends evaluation cycle closure notification emails to all
     * users.
     * Operates in the background to avoid blocking the calling thread during bulk
     * email operations.
     * 
     * @param cycleId the unique identifier of the evaluation cycle that is closing
     */
    void sendCycleClosureEmailAsync(Long cycleId);

    /**
     * Asynchronously sends evaluation cycle start notification emails to all users.
     * Operates in the background to handle bulk email notifications efficiently.
     * 
     * @param cycleId the unique identifier of the evaluation cycle that is starting
     */
    void sendCycleStartEmailAsync(Long cycleId);
}
