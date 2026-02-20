def build_architecture_task(domain_kit):
    return {
        "description": f"""
        Basado en este Domain Kit: {domain_kit}
        Tarea: Diseña el inventario de archivos para una Arquitectura Hexagonal completa.
        Debes devolver UNICAMENTE un JSON con este formato:
        [
          {{"path": "backend/src/main/java/...", "description": "..."}},
          {{"path": "frontend/src/components/...", "description": "..."}}
        ]
        Incluye: Entities, Repositories, UseCases, Mappers, DTOs y Controllers.
        """,
        "expected_output": "Un JSON puro con la lista de archivos."
    }