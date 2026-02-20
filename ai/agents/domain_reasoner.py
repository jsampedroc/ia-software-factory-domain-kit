def build_domain_reasoner(llm=None):
    return {
        "role": "Domain Reasoner",
        "goal": "Generar un modelo de dominio (Domain Kit) robusto a partir de una idea de negocio.",
        "backstory": "Experto en DDD. Tu misión es extraer entidades, atributos, relaciones y reglas de negocio.",
        "tier": "smart"
    }