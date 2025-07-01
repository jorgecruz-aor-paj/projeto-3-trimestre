import React from 'react';
import PropTypes from 'prop-types';
import { Card, Row, Col, ListGroup } from 'react-bootstrap';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';

export const UserProfileInfo = ({ user }) => {
  const { t } = useTranslation();
  const { formatDate } = useLanguage();
  return (
    <Card className="mb-4">
      <Card.Header as="h5">{t('profile.info')}</Card.Header>
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
                <strong>{t('profile.name')}:</strong> {user.firstName}{' '}
                {user.lastName}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.email')}:</strong> {user.email}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.phone')}:</strong> {user.phoneNumber}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.address')}:</strong> {user.address}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.birthdate')}:</strong>{' '}
                {formatDate(user.birthdate, {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.workplace')}:</strong>{' '}
                {user.workplace.name}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.biography')}:</strong> {user.biography}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>{t('profile.role')}:</strong> {user.role}
              </ListGroup.Item>
            </ListGroup>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

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
