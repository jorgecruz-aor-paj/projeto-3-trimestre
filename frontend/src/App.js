import React from 'react';
import 'styles/App.css';
import { LanguageProvider } from 'contexts/LanguageContext';
import { BrowserRouter } from 'react-router-dom';
import { AppRouter } from 'routes/AppRouter';

export const App = () => {
  return (
    <LanguageProvider>
      <BrowserRouter>
        <AppRouter />
      </BrowserRouter>
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
