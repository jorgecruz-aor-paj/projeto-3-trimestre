import React, { useState, useRef } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import 'styles/components/MessageInput.css';

export const MessageInput = ({
  onSendMessage,
  disabled = false,
  placeholder,
}) => {
  const { t } = useTranslation();
  const [message, setMessage] = useState('');
  const inputRef = useRef(null);

  const handleSubmit = e => {
    e.preventDefault();
    if (message.trim() && !disabled) {
      onSendMessage(message.trim());
      setMessage('');
      inputRef.current?.focus();
    }
  };

  const handleKeyDown = e => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  return (
    <div className="message-input-container border-top p-3 bg-white">
      <form onSubmit={handleSubmit}>
        <div className="input-group">
          <input
            ref={inputRef}
            type="text"
            className="form-control"
            placeholder={
              placeholder || t('messages.typeMessage', 'Type a message...')
            }
            value={message}
            onChange={e => setMessage(e.target.value)}
            onKeyDown={handleKeyDown}
            disabled={disabled}
          />
          <button
            type="submit"
            className="btn btn-primary"
            disabled={!message.trim() || disabled}
          >
            {disabled ? (
              <div className="spinner-border spinner-border-sm" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            ) : (
              <i className="fas fa-paper-plane"></i>
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

MessageInput.propTypes = {
  onSendMessage: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  placeholder: PropTypes.string,
};
