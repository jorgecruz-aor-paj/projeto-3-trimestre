import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Home } from 'pages/Home';
import { MainLayout } from 'components/layout/MainLayout';
import { AuthTogglePage } from 'pages/auth/AuthTogglePage';
import { ResetPasswordPage } from 'pages/auth/ResetPasswordPage';
import { ConfirmRegistrationPage } from 'pages/auth/ConfirmRegistrationPage';
import { Dashboard } from 'pages/Dashboard';

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
      {/* Main Layout with Header and Footer */}
      <Route element={<MainLayout />}>
        {/* Routes */}
        <Route path="/" element={<Home />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/auth" element={<AuthTogglePage />} />
        <Route path="/login" element={<AuthTogglePage />} />
        <Route path="/register" element={<AuthTogglePage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
        <Route path="/register/confirm" element={<ConfirmRegistrationPage />} />
        {/* Definir o resto das Routes */}
      </Route>
    </Routes>
  );
};
