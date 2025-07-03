import React from 'react';
import { useAuthStore } from 'stores/AuthStore';
import {
  TrainingTeaser,
  AdminTrainingManagement,
  UserTrainingView,
} from 'components/trainings';

/**
 * Main Trainings page component
 * Shows different content based on user authentication and role:
 * - Non-authenticated users: Training teaser/marketing page
 * - Admin users: Training management interface
 * - Regular users: Training list/browsing view
 * @returns {React.Component} Trainings page
 */
export const TrainingsPage = () => {
  const { user, token } = useAuthStore();

  // Check if user is authenticated
  const isAuthenticated = !!token && !!user?.id;

  // Check if user is admin
  const isAdmin = user?.role === 'ADMIN';

  // Render based on authentication and role
  if (!isAuthenticated) {
    // Show teaser page for non-authenticated users
    return <TrainingTeaser />;
  }

  if (isAdmin) {
    // Show admin management interface for admin users
    return <AdminTrainingManagement />;
  }

  // For regular authenticated users, show the training browsing view
  return <UserTrainingView />;
};
