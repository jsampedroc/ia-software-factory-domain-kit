# ai/tasks/architecture_task.py

def build_architecture_task(domain_kit="", **kwargs):
    dk = domain_kit if domain_kit else kwargs.get('context_data', '')
    base_package = kwargs.get('base_package', 'com.application')
    base_package_path = kwargs.get('base_package_path', 'com/application')

    return {
        "agent": "architect",
        "description": f"""
        Based on this Domain Kit: {dk}
        
        TASK: Design the complete file inventory for a Flat Hexagonal Architecture.
        
        STRICT NAMING RULES:
        1. ROOT PACKAGE: All code must reside under '{base_package}'.
        2. MAVEN STRUCTURE: All file paths must start with 'backend/src/main/java/{base_package_path}/'.
        3. FLAT STRUCTURE:
           - Entities/Enums -> '.../domain/model/'
           - IDs/ValueObjects -> '.../domain/valueobject/'
           - Ports/Repositories -> '.../domain/repository/'
           - Services -> '.../application/service/'
           - Controllers -> '.../infrastructure/rest/'
        4. NO SUB-FOLDERS: Do not create modulefolders (e.g., NO .../model/student/).
        5. EXCLUSIONS: Do not include ValueObject, Entity, or EntityRepository (already injected).
        
        Return ONLY a valid JSON: 
        [
          {{"path": "backend/src/main/java/{base_package_path}/domain/model/EntityName.java", "description": "brief description"}}
        ]
        """,
        "expected_output": "Strict JSON inventory with correct Maven paths."
    }