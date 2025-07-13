package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.PersonalStatsDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.TrainingParticipation;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.interfaces.StatsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of StatsService for statistics operations.
 * Provides comprehensive user statistics including performance trends,
 * training analytics, and dashboard metrics.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class StatsServiceImpl extends BaseService implements StatsService {

    @Inject
    private DAOFactory daoFactory;

    @Override
    public PersonalStatsDTO getUserPersonalStats(Long requesterId, Long userId) {
        // Authorization check
        authorizePersonalStatsAccess(requesterId, userId);

        // Calculate all statistics
        Integer completedTrainings = countCompletedTrainingsForUser(userId);
        Integer pendingEvaluations = countPendingEvaluationsForUser(userId);
        Double performanceScore = calculateUserPerformanceScore(userId);

        // For now, mock goals data - this would need a goals system implementation
        Integer goalsAchieved = 5; // TODO: Implement goals system
        Integer totalGoals = 8; // TODO: Implement goals system

        // Get training history (last 6 months)
        List<PersonalStatsDTO.TrainingMonthStat> trainingHistory = getUserTrainingHistory(requesterId, userId);

        // Get performance trend (last 4 quarters)
        List<PersonalStatsDTO.PerformanceTrend> performanceHistory = getUserPerformanceTrend(requesterId, userId);

        return new PersonalStatsDTO(
                completedTrainings,
                pendingEvaluations,
                performanceScore,
                goalsAchieved,
                totalGoals,
                trainingHistory,
                performanceHistory);
    }

    @Override
    public List<PersonalStatsDTO.PerformanceTrend> getUserPerformanceTrend(Long requesterId, Long userId) {
        // Authorization check
        authorizePersonalStatsAccess(requesterId, userId);

        // Get user entity
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");

        // Get completed/closed evaluations for the user
        List<Evaluation> evaluations = daoFactory.forEvaluation()
                .findByEvaluatedAndStatusIn(user, Arrays.asList(
                        EvaluationStatus.COMPLETED,
                        EvaluationStatus.CLOSED));

        // Group evaluations by quarter and calculate average scores
        Map<String, List<Double>> quarterlyScores = new LinkedHashMap<>();

        for (Evaluation eval : evaluations) {
            if (eval.getScale() != null && eval.getCreatedAt() != null) {
                String quarter = getQuarterFromInstant(eval.getCreatedAt());
                quarterlyScores.computeIfAbsent(quarter, k -> new ArrayList<>())
                        .add(eval.getScale().getScore().doubleValue());
            }
        }

        // Convert to PersonalStatsDTO.PerformanceTrend list, ensuring we have last 4
        // quarters
        List<PersonalStatsDTO.PerformanceTrend> trends = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 3; i >= 0; i--) {
            LocalDate quarterDate = now.minusMonths(i * 3);
            String quarter = String.format("Q%d %d",
                    ((quarterDate.getMonthValue() - 1) / 3) + 1,
                    quarterDate.getYear());

            List<Double> scores = quarterlyScores.getOrDefault(quarter, new ArrayList<>());
            double avgScore = scores.isEmpty() ? 0.0
                    : scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            trends.add(new PersonalStatsDTO.PerformanceTrend(quarter, avgScore));
        }

        return trends;
    }

    @Override
    public List<PersonalStatsDTO.TrainingMonthStat> getUserTrainingHistory(Long requesterId, Long userId) {
        // Authorization check
        authorizePersonalStatsAccess(requesterId, userId);

        // Get actual training participations for the user
        List<TrainingParticipation> participations = daoFactory.forTrainingParticipation()
                .findByParticipantId(userId);

        // Group participations by month and sum training durations
        Map<String, Long> monthlyMinutes = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();

        // Initialize last 6 months with 0 minutes
        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            String monthKey = String.format("%d-%02d", monthDate.getYear(), monthDate.getMonthValue());
            monthlyMinutes.put(monthKey, 0L);
        }

        // Sum actual participation durations by month
        for (TrainingParticipation participation : participations) {
            if (participation.getParticipatedAt() != null && participation.getTraining() != null) {
                LocalDate participationDate = participation.getParticipatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                String monthKey = String.format("%d-%02d",
                        participationDate.getYear(), participationDate.getMonthValue());

                if (monthlyMinutes.containsKey(monthKey)) {
                    Integer duration = participation.getTraining().getDuration();
                    if (duration != null) {
                        monthlyMinutes.put(monthKey, monthlyMinutes.get(monthKey) + duration.longValue());
                    }
                }
            }
        }

        // Convert to PersonalStatsDTO.TrainingMonthStat list
        List<PersonalStatsDTO.TrainingMonthStat> history = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            String monthKey = String.format("%d-%02d", monthDate.getYear(), monthDate.getMonthValue());

            history.add(new PersonalStatsDTO.TrainingMonthStat(
                    monthDate.getYear(),
                    monthDate.getMonthValue(),
                    monthlyMinutes.getOrDefault(monthKey, 0L)));
        }

        return history;
    }

    @Override
    public Double calculateUserPerformanceScore(Long userId) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");

        List<Evaluation> completedEvaluations = daoFactory.forEvaluation()
                .findByEvaluatedAndStatusIn(user, Arrays.asList(
                        EvaluationStatus.COMPLETED,
                        EvaluationStatus.CLOSED));

        if (completedEvaluations.isEmpty()) {
            return 0.0;
        }

        double avgScore = completedEvaluations.stream()
                .filter(e -> e.getScale() != null)
                .mapToInt(e -> e.getScale().getScore())
                .average()
                .orElse(0.0);

        // Return the average score on the 1-4 scale
        return avgScore;
    }

    @Override
    public Integer countPendingEvaluationsForUser(Long userId) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");

        List<Evaluation> pendingEvaluations = daoFactory.forEvaluation()
                .findByEvaluatedAndStatusIn(user, Collections.singletonList(EvaluationStatus.ONGOING));

        return pendingEvaluations.size();
    }

    @Override
    public Integer countCompletedTrainingsForUser(Long userId) {
        // Count actual training participations for the user
        List<TrainingParticipation> participations = daoFactory.forTrainingParticipation()
                .findByParticipantId(userId);
        return participations.size();
    }

    /**
     * Authorizes access to personal statistics based on user roles and
     * relationships.
     * Users can access their own stats, managers can access their direct reports'
     * stats,
     * and admins can access anyone's stats.
     */
    private void authorizePersonalStatsAccess(Long requesterId, Long targetUserId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");

        // Self-access is always allowed
        if (requester.getId().equals(targetUserId)) {
            return;
        }

        // Admin access is always allowed
        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        // Manager can access direct reports' stats
        if (requester.getRole() == Role.MANAGER) {
            User targetUser = getEntity(daoFactory.forUser().findById(targetUserId), targetUserId, "User");
            if (targetUser.getManager() != null &&
                    targetUser.getManager().getId().equals(requester.getId())) {
                return; // Manager accessing direct report
            }
        }

        throw new ForbiddenException(
                "Insufficient permissions for user statistics",
                "User with id " + requesterId +
                        " attempted to access statistics for user " + targetUserId +
                        " without proper authorization.");
    }

    /**
     * Converts an Instant to a quarter string (e.g., "Q1 2024").
     */
    private String getQuarterFromInstant(Instant instant) {
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        int quarter = ((date.getMonthValue() - 1) / 3) + 1;
        return String.format("Q%d %d", quarter, date.getYear());
    }
}
