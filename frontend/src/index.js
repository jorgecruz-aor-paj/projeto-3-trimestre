import React from 'react';
import ReactDOM from 'react-dom/client';

// Import Bootstrap and Bootstrap Icons CSS
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
// Import our custom Bootstrap override styles (must be after Bootstrap import)
import 'styles/bootstrap-override.css';

import 'styles/index.css';
import reportWebVitals from 'reportWebVitals';
import { App } from 'App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
