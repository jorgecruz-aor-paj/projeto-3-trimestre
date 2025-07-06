from transformers import AutoTokenizer, AutoModelForCausalLM
import torch
import os
from training_data import training_conversations
import re
from difflib import SequenceMatcher

class LLMChatbot:
    def __init__(self):
        model_name = "microsoft/DialoGPT-small"
        
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
        
        # Improved conversation patterns with better keyword extraction
        self.conversation_patterns = []
        for item in training_conversations:
            # Extract keywords from input for better matching
            keywords = self.extract_keywords(item["input"])
            self.conversation_patterns.append({
                "input": item["input"].lower(),
                "output": item["output"],
                "keywords": keywords
            })
    
    def extract_keywords(self, text: str) -> list:
        """Extract meaningful keywords from text"""
        # Remove common words and extract meaningful terms
        stop_words = {'how', 'can', 'i', 'do', 'the', 'to', 'a', 'an', 'and', 'or', 'but', 'in', 'on', 'at', 'for', 'with', 'by', 'what', 'where', 'when', 'why', 'who', 'is', 'are', 'was', 'were', 'be', 'been', 'have', 'has', 'had', 'will', 'would', 'could', 'should', 'may', 'might', 'must'}
        
        # Extract words and filter out stop words
        words = re.findall(r'\b\w+\b', text.lower())
        keywords = [word for word in words if word not in stop_words and len(word) > 2]
        
        return keywords
    
    def find_best_match(self, message: str) -> str:
        """Find the best matching response using improved similarity matching"""
        message_lower = message.lower()
        message_keywords = self.extract_keywords(message)
        
        best_match = None
        best_score = 0
        
        for pattern in self.conversation_patterns:
            # Calculate similarity score
            score = 0
            
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
            
            print(f"🔍 Pattern: '{pattern['input'][:50]}...' | Score: {score:.2f}")
            
            if score > best_score and score > 3.0:  # Minimum threshold
                best_score = score
                best_match = pattern["output"]
        
        print(f"🎯 Best match score: {best_score:.2f}")
        return best_match
    
    def generate_response(self, message: str, context: str = "") -> str:
        try:
            print(f"🤖 Processing message: '{message}'")
            
            # Try to find best match in training data first
            best_match = self.find_best_match(message)
            if best_match:
                print("✅ Found training data match")
                return best_match
            
            # Fallback to LLM generation if model is loaded
            if self.model_loaded:
                print("🔄 Trying LLM generation...")
                return self.generate_llm_response(message, context)
            else:
                print("⚠️ LLM not available, using generic fallback")
                return self.get_generic_fallback()
                
        except Exception as e:
            print(f"❌ Error in generate_response: {e}")
            return self.get_generic_fallback()
    
    def generate_llm_response(self, message: str, context: str) -> str:
        """Generate response using the LLM"""
        try:
            # Create a more specific prompt
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
            assistant_response = response.split("Assistant:")[-1].strip()
            
            # Clean up the response
            assistant_response = assistant_response.replace("I can help you with that.", "").strip()
            
            # Validate response quality
            if len(assistant_response.strip()) < 10 or assistant_response.lower() == message.lower():
                raise ValueError("Poor quality response")
                
            print("✅ LLM generated response")
            return assistant_response
            
        except Exception as e:
            print(f"❌ LLM generation failed: {e}")
            raise e
    
    def get_generic_fallback(self) -> str:
        """Generic fallback response"""
        return "I'm here to help you with the Training and Performance Management System. Could you please be more specific about what you need assistance with? You can ask about evaluations, trainings, messages, or general system usage."

# Initialize the chatbot
llm_chatbot = LLMChatbot()