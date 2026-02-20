def build_code_generation_task(file_path, description, domain_kit):
    return {
        "description": f"""
        CONTEXTO DEL DOMINIO: {domain_kit}
        ARCHIVO A GENERAR: {file_path}
        DESCRIPCIÓN TÉCNICA: {description}
        
        Tarea: Escribe el código fuente completo, profesional y listo para compilar.
        Asegúrate de incluir todos los imports y anotaciones de Lombok necesarias.
        """,
        "expected_output": "El contenido íntegro del archivo (sin bloques markdown)."
    }