import React, { createContext, useContext, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { I18nextProvider } from 'react-i18next';
import { setupI18n } from 'config/i18n';

// Create context for language utility functions
const LanguageContext = createContext();

/**
 * Array of supported language codes
 * @type {Array<string>}
 */
export const SUPPORTED_LANGUAGES = ['pt', 'en'];

/**
 * Custom Language Provider component that wraps i18next's Provider.
 * This component leverages i18next's built-in language detection and storage
 * while providing additional utility functions for formatting.
 *
 * @component
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - Child components to be wrapped by the language provider
 * @returns {React.ReactElement} A React component providing language context
 *
 * @example
 * <LanguageProvider>
 *   <App />
 * </LanguageProvider>
 */
export function LanguageProvider({ children }) {
  const [i18nInstance, setI18nInstance] = useState(null);

  useEffect(() => {
    // Initialize i18next with default configuration
    const i18n = setupI18n({
      // Use default detection from i18n.js
      // Will detect browser language and use localStorage for persistence
      fallbackLng: 'pt',
      supportedLngs: SUPPORTED_LANGUAGES,

      // Enable missing translation detection
      saveMissing: true,
      saveMissingTo: 'all',

      // Configure missing key handling
      missingKeyHandler: (lng, ns, key) => {
        console.warn(`Missing translation: "${key}" for language: ${lng}`);
      },

      // Optional: Add placeholder text for missing translations
      parseMissingKeyHandler: key => {
        return `➤ ${key} ➤`;
      },
    });

    setI18nInstance(i18n);
  }, []);

  // Wait for i18n to be initialized
  if (!i18nInstance) {
    return null; // Or a loading spinner
  }

  // Helper functions for the context - using i18n's current language
  const languageUtils = {
    /**
     * Changes the current language
     * @param {string} lang - Language code to switch to
     */
    changeLanguage: lang => {
      if (SUPPORTED_LANGUAGES.includes(lang)) {
        i18nInstance.changeLanguage(lang);
      }
    },

    /**
     * Get the current language code
     * @returns {string} Current language code
     */
    getCurrentLanguage: () => i18nInstance.language,

    /**
     * Formats a date according to the current language
     * @param {Date|string|number} date - Date to format
     * @param {Object} options - Intl.DateTimeFormat options
     * @returns {string} Formatted date string
     */
    formatDate: (date, options = {}) => {
      return new Intl.DateTimeFormat(i18nInstance.language, options).format(
        date instanceof Date ? date : new Date(date)
      );
    },

    /**
     * Formats a number according to the current language
     * @param {number} number - Number to format
     * @param {Object} options - Intl.NumberFormat options
     * @returns {string} Formatted number string
     */
    formatNumber: (number, options = {}) => {
      return new Intl.NumberFormat(i18nInstance.language, options).format(
        number
      );
    },
  };

  return (
    <LanguageContext.Provider value={languageUtils}>
      <I18nextProvider i18n={i18nInstance}>{children}</I18nextProvider>
    </LanguageContext.Provider>
  );
}

LanguageProvider.propTypes = {
  /**
   * Child components to be wrapped by the language provider
   */
  children: PropTypes.node.isRequired,
};

/**
 * Custom hook to access the language context for utility functions
 * @returns {Object} Language context containing utility functions
 */
export const useLanguage = () => {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within a LanguageProvider');
  }
  return context;
};
