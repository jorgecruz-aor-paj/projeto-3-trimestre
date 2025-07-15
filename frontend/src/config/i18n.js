/**
 * @fileoverview Internationalization (i18n) configuration for the application
 * @module config/i18n
 * @requires i18next
 * @requires react-i18next
 * @requires i18next-browser-languagedetector
 * @requires i18next-http-backend
 */

import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import Backend from 'i18next-http-backend';

/**
 * @typedef {Object} I18nConfig
 * @property {string} fallbackLng - Default language code
 * @property {boolean} debug - Whether to enable debug mode
 * @property {Object} interpolation - Interpolation configuration
 * @property {Object} backend - Backend configuration for loading translations
 * @property {Object} detection - Language detection configuration
 * @property {Object} react - React-specific configuration
 */

/**
 * Singleton i18n instance that will be initialized once
 * This instance is configured with:
 * - HTTP backend for loading translation files
 * - Browser language detector for automatic language detection
 * - React integration for component rendering
 */
export const i18n = i18next
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next);

/**
 * Initialize and configure i18n for the application
 * 
 * This function sets up the complete internationalization system with:
 * - Portuguese as the default fallback language
 * - HTTP backend for loading translation files from /locale/ directory
 * - Browser language detection with localStorage persistence
 * - React integration with suspense disabled for better control
 * - Debug mode enabled in development environment
 * 
 * The function ensures the i18n instance is initialized only once,
 * making it safe to call multiple times.
 * 
 * @async
 * @function setupI18n
 * @param {I18nConfig} [customConfig={}] - Optional custom configuration to merge with defaults
 * @returns {Promise<Object>} Promise that resolves to the configured i18next instance
 * 
 * @example
 * // Basic setup with default configuration
 * import { setupI18n } from 'config/i18n';
 * 
 * const i18n = await setupI18n();
 * 
 * @example
 * // Setup with custom configuration
 * const i18n = await setupI18n({
 *   fallbackLng: 'en',
 *   debug: true,
 *   backend: {
 *     loadPath: '/custom/locale/{{lng}}.json'
 *   }
 * });
 * 
 * @example
 * // Using in a React component
 * import { useEffect } from 'react';
 * import { setupI18n } from 'config/i18n';
 * 
 * function App() {
 *   useEffect(() => {
 *     setupI18n().then(() => {
 *       console.log('i18n initialized');
 *     });
 *   }, []);
 * 
 *   return <div>...</div>;
 * }
 * 
 * @throws {Error} When i18n initialization fails
 * @since 1.0.0
 */
export const setupI18n = async (customConfig = {}) => {
  if (!i18n.isInitialized) {
    await i18n.init({
      fallbackLng: 'pt', // Default language (Portuguese)
      debug: process.env.NODE_ENV === 'development',

      interpolation: {
        escapeValue: false, // React already escapes values
      },

      // Backend configuration
      backend: {
        loadPath: '/locale/{{lng}}.json', // Path to translation files relative to public directory
      },

      // Detection options
      detection: {
        order: ['localStorage', 'navigator'],
        caches: ['localStorage'],
      },

      // React configuration
      react: {
        useSuspense: false,
        bindI18n: 'languageChanged loaded',
        bindI18nStore: 'added removed',
        transEmptyNodeValue: '',
      },

      ...customConfig, // Merge any custom config passed in
    });
  }

  return i18n;
};
