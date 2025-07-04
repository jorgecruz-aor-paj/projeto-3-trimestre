import { axiosInstance } from 'api/axiosInstance';
import { API_ENDPOINTS } from 'api/apiConfig';

/**
 * Service for chatbot interactions
 */
export const chatbotService = {
  /**
   * Send a message to the chatbot and get a response
   * @param {string} message - The user's message
   * @returns {Promise<string>} The chatbot's reply
   */
  async sendMessage(message) {
    try {
      const response = await axiosInstance.post(
        API_ENDPOINTS.ai.chatbot.url,
        { message },
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );
      return (
        response.reply || 'Desculpe, não consegui processar a sua mensagem.'
      );
    } catch (error) {
      console.error('Error sending message to chatbot:', error);
      throw new Error(
        'Erro ao comunicar com o assistente virtual. Tente novamente.'
      );
    }
  },
};
