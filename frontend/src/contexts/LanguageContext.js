import React, { createContext, useContext, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { I18nextProvider } from 'react-i18next';
import { setupI18n, i18n } from 'config/i18n';

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
export const LanguageProvider = ({ children }) => {
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    // Initialize i18next with default configuration
    const initI18n = async () => {
      await setupI18n({
        // Use default detection from i18n.js
        // Will detect browser language and use localStorage for persistence
        fallbackLng: 'pt',
        supportedLngs: SUPPORTED_LANGUAGES,

        // Add react configuration
        react: {
          useSuspense: false,
          bindI18n: 'languageChanged loaded',
          bindI18nStore: 'added removed',
        },

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
      setIsInitialized(true);
    };

    initI18n();
  }, []);

  // Wait for i18n to be initialized
  if (!isInitialized) {
    return <div>Loading translations...</div>; // Or a loading spinner
  }

  // Helper functions for the context - using i18n's current language
  const languageUtils = {
    // Get current language code
    getCurrentLanguage: () => i18n.language,

    // Change language
    changeLanguage: langCode => {
      if (SUPPORTED_LANGUAGES.includes(langCode)) {
        return i18n.changeLanguage(langCode);
      }
      return Promise.reject(new Error(`Language ${langCode} not supported`));
    },

    // Format numbers according to the current language
    formatNumber: (number, options = {}) => {
      return new Intl.NumberFormat(i18n.language, options).format(number);
    },

    // Format dates according to the current language
    // Handles Java Instant format strings (ISO 8601)
    formatDate: (date, options = {}) => {
      try {
        // Convert Java Instant/ISO string or keep as Date object
        const dateObj = date instanceof Date ? date : new Date(date);
        return new Intl.DateTimeFormat(i18n.language, options).format(dateObj);
      } catch (error) {
        console.warn('Error formatting date:', error);
        return 'Invalid date';
      }
    },
  };

  return (
    <LanguageContext.Provider value={languageUtils}>
      <I18nextProvider i18n={i18n}>{children}</I18nextProvider>
    </LanguageContext.Provider>
  );
};

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
