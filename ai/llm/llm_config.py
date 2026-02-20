import os
from dotenv import load_dotenv

load_dotenv()

def get_model_config(tier="cheap"):
    """
    Configuración de Tiers basada en tu archivo profesional.
    Smart: Para diseño y arquitectura (8000 tokens).
    Cheap: Para código masivo Java (4000 tokens).
    """
    if tier == "smart":
        return {
            "model": os.getenv("AI_SMART_MODEL", "deepseek-chat"),
            "max_tokens": 8000,
            "temperature": 0.2
        }
    else:
        # Usamos deepseek-coder o chat para código masivo
        return {
            "model": os.getenv("AI_CHEAP_MODEL", "deepseek-chat"),
            "max_tokens": 4000, 
            "temperature": 0.1
        }

def build_llm_client():
    """Crea el cliente para conectar con la API"""
    from openai import OpenAI
    
    base_url = os.getenv("AI_BASE_URL", "https://api.deepseek.com/v1")
    api_key = os.getenv("DEEPSEEK_API_KEY")

    if not api_key:
        raise RuntimeError("❌ Falta DEEPSEEK_API_KEY en el archivo .env")

    return OpenAI(
        api_key=api_key,
        base_url=base_url
    )