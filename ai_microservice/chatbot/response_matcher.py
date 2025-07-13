"""
Response Matcher Service
Handles pattern matching against training data and finding best responses.
"""

import re
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
        """Enhanced matching with better fuzzy logic and multiple scoring factors"""
        message_lower = message.lower()
        message_keywords = self.language_service.extract_keywords(message, user_language)
        
        # Create message variations for better matching
        message_variations = self._create_input_variations(message_lower)
        
        best_match = None
        best_score = 0
        confidence_threshold = CONFIDENCE_THRESHOLD
        
        for pattern in self.conversation_patterns:
            score = 0
            
            # 1. Enhanced phrase similarity - check against all variations
            max_similarity = 0
            for msg_var in message_variations:
                similarity = SequenceMatcher(None, msg_var, pattern["input"]).ratio()
                max_similarity = max(max_similarity, similarity)
            
            if max_similarity > PHRASE_SIMILARITY_HIGH:
                score += 20  # Higher reward for very similar phrases
            elif max_similarity > PHRASE_SIMILARITY_MEDIUM:
                score += 12
            elif max_similarity > 0.4:  # Even moderate similarity gets some points
                score += 6
            
            # 2. Enhanced keyword matching with context awareness
            matching_keywords = set(message_keywords) & set(pattern["keywords"])
            if matching_keywords:
                keyword_coverage = len(matching_keywords) / max(len(message_keywords), 1)
                if keyword_coverage >= KEYWORD_COVERAGE_REQUIRED:
                    score += len(matching_keywords) * 5  # Higher reward
                elif len(matching_keywords) >= MIN_MATCHING_KEYWORDS:
                    score += len(matching_keywords) * 3
                else:
                    score += len(matching_keywords) * 1
            
            # 3. Semantic context check - stronger weight
            if self._check_semantic_context(message_lower, pattern["input"]):
                score += 4
            
            # 4. Language preference - small bonus
            if pattern.get("language") == user_language:
                score += 2
            
            # 5. Question type alignment bonus
            if self._check_question_type_alignment(message_lower, pattern["input"]):
                score += 3
            
            # 6. Length similarity bonus (similar question lengths often indicate similar questions)
            length_ratio = min(len(message_lower), len(pattern["input"])) / max(len(message_lower), len(pattern["input"]))
            if length_ratio > 0.6:
                score += 1
            
            # Log potential matches for debugging
            if score >= POTENTIAL_MATCH_THRESHOLD:
                print(f"🔍 Match candidate: '{pattern['input'][:50]}...' | Score: {score:.1f}")
            
            if score > best_score:
                best_score = score
                best_match = pattern["output"]
        
        print(f"🎯 Best match score: {best_score:.1f} (needed: {confidence_threshold})")
        
        # Only return match if we're confident
        if best_score >= confidence_threshold:
            print("✅ High confidence match found")
            return best_match
        else:
            print("❌ No confident match found")
            return None

    def find_backup_match(self, message: str, user_language: str) -> str | None:
        """Find a reasonable backup match with relaxed criteria"""
        message_lower = message.lower()
        message_keywords = self.language_service.extract_keywords(message, user_language)
        
        best_match = None
        best_score = 0
        backup_threshold = BACKUP_THRESHOLD
        
        for pattern in self.conversation_patterns:
            score = 0
            
            # More permissive phrase matching
            similarity = SequenceMatcher(None, message_lower, pattern["input"]).ratio()
            if similarity > 0.4:
                score += similarity * 10
            
            # Keyword overlap
            matching_keywords = set(message_keywords) & set(pattern["keywords"])
            if matching_keywords:
                score += len(matching_keywords) * 2
            
            # Context and language bonuses
            if self._check_semantic_context(message_lower, pattern["input"]):
                score += 2
            if pattern.get("language") == user_language:
                score += 1
            
            if score > best_score:
                best_score = score
                best_match = pattern["output"]
        
        if best_score >= backup_threshold:
            print(f"🔄 Backup match found (score: {best_score:.1f})")
            return best_match
        else:
            print("❌ No backup match found")
            return None

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
    
    def _create_input_variations(self, input_text: str) -> list:
        """Create variations of input text for better fuzzy matching"""
        import re
        variations = [input_text.lower()]
        
        # Remove common question words that might vary
        cleaned = re.sub(r'\b(como|how|posso|can|i|eu|o que|what|que|por favor|please)\b', '', input_text.lower())
        cleaned = re.sub(r'\s+', ' ', cleaned).strip()
        if cleaned and cleaned != input_text.lower():
            variations.append(cleaned)
        
        # Create stem-like variations (simple)
        stemmed = re.sub(r'(ão|ões|ing|ed|er|ar|ir)$', '', cleaned)
        if stemmed != cleaned:
            variations.append(stemmed)
        
        return variations

    def _check_question_type_alignment(self, user_message: str, pattern_message: str) -> bool:
        """Check if both messages are asking similar types of questions"""
        user_type = self._get_question_type(user_message)
        pattern_type = self._get_question_type(pattern_message)
        return user_type == pattern_type and user_type != 'general'

    def _get_question_type(self, message: str) -> str:
        """Get the type of question being asked"""
        if any(word in message for word in ['como', 'how']):
            return 'how'
        elif any(word in message for word in ['o que', 'que é', 'what']):
            return 'what'
        elif any(word in message for word in ['posso', 'can', 'conseguir']):
            return 'can'
        elif any(word in message for word in ['onde', 'where']):
            return 'where'
        elif any(word in message for word in ['quando', 'when']):
            return 'when'
        else:
            return 'general'
