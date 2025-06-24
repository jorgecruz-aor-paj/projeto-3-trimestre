import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Container, Navbar, Nav, Button, Dropdown } from 'react-bootstrap';

import { LanguageSelector } from 'components/LanguageSelector';
import logo from '../../logo.svg';
import 'styles/components/Header.css';

/**
 * Header component with responsive navigation
 * @returns {React.Component} Header component
 */
const Header = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const { t } = useTranslation();
  const location = useLocation();

  // Check if the current route is active
  const isActive = path => location.pathname === path;

  // Toggle mobile menu
  const toggleMobileMenu = () => setShowMobileMenu(!showMobileMenu);

  // Close mobile menu when route changes
  useEffect(() => {
    setShowMobileMenu(false);
  }, [location.pathname]);

  // Check authentication status (placeholder - replace with your auth logic)
  useEffect(() => {
    // Replace with your auth check logic
    const token = localStorage.getItem('token');
    setIsAuthenticated(!!token);
  }, []);

  return (
    <header className="main-header">
      <Container fluid className="px-lg-5">
        <Navbar expand="lg" className="py-2">
          <Navbar.Brand as={Link} to="/">
            <img
              src={logo}
              alt={t('common.appName')}
              height="40"
              className="d-inline-block align-top"
            />
            <span className="brand-text">{t('common.appName')}</span>
          </Navbar.Brand>

          <div className="d-flex align-items-center order-lg-last ms-auto">
            <LanguageSelector className="me-3 d-none d-md-block" />

            {isAuthenticated ? (
              <div className="d-flex align-items-center">
                <Dropdown align="end">
                  <Dropdown.Toggle
                    variant="link"
                    id="user-dropdown"
                    className="user-menu-toggle"
                  >
                    <img
                      src="/default-avatar.png"
                      alt={t('common.userAvatar')}
                      className="user-avatar"
                    />
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item as={Link} to="/profile">
                      {t('user.profile')}
                    </Dropdown.Item>
                    <Dropdown.Item as={Link} to="/settings">
                      {t('user.settings')}
                    </Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item onClick={() => console.log('Logout')}>
                      {t('user.logout')}
                    </Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </div>
            ) : (
              <div className="auth-buttons">
                <Button
                  as={Link}
                  to="/login"
                  variant="outline-primary"
                  className="me-2"
                >
                  {t('auth.login')}
                </Button>
                <Button as={Link} to="/register" variant="primary">
                  {t('auth.register')}
                </Button>
              </div>
            )}

            <Navbar.Toggle
              aria-controls="main-navbar"
              className="ms-3 border-0 mobile-toggle"
              onClick={toggleMobileMenu}
            />
          </div>

          <Navbar.Collapse
            id="main-navbar"
            in={showMobileMenu}
            className="order-lg-2"
          >
            <Nav className="mx-auto">
              <Nav.Link
                as={Link}
                to="/"
                className={isActive('/') ? 'active' : ''}
              >
                {t('nav.home')}
              </Nav.Link>
              <Nav.Link
                as={Link}
                to="/trainings"
                className={isActive('/trainings') ? 'active' : ''}
              >
                {t('nav.trainings')}
              </Nav.Link>
              <Nav.Link
                as={Link}
                to="/evaluations"
                className={isActive('/evaluations') ? 'active' : ''}
              >
                {t('nav.evaluations')}
              </Nav.Link>
              {isAuthenticated && (
                <Nav.Link
                  as={Link}
                  to="/dashboard"
                  className={isActive('/dashboard') ? 'active' : ''}
                >
                  {t('nav.dashboard')}
                </Nav.Link>
              )}
              <Nav.Link
                as={Link}
                to="/about"
                className={isActive('/about') ? 'active' : ''}
              >
                {t('nav.about')}
              </Nav.Link>
            </Nav>

            <div className="d-block d-md-none mt-3">
              <LanguageSelector className="mb-3" />
            </div>
          </Navbar.Collapse>
        </Navbar>
      </Container>
    </header>
  );
};

export default Header;
