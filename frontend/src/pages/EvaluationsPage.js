import React from 'react';
import { useAuthStore } from 'stores/AuthStore';
import {
  EvaluationTeaser,
  AdminEvaluationManagement,
  UserEvaluationView,
} from 'components/evaluations';

/**
 * Main Evaluations page component
 * Shows different content based on user authentication and role:
 * - Non-authenticated users: Evaluation teaser/marketing page
 * - Admin users: Evaluation management interface
 * - Regular users: Evaluation list/browsing view
 * @returns {React.Component} Evaluations page
 */
export const EvaluationsPage = () => {
  const { user, token } = useAuthStore();

  // Check if user is authenticated
  const isAuthenticated = !!token && !!user?.id;

  // Check if user is admin
  const isAdmin = user?.role === 'ADMIN';

  // Render based on authentication and role
  if (!isAuthenticated) {
    // Show teaser page for non-authenticated users
    return <EvaluationTeaser />;
  }

  if (isAdmin) {
    // Show admin management interface for admin users
    return <AdminEvaluationManagement />;
  }

  // For regular authenticated users, show the evaluation browsing view
  return <UserEvaluationView />;
};
