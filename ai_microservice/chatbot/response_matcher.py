"""
Response Matcher Service
Handles pattern matching against training data and finding best responses.
"""

from difflib import SequenceMatcher
from training_data import training_conversations


class ResponseMatcher:
    """Service for matching user input against training data patterns"""
    
    def __init__(self, language_service):
        self.language_service = language_service
        self.conversation_patterns = []
        self._prepare_conversation_patterns()

    def _prepare_conversation_patterns(self):
        """Prepare conversation patterns with language detection and keywords"""
        for item in training_conversations:
            detected_lang = self.language_service.detect_language(item["input"])
            keywords = self.language_service.extract_keywords(item["input"], detected_lang)
            self.conversation_patterns.append({
                "input": item["input"].lower(),
                "output": item["output"],
                "keywords": keywords,
                "language": detected_lang
            })

    def find_best_match(self, message: str, user_language: str) -> str | None:
        """Find the best matching response considering language preference"""
        message_lower = message.lower()
        message_keywords = self.language_service.extract_keywords(message, user_language)
        
        best_match = None
        best_score = 0
        
        for pattern in self.conversation_patterns:
            # Prioritize responses in the same language
            language_bonus = 5 if pattern.get("language") == user_language else 0
            
            score = language_bonus
            
            # Exact phrase matching (highest priority)
            if pattern["input"] in message_lower or message_lower in pattern["input"]:
                score += 10
            
            # Keyword matching
            matching_keywords = set(message_keywords) & set(pattern["keywords"])
            if matching_keywords:
                score += len(matching_keywords) * 2
            
            # Sequence similarity
            similarity = SequenceMatcher(None, message_lower, pattern["input"]).ratio()
            score += similarity * 3
            
            print(f"🔍 Pattern: '{pattern['input'][:50]}...' | Lang: {pattern.get('language', 'unknown')} | Score: {score:.2f}")
            
            if score > best_score and score > 6.0:  # Minimum threshold
                best_score = score
                best_match = pattern["output"]
        
        print(f"🎯 Best match score: {best_score:.2f}")
        return best_match
