def build_domain_model_task(idea):
    return {
        "description": f"""
        Analiza la siguiente idea de negocio: '{idea}'
        Tarea: Crea un Domain Kit profesional en Markdown que incluya:
        1. Lenguaje Ubicuo (Glosario).
        2. Entidades y Atributos (con tipos de datos).
        3. Relaciones (1:N, N:N).
        4. Reglas de Negocio críticas.
        """,
        "expected_output": "Un documento Markdown con el ADN del negocio."
    }