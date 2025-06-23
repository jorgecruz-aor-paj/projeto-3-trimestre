import React from 'react';
import { useTranslation } from 'react-i18next';

import { LanguageSelector } from 'components/LanguageSelector';

export const Home = () => {
  const { t, ready } = useTranslation();

  if (!ready) {
    return <div>Loading translations...</div>;
  }

  return (
    <div className="home-container">
      <div className="language-selector">{<LanguageSelector />}</div>

      <h1>{t('app.title')}</h1>
      <p>{t('app.subtitle')}</p>
    </div>
  );
};
