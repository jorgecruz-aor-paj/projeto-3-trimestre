"""
Configuration Constants
Centralized configuration settings for the AI microservice chatbot.
VERY RESTRICTIVE settings to minimize LLM fallback and prioritize training data.
"""

# Response quality thresholds - MUCH MORE RESTRICTIVE
CONFIDENCE_THRESHOLD = 12.0  # Increased for more conservative matching
BACKUP_THRESHOLD = 8.0       # Reasonable fallback threshold
PHRASE_SIMILARITY_HIGH = 0.8  # High similarity required
PHRASE_SIMILARITY_MEDIUM = 0.6
KEYWORD_COVERAGE_REQUIRED = 0.4  # Require 40% keyword overlap
MIN_MATCHING_KEYWORDS = 2        # At least 2 matching keywords
MIN_RESPONSE_WORDS = 5
MIN_RESPONSE_LENGTH = 20
MAX_PATTERN_DOMINANCE = 0.3
MIN_WORD_DIVERSITY = 0.4
MIN_RELEVANCE_SCORE = 0.15

# LLM Usage Control - VERY RESTRICTIVE
LLM_USAGE_ENABLED = False  # Set to False to disable LLM completely
LLM_CONFIDENCE_REQUIRED = 20.0  # Extremely high threshold if LLM is used
LLM_MAX_ATTEMPTS = 1  # Only one attempt before giving up

# LLM generation parameters
LLM_MAX_LENGTH_OFFSET = 50
LLM_MIN_LENGTH_OFFSET = 10
LLM_TEMPERATURE = 0.8
LLM_TOP_P = 0.85
LLM_TOP_K = 40
LLM_NO_REPEAT_NGRAM_SIZE = 3
LLM_REPETITION_PENALTY = 1.3

# Validation logging thresholds
POTENTIAL_MATCH_THRESHOLD = 4.0  # Lower threshold to see more candidates

# System behavior flags
DEFAULT_LANGUAGE = 'pt'
ENABLE_DEBUG_LOGGING = True  # Enable debug to see matching process
MAX_RESPONSE_PREVIEW_LENGTH = 50

# Fallback messages when training data doesn't match
FALLBACK_RESPONSES = {
    'pt': [
        "Peço desculpa, mas não tenho informação específica sobre isso.",
        "Não consigo responder a essa questão no momento.",
        "Por favor, reformule a sua pergunta ou contacte o suporte técnico.",
        "Essa questão está fora do meu âmbito de conhecimento.",
        "Não encontrei informação relevante sobre esse tópico."
    ],
    'en': [
        "I'm sorry, but I don't have specific information about that.",
        "I cannot answer that question at the moment.",
        "Please rephrase your question or contact technical support.",
        "That question is outside my scope of knowledge.",
        "I couldn't find relevant information about that topic."
    ]
}
