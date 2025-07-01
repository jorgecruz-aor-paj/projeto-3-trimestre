import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useMessageStore } from 'stores/MessageStore';
import { useAuthStore } from 'stores/AuthStore';
import { ChatHeader } from 'components/messages/ChatHeader';
import { MessagesContainer } from 'components/messages/MessagesContainer';
import { MessageInput } from 'components/messages/MessageInput';
import { axiosInstance } from 'api/axiosInstance';
import { API_ENDPOINTS } from 'api/apiConfig';

export const Messages = () => {
  const { t } = useTranslation();
  const { user } = useAuthStore();
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
    initializeWebSocket,
  } = useMessageStore();

  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [showUserList, setShowUserList] = useState(false);

  useEffect(() => {
    // Initialize WebSocket connection
    initializeWebSocket();

    // Load conversations when component mounts
    loadConversations();

    // Cleanup when component unmounts
    return () => {
      clearStore();
    };
  }, [loadConversations, clearStore, initializeWebSocket]);

  // Load all users for starting new conversations
  const loadUsers = async () => {
    if (users.length > 0) return; // Already loaded

    setLoadingUsers(true);
    try {
      const response = await axiosInstance.get(
        API_ENDPOINTS.users.getMiniUsers.url
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
    setShowUserList(false);
  };

  const handleSendMessage = async content => {
    if (activeConversation && content.trim()) {
      try {
        await sendMessage(activeConversation.id, content);
      } catch (err) {
        console.error('Failed to send message:', err);
      }
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
              <div className="row g-0" style={{ height: '600px' }}>
                {/* Conversations Sidebar */}
                <div className="col-md-4 border-end">
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
                          <div
                            className="user-list"
                            style={{ maxHeight: '200px', overflowY: 'auto' }}
                          >
                            {users.map(userItem => (
                              <div
                                key={userItem.id}
                                className="d-flex align-items-center p-2 cursor-pointer user-item"
                                style={{ cursor: 'pointer' }}
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
                        conversations.map(conversation => (
                          <div
                            key={conversation.id}
                            className={`conversation-item p-3 border-bottom cursor-pointer ${
                              activeConversation?.id === conversation.id
                                ? 'bg-light'
                                : ''
                            }`}
                            style={{ cursor: 'pointer' }}
                            onClick={() => setActiveConversation(conversation)}
                          >
                            <div className="d-flex align-items-center">
                              <div className="avatar-sm me-3">
                                <div className="avatar-initial rounded-circle bg-primary">
                                  {conversation.firstName?.charAt(0)}
                                  {conversation.lastName?.charAt(0)}
                                </div>
                              </div>
                              <div className="flex-fill">
                                <div className="fw-medium">
                                  {conversation.firstName}{' '}
                                  {conversation.lastName}
                                </div>
                                <small className="text-muted">
                                  {conversation.email}
                                </small>
                              </div>
                            </div>
                          </div>
                        ))
                      )}
                    </div>
                  </div>
                </div>

                {/* Chat Area */}
                <div className="col-md-8">
                  {activeConversation ? (
                    <div className="h-100 d-flex flex-column">
                      <ChatHeader user={activeConversation} />
                      <MessagesContainer
                        messages={messages}
                        currentUserId={user?.id}
                      />
                      <MessageInput
                        onSendMessage={handleSendMessage}
                        disabled={isLoading}
                      />
                    </div>
                  ) : (
                    <div className="h-100 d-flex align-items-center justify-content-center text-muted">
                      <div className="text-center">
                        <i className="fas fa-comment-dots fa-3x mb-3"></i>
                        <p className="fs-5">
                          {t(
                            'messages.selectConversation',
                            'Select a conversation to start messaging'
                          )}
                        </p>
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

      <style>{`
        .user-item:hover {
          background-color: #f8f9fa !important;
        }
        
        .conversation-item:hover {
          background-color: #f8f9fa !important;
        }
        
        .avatar-sm {
          width: 40px;
          height: 40px;
        }
        
        .avatar-initial {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 600;
          color: white;
          font-size: 14px;
        }
      `}</style>
    </div>
  );
};
