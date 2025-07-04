import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Card, Row, Col, ListGroup, Button, Dropdown } from 'react-bootstrap';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import { FormInput } from '../forms/FormInput';
import { useAuthStore } from '../../stores/AuthStore';
import { userService } from 'services';
import { useUserList } from '../../hooks/useUserList';

export const UserProfileInfo = ({ user: initialUser, onUserUpdate }) => {
  const { t } = useTranslation();
  const { formatDate } = useLanguage();
  const loggedUser = useAuthStore(state => state.user);
  const [user, setUser] = useState(initialUser);
  const isSelf = loggedUser?.id === user.id;
  const isAdmin = loggedUser?.role === 'ADMIN';

  const {
    userList,
    loading: userListLoading,
    error: userListError,
  } = useUserList();

  // Filtered manager list (exclude current user)
  const [filteredManagerList, setFilteredManagerList] = useState([]);
  useEffect(() => {
    if (userList && user && user.id) {
      setFilteredManagerList(
        userList.filter(u => String(u.id) !== String(user.id))
      );
    } else {
      setFilteredManagerList(userList || []);
    }
  }, [userList, user]);

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

  // Sync user state when initialUser prop changes
  useEffect(() => {
    setUser(initialUser);
  }, [initialUser]);

  // Sync formData when user changes
  useEffect(() => {
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
  }, [user]);

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

  const handleSave = async () => {
    // Only call assignManager if manager changed (admin only)
    let newManager = user.manager;
    if (
      isAdmin &&
      user.id &&
      String(formData.managerId) !== String(user.manager?.id) &&
      formData.managerId
    ) {
      try {
        console.log(formData.managerId);
        const response = await userService.assignManager({
          managerId: formData.managerId,
          userIds: [user.id],
        });
        // Find the selected manager from the filteredManagerList
        newManager = filteredManagerList.find(
          u => String(u.id) === String(formData.managerId)
        );
        console.log('Manager assigned successfully:', response);
      } catch (err) {
        // Optionally handle error, e.g., show notification
        console.error('Failed to assign manager:', err);
      }
    }

    // Always update user with new data
    try {
      const updatePayload = {
        ...user,
        firstName: formData.firstName,
        lastName: formData.lastName,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
        birthdate: formData.birthdate,
        workplace: { ...user.workplace, name: formData.workplace },
        biography: formData.biography,
        role: formData.role,
        manager: newManager,
      };
      const response = await userService.update(updatePayload);
      console.log('User updated successfully:', response);

      // Always update local user state
      setUser(response);

      if (isSelf) {
        // Update logged user in auth store if it's the self profile
        useAuthStore.setState({ user: response });
      }

      // Call the parent's update callback if provided
      if (onUserUpdate) {
        onUserUpdate(response);
      }
    } catch (err) {
      // Optionally handle error, e.g., show notification
      console.error('Failed to update user:', err);
    }

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
                      {(() => {
                        if (!filteredManagerList)
                          return t('profile.selectManager');
                        const selected = filteredManagerList.find(
                          u => String(u.id) === String(formData.managerId)
                        );
                        return selected
                          ? `${selected.firstName} ${selected.lastName}`
                          : t('profile.selectManager');
                      })()}
                    </Dropdown.Toggle>
                    <Dropdown.Menu>
                      {filteredManagerList && filteredManagerList.length > 0 ? (
                        filteredManagerList.map(u => (
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
                ) : user.manager &&
                  (user.manager.firstName || user.manager.lastName) ? (
                  `${user.manager.firstName ? user.manager.firstName : ''}${
                    user.manager.firstName && user.manager.lastName ? ' ' : ''
                  }${user.manager.lastName ? user.manager.lastName : ''}`.trim()
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
  onUserUpdate: PropTypes.func, // New optional prop
};
