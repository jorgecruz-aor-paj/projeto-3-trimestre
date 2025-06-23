import React from 'react';
import { Routes, Route } from 'react-router-dom';

/**
 * AppRouter component that defines all application routes
 *
 * This component sets up all the routes for the application, including:
 * - Public routes (home, registration, reset password)
 * - User routes (profile, messages)
 * - Admin routes (user management)
 * - Authentication routes (password reset, account confirmation)
 * - Error routes (not found, forbidden)
 *
 * @component
 * @returns {React.ReactElement} A React Router configuration with all application routes
 *
 * @example
 * <BrowserRouter>
 *   <AppRouter />
 * </BrowserRouter>
 */
export const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      {/* Definir o resto das Routes */}
    </Routes>
  );
};
