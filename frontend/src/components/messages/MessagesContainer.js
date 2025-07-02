import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import 'styles/components/MessagesContainer.css';

export const MessagesContainer = ({ messages, currentUserId }) => {
  const { t } = useTranslation();
  const { formatInstant, convertInstantToDate } = useLanguage();
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const formatMessageTime = timestamp => {
    return formatInstant(timestamp, {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false, // Use 24-hour format
    });
  };

  if (!messages || messages.length === 0) {
    return (
      <div className="flex-fill d-flex align-items-center justify-content-center text-muted">
        <div className="text-center">
          <i className="fas fa-comments fa-2x mb-2"></i>
          <p>
            {t(
              'messages.noMessages',
              'No messages yet. Start the conversation!'
            )}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="messages-container">
      {messages.map((message, index) => {
        const isOwnMessage = message.senderId === currentUserId;

        const showTime =
          index === 0 ||
          messages[index - 1]?.senderId !== message.senderId ||
          convertInstantToDate(message.timestampSent) -
            convertInstantToDate(messages[index - 1]?.timestampSent) >
            300000; // 5 minutes

        return (
          <div
            key={message.id}
            className={`message-wrapper ${
              isOwnMessage ? 'own-message' : 'other-message'
            }`}
          >
            <div
              className={`message-bubble ${
                isOwnMessage ? 'bg-primary text-white' : 'bg-light'
              }`}
            >
              <div className="message-content">{message.content}</div>
              {showTime && (
                <div
                  className={`message-time ${
                    isOwnMessage ? 'text-white-50' : 'text-muted'
                  }`}
                >
                  {formatMessageTime(message.timestampSent)}
                  {isOwnMessage && (
                    <i
                      className={`fas fa-check ms-1 ${
                        message.read ? 'text-success' : ''
                      }`}
                    ></i>
                  )}
                </div>
              )}
            </div>
          </div>
        );
      })}
      <div ref={messagesEndRef} />
    </div>
  );
};

MessagesContainer.propTypes = {
  messages: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number,
      content: PropTypes.string,
      senderId: PropTypes.number,
      recipientId: PropTypes.number,
      timestampSent: PropTypes.string,
      read: PropTypes.bool,
    })
  ),
  currentUserId: PropTypes.number,
};
