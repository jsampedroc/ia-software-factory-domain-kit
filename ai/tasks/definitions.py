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
        Diseña el inventario de archivos para una Arquitectura Hexagonal de Gama Alta.
        REGLAS CRÍTICAS (INNEGOCIABLES):
        1. DEBES incluir las piezas del Shared Kernel en: 'backend/src/main/java/{base_package_path}/domain/shared/':
           - ValueObject.java (Interfaz)
           - Entity.java (Clase abstracta)
           - AggregateRoot.java (Clase abstracta)
        2. DEBES incluir: 'backend/src/main/java/{base_package_path}/domain/exception/DomainException.java'.
        3. Identificadores: CADA entidad debe tener su ID en 'backend/src/main/java/{base_package_path}/domain/valueobject/'.
        4. DTOs: Incluye RequestDTO y ResponseDTO por cada controlador.
        Devuelve JSON: [{{'path': '...', 'description': '...'}}].
        """,
        "expected_output": "JSON de inventario completo siguiendo Maven Standard."
    },
    "write_code": {
        "agent": "backend_builder",
        "description": "Implementa el código del archivo '{path}' ({desc}). REGLA: El paquete debe ser '{base_package}'. Usa Java 17 y Lombok. Si implementas un ValueObject, DEBE ser un 'record' que implemente la interfaz 'ValueObject'.",
        "expected_output": "Código fuente puro."
    },
    "write_tests": {
        "agent": "backend_builder",
        "description": "Genera tests unitarios JUnit 5 + Mockito para '{path}'. Paquete: '{base_package}'.",
        "expected_output": "Código de test."
    },
    "audit_code": {
        "agent": "qa_agent",
        "description": "Audita el archivo '{path}'. RECHAZA si no usa el paquete raíz '{base_package}' o si un ValueObject no implementa 'ValueObject'.",
        "expected_output": "Aprobación o lista de errores."
    },
    "heal_code": {
        "agent": "backend_builder",
        "description": "REPARACIÓN: El archivo '{path}' falló. ERROR: '{error_log}'. Analiza si el error es por un import faltante o un genérico mal definido y corrígelo.",
        "expected_output": "Código corregido."
    },
    "project_debug": {
        "agent": "qa_agent",
        "description": """
        Analiza el LOG DE ERROR GLOBAL de Maven: '{error_log}'.
        Tu misión es identificar las inconsistencias de contratos.
        Busca especialmente:
        1. ¿ValueObject es una interface pero el código intenta extenderla como clase?
        2. ¿Entity<T> requiere que T extienda de algo que los IDs no tienen?
        3. ¿Faltan DTOs que el Arquitecto definió pero el Builder no creó?
        
        Devuelve una LISTA DE ACCIONES CORRECTIVAS para el Builder.
        """,
        "expected_output": "Lista técnica de correcciones estructurales."
    },
}