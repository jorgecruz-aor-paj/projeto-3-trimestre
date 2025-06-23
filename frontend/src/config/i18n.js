import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import Backend from 'i18next-http-backend';

/**
 * Initialize and configure i18n for the application
 * @param {Object} customConfig - Optional custom configuration to merge with defaults
 * @returns {Object} - Configured i18next instance
 */
export const setupI18n = (customConfig = {}) => {
  const i18n = i18next
    // Load translations using http backend
    .use(Backend)
    // Detect user language
    .use(LanguageDetector)
    // Pass the i18n instance to react-i18next
    .use(initReactI18next)
    // Initialize i18next
    .init({
      fallbackLng: 'pt', // Default language (Portuguese)
      debug: process.env.NODE_ENV === 'development',

      interpolation: {
        escapeValue: false, // React already escapes values
      },

      // Backend configuration
      backend: {
        loadPath: '/src/locale/{{lng}}.json',
      },

      // Detection options
      detection: {
        order: ['localStorage', 'navigator'],
        caches: ['localStorage'],
      },
      ...customConfig, // Merge any custom config passed in
    });

  return i18n;
};
