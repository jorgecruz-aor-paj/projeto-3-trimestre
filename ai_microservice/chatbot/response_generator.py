"""
Response Generator Service
Handles LLM-based response generation with language-specific prompts.
"""

import torch


class ResponseGenerator:
    """Service for generating responses using the LLM model"""
    
    def __init__(self, model_manager):
        self.model_manager = model_manager

    def generate_llm_response(self, message: str, context: str, language: str) -> str:
        """Generate response using the LLM with language-specific prompts"""
        if not self.model_manager.is_model_loaded():
            raise RuntimeError("LLM model is not loaded")

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

            tokenizer = self.model_manager.get_tokenizer()
            model = self.model_manager.get_model()

            inputs = tokenizer.encode(prompt, return_tensors='pt', max_length=400, truncation=True)
            
            with torch.no_grad():
                outputs = model.generate(
                    inputs,
                    max_length=inputs.shape[1] + 80,
                    temperature=0.8,
                    do_sample=True,
                    pad_token_id=tokenizer.eos_token_id,
                    num_return_sequences=1,
                    no_repeat_ngram_size=2
                )
            
            response = tokenizer.decode(outputs[0], skip_special_tokens=True)
            
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
