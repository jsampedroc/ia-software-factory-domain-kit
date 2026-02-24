# ai/llm/llm_config.py
import os
from dotenv import load_dotenv

load_dotenv()

def get_model_config(tier="cheap"):
    """
    Tier-based model configuration for optimized performance and cost.
    
    'smart' Tier: Optimized for high-level reasoning, complex architecture design, 
                  and domain modeling (8000 token limit).
    'cheap' Tier: Optimized for high-volume source code generation, 
                  ensuring full Java class completion (4000 token limit).
    """
    if tier == "smart":
        return {
            "model": os.getenv("AI_SMART_MODEL", "deepseek-chat"),
            "max_tokens": 8000,
            "temperature": 0.2
        }
    else:
        # Default tier for heavy code generation tasks
        return {
            "model": os.getenv("AI_CHEAP_MODEL", "deepseek-chat"),
            "max_tokens": 4000, 
            "temperature": 0.1
        }

def build_llm_client():
    """
    Initializes and returns an OpenAI-compatible client for API communication.
    Supports DeepSeek, OpenAI, or local endpoints via environment variables.
    """
    from openai import OpenAI
    
    base_url = os.getenv("AI_BASE_URL", "https://api.deepseek.com/v1")
    api_key = os.getenv("DEEPSEEK_API_KEY")

    if not api_key:
        raise RuntimeError("❌ ERROR: DEEPSEEK_API_KEY is missing from the .env file.")

    return OpenAI(
        api_key=api_key,
        base_url=base_url
    )