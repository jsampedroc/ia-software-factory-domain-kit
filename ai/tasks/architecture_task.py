# ai/tasks/architecture_task.py

def build_architecture_task(domain_kit="", **kwargs):
    dk = domain_kit if domain_kit else kwargs.get('context_data', '')
    base_package = kwargs.get('base_package', 'com.application')
    base_package_path = kwargs.get('base_package_path', 'com/application')

    return {
        "agent": "architect",
        "description": f"""
        Based on this Domain Model: {dk}
        
        TASK: Design the file inventory for a Flat Hexagonal Architecture.
        
        INVENTORY RULES:
        1. IDENTIFIERS: Create a specific 'record' file for every Entity ID in '{base_package}.domain.valueobject'.
        2. MODELS: Entities and Enums in '{base_package}.domain.model'.
        3. REPOSITORIES: Interfaces in '{base_package}.domain.repository'.
        4. APPLICATION: Services in '{base_package}.application.service' and DTOs in '{base_package}.application.dto'.
        
        IMPORTANT: Exclude Shared Kernel classes (Entity, ValueObject) as they are pre-injected.
        
        REQUIRED JSON FORMAT:
        [
          {{"path": "backend/src/main/java/{base_package_path}/domain/valueobject/PatientId.java", "description": "ID record for Patient entity"}},
          {{"path": "backend/src/main/java/{base_package_path}/domain/model/Patient.java", "description": "Core Patient entity"}}
        ]
        """,
        "expected_output": "Strict JSON list of file paths following Maven conventions."
    }