import React from 'react';
import logo from 'logo.svg';
import 'styles/App.css';

export function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload. Or simply run
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

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
