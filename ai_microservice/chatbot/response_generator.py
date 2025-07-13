"""
Unified Response Generator
Combines all response generation strategies into a single, well-organized class.
Handles business responses, LLM generation, templates, and fallbacks in a clear hierarchy.
"""

import torch
import re
from typing import Optional
from training_data import (
    business_advice, 
    response_templates, 
    domain_knowledge, 
    inappropriate_indicators,
    POOR_RESPONSE_PATTERNS,
    CONTEXT_FRAGMENTS,
    COMMON_WORDS,
    SINGLE_WORD_RESPONSES,
    VALIDATION_MESSAGES,
    GENERATION_MESSAGES,
    SYSTEM_RESPONSE_TEMPLATES,
    TOPIC_KEYWORDS,
    CAPABILITY_RESPONSE_TEMPLATES
)
from config_constants import (
    LLM_MAX_LENGTH_OFFSET,
    LLM_MIN_LENGTH_OFFSET,
    LLM_TEMPERATURE,
    LLM_TOP_P,
    LLM_TOP_K,
    LLM_NO_REPEAT_NGRAM_SIZE,
    LLM_REPETITION_PENALTY,
    MIN_RESPONSE_WORDS,
    MIN_RESPONSE_LENGTH,
    MAX_PATTERN_DOMINANCE,
    MIN_WORD_DIVERSITY,
    MIN_RELEVANCE_SCORE,
    MAX_RESPONSE_PREVIEW_LENGTH
)


class ResponseGenerator:
    """Unified service for all response generation strategies"""
    
    def __init__(self, model_manager):
        self.model_manager = model_manager
        
        # Import all data from training_data file
        self.business_advice = business_advice
        self.response_templates = response_templates
        self.domain_knowledge = domain_knowledge
        self.inappropriate_indicators = inappropriate_indicators

    def generate_response(self, message: str, context: str, language: str) -> str:
        """
        Main response generation method using hierarchical approach:
        1. Business curated responses (highest priority)
        2. LLM generation (if model available)
        3. Template responses (structured fallback)
        4. Generic fallback (last resort)
        """
        # 1. Try business curated responses first
        business_response = self._generate_business_response(message, language)
        if business_response:
            print("✅ Using business response")
            return business_response
        
        # 2. Try LLM generation if model is available
        if self.model_manager.is_model_loaded():
            try:
                llm_response = self._generate_safe_llm_response(message, language)
                if self._is_good_response(llm_response, message):
                    print("✅ Using LLM response")
                    return llm_response
            except Exception as e:
                print(f"❌ LLM failed: {str(e)[:50]}...")
        
        # 3. Try template-based response
        template_response = self._generate_template_response(message, language)
        if template_response:
            print("✅ Using template response")
            return template_response
        
        # 4. Final fallback - generic response
        return self._generate_fallback_response(message, language)

    def _generate_business_response(self, message: str, language: str) -> Optional[str]:
        """Generate response from curated business advice"""
        message_lower = message.lower()
        
        # Check each business topic for relevance
        for topic, content in self.business_advice.items():
            topic_content = content.get(language, content.get('pt', {}))
            
            # Simple keyword matching for business topics
            if topic == 'feedback' and any(word in message_lower for word in ['feedback', 'dar feedback', 'give feedback']):
                return topic_content.get('response', '')
            elif topic == 'management' and any(word in message_lower for word in ['gerir', 'manage', 'gestão', 'management', 'equipa', 'team']):
                return topic_content.get('response', '')
            elif topic == 'performance' and any(word in message_lower for word in ['desempenho', 'performance', 'melhorar', 'improve']):
                return topic_content.get('response', '')
        
        return None

    def _generate_safe_llm_response(self, message: str, language: str) -> str:
        """Generate LLM response with business content filtering"""
        if not self.model_manager.is_model_loaded():
            raise RuntimeError(GENERATION_MESSAGES['model_not_loaded'])

        try:
            # Enhanced prompts with better context to improve response quality
            if language == 'pt':
                # Provide more context about the system to get better responses
                prompt = f"""Sistema de Gestão de Avaliações e Formações

Utilizador: {message}
Assistente: Sobre o sistema de gestão de avaliações e formações, posso ajudar com"""
            else:
                prompt = f"""Evaluation and Training Management System

User: {message}
Assistant: Regarding the evaluation and training management system, I can help with"""

            tokenizer = self.model_manager.get_tokenizer()
            model = self.model_manager.get_model()

            # Create inputs with proper tokenization and attention mask
            encoded = tokenizer(
                prompt, 
                return_tensors='pt', 
                max_length=300, 
                truncation=True, 
                padding=False  # Don't pad since we're only encoding one sequence
            )
            
            inputs = encoded['input_ids']
            # Create attention mask - all tokens are real tokens (no padding)
            attention_mask = encoded.get('attention_mask', torch.ones_like(inputs))
            
            with torch.no_grad():
                outputs = model.generate(
                    inputs,
                    attention_mask=attention_mask,  # Add attention mask to fix warning
                    max_length=inputs.shape[1] + LLM_MAX_LENGTH_OFFSET,
                    min_length=inputs.shape[1] + LLM_MIN_LENGTH_OFFSET,
                    temperature=LLM_TEMPERATURE,
                    do_sample=True,
                    pad_token_id=tokenizer.eos_token_id,
                    num_return_sequences=1,
                    no_repeat_ngram_size=LLM_NO_REPEAT_NGRAM_SIZE,
                    repetition_penalty=LLM_REPETITION_PENALTY,
                    top_p=LLM_TOP_P,
                    top_k=LLM_TOP_K
                    # Removed early_stopping to fix warning
                )
            
            response = tokenizer.decode(outputs[0], skip_special_tokens=True)
            
            # Extract the assistant's response with better parsing
            if language == 'pt':
                if "Assistente:" in response:
                    assistant_response = response.split("Assistente:")[-1].strip()
                    # If the response is incomplete, try to complete the context
                    if assistant_response.startswith("Sobre o sistema de gestão") and len(assistant_response.split()) < 10:
                        assistant_response = f"Posso ajudar com informações sobre avaliações, formações, perfis e funcionalidades do sistema. {assistant_response}"
                else:
                    assistant_response = response.replace(prompt, "").strip()
            else:
                if "Assistant:" in response:
                    assistant_response = response.split("Assistant:")[-1].strip()
                    # If the response is incomplete, try to complete the context
                    if assistant_response.startswith("Regarding the evaluation") and len(assistant_response.split()) < 10:
                        assistant_response = f"I can help with information about evaluations, training, profiles, and system features. {assistant_response}"
                else:
                    assistant_response = response.replace(prompt, "").strip()
            
            # Clean and validate the response
            cleaned_response = self._clean_response(assistant_response, message)
            
            # Additional strict filtering for known problematic patterns
            if self._contains_problematic_patterns(cleaned_response):
                raise ValueError(GENERATION_MESSAGES['problematic_patterns'])
            
            # Business appropriateness check
            if not self._is_business_appropriate(cleaned_response, message):
                raise ValueError(GENERATION_MESSAGES['business_check_failed'])
            
            return cleaned_response
            
        except Exception as e:
            raise

    def _generate_template_response(self, message: str, language: str) -> Optional[str]:
        """Generate response using templates based on question type"""
        question_type = self._detect_question_type(message, language)
        if not question_type:
            return None
        
        template = self.response_templates.get(language, {}).get(question_type)
        if not template:
            return None
        
        # Extract topic from message
        topic = self._detect_topic(message)
        if not topic:
            return None
        
        # Get domain-specific information with proper fallback
        topic_info = self.domain_knowledge.get(topic, {})
        if language in topic_info:
            domain_info = topic_info[language]
        else:
            # Provide better fallback based on language
            if language == 'pt':
                domain_info = "funcionalidades do sistema de gestão de avaliações e formações"
            else:
                domain_info = "features of the evaluation and training management system"
        
        # Translate topic to appropriate language
        topic_translation = {
            'pt': {
                'evaluations': 'avaliações',
                'training': 'formações', 
                'messaging': 'mensagens',
                'profile': 'perfil',
                'system': 'sistema'
            },
            'en': {
                'evaluations': 'evaluations',
                'training': 'training',
                'messaging': 'messaging', 
                'profile': 'profile',
                'system': 'system'
            }
        }
        
        translated_topic = topic_translation.get(language, {}).get(topic, topic)
        
        try:
            # Simple template filling with proper variables
            return template.format(
                topic=translated_topic,
                information=domain_info,
                action=self._extract_action(message, language),
                steps=self._get_steps_for_action(topic, language),
                concept=translated_topic,
                explanation=domain_info,
                specific_info=domain_info,
                reason=domain_info,
                benefit="melhor experiência do utilizador" if language == 'pt' else "better user experience",
                location="menu principal" if language == 'pt' else "main menu",
                navigation="navegação do sistema" if language == 'pt' else "system navigation"
            )
        except Exception as e:
            print(f"Template formatting error: {e}")
            return None

    def _generate_fallback_response(self, message: str, language: str) -> str:
        """Generate contextual fallback response based on message content"""
        message_lower = message.lower()
        
        # Detect if user is asking about capabilities/what the assistant can do
        capability_keywords = {
            'pt': ['que podes', 'o que podes', 'que pode', 'o que pode', 'como podes', 'como pode', 'ajudar', 'fazer'],
            'en': ['what can you', 'what do you', 'how can you', 'help me', 'what are you', 'capabilities']
        }
        
        if any(keyword in message_lower for keyword in capability_keywords[language]):
            return CAPABILITY_RESPONSE_TEMPLATES[language]['comprehensive']
        
        # Default fallback with system context
        return SYSTEM_RESPONSE_TEMPLATES[language]['general_help']

    def _is_business_appropriate(self, response: str, original_message: str) -> bool:
        """Check if response is appropriate for business context"""
        response_lower = response.lower()
        
        # Check for inappropriate content
        for indicator in self.inappropriate_indicators:
            if indicator in response_lower:
                return False
        
        # Must be reasonably long and coherent
        if len(response.strip()) < 10:
            return False
        
        # Should not just repeat the question
        if original_message.lower() in response_lower:
            return False
        
        return True

    def _is_good_response(self, response: str, original_message: str) -> bool:
        """Validate response quality with strict criteria"""
        if not response or len(response.strip()) < MIN_RESPONSE_LENGTH:
            print(f"{VALIDATION_MESSAGES['too_short']}: '{response}'")
            return False
        
        response_lower = response.lower().strip()
        
        # Check for context leakage
        if any(fragment in response for fragment in CONTEXT_FRAGMENTS):
            print(f"{VALIDATION_MESSAGES['context_leakage']}: {response[:30]}...")
            return False
        
        # Filter out generic/poor responses (same as response_matcher)
        for pattern in POOR_RESPONSE_PATTERNS:
            if pattern in response_lower:
                # If it's a short response containing these patterns, reject
                if len(response_lower) < 50:
                    print(f"{VALIDATION_MESSAGES['generic_response']}: '{response[:30]}...'")
                    return False
                # If the pattern dominates the response, reject
                if len(pattern) / len(response_lower) > MAX_PATTERN_DOMINANCE:
                    print(f"{VALIDATION_MESSAGES['pattern_dominated']}: '{response[:MAX_RESPONSE_PREVIEW_LENGTH]}...'")
                    return False
        
        # Response should have some substance - require at least 8 words
        words = response.split()
        if len(words) < MIN_RESPONSE_WORDS:
            print(f"{VALIDATION_MESSAGES['too_short']} (word count): '{response}'")
            return False
        
        # Check for coherent sentence structure
        if not any(char in response for char in '.!?'):
            print(f"{VALIDATION_MESSAGES['no_punctuation']}: '{response[:MAX_RESPONSE_PREVIEW_LENGTH]}...'")
            return False
        
        # Reject responses that are just repetitive phrases
        word_set = set(words)
        if len(words) > 5 and len(word_set) / len(words) < MIN_WORD_DIVERSITY:
            print(f"{VALIDATION_MESSAGES['too_repetitive']}: '{response[:MAX_RESPONSE_PREVIEW_LENGTH]}...'")
            return False
        
        # Check if response actually relates to the question
        if not self._check_response_relevance(response_lower, original_message.lower()):
            print(f"{VALIDATION_MESSAGES['not_relevant']}: '{response[:MAX_RESPONSE_PREVIEW_LENGTH]}...'")
            return False
        
        print(f"{VALIDATION_MESSAGES['passed_validation']}: '{response[:MAX_RESPONSE_PREVIEW_LENGTH]}...'")
        return True

    def _check_response_relevance(self, response: str, question: str) -> bool:
        """Check if response is somewhat relevant to the question"""
        # Extract key words from question (ignore common words)
        question_words = set(question.split()) - COMMON_WORDS
        response_words = set(response.split()) - COMMON_WORDS
        
        if not question_words:
            return True  # Can't judge relevance if no meaningful words in question
        
        # Check for some overlap in meaningful words
        overlap = len(question_words & response_words)
        relevance_score = overlap / len(question_words)
        
        # Require at least 10% word overlap for relevance
        return relevance_score >= MIN_RELEVANCE_SCORE

    def _clean_response(self, response: str, original_message: str) -> str:
        """Clean artifacts from response"""
        # Remove common artifacts
        artifacts = [
            "User question:", "Pergunta do utilizador:", 
            "Response:", "Resposta:", "System:", "Sistema:",
            "Utilizador:", "User:", original_message
        ]
        
        cleaned = response
        for artifact in artifacts:
            if artifact in cleaned:
                parts = cleaned.split(artifact)
                cleaned = parts[-1] if len(parts) > 1 else parts[0]
        
        return cleaned.strip()

    def _detect_question_type(self, message: str, language: str) -> Optional[str]:
        """Detect the type of question being asked"""
        message_lower = message.lower()
        
        if language == 'pt':
            if any(word in message_lower for word in ['como', 'como fazer']):
                return 'how_to'
            elif any(word in message_lower for word in ['o que é', 'que é']):
                return 'what_is'
            elif 'porque' in message_lower or 'por que' in message_lower:
                return 'why'
            elif 'onde' in message_lower:
                return 'where'
        else:
            if any(word in message_lower for word in ['how', 'how to']):
                return 'how_to'
            elif any(word in message_lower for word in ['what is', 'what are']):
                return 'what_is'
            elif 'why' in message_lower:
                return 'why'
            elif 'where' in message_lower:
                return 'where'
        
        return 'general'

    def _detect_topic(self, message: str) -> Optional[str]:
        """Detect the main topic of the message"""
        message_lower = message.lower()
        
        topics = {
            'evaluations': ['avaliação', 'evaluation', 'avaliar', 'desempenho', 'performance'],
            'training': ['formação', 'training', 'curso', 'treino'],
            'messaging': ['mensagem', 'message', 'chat', 'comunicação'],
            'profile': ['perfil', 'profile', 'conta', 'account']
        }
        
        for topic, keywords in topics.items():
            if any(keyword in message_lower for keyword in keywords):
                return topic
        
        return 'system'

    def _extract_action(self, message: str, language: str) -> str:
        """Extract the action being asked about"""
        if language == 'pt':
            return "usar o sistema"
        else:
            return "use the system"

    def _get_steps_for_action(self, topic: str, language: str) -> str:
        """Get steps for performing an action on a topic"""
        if language == 'pt':
            return f"aceder à secção de {topic} no menu principal"
        else:
            return f"access the {topic} section in the main menu"

    def _contains_problematic_patterns(self, response: str) -> bool:
        """Check if response contains known problematic patterns"""
        if not response:
            return True
            
        response_lower = response.lower().strip()
        
        # Check for exact matches or if pattern dominates the response
        for pattern in POOR_RESPONSE_PATTERNS:
            if pattern in response_lower:
                # If response is short and contains the pattern, reject it
                if len(response_lower) < 30:
                    print(f"{VALIDATION_MESSAGES['short_with_pattern']} '{pattern}': '{response}'")
                    return True
                # If pattern takes up significant portion of response, reject it
                if len(pattern) / len(response_lower) > 0.4:
                    print(f"{VALIDATION_MESSAGES['pattern_dominated']} '{pattern}': '{response[:30]}...'")
                    return True
        
        # Additional checks for very short or meaningless responses
        words = response.split()
        if len(words) < 3:
            print(f"{VALIDATION_MESSAGES['too_short']}: '{response}'")
            return True
            
        # Check for responses that are just single words or very generic
        if len(words) == 1 and words[0].lower() in SINGLE_WORD_RESPONSES:
            print(f"{VALIDATION_MESSAGES['single_word']}: '{response}'")
            return True
        
        return False
