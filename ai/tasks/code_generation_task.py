# ai/tasks/code_generation_task.py

def build_code_generation_task(path, context_data=None, **kwargs):
    description = kwargs.get('description') or kwargs.get('desc') or "No description provided"
    ctx = context_data or ""
    base_package = kwargs.get('base_package', 'com.application')

    return {
        "agent": "backend_builder",
        "description": f"""
        Implement the file '{path}' ({description}).
        
        PROJECT CONTEXT:
        {ctx}
        
        STRICT SYNTAX RULES (Java 17):
        1. PACKAGE: The base package for shared logic is '{base_package}.domain.shared'.
        2. VALUE OBJECTS: Must ALWAYS be 'public record' and 'implements ValueObject'. NEVER use 'extends ValueObject'.
        3. ENTITIES: Must be classes that 'extends Entity<ID>'. DO NOT declare the 'id' field (inherited).
        4. GENERICS: In diamonds <>, always use 'extends' (e.g., <ID extends ValueObject>).
        5. LOMBOK: Use @Getter, @NoArgsConstructor(access = PROTECTED), @AllArgsConstructor, and @Builder where applicable.
        """,
        "expected_output": "Clean, professional Java 17 source code."
    }