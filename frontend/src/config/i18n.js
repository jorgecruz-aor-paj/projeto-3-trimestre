import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import Backend from 'i18next-http-backend';

// Create a singleton i18n instance that will be initialized once
export const i18n = i18next
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next);

/**
 * Initialize and configure i18n for the application
 * @param {Object} customConfig - Optional custom configuration to merge with defaults
 * @returns {Promise<Object>} - Configured i18next instance
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
