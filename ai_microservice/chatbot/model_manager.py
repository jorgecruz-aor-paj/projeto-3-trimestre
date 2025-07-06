"""
LLM Model Manager
Handles the loading, management, and configuration of the language model.
"""

import os
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM


class ModelManager:
    """Manages the LLM model loading and configuration"""
    
    def __init__(self, model_name: str = "microsoft/DialoGPT-medium"):
        self.model_name = model_name
        self.tokenizer = None
        self.model = None
        self.model_loaded = False
        self._load_model()

    def _load_model(self):
        """Load the LLM model and tokenizer"""
        cache_dir = os.environ.get("HF_HOME", "/app/models/chatbot_cache")
        print(f"🤖 Loading model from/to: {cache_dir}")

        try:
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_name, cache_dir=cache_dir)
            self.model = AutoModelForCausalLM.from_pretrained(self.model_name, cache_dir=cache_dir)
            
            if self.tokenizer.pad_token is None:
                self.tokenizer.pad_token = self.tokenizer.eos_token
            
            self.model_loaded = True
            print("✅ LLM model loaded successfully")
        except Exception as e:
            print(f"❌ Failed to load LLM model: {e}")
            self.model_loaded = False

    def is_model_loaded(self) -> bool:
        """Check if the model is successfully loaded"""
        return self.model_loaded

    def get_tokenizer(self):
        """Get the loaded tokenizer"""
        return self.tokenizer

    def get_model(self):
        """Get the loaded model"""
        return self.model
