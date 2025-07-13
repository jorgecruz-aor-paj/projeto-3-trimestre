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
        """Generate a response to the user's message with enhanced LLM utilization"""
        try:
            # Detect user's language
            user_language = self.language_service.detect_language(message)
            print(f"🤖 Processing: '{message[:30]}...' ({user_language})")
            
            # Try to find CONFIDENT match in training data first
            best_match = self.response_matcher.find_best_match(message, user_language)
            if best_match:
                return best_match
            
            # Use unified response generation for anything without confident match
            try:
                unified_response = self.response_generator.generate_response(message, context, user_language)
                
                # Double-check response quality using response_matcher validation too
                if self.response_matcher.validate_response_quality(unified_response, message):
                    print("✅ Response generated and validated")
                    return unified_response
                else:
                    print("❌ Generated response failed quality validation")
                    # Try a less confident pattern match as backup
                    backup_match = self.response_matcher.find_backup_match(message, user_language)
                    if backup_match:
                        return backup_match
                    else:
                        print("⚠️ Using fallback response")
                        return self.language_service.get_fallback_message(user_language)
                
            except Exception as generation_error:
                print(f"❌ Generation failed: {generation_error}")
                # Try a less confident pattern match as backup
                backup_match = self.response_matcher.find_backup_match(message, user_language)
                if backup_match:
                    return backup_match
                else:
                    print("⚠️ Using fallback response")
                    return self.language_service.get_fallback_message(user_language)
                
        except Exception as e:
            print(f"❌ Error in generate_response: {e}")
            return self.language_service.get_fallback_message('pt')  # Default to Portuguese


# Initialize the chatbot
llm_chatbot = LLMChatbot()
