import React from 'react';
import { Outlet } from 'react-router-dom';

import Header from 'components/common/Header';
import Footer from 'components/common/Footer';
import 'styles/components/MainLayout.css';

/**
 * Main layout component that wraps all pages with common header and footer
 * @returns {React.Component} Layout with header, main content area, and footer
 */
const MainLayout = () => {
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

export default MainLayout;
