package aor.proj3t.dto;

import java.util.List;

/**
 * Comprehensive Data Transfer Object for user personal statistics.
 * Contains all dashboard-related statistics including nested performance trends
 * and training history.
 * Consolidates multiple related DTOs into a single cohesive structure.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
public class PersonalStatsDTO {

    private Integer completedTrainings;
    private Integer pendingEvaluations;
    private Double performanceScore;
    private Integer goalsAchieved;
    private Integer totalGoals;
    private List<TrainingMonthStat> trainingHistory;
    private List<PerformanceTrend> performanceHistory;

    /**
     * Default constructor for Jackson serialization.
     */
    public PersonalStatsDTO() {
    }

    /**
     * Constructor with all fields.
     */
    public PersonalStatsDTO(Integer completedTrainings, Integer pendingEvaluations,
            Double performanceScore, Integer goalsAchieved, Integer totalGoals,
            List<TrainingMonthStat> trainingHistory,
            List<PerformanceTrend> performanceHistory) {
        this.completedTrainings = completedTrainings;
        this.pendingEvaluations = pendingEvaluations;
        this.performanceScore = performanceScore;
        this.goalsAchieved = goalsAchieved;
        this.totalGoals = totalGoals;
        this.trainingHistory = trainingHistory;
        this.performanceHistory = performanceHistory;
    }

    // Getters and Setters
    public Integer getCompletedTrainings() {
        return completedTrainings;
    }

    public void setCompletedTrainings(Integer completedTrainings) {
        this.completedTrainings = completedTrainings;
    }

    public Integer getPendingEvaluations() {
        return pendingEvaluations;
    }

    public void setPendingEvaluations(Integer pendingEvaluations) {
        this.pendingEvaluations = pendingEvaluations;
    }

    public Double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(Double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public Integer getGoalsAchieved() {
        return goalsAchieved;
    }

    public void setGoalsAchieved(Integer goalsAchieved) {
        this.goalsAchieved = goalsAchieved;
    }

    public Integer getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }

    public List<TrainingMonthStat> getTrainingHistory() {
        return trainingHistory;
    }

    public void setTrainingHistory(List<TrainingMonthStat> trainingHistory) {
        this.trainingHistory = trainingHistory;
    }

    public List<PerformanceTrend> getPerformanceHistory() {
        return performanceHistory;
    }

    public void setPerformanceHistory(List<PerformanceTrend> performanceHistory) {
        this.performanceHistory = performanceHistory;
    }

    /**
     * Nested class for training statistics by month.
     * Consolidates training data to avoid separate DTO.
     */
    public static class TrainingMonthStat {
        private Integer year;
        private Integer month;
        private Long minutes;

        /**
         * Default constructor for Jackson serialization.
         */
        public TrainingMonthStat() {
        }

        /**
         * Constructor with all fields.
         */
        public TrainingMonthStat(Integer year, Integer month, Long minutes) {
            this.year = year;
            this.month = month;
            this.minutes = minutes;
        }

        // Getters and Setters
        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Long getMinutes() {
            return minutes;
        }

        public void setMinutes(Long minutes) {
            this.minutes = minutes;
        }
    }

    /**
     * Nested class for performance trend data.
     * Consolidates performance data to avoid separate DTO.
     */
    public static class PerformanceTrend {
        private String period;
        private Double score;

        /**
         * Default constructor for Jackson serialization.
         */
        public PerformanceTrend() {
        }

        /**
         * Constructor with all fields.
         */
        public PerformanceTrend(String period, Double score) {
            this.period = period;
            this.score = score;
        }

        // Getters and Setters
        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }
    }
}
