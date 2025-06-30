import React from 'react';
import PropTypes from 'prop-types';
import { Card, Row, Col, ListGroup } from 'react-bootstrap';
import { ProfilePicture } from 'components/common/ProfilePicture';

export const UserProfileInfo = ({ user }) => (
  <Card className="mb-4">
    <Card.Header as="h5">Profile Information</Card.Header>
    <Card.Body>
      <Row>
        <Col md={4} className="text-center">
          <ProfilePicture
            userId={user.id}
            alt={`${user.firstName} ${user.lastName}`}
            className="rounded-circle mb-3"
            style={{ width: 120, height: 120, objectFit: 'cover' }}
          />
        </Col>
        <Col md={8}>
          <ListGroup variant="flush">
            <ListGroup.Item>
              <strong>Name:</strong> {user.firstName} {user.lastName}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Email:</strong> {user.email}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Phone:</strong> {user.phoneNumber}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Address:</strong> {user.address}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Birthdate:</strong> {user.birthdate}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Workplace:</strong>{' '}
              {user.workplaceName || user.workplaceId}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Biography:</strong> {user.biography}
            </ListGroup.Item>
            <ListGroup.Item>
              <strong>Role:</strong> {user.role}
            </ListGroup.Item>
          </ListGroup>
        </Col>
      </Row>
    </Card.Body>
  </Card>
);

UserProfileInfo.propTypes = {
  user: PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    firstName: PropTypes.string.isRequired,
    lastName: PropTypes.string.isRequired,
    email: PropTypes.string,
    phoneNumber: PropTypes.string,
    address: PropTypes.string,
    birthdate: PropTypes.string,
    workplaceName: PropTypes.string,
    workplaceId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    biography: PropTypes.string,
    role: PropTypes.string,
  }).isRequired,
};
