import React, { useState, useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useUserList } from 'hooks/useUserList';
import { useTranslation } from 'react-i18next';

export const SearchableUserSelect = ({
  value,
  onChange,
  excludeUserId,
  label,
  name,
  placeholder,
}) => {
  const { t } = useTranslation();
  const { userList, loading } = useUserList();
  const [searchTerm, setSearchTerm] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const dropdownRef = useRef(null);
  const inputRef = useRef(null);

  // Filter users based on excludeUserId
  const filteredUserList =
    userList && excludeUserId
      ? userList.filter(u => String(u.id) !== String(excludeUserId))
      : userList;

  // Filter users based on search term
  const filteredUsers =
    filteredUserList?.filter(user =>
      `${user.firstName} ${user.lastName}`
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    ) || [];

  // Find selected user when value changes
  useEffect(() => {
    if (value && filteredUserList) {
      const user = filteredUserList.find(u => String(u.id) === String(value));
      setSelectedUser(user);
      setSearchTerm(user ? `${user.firstName} ${user.lastName}` : '');
    } else {
      setSelectedUser(null);
      setSearchTerm('');
    }
  }, [value, filteredUserList]);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = event => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
        // Reset search term if no selection was made
        if (!selectedUser) {
          setSearchTerm('');
        }
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [selectedUser]);

  const handleInputChange = e => {
    const term = e.target.value;
    setSearchTerm(term);
    setIsOpen(true);

    // If search term is empty, clear the selection
    if (!term) {
      setSelectedUser(null);
      onChange({
        target: {
          name,
          value: '',
        },
      });
    }
  };

  const handleUserSelect = user => {
    setSelectedUser(user);
    setSearchTerm(`${user.firstName} ${user.lastName}`);
    setIsOpen(false);

    onChange({
      target: {
        name,
        value: user.id,
      },
    });
  };

  const handleClearSelection = () => {
    setSelectedUser(null);
    setSearchTerm('');
    setIsOpen(false);

    onChange({
      target: {
        name,
        value: '',
      },
    });
  };

  const handleInputFocus = () => {
    setIsOpen(true);
  };

  const handleKeyDown = e => {
    if (e.key === 'Escape') {
      setIsOpen(false);
      inputRef.current?.blur();
    }
  };

  return (
    <div style={{ position: 'relative' }} ref={dropdownRef}>
      <label htmlFor={name} className="form-label small fw-semibold">
        {label || t('profile.evaluator')}
      </label>
      <div style={{ position: 'relative' }}>
        <input
          ref={inputRef}
          id={name}
          name={name}
          type="text"
          value={searchTerm}
          onChange={handleInputChange}
          onFocus={handleInputFocus}
          onKeyDown={handleKeyDown}
          className="form-control"
          placeholder={placeholder || t('common.search')}
          disabled={loading}
          autoComplete="off"
        />

        {/* Clear button */}
        {selectedUser && (
          <button
            type="button"
            className="btn btn-sm position-absolute"
            style={{
              right: '24px',
              top: '50%',
              transform: 'translateY(-50%)',
              padding: '2px 4px',
              fontSize: '10px',
              border: 'none',
              background: 'transparent',
            }}
            onClick={handleClearSelection}
            title={t('common.clear')}
          >
            <i className="fas fa-times text-muted" />
          </button>
        )}

        {/* Dropdown arrow */}
        <button
          type="button"
          className="btn btn-sm position-absolute"
          style={{
            right: '4px',
            top: '50%',
            transform: 'translateY(-50%)',
            padding: '2px 4px',
            fontSize: '10px',
            border: 'none',
            background: 'transparent',
          }}
          onClick={() => setIsOpen(!isOpen)}
          disabled={loading}
        >
          <i
            className={`fas fa-chevron-${isOpen ? 'up' : 'down'} text-muted`}
          />
        </button>

        {/* Dropdown menu */}
        {isOpen && (
          <div
            className="dropdown-menu show w-100"
            style={{
              position: 'absolute',
              top: '100%',
              left: 0,
              zIndex: 1000,
              maxHeight: '200px',
              overflowY: 'auto',
            }}
          >
            {/* "All" option */}
            <button
              type="button"
              className={`dropdown-item ${!selectedUser ? 'active' : ''}`}
              onClick={handleClearSelection}
            >
              {t('common.all')}
            </button>

            {loading ? (
              <div className="dropdown-item text-muted">
                <i className="fas fa-spinner fa-spin me-2" />
                {t('common.loading')}
              </div>
            ) : filteredUsers.length > 0 ? (
              filteredUsers.map(user => (
                <button
                  key={user.id}
                  type="button"
                  className={`dropdown-item ${
                    selectedUser?.id === user.id ? 'active' : ''
                  }`}
                  onClick={() => handleUserSelect(user)}
                >
                  {user.firstName} {user.lastName}
                </button>
              ))
            ) : searchTerm ? (
              <div className="dropdown-item text-muted">
                {t('common.noResults', 'No results found')}
              </div>
            ) : (
              <div className="dropdown-item text-muted">
                {t('common.noData', 'No users available')}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

SearchableUserSelect.propTypes = {
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  onChange: PropTypes.func.isRequired,
  excludeUserId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  label: PropTypes.string,
  name: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
};
