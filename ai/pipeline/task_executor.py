# ai/pipeline/task_executor.py
import os
from ai.llm.llm_config import build_llm_client, get_model_config
from ai.llm.grounding import build_grounded_prompt
from ai.agents.factory import AgentFactory
from ai.tasks.definitions import TASKS

class TaskExecutor:
    def __init__(self):
        self.client = build_llm_client()

    def run_task(self, task_key, context_data="", agent_override=None, **kwargs):
        if task_key not in TASKS:
            raise ValueError(f"❌ La tarea '{task_key}' no existe.")

        task_def = TASKS[task_key]
        role_key = agent_override if agent_override else task_def['agent']
        agent_config = AgentFactory.build_agent(role_key)
        
        try:
            task_prompt = task_def['description'].format(**kwargs)
        except KeyError as e:
            task_prompt = task_def['description']

        model_params = get_model_config(agent_config["tier"])
        system_rules = agent_config['system']
        
        # Inyectamos una regla de salida innegociable
        system_rules += "\nSALIDA: Devuelve exclusivamente el contenido solicitado, sin explicaciones ni markdown."

        full_prompt = build_grounded_prompt(
            system_rules=system_rules,
            context_data=context_data,
            task_prompt=task_prompt
        )

        try:
            response = self.client.chat.completions.create(
                messages=[{"role": "user", "content": full_prompt}],
                **model_params
            )
            return response.choices[0].message.content
        except Exception as e:
            return f"❌ Error: {str(e)}"