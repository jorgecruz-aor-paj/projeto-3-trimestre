"""
Language Detection and Processing Service
Handles language detection, keyword extraction, and language-specific utilities.
"""

import re
from langdetect import detect, LangDetectException
from training_data import stop_words, fallback_messages, LANGUAGE_INDICATORS


class LanguageService:
    """Service for language detection and text processing"""
    
    def __init__(self):
        # Import language data from centralized training_data file
        self.stop_words = stop_words
        self.fallback_messages = fallback_messages

    def detect_language(self, text: str) -> str:
        """Detect the language of input text"""
        try:
            detected = detect(text)
            # Only log for debugging when needed - remove or comment out in production
            # print(f"🌐 Detected language: {detected} for text: '{text[:50]}...'")
            
            # Map common language codes
            if detected in ['pt', 'pt-br', 'pt-pt']:
                return 'pt'
            elif detected == 'en':
                return 'en'
            else:
                return 'pt'  # Default to Portuguese for your Portuguese system
        except LangDetectException:
            # Fallback: simple keyword detection
            text_lower = text.lower()
            
            portuguese_count = sum(1 for word in LANGUAGE_INDICATORS['pt'] if word in text_lower)
            english_count = sum(1 for word in LANGUAGE_INDICATORS['en'] if word in text_lower)
            
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
