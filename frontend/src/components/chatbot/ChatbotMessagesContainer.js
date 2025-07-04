import React, { useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { useLanguage } from 'contexts/LanguageContext';
import 'styles/components/ChatbotMessagesContainer.css';

export const ChatbotMessagesContainer = ({ messages, isLoading }) => {
  const { t } = useTranslation();
  const { formatInstant } = useLanguage();
  const messagesEndRef = useRef(null);
  const prevMessagesLengthRef = useRef(0);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({
      behavior: 'smooth',
      block: 'end',
    });
  };

  useEffect(() => {
    // Only scroll to bottom if new messages were added
    if (messages && messages.length > prevMessagesLengthRef.current) {
      scrollToBottom();
    }
    prevMessagesLengthRef.current = messages ? messages.length : 0;
  }, [messages]);

  const formatMessageTime = timestamp => {
    return formatInstant(timestamp, {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  if (!messages || messages.length === 0) {
    return (
      <div className="flex-fill d-flex align-items-center justify-content-center text-muted">
        <div className="text-center">
          <i className="fas fa-robot fa-3x mb-3 text-success"></i>
          <h5>{t('chatbot.welcome', 'Bem-vindo ao Assistente Virtual!')}</h5>
          <p>
            {t(
              'chatbot.startConversation',
              'Digite uma mensagem abaixo para começar a conversa.'
            )}
          </p>
          <div className="mt-3">
            <small className="text-muted">
              {t('chatbot.capabilities', 'Posso ajudar com:')}
            </small>
            <ul className="list-unstyled mt-2 small text-muted">
              <li>• {t('chatbot.help1', 'Informações sobre treinamentos')}</li>
              <li>
                • {t('chatbot.help2', 'Esclarecimentos sobre avaliações')}
              </li>
              <li>• {t('chatbot.help3', 'Orientações gerais do sistema')}</li>
              <li>• {t('chatbot.help4', 'Dúvidas sobre funcionalidades')}</li>
            </ul>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="chatbot-messages-container">
      {messages.map((message, index) => {
        const showTime =
          index === 0 ||
          messages[index - 1]?.isBot !== message.isBot ||
          new Date(message.timestamp) -
            new Date(messages[index - 1]?.timestamp) >
            300000; // 5 minutes

        return (
          <div
            key={`chatbot-message-${message.id || index}`}
            className={`message-wrapper ${
              message.isBot ? 'bot-message' : 'user-message'
            }`}
          >
            {message.isBot && (
              <div className="bot-avatar">
                <div className="avatar-sm">
                  <div className="avatar-initial rounded-circle bg-success">
                    <i className="fas fa-robot text-white"></i>
                  </div>
                </div>
              </div>
            )}

            <div
              className={`message-bubble ${
                message.isBot
                  ? message.isError
                    ? 'bg-danger text-white'
                    : 'bg-light border'
                  : 'bg-primary text-white'
              }`}
            >
              <div className="message-content">{message.content}</div>
              {showTime && (
                <div
                  className={`message-time ${
                    message.isBot ? 'text-muted' : 'text-white-50'
                  }`}
                >
                  {formatMessageTime(message.timestamp)}
                </div>
              )}
            </div>
          </div>
        );
      })}

      {isLoading && (
        <div className="message-wrapper bot-message">
          <div className="bot-avatar">
            <div className="avatar-sm">
              <div className="avatar-initial rounded-circle bg-success">
                <i className="fas fa-robot text-white"></i>
              </div>
            </div>
          </div>
          <div className="message-bubble bg-light border">
            <div className="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      )}

      <div ref={messagesEndRef} />
    </div>
  );
};

ChatbotMessagesContainer.propTypes = {
  messages: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
      content: PropTypes.string.isRequired,
      isBot: PropTypes.bool.isRequired,
      timestamp: PropTypes.string.isRequired,
      isError: PropTypes.bool,
    })
  ),
  isLoading: PropTypes.bool,
};

ChatbotMessagesContainer.defaultProps = {
  messages: [],
  isLoading: false,
};
