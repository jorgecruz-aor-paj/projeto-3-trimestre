"""
LLM Chatbot Main Interface
Orchestrates all chatbot components to provide intelligent responses.
"""

from .language_service import LanguageService
from .model_manager import ModelManager
from .response_matcher import ResponseMatcher
from .response_generator import ResponseGenerator


class LLMChatbot:
    """Main chatbot interface that orchestrates all components"""
    
    def __init__(self):
        # Initialize all services
        self.language_service = LanguageService()
        self.model_manager = ModelManager()
        self.response_matcher = ResponseMatcher(self.language_service)
        self.response_generator = ResponseGenerator(self.model_manager)

    def generate_response(self, message: str, context: str = "") -> str:
        """Generate a response to the user's message"""
        try:
            # Detect user's language
            user_language = self.language_service.detect_language(message)
            print(f"🤖 Processing message: '{message}' in detected language: {user_language}")
            
            # Try to find best match in training data first
            best_match = self.response_matcher.find_best_match(message, user_language)
            if best_match:
                print("✅ Found training data match")
                return best_match
            
            # Fallback to LLM generation if model is loaded
            if self.model_manager.is_model_loaded():
                print("🔄 Trying LLM generation...")
                return self.response_generator.generate_llm_response(message, context, user_language)
            else:
                print("⚠️ LLM not available, using generic fallback")
                return self.language_service.get_fallback_message(user_language)
                
        except Exception as e:
            print(f"❌ Error in generate_response: {e}")
            return self.language_service.get_fallback_message('pt')  # Default to Portuguese


# Initialize the chatbot
llm_chatbot = LLMChatbot()
