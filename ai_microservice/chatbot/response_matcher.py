"""
Response Matcher Service
Handles pattern matching against training data and finding best responses.
"""

from difflib import SequenceMatcher
from training_data import (
    training_conversations, 
    POOR_RESPONSE_PATTERNS, 
    QUESTION_WORDS, 
    TOPIC_KEYWORDS,
    COMMON_WORDS,
    SINGLE_WORD_RESPONSES,
    VALIDATION_MESSAGES
)
from config_constants import (
    CONFIDENCE_THRESHOLD,
    BACKUP_THRESHOLD,
    PHRASE_SIMILARITY_HIGH,
    PHRASE_SIMILARITY_MEDIUM,
    KEYWORD_COVERAGE_REQUIRED,
    MIN_MATCHING_KEYWORDS,
    MIN_RESPONSE_WORDS,
    POTENTIAL_MATCH_THRESHOLD,
    MAX_RESPONSE_PREVIEW_LENGTH
)


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
        confidence_threshold = CONFIDENCE_THRESHOLD
        
        for pattern in self.conversation_patterns:
            score = 0
            
            # Exact phrase matching (highest priority) - must be very similar
            phrase_similarity = SequenceMatcher(None, message_lower, pattern["input"]).ratio()
            if phrase_similarity > PHRASE_SIMILARITY_HIGH:
                score += 18
            elif phrase_similarity > PHRASE_SIMILARITY_MEDIUM:
                score += 10
            
            # Strong keyword matching - require significant overlap
            matching_keywords = set(message_keywords) & set(pattern["keywords"])
            keyword_coverage = len(matching_keywords) / max(len(message_keywords), 1)
            if keyword_coverage >= KEYWORD_COVERAGE_REQUIRED:
                score += len(matching_keywords) * 4
            elif len(matching_keywords) >= MIN_MATCHING_KEYWORDS:
                score += len(matching_keywords) * 2
            
            # Language preference bonus (smaller bonus)
            if pattern.get("language") == user_language:
                score += 2
            
            # Semantic context check - ensure the question types align
            if self._check_semantic_context(message_lower, pattern["input"]):
                score += 3
            
            # Only log if score is above a threshold to reduce noise
            if score >= POTENTIAL_MATCH_THRESHOLD:
                print(f"🔍 Potential match: '{pattern['input'][:MAX_RESPONSE_PREVIEW_LENGTH]}...' | Score: {score:.1f}")
            
            if score > best_score and score >= confidence_threshold:
                best_score = score
                best_match = pattern["output"]
        
        print(f"🎯 Best match score: {best_score:.1f} (needed: {confidence_threshold})")
        
        # Only return match if we're confident it's relevant
        if best_score >= confidence_threshold:
            print("✅ High confidence match found")
            return best_match
        else:
            return None

    def find_backup_match(self, message: str, user_language: str) -> str | None:
        """Find a lower-confidence match when LLM is unavailable or fails"""
        message_lower = message.lower()
        message_keywords = self.language_service.extract_keywords(message, user_language)
        
        best_match = None
        best_score = 0
        backup_threshold = BACKUP_THRESHOLD
        
        for pattern in self.conversation_patterns:
            score = 0
            
            # More permissive matching for backup scenarios
            phrase_similarity = SequenceMatcher(None, message_lower, pattern["input"]).ratio()
            if phrase_similarity > 0.5:  # 50% similarity
                score += phrase_similarity * 8
            
            # Keyword matching with lower requirements
            matching_keywords = set(message_keywords) & set(pattern["keywords"])
            if matching_keywords:
                score += len(matching_keywords) * 2
            
            # Language preference
            if pattern.get("language") == user_language:
                score += 2
            
            if score > best_score and score >= backup_threshold:
                best_score = score
                best_match = pattern["output"]
        
        if best_score >= backup_threshold:
            print(f"🔄 Backup match found (score: {best_score:.1f})")
        return best_match if best_score >= backup_threshold else None

    def _check_semantic_context(self, user_message: str, pattern_message: str) -> bool:
        """Check if messages are asking about similar topics"""
        # Extract question types and topics from both messages
        user_context = self._extract_context(user_message, QUESTION_WORDS, TOPIC_KEYWORDS)
        pattern_context = self._extract_context(pattern_message, QUESTION_WORDS, TOPIC_KEYWORDS)
        
        # Check if they share similar question type OR topic
        return bool(user_context['questions'] & pattern_context['questions'] or 
                   user_context['topics'] & pattern_context['topics'])
    
    def _extract_context(self, message: str, question_words: dict, topics: dict) -> dict:
        """Extract question types and topics from a message"""
        context = {'questions': set(), 'topics': set()}
        
        # Find question types
        for eng_word, port_words in question_words.items():
            if eng_word in message or any(pw in message for pw in port_words):
                context['questions'].add(eng_word)
        
        # Find topics
        for topic, keywords in topics.items():
            if any(keyword in message for keyword in keywords):
                context['topics'].add(topic)
        
        return context

    def validate_response_quality(self, response: str, user_message: str) -> bool:
        """Validate if a response is of good quality and relevant"""
        if not response or len(response.strip()) < 10:
            return False
        
        response_lower = response.lower().strip()
        
        # Filter out generic/poor responses
        for pattern in POOR_RESPONSE_PATTERNS:
            if pattern in response_lower and len(response_lower) < 50:
                print(f"{VALIDATION_MESSAGES['generic_response']}: '{response[:30]}...'")
                return False
        
        # Check if response is mostly just the pattern phrase
        if len(response_lower) < 20 and any(pattern in response_lower for pattern in POOR_RESPONSE_PATTERNS):
            print(f"{VALIDATION_MESSAGES['generic_response']}: '{response}'")
            return False
        
        # Response should have some substance
        words = response.split()
        if len(words) < MIN_RESPONSE_WORDS:
            print(f"{VALIDATION_MESSAGES['too_short']}: '{response}'")
            return False
        
        # Check for coherent sentence structure
        if not any(char in response for char in '.!?'):
            print(f"{VALIDATION_MESSAGES['no_punctuation']}: '{response[:30]}...'")
            return False
        
        return True
