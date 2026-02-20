# ai/pipeline/task_executor.py
import os
from ai.llm.llm_config import build_llm_client, get_model_config
from ai.llm.grounding import build_grounded_prompt
from ai.agents.factory import AgentFactory
from ai.tasks.definitions import TASKS

class TaskExecutor:
    def __init__(self):
        """
        Inicializa el cliente de IA una sola vez para reutilizar la conexión.
        """
        self.client = build_llm_client()

    def run_task(self, task_key, context_data="", agent_override=None, **kwargs):
        """
        Ejecuta una tarea del manual de procedimientos resolviendo el agente y 
        formateando la descripción dinámicamente.
        """
        if task_key not in TASKS:
            raise ValueError(f"❌ La tarea '{task_key}' no existe en ai/tasks/definitions.py")

        task_def = TASKS[task_key]
        
        # 1. RESOLUCIÓN DE AGENTE
        role_key = agent_override if agent_override else task_def['agent']
        agent_config = AgentFactory.build_agent(role_key)
        
        # 2. FORMATEO DINÁMICO (Remplaza {idea}, {base_package}, etc.)
        try:
            task_prompt = task_def['description'].format(**kwargs)
        except KeyError as e:
            print(f"⚠️ Warning: Falta el argumento {e} para la tarea '{task_key}'")
            task_prompt = task_def['description']

        # 3. CONFIGURACIÓN DEL MODELO (Tiers: smart vs cheap)
        model_params = get_model_config(agent_config["tier"])
        
        # 4. CONSOLIDACIÓN DE REGLAS (System Rules)
        # CORRECCIÓN: Usamos directamente la llave 'system' definida en definitions.py
        system_rules = agent_config['system']
        
        # 5. CONSTRUCCIÓN DEL PROMPT BLINDADO (Grounding)
        full_prompt = build_grounded_prompt(
            system_rules=system_rules,
            context_data=context_data,
            task_prompt=task_prompt
        )

        # 6. LLAMADA A LA API
        try:
            print(f"   [LLM] Ejecutando '{task_key}' con agente '{role_key}' ({agent_config['tier']})...")
            response = self.client.chat.completions.create(
                messages=[{"role": "user", "content": full_prompt}],
                **model_params
            )
            
            content = response.choices[0].message.content
            if not content:
                raise ValueError("La IA devolvió una respuesta vacía.")
                
            return content

        except Exception as e:
            return f"❌ Error en TaskExecutor: {str(e)}"