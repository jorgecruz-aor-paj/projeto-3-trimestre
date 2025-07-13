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
        """Generate a response prioritizing training data, minimal LLM usage"""
        try:
            # Detect user's language
            user_language = self.language_service.detect_language(message)
            print(f"🤖 Processing: '{message[:30]}...' ({user_language})")
            
            # STEP 1: Try to find CONFIDENT match in training data first
            best_match = self.response_matcher.find_best_match(message, user_language)
            if best_match:
                print("✅ High confidence training data match found")
                return best_match
            
            # STEP 2: Try backup match with lower confidence from training data
            backup_match = self.response_matcher.find_backup_match(message, user_language)
            if backup_match:
                print("✅ Backup training data match found")
                return backup_match
            
            # STEP 3: Use simple fallback (NO LLM unless specifically enabled)
            print("⚠️ No training data match found, using safe fallback")
            return self.language_service.get_fallback_message(user_language)
                
        except Exception as e:
            print(f"❌ Error in generate_response: {e}")
            return self.language_service.get_fallback_message('pt')  # Default to Portuguese


# Initialize the chatbot
llm_chatbot = LLMChatbot()
