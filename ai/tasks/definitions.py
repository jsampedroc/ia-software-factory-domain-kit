# ai/tasks/definitions.py

TASKS = {
    "model_domain": {
        "agent": "domain_reasoner",
        "description": "Analiza la idea '{idea}' y genera el Domain Kit. Paquete base: '{base_package}'.",
        "expected_output": "Markdown con el ADN del negocio."
    },
    "create_inventory": {
        "agent": "architect",
        "description": """
        Genera el inventario para Arquitectura Hexagonal. 
        REGLAS INNEGOCIABLES:
        1. PAQUETES: Usa ÚNICAMENTE '{base_package}.domain.model', '{base_package}.domain.valueobject' y '{base_package}.infrastructure'.
        2. PROHIBIDO: No generes nada en un paquete llamado 'sharedkernel' o similar.
        3. CLASES BASE: NO incluyas ValueObject.java ni Entity.java, el sistema ya los inyecta.
        4. RUTA: Empieza siempre por 'backend/src/main/java/{base_package_path}/'.
        Devuelve JSON puro: [{{'path': '...', 'description': '...'}}].
        """,
        "expected_output": "JSON de inventario alineado."
    },
    "write_code": {
        "agent": "backend_builder",
        "description": """
        Implementa el código para '{path}' ({desc}).
        REGLA CRÍTICA DE HERENCIA:
        - Si es una Entidad que extiende de 'Entity<ID>', NO declares el campo 'id' (ya se hereda).
        - No llames a super() en constructores si usas Lombok @NoArgsConstructor. 
        - Asegúrate de que el paquete raíz sea '{base_package}'.
        """,
        "expected_output": "Código fuente puro."
    },
    "write_tests": {
        "agent": "backend_builder",
        "description": "Genera tests JUnit 5 para '{path}'. Paquete: '{base_package}'. Usa Mockito.",
        "expected_output": "Código JUnit 5."
    },
    "audit_code": {
        "agent": "qa_agent",
        "description": "Audita '{path}'. RECHAZA si el código declara un campo 'id' que ya debería heredarse de Entity.",
        "expected_output": "APROBADO o errores."
    },
    "heal_code": {
        "agent": "backend_builder",
        "description": "REPARACIÓN: El archivo '{path}' falló. ERROR: '{error_log}'. Corrige el código respetando la herencia de '{base_package}.domain.shared.Entity'.",
        "expected_output": "Código corregido."
    },
    "project_debug": {
        "agent": "qa_agent",
        "description": "Analiza el fallo masivo: '{error_log}'. Identifica qué archivos están declarando duplicados de 'id' o tienen problemas de constructor.",
        "expected_output": "Guía de reparación."
    }
}