import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Card, Row, Col, ListGroup, Button, Dropdown } from 'react-bootstrap';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import { FormInput } from '../forms/FormInput';
import { useAuthStore } from '../../stores/AuthStore';
import { userService } from 'services';

export const UserProfileInfo = ({ user }) => {
  const { t } = useTranslation();
  const { formatDate } = useLanguage();
  const loggedUser = useAuthStore(state => state.user);
  const isSelf = loggedUser?.id === user.id;
  const isAdmin = loggedUser?.role === 'ADMIN';
  const [userList, setUserList] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await userService.getMiniUsers();
        if (!response || response.length === 0) {
          // Fallback: invalid user param, show logged user
          setUserList([]);
          return;
        }
        console.log(response);
        setUserList(response);
      } catch (err) {
        console.error(
          'Failed to fetch user:',
          err.message || t('auth.loginFailed')
        );
        setProfileUser(user);
      }
    };
    fetchData();
  }, []);

  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({
    firstName: user.firstName || '',
    lastName: user.lastName || '',
    phoneNumber: user.phoneNumber || '',
    address: user.address || '',
    birthdate: user.birthdate || '',
    workplace: user.workplace?.name || '',
    biography: user.biography || '',
    role: user.role || '',
    managerId: user.manager?.id || '',
  });
  const [errors, setErrors] = useState({});

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleManagerSelect = id => {
    setFormData(prev => ({ ...prev, managerId: id }));
  };

  const handleCancel = () => {
    setFormData({
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      phoneNumber: user.phoneNumber || '',
      address: user.address || '',
      birthdate: user.birthdate || '',
      workplace: user.workplace?.name || '',
      biography: user.biography || '',
      role: user.role || '',
      managerId: user.manager?.id || '',
    });
    setEditMode(false);
    setErrors({});
  };

  const handleSave = () => {
    // TODO: Add validation and save logic (API call)
    setEditMode(false);
  };

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
            <div className="mt-3">
              {(isSelf || isAdmin) &&
                (!editMode ? (
                  <Button variant="primary" onClick={() => setEditMode(true)}>
                    {t('common.edit')}
                  </Button>
                ) : (
                  <>
                    <Button
                      variant="success"
                      className="me-2"
                      onClick={handleSave}
                    >
                      {t('common.save')}
                    </Button>
                    <Button variant="secondary" onClick={handleCancel}>
                      {t('common.cancel')}
                    </Button>
                  </>
                ))}
            </div>
          </Col>
          <Col md={8}>
            <ListGroup variant="flush">
              {/* Email (not editable) - now first */}
              <ListGroup.Item>
                <strong>{t('profile.email')}:</strong> {user.email}
              </ListGroup.Item>
              {/* Name */}
              <ListGroup.Item>
                <strong>{t('profile.name')}:</strong>{' '}
                {editMode ? (
                  <>
                    <FormInput
                      name="firstName"
                      label={t('profile.firstName')}
                      type="text"
                      value={formData.firstName}
                      onChange={handleChange}
                      error={errors.firstName}
                    />
                    <FormInput
                      name="lastName"
                      label={t('profile.lastName')}
                      type="text"
                      value={formData.lastName}
                      onChange={handleChange}
                      error={errors.lastName}
                    />
                  </>
                ) : (
                  `${user.firstName} ${user.lastName}`
                )}
              </ListGroup.Item>
              {/* Phone */}
              <ListGroup.Item>
                <strong>{t('profile.phone')}:</strong>{' '}
                {editMode ? (
                  <FormInput
                    name="phoneNumber"
                    label={t('profile.phone')}
                    type="text"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    error={errors.phoneNumber}
                  />
                ) : (
                  user.phoneNumber
                )}
              </ListGroup.Item>
              {/* Address */}
              <ListGroup.Item>
                <strong>{t('profile.address')}:</strong>{' '}
                {editMode ? (
                  <FormInput
                    name="address"
                    label={t('profile.address')}
                    type="text"
                    value={formData.address}
                    onChange={handleChange}
                    error={errors.address}
                  />
                ) : (
                  user.address
                )}
              </ListGroup.Item>
              {/* Birthdate */}
              <ListGroup.Item>
                <strong>{t('profile.birthdate')}:</strong>{' '}
                {editMode ? (
                  <FormInput
                    name="birthdate"
                    label={t('profile.birthdate')}
                    type="date"
                    value={formData.birthdate}
                    onChange={handleChange}
                    error={errors.birthdate}
                  />
                ) : (
                  formatDate(user.birthdate, {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  })
                )}
              </ListGroup.Item>
              {/* Workplace */}
              <ListGroup.Item>
                <strong>{t('profile.workplace')}:</strong>{' '}
                {editMode ? (
                  <FormInput
                    name="workplace"
                    label={t('profile.workplace')}
                    type="text"
                    value={formData.workplace}
                    onChange={handleChange}
                    error={errors.workplace}
                  />
                ) : (
                  user.workplace?.name
                )}
              </ListGroup.Item>
              {/* Biography */}
              <ListGroup.Item>
                <strong>{t('profile.biography')}:</strong>{' '}
                {editMode ? (
                  <FormInput
                    name="biography"
                    label={t('profile.biography')}
                    type="text"
                    value={formData.biography}
                    onChange={handleChange}
                    error={errors.biography}
                  />
                ) : (
                  user.biography
                )}
              </ListGroup.Item>
              {/* Manager (always display, only admin can edit) */}
              <ListGroup.Item>
                <strong>{t('profile.manager')}:</strong>{' '}
                {editMode && isAdmin ? (
                  <Dropdown onSelect={handleManagerSelect}>
                    <Dropdown.Toggle
                      variant="outline-secondary"
                      id="manager-dropdown"
                    >
                      {userList &&
                      userList.find(u => u.id === formData.managerId)
                        ? `${
                            userList.find(u => u.id === formData.managerId)
                              .firstName
                          } ${
                            userList.find(u => u.id === formData.managerId)
                              .lastName
                          }`
                        : t('profile.selectManager')}
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {userList && userList.length > 0 ? (
                        userList.map(u => (
                          <Dropdown.Item
                            key={u.id}
                            eventKey={u.id}
                            active={formData.managerId === u.id}
                          >
                            {u.firstName} {u.lastName}
                          </Dropdown.Item>
                        ))
                      ) : (
                        <Dropdown.Item disabled>
                          {t('profile.noManager')}
                        </Dropdown.Item>
                      )}
                    </Dropdown.Menu>
                  </Dropdown>
                ) : user.manager ? (
                  `${user.manager.firstName} ${user.manager.lastName}`
                ) : (
                  t('profile.noManager')
                )}
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
    workplace: PropTypes.shape({
      name: PropTypes.string,
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    }),
    biography: PropTypes.string,
    role: PropTypes.string,
    manager: PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
      firstName: PropTypes.string,
      lastName: PropTypes.string,
    }),
  }).isRequired,
};
