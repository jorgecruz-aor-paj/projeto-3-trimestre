import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import 'styles/components/MessagesContainer.css';

export const MessagesContainer = ({
  messages,
  currentUserId,
  onLoadOlder,
  isLoading,
}) => {
  const { t } = useTranslation();
  const { formatInstant, convertInstantToDate } = useLanguage();
  const messagesEndRef = useRef(null);
  const messagesContainerRef = useRef(null);
  const prevMessagesLengthRef = useRef(0);
  const [isLoadingOlder, setIsLoadingOlder] = useState(false);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({
      behavior: 'smooth',
      block: 'end',
    });
  };

  useEffect(() => {
    // Only scroll to bottom if new messages were added (not on initial load or message updates)
    if (messages && messages.length > prevMessagesLengthRef.current) {
      scrollToBottom();
    }
    prevMessagesLengthRef.current = messages ? messages.length : 0;
  }, [messages]);

  // Handle scroll event to load older messages
  const handleScroll = async e => {
    const { scrollTop, scrollHeight } = e.target;

    // If user scrolled to the top (within 50px) and we're not already loading
    if (scrollTop < 50 && !isLoadingOlder && !isLoading && onLoadOlder) {
      setIsLoadingOlder(true);

      // Store the current scroll height to maintain position after loading
      const prevScrollHeight = scrollHeight;

      try {
        await onLoadOlder();

        // After loading, adjust scroll position to maintain user's view
        setTimeout(() => {
          if (messagesContainerRef.current) {
            const newScrollHeight = messagesContainerRef.current.scrollHeight;
            const scrollDiff = newScrollHeight - prevScrollHeight;
            messagesContainerRef.current.scrollTop = scrollTop + scrollDiff;
          }
        }, 100);
      } catch (error) {
        console.error('Error loading older messages:', error);
      } finally {
        setIsLoadingOlder(false);
      }
    }
  };

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
    <div
      className="messages-container"
      ref={messagesContainerRef}
      onScroll={handleScroll}
    >
      {/* Loading indicator for older messages */}
      {isLoadingOlder && (
        <div className="loading-older-messages text-center p-2 text-muted">
          <i className="fas fa-spinner fa-spin me-2"></i>
          <span>{t('messages.loadingOlder', 'Loading older messages...')}</span>
        </div>
      )}

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
            key={`message-${message.id || index}`}
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
  onLoadOlder: PropTypes.func,
  isLoading: PropTypes.bool,
};
