import React from 'react';
import { BrowserRouter } from 'react-router-dom';

import { LanguageProvider } from 'contexts/LanguageContext';
import { AppRouter } from 'routes/AppRouter';
import { AlertProvider } from 'contexts/AlertContext';

import 'styles/App.css';

/**
 * @fileoverview Main application component that sets up the core providers and routing
 * @module App
 * @requires React
 * @requires react-router-dom
 * @requires contexts/LanguageContext
 * @requires routes/AppRouter
 * @requires contexts/AlertContext
 * @requires styles/App.css
 */

/**
 * Main application component that serves as the root of the React application.
 *
 * This component establishes the foundational structure by wrapping the entire
 * application with essential providers and routing configuration:
 * - LanguageProvider: Manages internationalization (i18n) throughout the app
 * - AlertProvider: Provides global alert/notification system
 * - BrowserRouter: Enables client-side routing with HTML5 history API
 * - AppRouter: Contains all route definitions and navigation logic
 *
 * The providers are arranged in a hierarchical structure where each provider
 * makes its context available to all child components.
 *
 * @component
 * @name App
 * @returns {React.ReactElement} The root React element with all providers and routing
 *
 * @example
 * // Basic usage (typically in index.js)
 * import { App } from './App';
 *
 * root.render(
 *   <React.StrictMode>
 *     <App />
 *   </React.StrictMode>
 * );
 *
 * @example
 * // The component structure provides:
 * // - Multi-language support via LanguageProvider
 * // - Global alert system via AlertProvider
 * // - Client-side routing via BrowserRouter
 * // - Complete application routes via AppRouter
 *
 * @since 1.0.0
 * @author Development Team
 */
export const App = () => {
  return (
    <LanguageProvider>
      <AlertProvider>
        <BrowserRouter>
          <AppRouter />
        </BrowserRouter>
      </AlertProvider>
    </LanguageProvider>
  );
};
