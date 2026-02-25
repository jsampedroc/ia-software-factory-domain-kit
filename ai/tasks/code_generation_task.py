# ai/tasks/code_generation_task.py

def build_code_generation_task(path, context_data=None, **kwargs):
    """
    Elite Task Generator: Enforces strict architectural boundaries and 
    prevents common AI package hallucinations.
    """
    description = kwargs.get('description') or kwargs.get('desc') or "Generate domain-driven logic."
    ctx = context_data or ""
    base_package = kwargs.get('base_package', 'com.application')

    return {
        "agent": "backend_builder",
        "description": f"""
        TASK: Implement the high-quality Java 17 source code for: '{path}'
        OBJECTIVE: {description}
        
        PROJECT ARCHITECTURE & IMPORT MAP:
        {ctx}
        
        STRICT PACKAGE & IMPORT RULES:
        1. BASE CLASSES: Entity, ValueObject, and EntityRepository are located in '{base_package}.domain.shared'. 
           NEVER import from '{base_package}.domain.base', '.common', or '.core'.
        2. INTERNAL IMPORTS: You MUST use the PROJECT_FILE_MAP above to find the correct package for any internal class.
        3. CONSISTENCY: Ensure the 'package' declaration matches the file path exactly in lowercase.

        STRICT LAYER RULES:
        - IF PATH CONTAINS '/domain/': Generate PURE JAVA. NO JPA (@Entity, @Table) allowed. Use Lombok @SuperBuilder, @Getter, and @ToString.
        - IF PATH CONTAINS '/infrastructure/': Generate PERSISTENCE code. Use JPA annotations (@Entity, @Table, @Column).
        - IF PATH CONTAINS '/mapper/': Generate MapStruct interfaces to bridge Domain and JpaEntity.
        
        SINTAX RULES (Java 17):
        - VALUE OBJECTS & IDs: Use 'public record' and 'implements ValueObject'.
        - ENTITIES: Use 'public class' and 'extends Entity<ID>'.
        - GENERICS: In diamonds <>, always use 'extends' (e.g., <ID extends ValueObject>).
        """,
        "expected_output": "Full, compilable Java 17 source code with verified imports and correct layer annotations."
    }