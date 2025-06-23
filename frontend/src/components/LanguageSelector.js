import React from 'react';
import { useLanguage } from '../contexts/LanguageContext';
import { Dropdown } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGlobe } from '@fortawesome/free-solid-svg-icons';

/**
 * Available languages in the application
 * @type {Array<{code: string, name: string}>}
 */
const AVAILABLE_LANGUAGES = [
  { code: 'pt', name: 'Português' },
  { code: 'en', name: 'English' },
];

/**
 * Language selector component that allows users to switch languages
 * @returns {React.Component} - Language selector component
 */
const LanguageSelector = () => {
  const { changeLanguage, getCurrentLanguage } = useLanguage();
  const currentLanguage = getCurrentLanguage();

  return (
    <Dropdown>
      <Dropdown.Toggle variant="outline-light" id="language-dropdown">
        <FontAwesomeIcon icon={faGlobe} />{' '}
        {AVAILABLE_LANGUAGES.find(lang => lang.code === currentLanguage)
          ?.name || 'Language'}
      </Dropdown.Toggle>

      <Dropdown.Menu>
        {AVAILABLE_LANGUAGES.map(lang => (
          <Dropdown.Item
            key={lang.code}
            onClick={() => changeLanguage(lang.code)}
            active={currentLanguage === lang.code}
          >
            {lang.name}
          </Dropdown.Item>
        ))}
      </Dropdown.Menu>
    </Dropdown>
  );
};

export default LanguageSelector;
