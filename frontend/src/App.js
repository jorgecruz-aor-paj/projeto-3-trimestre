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

// TODO Ler e definir se é boa escolha para o design pattern da aplicação
/* Application design pattern!
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│  API Utilities │     │     Service    │     │  Data Store    │
│                │     │                │     │                │
│ • Fetch        │     │ • Orchestrate  │     │ • Manage state │
│ • Parse        │ ──► │ • Format data  │ ──► │ • Update UI    │
│ • Handle errors│     │ • Add context  │     │ • Handle logic │
└────────────────┘     └────────────────┘     └────────────────┘
*/
