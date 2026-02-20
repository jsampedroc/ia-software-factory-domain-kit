def build_grounded_prompt(*, system_rules, context_data, task_prompt):
    """Estructura de prompt profesional del repo original adaptada a DDD"""
    return f"""[SYSTEM]
{system_rules}

[CONTEXT / DOMAIN KIT]
{context_data}

[TASK]
{task_prompt}

[OUTPUT CONTRACT]
- Responde ÚNICAMENTE con el contenido del archivo solicitado.
- NO uses bloques Markdown (```java).
- Si es JSON, debe ser parseable por Python.
""".strip()