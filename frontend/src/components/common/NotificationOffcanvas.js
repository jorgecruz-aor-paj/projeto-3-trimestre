import React, { useEffect } from 'react';
import {
  Offcanvas,
  Button,
  ListGroup,
  Badge,
  Spinner,
  Alert,
} from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useNotificationStore } from 'stores/NotificationStore';
import { useLanguage } from 'contexts/LanguageContext';
import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import 'styles/components/NotificationOffcanvas.css';

/**
 * Notification Offcanvas Component
 * Displays notifications in a sliding panel
 */
export const NotificationOffcanvas = ({ show, onHide }) => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { formatDate, convertInstantToDate } = useLanguage();

  const {
    notifications,
    unreadCount,
    loading,
    error,
    loadNotifications,
    loadMoreNotifications,
    markAsRead,
    markAllAsRead,
    deleteNotification,
    clearError,
    hasMore,
    loadingMore,
  } = useNotificationStore();

  // Load notifications when offcanvas is shown
  useEffect(() => {
    if (show) {
      loadNotifications();
    }
  }, [show, loadNotifications]);

  // Format relative time using existing language utilities
  const formatRelativeTime = timestamp => {
    try {
      const date = convertInstantToDate(timestamp);
      if (!date) return t('common.invalidDate', 'Data inválida');

      const now = new Date();
      const diffInSeconds = Math.floor((now - date) / 1000);

      if (diffInSeconds < 60) {
        return t('time.justNow', 'Agora mesmo');
      } else if (diffInSeconds < 3600) {
        const minutes = Math.floor(diffInSeconds / 60);
        return t('time.minutesAgo', '{{count}} min', { count: minutes });
      } else if (diffInSeconds < 86400) {
        const hours = Math.floor(diffInSeconds / 3600);
        return t('time.hoursAgo', 'há {{count}}h', { count: hours });
      } else if (diffInSeconds < 604800) {
        const days = Math.floor(diffInSeconds / 86400);
        return t('time.daysAgo', 'há {{count}}d', { count: days });
      } else {
        // For older dates, use the formatted date
        return formatDate(date, {
          month: 'short',
          day: 'numeric',
          ...(date.getFullYear() !== now.getFullYear() && { year: 'numeric' }),
        });
      }
    } catch (formatError) {
      console.error('Error formatting relative time:', formatError);
      return t('common.invalidDate', 'Data inválida');
    }
  };

  // Handle notification click
  const handleNotificationClick = async notification => {
    if (!notification.read) {
      await markAsRead(notification.id);
    }

    // Handle navigation based on notification type
    switch (notification.type) {
      case 'MESSAGE':
        navigate('/messages');
        break;
      case 'EVALUATION':
        navigate('/evaluations');
        break;
      case 'EDIT':
        navigate('/dashboard');
        break;
      case 'DELETE':
        navigate('/dashboard');
        break;
      default:
        break;
    }

    onHide();
  };

  // Get notification icon based on type
  const getNotificationIcon = type => {
    switch (type) {
      case 'MESSAGE':
        return 'fas fa-envelope';
      case 'EVALUATION':
        return 'fas fa-star';
      case 'EDIT':
        return 'fas fa-edit';
      case 'DELETE':
        return 'fas fa-trash';
      default:
        return 'fas fa-bell';
    }
  };

  // Get notification variant based on type
  const getNotificationVariant = type => {
    switch (type) {
      case 'MESSAGE':
        return 'primary';
      case 'EVALUATION':
        return 'warning';
      case 'EDIT':
        return 'info';
      case 'DELETE':
        return 'danger';
      default:
        return 'secondary';
    }
  };

  // Handle delete notification
  const handleDeleteNotification = async (e, notificationId) => {
    e.stopPropagation();
    try {
      await deleteNotification(notificationId);
    } catch (deleteError) {
      console.error('Error deleting notification:', deleteError);
    }
  };

  // Handle mark all as read
  const handleMarkAllAsRead = async () => {
    try {
      await markAllAsRead();
    } catch (markError) {
      console.error('Error marking all as read:', markError);
    }
  };

  return (
    <Offcanvas
      show={show}
      onHide={onHide}
      placement="end"
      className="notification-offcanvas"
    >
      <Offcanvas.Header closeButton>
        <Offcanvas.Title>
          <i className="fas fa-bell me-2"></i>
          {t('notifications.title', 'Notificações')}
          {unreadCount > 0 && (
            <Badge bg="danger" className="ms-2">
              {unreadCount > 99 ? '99+' : unreadCount}
            </Badge>
          )}
        </Offcanvas.Title>
      </Offcanvas.Header>

      <Offcanvas.Body className="p-0">
        {/* Error Alert */}
        {error && (
          <Alert
            variant="danger"
            className="m-3 mb-0"
            dismissible
            onClose={clearError}
          >
            {error}
          </Alert>
        )}

        {/* Header Actions */}
        {notifications.length > 0 && (
          <div className="notification-actions p-3 border-bottom">
            <Button
              variant="outline-primary"
              size="sm"
              onClick={handleMarkAllAsRead}
              disabled={unreadCount === 0 || loading}
            >
              <i className="fas fa-check-double me-1"></i>
              {t('notifications.markAllRead', 'Marcar todas como lidas')}
            </Button>
          </div>
        )}

        {/* Notifications List */}
        <div className="notifications-list">
          {loading ? (
            <div className="text-center p-4">
              <Spinner animation="border" size="sm" />
              <p className="mt-2 mb-0">
                {t('common.loading', 'A carregar...')}
              </p>
            </div>
          ) : notifications.length === 0 ? (
            <div className="text-center p-4 text-muted">
              <i className="fas fa-bell-slash fa-2x mb-3"></i>
              <p>{t('notifications.empty', 'Sem notificações')}</p>
            </div>
          ) : (
            <ListGroup variant="flush">
              {notifications.map(notification => (
                <ListGroup.Item
                  key={notification.id}
                  className={`notification-item ${
                    !notification.read ? 'unread' : ''
                  } cursor-pointer`}
                  onClick={() => handleNotificationClick(notification)}
                >
                  <div className="d-flex align-items-start">
                    <div className="notification-icon me-3">
                      <i
                        className={`${getNotificationIcon(
                          notification.type
                        )} text-${getNotificationVariant(notification.type)}`}
                      ></i>
                    </div>

                    <div className="notification-content flex-grow-1">
                      <div className="notification-text">
                        {notification.content}
                      </div>

                      <div className="notification-meta">
                        <small className="text-muted">
                          {formatRelativeTime(notification.timestampSent)}
                        </small>
                        {!notification.read && (
                          <Badge bg="primary" size="sm" className="ms-2">
                            {t('notifications.new', 'Nova')}
                          </Badge>
                        )}
                      </div>
                    </div>

                    <div className="notification-actions">
                      <Button
                        variant="link"
                        size="sm"
                        className="p-1 text-muted"
                        onClick={e =>
                          handleDeleteNotification(e, notification.id)
                        }
                        title={t('common.delete', 'Eliminar')}
                      >
                        <i className="fas fa-times"></i>
                      </Button>
                    </div>
                  </div>
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}

          {/* Load More Button */}
          {notifications.length > 0 && hasMore && (
            <div className="text-center p-3 border-top">
              <Button
                variant="outline-primary"
                size="sm"
                onClick={loadMoreNotifications}
                disabled={loadingMore}
              >
                {loadingMore ? (
                  <>
                    <Spinner animation="border" size="sm" className="me-2" />
                    {t('common.loading', 'A carregar...')}
                  </>
                ) : (
                  <>
                    <i className="fas fa-chevron-down me-1"></i>
                    {t('notifications.loadMore', 'Carregar mais')}
                  </>
                )}
              </Button>
            </div>
          )}
        </div>
      </Offcanvas.Body>
    </Offcanvas>
  );
};

// PropTypes validation
NotificationOffcanvas.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
};
