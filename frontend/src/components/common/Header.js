import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Container, Navbar, Nav, Button, Dropdown } from 'react-bootstrap';

import { LanguageSelector } from 'components/LanguageSelector';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { NotificationOffcanvas } from 'components/common/NotificationOffcanvas';
import { useAuthStore } from 'stores/AuthStore';
import { useMessageStore } from 'stores/MessageStore';
import { useNotificationStore } from 'stores/NotificationStore';
import { authService } from 'services/authService';
import { websocketService } from 'services/websocketService';
import 'styles/components/Header.css';

/**
 * Header component with responsive navigation
 * @returns {React.Component} Header component
 */
export const Header = () => {
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const { t } = useTranslation();
  const location = useLocation();
  const { token, user, clearState } = useAuthStore();
  const { unreadCount: messageUnreadCount, loadUnreadCount } =
    useMessageStore();
  const { unreadCount: notificationUnreadCount, loadNotifications } =
    useNotificationStore();
  const navigate = useNavigate();

  const isAuthenticated = !!token;

  // Combined unread count for both messages and notifications
  const totalUnreadCount = messageUnreadCount + notificationUnreadCount;

  // Handle WebSocket notifications in the Header
  const handleHeaderNotification = notificationData => {
    console.log('Header received notification:', notificationData);

    switch (notificationData.type) {
      case 'new_message':
        // Update unread count when new message arrives
        loadUnreadCount();
        break;
      case 'new_notification':
        // Refresh the notification list to get notifications from backend
        loadNotifications();
        break;
      case 'message_read':
        // Handle message read status updates
        break;
      default:
        console.warn('Unknown notification type:', notificationData.type);
    }
  };

  // Check if the current route is active
  const isActive = path => location.pathname === path;

  // Toggle mobile menu
  const toggleMobileMenu = () => setShowMobileMenu(!showMobileMenu);

  // Toggle notifications offcanvas
  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
    if (!showNotifications) {
      // Load latest notifications when opening
      loadNotifications();
    }
  };

  // Close mobile menu when route changes
  useEffect(() => {
    setShowMobileMenu(false);
  }, [location.pathname]);

  // Initialize WebSocket and load notifications when user is authenticated
  useEffect(() => {
    if (isAuthenticated && user?.id) {
      websocketService.connect();
      const unsubscribe = websocketService.onNotification(
        handleHeaderNotification
      );
      loadUnreadCount();
      loadNotifications();

      return () => {
        unsubscribe();
      };
    } else {
      websocketService.disconnect();
    }
  }, [isAuthenticated, user?.id, loadUnreadCount, loadNotifications]);

  // Handle logout
  const handleLogout = async () => {
    try {
      websocketService.disconnect();
      await authService.logout();
      navigate('/');
      clearState();
    } catch (error) {
      console.error('Logout failed:', error);
      // Clear state anyway in case of network error
      clearState();
    }
  };

  return (
    <header className="main-header">
      <Container fluid className="px-lg-5">
        <Navbar expand="lg" className="py-2">
          <Navbar.Brand as={Link} to="/">
            <img
              src="/images/logo.png"
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
                {/* Notification Bell */}
                <div className="position-relative me-3">
                  <Button
                    variant="link"
                    className="p-0 text-white notification-bell"
                    onClick={toggleNotifications}
                    title={t('nav.notifications', 'Notifications')}
                  >
                    <i className="fas fa-bell" aria-hidden="true"></i>
                    {totalUnreadCount > 0 && (
                      <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill">
                        {totalUnreadCount > 99 ? '99+' : totalUnreadCount}
                        <span className="visually-hidden">
                          unread notifications
                        </span>
                      </span>
                    )}
                  </Button>
                </div>

                <Dropdown align="end">
                  <Dropdown.Toggle
                    variant="link"
                    id="user-dropdown"
                    className="user-menu-toggle"
                  >
                    <ProfilePicture
                      userId={user?.id}
                      alt={t('common.userAvatar')}
                      className="user-avatar"
                    />
                    <span className="user-name ms-2">
                      {user.firstName} {user.lastName}
                    </span>
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item as={Link} to="/profile">
                      {t('user.profile')}
                    </Dropdown.Item>
                    <Dropdown.Item as={Link} to="/dashboard">
                      {t('user.dashboard')}
                    </Dropdown.Item>
                    <Dropdown.Item as={Link} to="/messages">
                      {t('user.messages')}
                    </Dropdown.Item>
                    <Dropdown.Item as={Link} to="/settings">
                      {t('user.settings')}
                    </Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item onClick={handleLogout}>
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
                <>
                  <Nav.Link
                    as={Link}
                    to="/users"
                    className={isActive('/users') ? 'active' : ''}
                  >
                    {t('nav.users')}
                  </Nav.Link>
                </>
              )}
              {isAuthenticated && user.role === 'ADMIN' && (
                <>
                  <Nav.Link
                    as={Link}
                    to="/evaluation-cycles"
                    className={isActive('/evaluation-cycles') ? 'active' : ''}
                  >
                    {t('nav.cycles')}
                  </Nav.Link>
                </>
              )}
            </Nav>

            <div className="d-block d-md-none mt-3">
              <LanguageSelector className="mb-3" />
            </div>
          </Navbar.Collapse>
        </Navbar>
      </Container>

      {/* Notification Offcanvas */}
      <NotificationOffcanvas
        show={showNotifications}
        onHide={() => setShowNotifications(false)}
      />
    </header>
  );
};
