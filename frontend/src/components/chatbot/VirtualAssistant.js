import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import 'styles/components/VirtualAssistant.css';

/**
 * Virtual Assistant conversation item for the Messages page
 */
export const VirtualAssistant = ({ onClick, isActive }) => {
  const { t } = useTranslation();

  return (
    <div
      className={`conversation-item p-3 border-bottom cursor-pointer virtual-assistant ${
        isActive ? 'bg-light' : ''
      }`}
      onClick={onClick}
    >
      <div className="d-flex align-items-center">
        <div className="avatar-sm me-3">
          <div className="avatar-initial rounded-circle bg-success">
            <i className="fas fa-robot text-white"></i>
          </div>
        </div>
        <div className="flex-fill">
          <div className="fw-medium d-flex align-items-center">
            {t('chatbot.assistant', 'Assistente Virtual')}
            <span className="badge bg-success ms-2 small">AI</span>
          </div>
          <small className="text-muted">
            {t(
              'chatbot.description',
              'Assistente inteligente para suas dúvidas'
            )}
          </small>
        </div>
        <div className="text-end">
          <div className="online-indicator-small bg-success rounded-circle"></div>
        </div>
      </div>
    </div>
  );
};

VirtualAssistant.propTypes = {
  onClick: PropTypes.func.isRequired,
  isActive: PropTypes.bool,
};

VirtualAssistant.defaultProps = {
  isActive: false,
};
