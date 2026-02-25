# ai/tasks/architecture_task.py

def build_architecture_task(domain_kit="", **kwargs):
    # Safe retrieval of dynamic arguments
    dk = domain_kit if domain_kit else kwargs.get('context_data', '')
    base_package = kwargs.get('base_package', 'com.application')
    base_package_path = kwargs.get('base_package_path', 'com/application')

    return {
        "agent": "architect",
        "description": f"""
        Based on this Domain Kit: {dk}
        
        TASK: Design the complete file inventory for a Flat Hexagonal Architecture.
        
        STRICT ARCHITECTURAL RULES:
        1. ROOT PACKAGE: All code MUST start with '{base_package}'.
        2. MAVEN STRUCTURE: File paths MUST start with 'backend/src/main/java/{base_package_path}/'.
        3. FLAT HIERARCHY (NO modules sub-folders):
           - Entities/Enums -> '.../domain/model/'
           - IDs/ValueObjects -> '.../domain/valueobject/'
           - Repositories -> '.../domain/repository/'
           - Services -> '.../application/service/'
           - Controllers -> '.../infrastructure/rest/'
        4. IDENTIFIERS: Never use UUID in the Entity diamond. Create a specific 'record' ID for each entity (e.g., PatientId.java).
        5. EXCLUSIONS: Do not include ValueObject, Entity, or EntityRepository (already injected).
        
        Return ONLY a valid JSON: 
        [
          {{"path": "backend/src/main/java/{base_package_path}/domain/model/EntityName.java", "description": "brief description"}}
        ]
        """,
        "expected_output": "Strict JSON inventory with correct Maven paths and flat structure."
    }