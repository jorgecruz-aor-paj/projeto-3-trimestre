import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

export const MessagesContainer = ({ messages, currentUserId }) => {
  const { t } = useTranslation();
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const formatMessageTime = timestamp => {
    return new Date(timestamp).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
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
    <div className="flex-fill overflow-auto p-3 messages-container">
      {messages.map((message, index) => {
        const isOwnMessage = message.senderId === currentUserId;
        const showTime =
          index === 0 ||
          messages[index - 1]?.senderId !== message.senderId ||
          new Date(message.timestampSent) -
            new Date(messages[index - 1]?.timestampSent) >
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

      <style>{`
        .messages-container {
          background: #f8f9fa;
        }

        .message-wrapper {
          margin-bottom: 8px;
          display: flex;
        }

        .message-wrapper.own-message {
          justify-content: flex-end;
        }

        .message-wrapper.other-message {
          justify-content: flex-start;
        }

        .message-bubble {
          max-width: 70%;
          padding: 8px 12px;
          border-radius: 18px;
          word-wrap: break-word;
        }

        .own-message .message-bubble {
          background-color: #C01722 !important;
          border-bottom-right-radius: 4px;
        }

        .other-message .message-bubble {
          background-color: #e9ecef !important;
          border-bottom-left-radius: 4px;
        }

        .message-content {
          font-size: 14px;
          line-height: 1.4;
        }

        .message-time {
          font-size: 11px;
          margin-top: 4px;
          display: flex;
          align-items: center;
          justify-content: flex-end;
        }

        .other-message .message-time {
          justify-content: flex-start;
        }

        .fa-check {
          font-size: 10px;
        }

        .fa-check.text-success {
          color: #28a745 !important;
        }
      `}</style>
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
