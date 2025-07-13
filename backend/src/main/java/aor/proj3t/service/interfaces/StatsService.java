package aor.proj3t.service.interfaces;

import java.util.List;

import aor.proj3t.dto.PersonalStatsDTO;

/**
 * Service interface for statistics operations.
 * This interface defines the contract for comprehensive statistics
 * functionality
 * including personal user statistics, performance trends, and training
 * analytics.
 * 
 * <p>
 * Key statistics services include:
 * <ul>
 * <li><b>Personal Statistics:</b> User-specific performance and activity
 * metrics</li>
 * <li><b>Performance Trends:</b> Time-series analysis of user performance</li>
 * <li><b>Training Analytics:</b> Monthly and yearly training participation
 * stats</li>
 * <li><b>Authorization:</b> Role-based access control for statistics data</li>
 * </ul>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public interface StatsService {

    /**
     * Retrieves comprehensive personal statistics for a user.
     * Aggregates evaluation performance, training participation, and goal tracking
     * into a unified dashboard-ready format.
     * 
     * @param requesterId the ID of the user requesting statistics (for
     *                    authorization)
     * @param userId      the ID of the user whose statistics are being retrieved
     * @return PersonalStatsDTO containing comprehensive user statistics
     * @throws ForbiddenException if the requester lacks permission to view the
     *                            statistics
     */
    PersonalStatsDTO getUserPersonalStats(Long requesterId, Long userId);

    /**
     * Retrieves performance trend data for a user over time.
     * Analyzes historical evaluation scores to generate quarterly or monthly
     * trends.
     * 
     * @param requesterId the ID of the user requesting the trend data
     * @param userId      the ID of the user whose performance trend is being
     *                    analyzed
     * @return List of PersonalStatsDTO.PerformanceTrend representing performance
     *         over time
     * @throws ForbiddenException if the requester lacks permission to view the data
     */
    List<PersonalStatsDTO.PerformanceTrend> getUserPerformanceTrend(Long requesterId, Long userId);

    /**
     * Retrieves monthly training participation statistics for a user.
     * Provides detailed breakdown of training hours by month for chart
     * visualization.
     * 
     * @param requesterId the ID of the user requesting the training statistics
     * @param userId      the ID of the user whose training history is being
     *                    retrieved
     * @return List of PersonalStatsDTO.TrainingMonthStat representing monthly
     *         training participation
     * @throws ForbiddenException if the requester lacks permission to view the data
     */
    List<PersonalStatsDTO.TrainingMonthStat> getUserTrainingHistory(Long requesterId, Long userId);

    /**
     * Calculates the average performance score for a user based on completed
     * evaluations.
     * Converts evaluation scale scores (1-4) to percentage format for dashboard
     * display.
     * 
     * @param userId the ID of the user whose performance score is being calculated
     * @return Double representing the user's average performance as a percentage
     *         (0-100)
     */
    Double calculateUserPerformanceScore(Long userId);

    /**
     * Counts the number of pending evaluations for a user.
     * Includes evaluations with status ONGOING where the user is being evaluated.
     * 
     * @param userId the ID of the user whose pending evaluations are being counted
     * @return Integer count of pending evaluations
     */
    Integer countPendingEvaluationsForUser(Long userId);

    /**
     * Counts the number of completed training participations for a user.
     * Includes all training sessions the user has successfully completed.
     * 
     * @param userId the ID of the user whose completed trainings are being counted
     * @return Integer count of completed training participations
     */
    Integer countCompletedTrainingsForUser(Long userId);
}
