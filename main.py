# main.py
import os
import sys
import json
import yaml
import re
from pathlib import Path
from dotenv import load_dotenv

# Infraestructura y Helpers
from ai.llm.llm_config import build_llm_client
from ai.pipeline.state_manager import StateManager
from ai.pipeline.task_executor import TaskExecutor
from ai.utils.logging_helper import Tee
from ai.reporting.report_generator import generate_report

# Agentes y Tareas Modulares
from ai.agents import (
    build_domain_reasoner, build_software_architect, 
    build_backend_builder, build_qa_agent, build_sre_agent,
    build_frontend_builder
)
from ai.tasks import (
    build_domain_model_task, build_architecture_task, build_code_generation_task
)

class PipelineState:
    def __init__(self, idea, project_name):
        self.idea = idea
        self.project_name = project_name
        self.domain_model = None
        self.architecture = None
        self.generated_files = []
        self.status = "IN_PROGRESS"

class SoftwareFactory:
    def __init__(self, idea):
        load_dotenv()
        self.idea = idea
        self.executor = TaskExecutor()
        
        # 1. Cargar Configuración desde architecture.yaml
        try:
            with open("config/architecture.yaml", "r") as f:
                self.arch_config = yaml.safe_load(f)
        except FileNotFoundError:
            print("❌ Error: No se encontró config/architecture.yaml")
            sys.exit(1)
        
        # 2. PROCESAMIENTO AGNOSTICO DE NOMBRES
        self.project_name = self.arch_config['project']['name']
        # Slug para paquetes y carpetas (solo letras y números)
        self.project_slug = re.sub(r'[^a-zA-Z0-9]', '', self.project_name.lower())
        self.base_package = f"com.{self.project_slug}"
        
        self.out_dir = Path("outputs") / self.project_slug
        self.spec_file = Path("specs") / f"{self.project_slug}.json"
        
        self.out_dir.mkdir(parents=True, exist_ok=True)
        Path("specs").mkdir(exist_ok=True)
        
        self.state = PipelineState(self.idea, self.project_name)

    def clean_llm_output(self, text):
        """Limpia bloques markdown y asegura que el contenido sea puro"""
        pattern = r"```(?:java|javascript|jsx|yaml|json|xml|text)?\n?(.*?)\n?```"
        match = re.search(pattern, text, re.DOTALL)
        return match.group(1).strip() if match else text.strip()

    def save_to_disk(self, relative_path, content):
        """Guarda archivos físicamente y actualiza el StateManager"""
        full_path = self.out_dir / relative_path
        full_path.parent.mkdir(parents=True, exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        if str(relative_path) not in self.state.generated_files:
            self.state.generated_files.append(str(relative_path))
            StateManager.save_specs(
                self.spec_file, 
                self.state.domain_model, 
                self.state.architecture, 
                self.state.generated_files
            )

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee = Tee(log_path)
        sys.stdout = tee 

        try:
            print(f"🚀 --- INICIANDO FACTORÍA INDUSTRIAL: {self.project_name.upper()} ---")
            print(f"💡 IDEA: {self.idea[:100]}...")
            print(f"📦 PAQUETE BASE: {self.base_package}")

            # ------------------------------------------------------------------
            # FASE 1 & 2: DISEÑO CON MEMORIA
            # ------------------------------------------------------------------
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 FASE 1: Inferencia de Dominio (Domain Reasoner)...")
                self.state.domain_model = self.executor.run_task(
                    "model_domain", 
                    idea=self.idea, 
                    base_package=self.base_package
                )

                print("\n📐 FASE 2: Diseño de Inventario (Architect)...")
                res_arch = self.executor.run_task(
                    "create_inventory", 
                    context_data=self.state.domain_model, 
                    base_package=self.base_package
                )
                
                inventory_json = self.clean_llm_output(res_arch)
                self.state.architecture = {"file_inventory": json.loads(inventory_json)}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Diseño cargado desde caché (Specs).")

            inventory = self.state.architecture.get("file_inventory", [])
            inventory_map = "\n".join([f"- Clase: {Path(f['path']).stem}, Path: {f['path']}" for f in inventory])

            # ------------------------------------------------------------------
            # FASE: BOOTSTRAP (Kernel del Sistema)
            # ------------------------------------------------------------------
            print("\n🏗️  FASE: BOOTSTRAP (Shared Kernel)...")
            pkg_path = self.base_package.replace('.', '/')
            bootstrap_tasks = [
                ("backend/pom.xml", f"Proyecto Maven para {self.project_name}."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/ValueObject.java", "Base ValueObject."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/Entity.java", "Base Entity."),
                (f"backend/src/main/java/{pkg_path}/domain/exception/DomainException.java", "Excepción base.")
            ]
            
            for path, task_desc in bootstrap_tasks:
                if path not in self.state.generated_files:
                    print(f"   [BOOT] Fabricando: {path}")
                    agent = build_sre_agent() if "pom.xml" in path else build_backend_builder()
                    code = self.executor.run_task(
                        "write_code", 
                        context_data=self.state.domain_model, 
                        path=path, 
                        desc=task_desc, 
                        base_package=self.base_package
                    )
                    self.save_to_disk(path, self.clean_llm_output(code))

            # ------------------------------------------------------------------
            # FASE 3: CONSTRUCCIÓN Y QA (INCREMENTAL)
            # ------------------------------------------------------------------
            print(f"\n🛠️  FASE 3: Fabricando {len(inventory)} archivos de negocio...")

            for file_info in inventory:
                path = file_info['path']
                desc = file_info['description']

                if path in self.state.generated_files:
                    print(f"   ⏩ Saltando (Ya existe): {path}")
                    continue

                print(f"   👉 Generando: {path}")
                
                agent = build_frontend_builder() if (".js" in path or ".jsx" in path) else build_backend_builder()
                context_enriquecido = (
                    f"REGLA DE ORO: El paquete raíz es '{self.base_package}'.\n"
                    f"ESTRUCTURA DEL PROYECTO:\n{inventory_map}\n\n"
                    f"DOMAIN KIT:\n{self.state.domain_model}"
                )
                
                code = self.executor.run_task("write_code", context_data=context_enriquecido, path=path, desc=desc, base_package=self.base_package)
                code = self.clean_llm_output(code)

                # Auditoría QA
                qa_res = self.executor.run_task("audit_code", context_data=code, path=path, base_package=self.base_package)
                
                if "APROBADO" not in qa_res.upper():
                    print(f"   ⚠️ QA pidió correcciones. Re-intentando...")
                    fix_context = f"ERROR: {qa_res}\nMAPA: {inventory_map}\nDOMAIN: {self.state.domain_model}"
                    code = self.executor.run_task("write_code", context_data=fix_context, path=path, desc=f"CORRECCIÓN: {desc}", base_package=self.base_package)
                    code = self.clean_llm_output(code)

                self.save_to_disk(path, code)

            # ------------------------------------------------------------------
            # FASE 4: INFRAESTRUCTURA (SRE)
            # ------------------------------------------------------------------
            print("\n🐳 FASE 4: Generando Infraestructura de Despliegue...")
            infra_tasks = [
                ("backend/Dockerfile", "Dockerfile multi-stage para optimizar la imagen de producción."),
                ("docker-compose.yml", "Archivo docker-compose para levantar Backend y PostgreSQL."),
                ("README.md", "Guía profesional de instalación y ejecución del proyecto.")
            ]

            for path, task_desc in infra_tasks:
                if path not in self.state.generated_files:
                    print(f"   [SRE] Generando {path}...")
                    infra_code = self.executor.run_task(
                        "write_code", context_data=self.state.domain_model,
                        path=path, desc=task_desc, base_package=self.base_package, agent_override="sre_agent"
                    )
                    self.save_to_disk(path, self.clean_llm_output(infra_code))

            self.state.status = "COMPLETED"
            print(f"\n✨ --- PROYECTO {self.project_name.upper()} FINALIZADO CON ÉXITO ---")

        except Exception as e:
            self.state.status = "FAILED"
            print(f"❌ Error Crítico: {e}")
            import traceback
            traceback.print_exc()

        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout = sys.__stdout__
            if 'tee' in locals():
                tee.close()

if __name__ == "__main__":
    # Capturar idea desde la terminal
    input_idea = " ".join(sys.argv[1:]).strip()

    if not input_idea:
        print("❌ Error: Proporciona una idea de software.")
        print("Uso: python main.py 'Idea del proyecto'")
        sys.exit(1)
    
    factory = SoftwareFactory(input_idea)
    factory.run()