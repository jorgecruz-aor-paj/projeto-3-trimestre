import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useMessageStore } from 'stores/MessageStore';
import { useChatbotStore } from 'stores/ChatbotStore';
import { useAuthStore } from 'stores/AuthStore';
import { ChatHeader } from 'components/messages/ChatHeader';
import { MessagesContainer } from 'components/messages/MessagesContainer';
import { ChatbotMessagesContainer } from 'components/chatbot/ChatbotMessagesContainer';
import { VirtualAssistant } from 'components/chatbot/VirtualAssistant';
import { MessageInput } from 'components/messages/MessageInput';
import { axiosInstance } from 'api/axiosInstance';
import { API_ENDPOINTS } from 'api/apiConfig';
import { websocketService } from 'services/websocketService';
import { useFormValidation, messageValidationRules } from 'utils/validation';
import { useErrorMessage } from 'hooks/useErrorMessage';
import 'styles/pages/Messages.css';

export const Messages = () => {
  const { t } = useTranslation();
  const { user } = useAuthStore();
  const { getErrorMessage } = useErrorMessage();
  const {
    conversations,
    activeConversation,
    messages,
    isLoading,
    error,
    setActiveConversation,
    loadConversations,
    sendMessage,
    clearStore,
    handleNotification,
    getUnreadCountForConversation,
    refreshUnreadCounts,
    loadOlderMessages,
  } = useMessageStore();

  // Chatbot state
  const {
    messages: chatbotMessages,
    isLoading: chatbotLoading,
    sendMessage: sendChatbotMessage,
  } = useChatbotStore();

  // Form validation for message input
  const {
    formData: messageData,
    errors: messageErrors,
    handleChange: handleMessageChange,
    validateAll: validateMessage,
    resetForm: resetMessageForm,
  } = useFormValidation({ content: '' }, messageValidationRules);

  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [showUserList, setShowUserList] = useState(false);
  const [isVirtualAssistantActive, setIsVirtualAssistantActive] =
    useState(false);
  const [validationError, setValidationError] = useState(null);

  // Virtual assistant conversation object
  const virtualAssistant = {
    id: 'virtual-assistant',
    firstName: t('chatbot.assistant', 'Assistente'),
    lastName: t('chatbot.virtual', 'Virtual'),
    email: 'ai@sistema.com',
    isVirtualAssistant: true,
  };

  useEffect(() => {
    // Subscribe to notifications (WebSocket is already connected via Header)
    const unsubscribe = websocketService.onNotification(handleNotification);

    // Load conversations when component mounts
    loadConversations();

    // Cleanup when component unmounts
    return () => {
      unsubscribe();
      clearStore();
    };
  }, [loadConversations, clearStore, handleNotification]);

  // Load all users for starting new conversations
  const loadUsers = async () => {
    if (users.length > 0) return; // Already loaded

    setLoadingUsers(true);
    try {
      const response = await axiosInstance.get(
        API_ENDPOINTS.user.getMiniUsers.url
      );
      // Filter out current user
      const filteredUsers = response.filter(u => u.id !== user?.id);
      setUsers(filteredUsers);
    } catch (err) {
      console.error('Error loading users:', err);
    } finally {
      setLoadingUsers(false);
    }
  };

  const handleStartConversation = selectedUser => {
    setActiveConversation(selectedUser);
    setIsVirtualAssistantActive(false);
    setShowUserList(false);
    setValidationError(null);
    resetMessageForm();
  };

  const handleSelectVirtualAssistant = () => {
    setActiveConversation(null);
    setIsVirtualAssistantActive(true);
    setShowUserList(false);
    setValidationError(null);
    resetMessageForm();
  };

  const handleSendMessage = async content => {
    // Update the message in the form
    handleMessageChange('content', content);

    // Validate the message
    if (!content || content.trim() === '') {
      setValidationError(t('validation.required', 'This field is required'));
      return;
    }

    if (content.length < messageValidationRules.content.minLength) {
      setValidationError(
        t('validation.minLength', {
          min: messageValidationRules.content.minLength,
          defaultValue: `Message must be at least ${messageValidationRules.content.minLength} characters`,
        })
      );
      return;
    }

    if (content.length > messageValidationRules.content.maxLength) {
      setValidationError(
        t('validation.maxLength', {
          max: messageValidationRules.content.maxLength,
          defaultValue: `Message cannot exceed ${messageValidationRules.content.maxLength} characters`,
        })
      );
      return;
    }

    // Clear validation error if message is valid
    setValidationError(null);

    if (isVirtualAssistantActive) {
      // Send to chatbot
      await sendChatbotMessage(content);
    } else if (activeConversation && content.trim()) {
      // Send to user
      try {
        await sendMessage(activeConversation.id, content);
      } catch (err) {
        console.error('Failed to send message:', err);
      }
    }

    // Reset form after sending
    resetMessageForm();
  };

  const getCurrentMessages = () => {
    return isVirtualAssistantActive ? chatbotMessages : messages;
  };

  const getCurrentLoading = () => {
    return isVirtualAssistantActive ? chatbotLoading : isLoading;
  };

  const handleLoadOlderMessages = async () => {
    // Only load older messages for regular conversations (not chatbot)
    if (!isVirtualAssistantActive) {
      await loadOlderMessages();
    }
  };

  return (
    <div className="container-fluid py-4">
      <div className="row">
        <div className="col-12">
          <div className="card">
            <div className="card-header d-flex justify-content-between align-items-center">
              <h4 className="mb-0">
                <i className="fas fa-comments me-2 text-primary"></i>
                {t('messages.title', 'Messages')}
              </h4>
              <button
                className="btn btn-primary btn-sm"
                onClick={() => {
                  setShowUserList(!showUserList);
                  if (!showUserList) loadUsers();
                }}
                disabled={loadingUsers}
              >
                <i className="fas fa-plus me-1"></i>
                {t('messages.newMessage', 'New Message')}
              </button>
            </div>

            <div className="card-body p-0">
              <div className="row g-0 messages-layout">
                {/* Conversations Sidebar */}
                <div className="col-md-4 border-end conversations-sidebar">
                  <div className="h-100 d-flex flex-column">
                    {/* User selection dropdown */}
                    {showUserList && (
                      <div className="border-bottom p-3">
                        <h6 className="mb-2">
                          {t('messages.selectUser', 'Select User')}
                        </h6>
                        {loadingUsers ? (
                          <div className="text-center py-2">
                            <div
                              className="spinner-border spinner-border-sm"
                              role="status"
                            >
                              <span className="visually-hidden">
                                Loading...
                              </span>
                            </div>
                          </div>
                        ) : (
                          <div className="user-list">
                            {users.map(userItem => (
                              <div
                                key={userItem.id}
                                className="d-flex align-items-center p-2 cursor-pointer user-item"
                                onClick={() =>
                                  handleStartConversation(userItem)
                                }
                              >
                                <div className="avatar-sm me-2">
                                  <div className="avatar-initial rounded-circle bg-primary">
                                    {userItem.firstName?.charAt(0)}
                                    {userItem.lastName?.charAt(0)}
                                  </div>
                                </div>
                                <div>
                                  <div className="fw-medium">
                                    {userItem.firstName} {userItem.lastName}
                                  </div>
                                  <small className="text-muted">
                                    {userItem.email}
                                  </small>
                                </div>
                              </div>
                            ))}
                          </div>
                        )}
                      </div>
                    )}

                    {/* Virtual Assistant */}
                    <VirtualAssistant
                      onClick={handleSelectVirtualAssistant}
                      isActive={isVirtualAssistantActive}
                    />

                    {/* Conversations List */}
                    <div className="flex-fill overflow-auto">
                      {isLoading ? (
                        <div className="text-center py-4">
                          <div className="spinner-border" role="status">
                            <span className="visually-hidden">Loading...</span>
                          </div>
                        </div>
                      ) : conversations.length === 0 ? (
                        <div className="text-center py-4 text-muted">
                          <i className="fas fa-comments fa-2x mb-2"></i>
                          <p>
                            {t(
                              'messages.noConversations',
                              'No conversations yet'
                            )}
                          </p>
                        </div>
                      ) : (
                        conversations.map(conversation => {
                          const unreadCount = getUnreadCountForConversation(
                            conversation.id
                          );

                          return (
                            <div
                              key={conversation.id}
                              className={`conversation-item p-3 border-bottom cursor-pointer ${
                                activeConversation?.id === conversation.id &&
                                !isVirtualAssistantActive
                                  ? 'bg-light'
                                  : ''
                              }`}
                              onClick={() =>
                                handleStartConversation(conversation)
                              }
                            >
                              <div className="d-flex align-items-center">
                                <div className="avatar-sm me-3 position-relative">
                                  <div className="avatar-initial rounded-circle bg-primary">
                                    {conversation.firstName?.charAt(0)}
                                    {conversation.lastName?.charAt(0)}
                                  </div>
                                  {unreadCount > 0 && (
                                    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                      {unreadCount > 99 ? '99+' : unreadCount}
                                      <span className="visually-hidden">
                                        unread messages
                                      </span>
                                    </span>
                                  )}
                                </div>
                                <div className="flex-fill">
                                  <div className="d-flex justify-content-between align-items-center">
                                    <div
                                      className={`fw-medium ${
                                        unreadCount > 0 ? 'fw-bold' : ''
                                      }`}
                                    >
                                      {conversation.firstName}{' '}
                                      {conversation.lastName}
                                    </div>
                                    {unreadCount > 0 && (
                                      <small className="text-primary">
                                        <i
                                          className="fas fa-circle"
                                          style={{ fontSize: '8px' }}
                                        ></i>
                                      </small>
                                    )}
                                  </div>
                                  <small className="text-muted">
                                    {conversation.email}
                                  </small>
                                </div>
                              </div>
                            </div>
                          );
                        })
                      )}
                    </div>
                  </div>
                </div>

                {/* Chat Area */}
                <div className="col-md-8 chat-area">
                  {isVirtualAssistantActive ? (
                    <div className="h-100 d-flex flex-column">
                      <ChatHeader user={virtualAssistant} />
                      <ChatbotMessagesContainer
                        messages={getCurrentMessages()}
                        isLoading={getCurrentLoading()}
                      />
                      <div>
                        {validationError && (
                          <div className="text-danger px-3 pt-2 small">
                            <i className="fas fa-exclamation-circle me-1"></i>
                            {validationError}
                          </div>
                        )}
                        <MessageInput
                          onSendMessage={handleSendMessage}
                          disabled={getCurrentLoading()}
                          placeholder={t(
                            'chatbot.typePlaceholder',
                            'Digite sua pergunta...'
                          )}
                        />
                      </div>
                    </div>
                  ) : activeConversation ? (
                    <div className="h-100 d-flex flex-column">
                      <ChatHeader user={activeConversation} />
                      <MessagesContainer
                        messages={getCurrentMessages()}
                        currentUserId={user?.id}
                        onLoadOlder={handleLoadOlderMessages}
                        isLoading={getCurrentLoading()}
                      />
                      <div>
                        {validationError && (
                          <div className="text-danger px-3 pt-2 small">
                            <i className="fas fa-exclamation-circle me-1"></i>
                            {validationError}
                          </div>
                        )}
                        <MessageInput
                          onSendMessage={handleSendMessage}
                          disabled={getCurrentLoading()}
                        />
                      </div>
                    </div>
                  ) : (
                    <div className="h-100 d-flex align-items-center justify-content-center text-muted">
                      <div className="text-center">
                        <i className="fas fa-comment-dots fa-3x mb-3"></i>
                        <p className="fs-5 mb-3">
                          {t(
                            'messages.selectConversation',
                            'Select a conversation to start messaging'
                          )}
                        </p>
                        <div className="d-flex justify-content-center gap-3">
                          <button
                            className="btn btn-outline-primary"
                            onClick={handleSelectVirtualAssistant}
                          >
                            <i className="fas fa-robot me-2"></i>
                            {t(
                              'chatbot.talkToAssistant',
                              'Talk to AI Assistant'
                            )}
                          </button>
                          <button
                            className="btn btn-primary"
                            onClick={() => {
                              setShowUserList(true);
                              loadUsers();
                            }}
                          >
                            <i className="fas fa-user me-2"></i>
                            {t(
                              'messages.startConversation',
                              'Start Conversation'
                            )}
                          </button>
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>

          {/* Error Display */}
          {error && (
            <div className="alert alert-danger mt-3" role="alert">
              <i className="fas fa-exclamation-triangle me-2"></i>
              {error}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
