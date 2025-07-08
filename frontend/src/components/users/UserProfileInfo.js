import React, { useEffect, useState, useRef } from 'react';
import PropTypes from 'prop-types';
import {
  Card,
  Row,
  Col,
  ListGroup,
  Button,
  Dropdown,
  Spinner,
  Form,
} from 'react-bootstrap';
import { ProfilePicture } from 'components/common/ProfilePicture';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import { FormInput } from '../forms/FormInput';
import { FormDropdownInput } from '../forms/FormDropdownInput';
import { useAuthStore } from '../../stores/AuthStore';
import { userService } from 'services';
import { useUserList } from '../../hooks/useUserList';
import { useWorkplaces } from '../../hooks/useWorkplaces';
import {
  useFormValidation,
  profileValidationRules,
} from '../../utils/validation';
import { useErrorMessage } from '../../hooks/useErrorMessage';
import { useAlert } from 'contexts/AlertContext';

export const UserProfileInfo = ({ user: initialUser, onUserUpdate }) => {
  const { t } = useTranslation();
  const { formatDate } = useLanguage();
  const loggedUser = useAuthStore(state => state.user);
  const { getErrorMessage } = useErrorMessage();
  const { showAlert } = useAlert();
  const [user, setUser] = useState(initialUser);
  const isSelf = loggedUser?.id === user.id;
  const isAdmin = loggedUser?.role === 'ADMIN';
  const fileInputRef = useRef(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadingProfilePicture, setUploadingProfilePicture] = useState(false);
  const [previewUrl, setPreviewUrl] = useState(null);

  const {
    userList,
    loading: userListLoading,
    error: userListError,
  } = useUserList();

  // Get workplace list
  const { workplaces, workplacesLoading } = useWorkplaces();

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

  // Initialize form validation with user data
  const initialFormData = {
    firstName: user.firstName || '',
    lastName: user.lastName || '',
    phoneNumber: user.phoneNumber || '',
    address: user.address || '',
    birthdate: user.birthdate || '',
    workplace: user.workplace?.id || '',
    biography: user.biography || '',
    role: user.role || '',
    managerId: user.manager?.id || '',
  };

  const {
    formData,
    errors,
    handleChange: handleValidationChange,
    validateAll,
    setFormData,
  } = useFormValidation(initialFormData, profileValidationRules);

  // Track which fields have been touched
  const [touched, setTouched] = useState({});

  // Sync user state when initialUser prop changes
  useEffect(() => {
    setUser(initialUser);
  }, [initialUser]);

  // Sync formData when user changes
  useEffect(() => {
    const newFormData = {
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      phoneNumber: user.phoneNumber || '',
      address: user.address || '',
      birthdate: user.birthdate || '',
      workplace: user.workplace?.id || '',
      biography: user.biography || '',
      role: user.role || '',
      managerId: user.manager?.id || '',
    };
    setFormData(newFormData);
  }, [user, setFormData]);

  const handleChange = e => {
    // Extract name and value from the event object
    if (e && e.target) {
      const { name, value } = e.target;
      console.log(`Field changed: ${name} = ${value}`);

      // Mark the field as touched when it changes
      setTouched(prev => ({
        ...prev,
        [name]: true,
      }));

      // Update the field value and validate it
      handleValidationChange(name, value);
    } else {
      console.error('Invalid event object passed to handleChange:', e);
    }
  };

  const handleManagerSelect = id => {
    console.log(`Manager selected: ${id}`);

    // Mark the field as touched
    setTouched(prev => ({
      ...prev,
      managerId: true,
    }));

    // Update the field value and validate it
    handleValidationChange('managerId', id);
  };

  const handleProfilePictureChange = e => {
    const file = e.target.files[0];
    if (file) {
      // Check file type
      if (!file.type.match('image.*')) {
        showAlert({
          title: t('profile.error'),
          message: t('profile.invalidFileType', 'Please select an image file'),
          variant: 'danger',
        });
        return;
      }

      // Check file size (max 5MB)
      const maxSize = 5 * 1024 * 1024; // 5MB in bytes
      if (file.size > maxSize) {
        showAlert({
          title: t('profile.error'),
          message: t('profile.fileTooLarge', 'File size must be less than 5MB'),
          variant: 'danger',
        });
        return;
      }

      setSelectedFile(file);

      // Create a preview URL
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewUrl(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleCancel = () => {
    // Reset form data to original user data
    const originalFormData = {
      firstName: user.firstName || '',
      lastName: user.lastName || '',
      phoneNumber: user.phoneNumber || '',
      address: user.address || '',
      birthdate: user.birthdate || '',
      workplace: user.workplace?.id || '',
      biography: user.biography || '',
      role: user.role || '',
      managerId: user.manager?.id || '',
    };
    setFormData(originalFormData);
    setTouched({}); // Reset touched state
    setEditMode(false);
    setSelectedFile(null);
    setPreviewUrl(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleSave = async () => {
    // Mark all fields as touched for validation feedback
    const allFieldsTouched = Object.keys(formData).reduce((acc, field) => {
      acc[field] = true;
      return acc;
    }, {});
    setTouched(allFieldsTouched);

    // Validate all fields before saving
    console.log('Attempting to save with data:', formData);
    const isValid = validateAll();
    if (!isValid) {
      console.error('Validation failed with errors:', errors);

      // Use AlertContext to show validation errors
      showAlert({
        title: t('validation.error'),
        message: t('validation.pleaseFixErrors'),
        variant: 'danger',
      });

      return; // Don't save if validation fails
    }

    // Upload profile picture if selected
    if (selectedFile && user.id) {
      try {
        setUploadingProfilePicture(true);
        await userService.uploadProfilePicture(user.id, selectedFile);
        // Clear profile picture cache to force reload
        if (window.profilePictureCache) {
          window.profilePictureCache.clearUser(user.id);
        }
        setSelectedFile(null);
        setPreviewUrl(null);
        if (fileInputRef.current) {
          fileInputRef.current.value = '';
        }

        showAlert({
          title: t('profile.success'),
          message: t(
            'profile.pictureUpdateSuccess',
            'Profile picture updated successfully'
          ),
          variant: 'success',
        });
      } catch (err) {
        console.error('Failed to upload profile picture:', err);
        showAlert({
          title: t('profile.error'),
          message: t(
            'profile.pictureUpdateError',
            'Failed to update profile picture'
          ),
          variant: 'danger',
        });
        setUploadingProfilePicture(false);
        return; // Don't continue if profile picture upload fails
      }
      setUploadingProfilePicture(false);
    }

    // Call assignManager if manager changed (for all users in development)
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
        console.error('Failed to assign manager:', err);
        showAlert({
          title: t('profile.error'),
          message: t('profile.managerAssignError', 'Failed to assign manager'),
          variant: 'danger',
        });
      }
    }

    // Always update user with new data
    try {
      // Find the selected workplace from the workplaces list
      const selectedWorkplace =
        workplaces.find(w => String(w.id) === String(formData.workplace)) ||
        user.workplace;

      const updatePayload = {
        ...user,
        firstName: formData.firstName,
        lastName: formData.lastName,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
        birthdate: formData.birthdate,
        workplace: selectedWorkplace,
        biography: formData.biography,
        role: formData.role,
        manager: newManager,
      };
      console.log('Updating user with payload:', updatePayload);
      const response = await userService.update(updatePayload);
      console.log('User updated successfully:', response);

      // Always update local user state
      setUser(response);

      if (isSelf) {
        // Update logged user in auth store if it's the self profile
        useAuthStore.setState({ user: response });
        console.log('Updated auth store with new user data');
      }

      // Call the parent's update callback if provided
      if (onUserUpdate) {
        onUserUpdate(response);
        console.log('Called onUserUpdate with response');
      }

      // Show success message
      showAlert({
        title: t('profile.success'),
        message: t('profile.updateSuccess', 'Profile updated successfully'),
        variant: 'success',
      });

      setEditMode(false);
    } catch (err) {
      console.error('Failed to update user:', err);
      showAlert({
        title: t('profile.error'),
        message:
          err.message ||
          t('profile.updateError', 'Failed to update user profile'),
        variant: 'danger',
      });
      return; // Don't close edit mode if there was an error
    }

    // Edit mode is closed in the success case
  };

  return (
    <Card className="mb-4">
      <Card.Header as="h5">{t('profile.info')}</Card.Header>
      <Card.Body>
        <Row>
          <Col md={4} className="text-center">
            {editMode ? (
              <div className="position-relative">
                {previewUrl ? (
                  <img
                    src={previewUrl}
                    alt={`${user.firstName} ${user.lastName} (Preview)`}
                    className="rounded-circle mb-3"
                    style={{ width: 120, height: 120, objectFit: 'cover' }}
                  />
                ) : (
                  <ProfilePicture
                    userId={user.id}
                    alt={`${user.firstName} ${user.lastName}`}
                    className="rounded-circle mb-3"
                    style={{ width: 120, height: 120, objectFit: 'cover' }}
                  />
                )}
                {uploadingProfilePicture && (
                  <div
                    className="position-absolute"
                    style={{
                      top: 0,
                      left: 0,
                      right: 0,
                      bottom: 0,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      backgroundColor: 'rgba(255, 255, 255, 0.7)',
                      borderRadius: '50%',
                    }}
                  >
                    <Spinner animation="border" variant="primary" />
                  </div>
                )}
                <div className="mt-2 mb-3">
                  <Form.Group controlId="profilePictureUpload">
                    <Form.Label className="btn btn-outline-primary btn-sm">
                      <i className="bi bi-camera me-1"></i>
                      {t('profile.changePicture', 'Change Picture')}
                      <Form.Control
                        ref={fileInputRef}
                        type="file"
                        accept="image/*"
                        onChange={handleProfilePictureChange}
                        style={{ display: 'none' }}
                      />
                    </Form.Label>
                    {selectedFile && (
                      <div className="mt-1 small text-muted">
                        {selectedFile.name} (
                        {Math.round(selectedFile.size / 1024)} KB)
                      </div>
                    )}
                  </Form.Group>
                </div>
              </div>
            ) : (
              <ProfilePicture
                userId={user.id}
                alt={`${user.firstName} ${user.lastName}`}
                className="rounded-circle mb-3"
                style={{ width: 120, height: 120, objectFit: 'cover' }}
              />
            )}
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
                      disabled={uploadingProfilePicture}
                    >
                      {uploadingProfilePicture ? (
                        <>
                          <Spinner
                            as="span"
                            animation="border"
                            size="sm"
                            role="status"
                            aria-hidden="true"
                            className="me-1"
                          />
                          {t('common.saving', 'Saving...')}
                        </>
                      ) : (
                        t('common.save')
                      )}
                    </Button>
                    <Button
                      variant="secondary"
                      onClick={handleCancel}
                      disabled={uploadingProfilePicture}
                    >
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
                      error={
                        touched.firstName && errors.firstName
                          ? getErrorMessage(errors.firstName)
                          : null
                      }
                    />
                    <FormInput
                      name="lastName"
                      label={t('profile.lastName')}
                      type="text"
                      value={formData.lastName}
                      onChange={handleChange}
                      error={
                        touched.lastName && errors.lastName
                          ? getErrorMessage(errors.lastName)
                          : null
                      }
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
                    error={
                      touched.phoneNumber && errors.phoneNumber
                        ? getErrorMessage(errors.phoneNumber)
                        : null
                    }
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
                    error={
                      touched.address && errors.address
                        ? getErrorMessage(errors.address)
                        : null
                    }
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
                    error={
                      touched.birthdate && errors.birthdate
                        ? getErrorMessage(errors.birthdate)
                        : null
                    }
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
                  <FormDropdownInput
                    name="workplace"
                    label={t('profile.workplace')}
                    value={formData.workplace}
                    onChange={handleChange}
                    options={workplaces.map(w => ({
                      value: w.id,
                      label: w.name,
                    }))}
                    placeholder={t('profile.selectWorkplace')}
                    error={
                      touched.workplace && errors.workplace
                        ? getErrorMessage(errors.workplace)
                        : null
                    }
                    disabled={workplacesLoading}
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
                    error={
                      touched.biography && errors.biography
                        ? getErrorMessage(errors.biography)
                        : null
                    }
                  />
                ) : (
                  user.biography
                )}
              </ListGroup.Item>
              {/* Manager (always display, always editable for development) */}
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
                {touched.managerId && errors.managerId && (
                  <div className="text-danger mt-1">
                    {getErrorMessage(errors.managerId)}
                  </div>
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
