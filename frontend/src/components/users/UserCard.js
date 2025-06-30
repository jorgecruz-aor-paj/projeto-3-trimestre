import React from 'react';
import { Card, Badge } from 'react-bootstrap';

export const UserCard = ({ user }) => {
  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        <div className="d-flex align-items-center">
          {user.photograph && (
            <img
              src={`/api/users/${user.id}/profile/picture`}
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
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};
