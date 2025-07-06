from transformers import AutoTokenizer, AutoModelForCausalLM
import torch
import os
from training_data import training_conversations
import re
from difflib import SequenceMatcher
from langdetect import detect, LangDetectException

class LLMChatbot:
    def __init__(self):
        # model_name = "microsoft/DialoGPT-small" # English model for better performance, smaller size
        model_name = "microsoft/DialoGPT-medium"  # Support multilingual conversations, larger size.
        
        cache_dir = os.environ.get("HF_HOME", "/app/models/chatbot_cache")
        print(f"🤖 Loading model from/to: {cache_dir}")

        try:
            self.tokenizer = AutoTokenizer.from_pretrained(model_name, cache_dir=cache_dir)
            self.model = AutoModelForCausalLM.from_pretrained(model_name, cache_dir=cache_dir)
            
            if self.tokenizer.pad_token is None:
                self.tokenizer.pad_token = self.tokenizer.eos_token
            
            self.model_loaded = True
            print("✅ LLM model loaded successfully")
        except Exception as e:
            print(f"❌ Failed to load LLM model: {e}")
            self.model_loaded = False
        
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
        
        # Improved conversation patterns with language detection
        self.conversation_patterns = []
        for item in training_conversations:
            detected_lang = self.detect_language(item["input"])
            keywords = self.extract_keywords(item["input"], detected_lang)
            self.conversation_patterns.append({
                "input": item["input"].lower(),
                "output": item["output"],
                "keywords": keywords,
                "language": detected_lang
            })

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

    def find_best_match(self, message: str, user_language: str) -> str:
        """Find the best matching response considering language preference"""
        message_lower = message.lower()
        message_keywords = self.extract_keywords(message, user_language)
        
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

    def generate_response(self, message: str, context: str = "") -> str:
        try:
            # Detect user's language
            user_language = self.detect_language(message)
            print(f"🤖 Processing message: '{message}' in detected language: {user_language}")
            
            # Try to find best match in training data first
            best_match = self.find_best_match(message, user_language)
            if best_match:
                print("✅ Found training data match")
                return best_match
            
            # Fallback to LLM generation if model is loaded
            if self.model_loaded:
                print("🔄 Trying LLM generation...")
                return self.generate_llm_response(message, context, user_language)
            else:
                print("⚠️ LLM not available, using generic fallback")
                return self.get_generic_fallback(user_language)
                
        except Exception as e:
            print(f"❌ Error in generate_response: {e}")
            return self.get_generic_fallback('pt')  # Default to Portuguese

    def generate_llm_response(self, message: str, context: str, language: str) -> str:
        """Generate response using the LLM with language-specific prompts"""
        try:
            # Language-specific prompts
            if language == 'pt':
                prompt = f"""Você é um assistente útil para um Sistema de Gestão de Formação e Desempenho. Responda sempre em português.

Contexto: {context}

Utilizador: {message}
Assistente: Posso ajudá-lo com isso."""
            else:
                prompt = f"""You are a helpful assistant for a Training and Performance Management System.

Context: {context}

User: {message}
Assistant: I can help you with that."""

            inputs = self.tokenizer.encode(prompt, return_tensors='pt', max_length=400, truncation=True)
            
            with torch.no_grad():
                outputs = self.model.generate(
                    inputs,
                    max_length=inputs.shape[1] + 80,
                    temperature=0.8,
                    do_sample=True,
                    pad_token_id=self.tokenizer.eos_token_id,
                    num_return_sequences=1,
                    no_repeat_ngram_size=2
                )
            
            response = self.tokenizer.decode(outputs[0], skip_special_tokens=True)
            
            # Extract assistant response based on language
            if language == 'pt':
                assistant_response = response.split("Assistente:")[-1].strip()
                assistant_response = assistant_response.replace("Posso ajudá-lo com isso.", "").strip()
            else:
                assistant_response = response.split("Assistant:")[-1].strip()
                assistant_response = assistant_response.replace("I can help you with that.", "").strip()
            
            # Validate response quality
            if len(assistant_response.strip()) < 10 or assistant_response.lower() == message.lower():
                raise ValueError("Poor quality response")
                
            print("✅ LLM generated response")
            return assistant_response
            
        except Exception as e:
            print(f"❌ LLM generation failed: {e}")
            raise e

    def get_generic_fallback(self, language: str = 'pt') -> str:
        """Generic fallback response in the specified language"""
        return self.fallback_messages.get(language, self.fallback_messages['pt'])

# Initialize the chatbot
llm_chatbot = LLMChatbot()