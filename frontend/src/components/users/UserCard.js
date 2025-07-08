import React from 'react';
import PropTypes from 'prop-types';
import { Card, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { useTranslation } from 'react-i18next';
import 'styles/components/UserCard.css';

export const UserCard = ({ user }) => {
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleCardClick = () => {
    navigate(`/profile?userId=${user.id}`);
  };

  return (
    <Card
      className="mb-3 shadow-sm user-card"
      onClick={handleCardClick}
      style={{ cursor: 'pointer' }}
    >
      <Card.Body>
        <div className="d-flex align-items-center">
          <ProfilePicture
            userId={user.id}
            alt={`${user.firstName} ${user.lastName}`}
            className="rounded-circle me-3"
            style={{ width: 64, height: 64, objectFit: 'cover' }}
          />
          <div>
            <h5 className="mb-1">
              {user.firstName} {user.lastName}{' '}
              <Badge bg="secondary">{user.role}</Badge>
            </h5>
            <div className="text-muted small mb-1">
              <i className="bi bi-envelope-fill me-1"></i>
              <span>{user.email}</span>
            </div>
            {user.phoneNumber && (
              <div className="text-muted small mb-1">
                <i className="bi bi-telephone-fill me-1"></i>
                <span>{user.phoneNumber}</span>
              </div>
            )}
            {user.workplace && (
              <div className="text-muted small">
                <i className="bi bi-building me-1"></i>
                {user.workplace.name}
              </div>
            )}
            <div className="text-muted small">
              <i className="bi bi-person-badge me-1"></i>
              {user.manager
                ? `${t('profile.manager')}: ${user.manager.firstName} ${
                    user.manager.lastName
                  }`
                : t('profile.noManagerAssigned')}
            </div>
            <small className="text-primary mt-2">
              <i className="fas fa-user me-1"></i>
              View profile
            </small>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

UserCard.propTypes = {
  user: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    firstName: PropTypes.string.isRequired,
    lastName: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
    role: PropTypes.string.isRequired,
    phoneNumber: PropTypes.string,
    address: PropTypes.string,
    biography: PropTypes.string,
    photograph: PropTypes.bool,
    workplace: PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      name: PropTypes.string,
    }),
    manager: PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      firstName: PropTypes.string,
      lastName: PropTypes.string,
      email: PropTypes.string,
      photograph: PropTypes.string,
    }),
  }).isRequired,
};
