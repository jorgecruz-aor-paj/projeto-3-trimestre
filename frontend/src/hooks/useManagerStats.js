import { useState, useEffect } from 'react';
import { userService } from 'services/userService';
import { evaluationService } from 'services/evaluationService';
import { trainingService } from 'services/trainingService';

/**
 * Custom hook for fetching manager dashboard statistics
 * @param {number} managerId - The manager's user ID
 * @returns {Object} - { stats, loading, error, refetch }
 */
export const useManagerStats = managerId => {
  const [stats, setStats] = useState({
    teamMembers: 0,
    activeTrainings: 0,
    pendingEvaluations: 0,
    teamPerformance: 0,
    completedEvaluations: 0,
    avgPerformanceRating: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchStats = async () => {
    if (!managerId) {
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError(null);

      // Fetch all data in parallel for better performance
      const [teamMembers, evaluations, allTrainings] = await Promise.all([
        userService.getAll({ managerId }).catch(() => []),
        evaluationService.getAll({ managerId }).catch(() => []),
        trainingService.getAll({ activeOnly: true }).catch(() => []),
      ]);

      // Calculate pending evaluations (status: ONGOING)
      const pendingEvaluations = evaluations.filter(
        evaluation => evaluation.status === 'ONGOING'
      );

      // Calculate completed evaluations
      const completedEvaluations = evaluations.filter(
        evaluation =>
          evaluation.status === 'COMPLETED' || evaluation.status === 'CLOSED'
      );

      // Calculate team performance average
      let avgPerformanceRating = 0;
      if (completedEvaluations.length > 0) {
        const totalScore = completedEvaluations.reduce(
          (sum, evaluation) => sum + (evaluation.performanceRating || 0),
          0
        );
        avgPerformanceRating = totalScore / completedEvaluations.length;
      }

      setStats({
        teamMembers: teamMembers.length,
        activeTrainings: allTrainings.length,
        pendingEvaluations: pendingEvaluations.length,
        teamPerformance:
          avgPerformanceRating > 0 ? avgPerformanceRating.toFixed(1) : 0,
        completedEvaluations: completedEvaluations.length,
        avgPerformanceRating,
      });
    } catch (err) {
      console.error('Failed to fetch manager statistics:', err);
      setError(err.message || 'Failed to load team statistics');
      // Set default values on error
      setStats({
        teamMembers: 0,
        activeTrainings: 0,
        pendingEvaluations: 0,
        teamPerformance: 0,
        completedEvaluations: 0,
        avgPerformanceRating: 0,
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, [managerId]);

  return {
    stats,
    loading,
    error,
    refetch: fetchStats,
  };
};
