import React, { useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { useChatbotStore } from 'stores/ChatbotStore';
import { MessageInput } from 'components/messages/MessageInput';
import 'styles/components/FloatingChatbot.css';

/**
 * Floating chatbot widget component
 */
export const FloatingChatbot = () => {
  const { t } = useTranslation();
  const messagesEndRef = useRef(null);

  const {
    messages,
    isLoading,
    error,
    isOpen,
    toggleChatbot,
    closeChatbot,
    sendMessage,
    clearError,
  } = useChatbotStore();

  // Auto-scroll to bottom when new messages arrive
  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  // Add initial welcome message if no messages exist
  useEffect(() => {
    if (isOpen && messages.length === 0) {
      const welcomeMessage = {
        id: 0,
        content: t(
          'chatbot.welcome',
          'Olá! Sou o seu assistente virtual. Como posso ajudá-lo hoje?'
        ),
        isBot: true,
        timestamp: new Date().toISOString(),
      };

      useChatbotStore.setState(() => ({
        messages: [welcomeMessage],
      }));
    }
  }, [isOpen, messages.length, t]);

  const handleSendMessage = async content => {
    await sendMessage(content);
  };

  if (!isOpen) {
    return (
      <div className="floating-chatbot-trigger">
        <button
          className="btn btn-primary rounded-circle shadow-lg"
          onClick={toggleChatbot}
          title={t('chatbot.openChat', 'Abrir chat com assistente')}
        >
          <i className="fas fa-robot fa-lg"></i>
        </button>
      </div>
    );
  }

  return (
    <div className="floating-chatbot-container shadow-lg">
      {/* Header */}
      <div className="chatbot-header bg-primary text-white p-3 d-flex justify-content-between align-items-center">
        <div className="d-flex align-items-center">
          <i className="fas fa-robot me-2"></i>
          <span className="fw-medium">
            {t('chatbot.title', 'Assistente Virtual')}
          </span>
        </div>
        <div className="d-flex align-items-center">
          <span className="online-indicator me-2"></span>
          <button
            className="btn btn-sm text-white"
            onClick={closeChatbot}
            title={t('chatbot.close', 'Fechar')}
          >
            <i className="fas fa-times"></i>
          </button>
        </div>
      </div>

      {/* Messages Container */}
      <div className="chatbot-messages">
        {messages.map(message => (
          <div
            key={message.id}
            className={`message-wrapper ${
              message.isBot ? 'bot-message' : 'user-message'
            }`}
          >
            <div
              className={`message-bubble ${
                message.isBot
                  ? message.isError
                    ? 'bg-danger text-white'
                    : 'bg-light'
                  : 'bg-primary text-white'
              }`}
            >
              {message.isBot && (
                <div className="bot-avatar mb-1">
                  <i className="fas fa-robot text-primary"></i>
                </div>
              )}
              <div className="message-content">{message.content}</div>
              <div
                className={`message-time ${
                  message.isBot ? 'text-muted' : 'text-white-50'
                }`}
              >
                {new Date(message.timestamp).toLocaleTimeString([], {
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </div>
            </div>
          </div>
        ))}

        {isLoading && (
          <div className="message-wrapper bot-message">
            <div className="message-bubble bg-light">
              <div className="bot-avatar mb-1">
                <i className="fas fa-robot text-primary"></i>
              </div>
              <div className="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        )}

        {error && (
          <div className="alert alert-danger alert-sm mx-3 mb-2">
            <small>
              <i className="fas fa-exclamation-triangle me-1"></i>
              {error}
            </small>
            <button
              className="btn-close btn-close-sm ms-2"
              onClick={clearError}
            ></button>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <div className="chatbot-input border-top">
        <MessageInput
          onSendMessage={handleSendMessage}
          disabled={isLoading}
          placeholder={t('chatbot.typePlaceholder', 'Digite sua mensagem...')}
        />
      </div>
    </div>
  );
};
