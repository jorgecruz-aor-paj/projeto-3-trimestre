import React from 'react';
import { Card, Row, Col, ListGroup } from 'react-bootstrap';

export const UserProfileInfo = ({ user }) => (
  <Card className="mb-4">
    <Card.Header as="h5">Profile Information</Card.Header>
    <Card.Body>
      <Row>
        <Col md={4} className="text-center">
          {user.photograph && (
            <img
              src={`/api/users/${user.id}/profile/picture`}
              alt={user.firstName + ' ' + user.lastName}
              className="rounded-circle mb-3"
              style={{ width: 120, height: 120, objectFit: 'cover' }}
            />
          )}
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
