# ai/tasks/definitions.py

TASKS = {
    "model_domain": {
        "agent": "domain_reasoner",
        "description": "Analiza la idea '{idea}' y genera un Domain Kit profesional. REGLA: El paquete base técnico es '{base_package}'.",
        "expected_output": "Markdown con el Domain Kit."
    },
    "create_inventory": {
        "agent": "architect",
        "description": """
        Diseña el inventario de archivos para una Arquitectura Hexagonal.
        REGLA DE ORO: Todo el código debe vivir bajo el paquete raíz '{base_package}'.
        Los modelos van en '{base_package}.domain.model', 
        los IDs en '{base_package}.domain.valueobject', 
        y los adaptadores en '{base_package}.infrastructure'.
        Devuelve JSON: [{{'path': '...', 'description': '...'}}].
        """,
        "expected_output": "JSON de inventario con rutas dinámicas."
    },
    "write_code": {
        "agent": "backend_builder",
        "description": "Implementa el código del archivo '{path}' ({desc}). REGLA: El paquete debe ser '{base_package}' o sus subcarpetas. Respeta los imports del mapa de archivos.",
        "expected_output": "Código fuente puro."
    },
    "audit_code": {
        "agent": "qa_agent",
        "description": "Audita el archivo '{path}'. RECHAZA si el paquete no comienza por '{base_package}' o si detectas inconsistencias.",
        "expected_output": "Aprobación o lista de errores."
    }
}