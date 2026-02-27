# ai/tasks/systemic_fix_task.py

def build_systemic_fix_task(error_log, base_path, **kwargs):
    return {
        "agent": "principal_architect",
        "description": f"""
        TASK: You are a Senior Java Architect & Python Scripting Expert.
        
        INPUT ERROR LOG:
        {error_log}
        
        PROJECT PATH: {base_path}
        
        GOAL:
        Analyze the Maven errors. Find systemic patterns (e.g., class names not matching filenames, wrong imports, missing DTO suffixes, or javax vs jakarta issues).
        Write a Python script that walks through '{base_path}' and automatically fixes these patterns.
        
        STRICT RULES FOR THE PYTHON SCRIPT:
        1. Use only 'os', 're', and 'pathlib'.
        2. The script must be idempotent (can run multiple times safely).
        3. Use re.sub with word boundaries (\\b) to avoid breaking variables.
        4. Print a log of what it is doing (e.g., "Fixing class name in PatientDTO.java").
        
        OUTPUT: Return ONLY the raw Python code. No markdown (```), no explanations, no text.
        """,
        "expected_output": "An autonomous Python repair script."
    }