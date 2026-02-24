def build_domain_model_task(idea, base_package, **kwargs):
    return {
        "agent": "domain_reasoner",
        "description": f"""
        Analyze the following business idea: '{idea}'
        CONTEXT RULE: The root package for the system is '{base_package}'.
        
        TASK: Create a professional Domain Kit in Markdown including:
        1. Ubiquitous Language (Glossary).
        2. Entities and Attributes (using Java 17 data types).
        3. Relationships (1:N, N:N).
        4. Critical Business Rules and Invariants.
        """,
        "expected_output": "A comprehensive Markdown document representing the Business ADN."
    }