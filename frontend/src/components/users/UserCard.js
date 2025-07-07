import React from 'react';
import PropTypes from 'prop-types';
import { Card, Badge } from 'react-bootstrap';
import { API_ENDPOINTS } from 'api/apiConfig';
import { useNavigate } from 'react-router-dom';
import 'styles/components/UserCard.css';

export const UserCard = ({ user }) => {
  const navigate = useNavigate();

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
          {user.photograph && user.id && (
            <img
              src={API_ENDPOINTS.user.profilePicture(user.id).url}
              alt={user.firstName + ' ' + user.lastName}
              className="rounded-circle me-3"
              style={{ width: 64, height: 64, objectFit: 'cover' }}
            />
          )}
          <div>
            <h5 className="mb-1">
              {user.firstName} {user.lastName}{' '}
              <Badge bg="secondary">{user.role}</Badge>
            </h5>
            <div className="text-muted small">{user.email}</div>
            <div className="text-muted small">{user.phoneNumber}</div>
            <div className="text-muted small">{user.address}</div>
            <div className="text-muted small">{user.biography}</div>
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
  }).isRequired,
};
