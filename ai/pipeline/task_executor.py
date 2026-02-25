# ai/pipeline/task_executor.py
import os
from ai.llm.llm_config import build_llm_client, get_model_config
from ai.llm.grounding import build_grounded_prompt

# Modular Agent and Task imports
import ai.agents as agents

class TaskExecutor:
    def __init__(self):
        """
        Initializes the AI client once to reuse the connection.
        In an industrial factory, connection pooling is vital for performance.
        """
        self.client = build_llm_client()

    def run_task(self, task_key, context_data="", agent_override=None, **kwargs):
        """
        Executes a task by dynamically resolving the agent and the task 
        specification from individual files in ai/agents/ and ai/tasks/.
        """
        # Late import to prevent circular dependency issues during initialization
        import ai.tasks as tasks
        
        # 1. TASK MAPPING (Resolves logic from ai/tasks/ folder)
        task_map = {
            "model_domain": tasks.build_domain_model_task,
            "create_inventory": tasks.build_architecture_task,
            "write_code": tasks.build_code_generation_task,
            "write_tests": tasks.build_write_tests_task,
            "audit_code": tasks.build_audit_code_task,
            "heal_code": tasks.build_heal_code_task,
            "project_debug": tasks.build_project_debug_task,
            "create_skeleton": tasks.build_create_skeleton_task,
            "arbitration": tasks.build_arbitration_task
        }

        if task_key not in task_map:
            raise ValueError(f"❌ Task '{task_key}' is not mapped in TaskExecutor.")

        # 2. ARGUMENT SYNCHRONIZATION (Critical for Auto-Healing)
        # We ensure context_data is passed down as 'domain_kit' or 'context_data'
        # and kwargs like 'error_log' are preserved for the task builders.
        task_args = kwargs.copy()
        task_args['context_data'] = context_data
        if 'domain_kit' not in task_args:
            task_args['domain_kit'] = context_data

        # 3. BUILD TASK DEFINITION
        # This calls functions like build_heal_code_task(**task_args)
        task_def = task_map[task_key](**task_args)
        
        # 4. AGENT RESOLUTION
        # Use override if provided (e.g. SRE for infra); otherwise, use task default
        role_key = agent_override if agent_override else task_def['agent']
        
        agent_map = {
            "domain_reasoner": agents.build_domain_reasoner,
            "architect": agents.build_software_architect,
            "backend_builder": agents.build_backend_builder,
            "qa_agent": agents.build_qa_agent,
            "sre_agent": agents.build_sre_agent,
            "frontend_builder": agents.build_frontend_builder,
            "principal_architect": agents.build_principal_architect
        }
        
        if role_key not in agent_map:
            raise ValueError(f"❌ Agent '{role_key}' is not defined in ai.agents.")
            
        agent_config = agent_map[role_key]()
        
        # 5. SYSTEM RULES CONSOLIDATION (Support for both formats)
        system_rules = agent_config.get('system') or f"You are a {agent_config['role']}. {agent_config['backstory']}"
        
        # 6. LLM CONFIGURATION (Smart vs. Cheap)
        model_params = get_model_config(agent_config["tier"])
        
        # 7. GROUNDED PROMPT CONSTRUCTION
        full_prompt = build_grounded_prompt(
            system_rules=system_rules,
            context_data=context_data,
            task_prompt=task_def['description']
        )

        # 8. API INVOCATION
        try:
            print(f"   [LLM] Running '{task_key}' via '{role_key}' ({agent_config['tier']})...")
            response = self.client.chat.completions.create(
                messages=[{"role": "user", "content": full_prompt}],
                **model_params
            )
            
            content = response.choices[0].message.content
            if not content:
                raise ValueError("The AI returned an empty response.")
                
            return content

        except Exception as e:
            return f"❌ TaskExecutor Error: {str(e)}"