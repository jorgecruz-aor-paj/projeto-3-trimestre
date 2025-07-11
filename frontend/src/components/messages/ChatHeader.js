import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import 'styles/components/ChatHeader.css';

export const ChatHeader = ({ user }) => {
  const { t } = useTranslation();

  if (!user) return null;

  return (
    <div className="border-bottom p-3 bg-light chat-header">
      <div className="d-flex align-items-center">
        <div className="avatar-sm me-3">
          <div className="avatar-initial rounded-circle bg-primary">
            {user.firstName?.charAt(0)}
            {user.lastName?.charAt(0)}
          </div>
        </div>
        <div className="flex-fill">
          <h6 className="mb-0">
            {user.firstName} {user.lastName}
          </h6>
          <small className="text-muted">{user.email}</small>
        </div>
        <div className="text-muted">
          <i
            className="fas fa-circle text-success"
            style={{ fontSize: '8px' }}
          ></i>
          <small className="ms-1">{t('messages.online', 'Online')}</small>
        </div>
      </div>
    </div>
  );
};

ChatHeader.propTypes = {
  user: PropTypes.shape({
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    email: PropTypes.string,
  }),
};
