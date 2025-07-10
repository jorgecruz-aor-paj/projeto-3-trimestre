import React from 'react';
import { BrowserRouter } from 'react-router-dom';

import { LanguageProvider } from 'contexts/LanguageContext';
import { AppRouter } from 'routes/AppRouter';
import { AlertProvider } from 'contexts/AlertContext';

import 'styles/App.css';

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

