# ai/tasks/code_generation_task.py

def build_code_generation_task(path, context_data=None, **kwargs):
    description = kwargs.get('description') or kwargs.get('desc') or "Generate domain-driven logic."
    ctx = context_data or ""
    base_package = kwargs.get('base_package', 'com.application')

    return {
        "agent": "backend_builder",
        "description": f"""
        TASK: Implement high-quality Java 17 source code for the file: '{path}'
        OBJECTIVE: {description}

        PROJECT IMPORT MAP (USE THIS FOR YOUR IMPORTS):
        {ctx}

        STRICT ARCHITECTURAL RULES:
        1. PACKAGE DECLARATION: Must exactly match the directory structure. 
           Example: If path is '.../domain/model/User.java', package MUST be 'package {base_package}.domain.model;'
        2. BASE CLASSES (SHARED KERNEL): 
           - Entity, ValueObject, and EntityRepository ARE located in '{base_package}.domain.shared'.
           - NEVER use '{base_package}.domain.base', '.common', or '.core'.
        3. SINGLE RESPONSIBILITY: Only one public class/record per file.
        4. JAVA 17+ STANDARDS:
           - Use 'public record' for all IDs and ValueObjects.
           - Records MUST implement 'ValueObject'. Example: 'public record PatientId(UUID value) implements ValueObject {{}}'
        5. LOMBOK: Use @Getter, @SuperBuilder, and @NoArgsConstructor(access=AccessLevel.PROTECTED) for Entities.
        6. NO JPA IN DOMAIN: Use only pure Java and Lombok in the domain layer.

        OUTPUT ONLY THE SOURCE CODE. NO EXPLANATIONS. NO MARKDOWN WRAPPERS IF POSSIBLE.
        """,
        "expected_output": "Impeccable and compilable Java 17 source code."
    }