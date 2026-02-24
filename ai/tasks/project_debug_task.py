# ai/tasks/project_debug_task.py

def build_project_debug_task(error_log, base_package, **kwargs):
    return {
        "agent": "qa_agent",
        "description": f"""
        STRUCTURAL ANALYSIS: A massive project failure has been detected.
        
        MAVEN ERROR LOG:
        '{error_log}'
        
        TASK: Analyze errors and identify broken base contracts in package '{base_package}'. 
        Determine if the issue is inheritance-based (Entity), type-based (ValueObject), or config-based (Lombok).
        """,
        "expected_output": "Technical repair guide for the developer."
    }