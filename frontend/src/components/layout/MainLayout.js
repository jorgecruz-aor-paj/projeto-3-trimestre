import React from 'react';
import { Outlet } from 'react-router-dom';

import { Header } from 'components/common/Header';
import { Footer } from 'components/common/Footer';
import 'styles/components/MainLayout.css';

/**
 * Main layout component that wraps all pages with common header and footer.
 * Uses React Router's Outlet component to render child route components dynamically
 * while maintaining consistent layout structure across all pages.
 *
 * The Outlet acts as a placeholder that renders the component associated with the
 * current route (e.g., HomePage for /home, AboutPage for /about), allowing this
 * layout to serve as a wrapper that provides consistent header and footer elements
 * without duplicating them in each page component.
 *
 * @returns {React.Component} Layout with header, main content area (via Outlet), and footer
 * @example
 * // Route: /home → Outlet renders <HomePage />
 * // Route: /about → Outlet renders <AboutPage />
 * // Result: [Header] [Page Content] [Footer]
 */
export const MainLayout = () => {
  return (
    <div className="app-container d-flex flex-column min-vh-100">
      <Header />
      <main className="flex-grow-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};
