# ai/tasks/heal_code_task.py

def build_heal_code_task(path, error_log, base_package, context_data="", **kwargs):
    return {
        "agent": "backend_builder",
        "description": f"""
        EMERGENCY REPAIR: File '{path}' failed Maven compilation/testing. 
        
        MAVEN ERROR LOG: 
        '{error_log}'
        
        CURRENT CODE:
        {context_data}
        
        TASK: Fix the code to resolve the error while respecting the '{base_package}' package and Hexagonal Architecture.
        """,
        "expected_output": "Full corrected source code."
    }