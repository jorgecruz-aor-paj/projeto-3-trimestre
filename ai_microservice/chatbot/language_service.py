"""
Language Detection and Processing Service
Handles language detection, keyword extraction, and language-specific utilities.
"""

import re
from langdetect import detect, LangDetectException


class LanguageService:
    """Service for language detection and text processing"""
    
    def __init__(self):
        # Language-specific stop words
        self.stop_words = {
            'en': {'how', 'can', 'i', 'do', 'the', 'to', 'a', 'an', 'and', 'or', 'but', 'in', 'on', 'at', 'for', 'with', 'by', 'what', 'where', 'when', 'why', 'who', 'is', 'are', 'was', 'were', 'be', 'been', 'have', 'has', 'had', 'will', 'would', 'could', 'should', 'may', 'might', 'must'},
            'pt': {'como', 'posso', 'eu', 'fazer', 'o', 'a', 'para', 'um', 'uma', 'e', 'ou', 'mas', 'em', 'no', 'na', 'por', 'com', 'de', 'que', 'onde', 'quando', 'porque', 'quem', 'é', 'são', 'foi', 'foram', 'ser', 'sido', 'ter', 'tem', 'teve', 'vai', 'iria', 'poderia', 'deveria', 'pode', 'deve'}
        }
        
        # Language-specific fallback messages
        self.fallback_messages = {
            'pt': "Estou aqui para ajudá-lo com o Sistema de Gestão de Formação e Desempenho. Pode perguntar-me sobre avaliações, formações, mensagens ou uso geral do sistema.",
            'en': "I'm here to help you with the Training and Performance Management System. You can ask me about evaluations, trainings, messages, or general system usage."
        }

    def detect_language(self, text: str) -> str:
        """Detect the language of input text"""
        try:
            detected = detect(text)
            print(f"🌐 Detected language: {detected} for text: '{text[:50]}...'")
            
            # Map common language codes
            if detected in ['pt', 'pt-br', 'pt-pt']:
                return 'pt'
            elif detected == 'en':
                return 'en'
            else:
                return 'pt'  # Default to Portuguese for your Portuguese system
        except LangDetectException:
            # Fallback: simple keyword detection
            portuguese_indicators = ['como', 'posso', 'onde', 'quando', 'porque', 'que', 'não', 'sim', 'obrigado', 'por favor', 'avaliação', 'formação', 'treinamento']
            english_indicators = ['how', 'can', 'where', 'when', 'why', 'what', 'yes', 'no', 'thank', 'please', 'evaluation', 'training']
            
            text_lower = text.lower()
            
            portuguese_count = sum(1 for word in portuguese_indicators if word in text_lower)
            english_count = sum(1 for word in english_indicators if word in text_lower)
            
            if portuguese_count > english_count:
                return 'pt'
            else:
                return 'en' if english_count > 0 else 'pt'  # Default to Portuguese

    def extract_keywords(self, text: str, language: str = 'pt') -> list:
        """Extract meaningful keywords from text based on language"""
        stop_words = self.stop_words.get(language, self.stop_words['pt'])
        
        # Extract words and filter out stop words
        words = re.findall(r'\b\w+\b', text.lower())
        keywords = [word for word in words if word not in stop_words and len(word) > 2]
        
        return keywords

    def get_fallback_message(self, language: str = 'pt') -> str:
        """Get generic fallback response in the specified language"""
        return self.fallback_messages.get(language, self.fallback_messages['pt'])
