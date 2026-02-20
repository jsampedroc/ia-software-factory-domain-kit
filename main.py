# main.py
import os
import sys
import json
import yaml
import re
import subprocess
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
        
        try:
            with open("config/architecture.yaml", "r") as f:
                self.arch_config = yaml.safe_load(f)
        except FileNotFoundError:
            print("❌ Error: No se encontró config/architecture.yaml")
            sys.exit(1)
        
        self.project_name = self.arch_config['project']['name']
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
        content = match.group(1).strip() if match else text.strip()
        return content

    def save_to_disk(self, relative_path, content):
        """Guarda archivos y actualiza el estado de persistencia"""
        full_path = self.out_dir / relative_path
        full_path.parent.mkdir(parents=True, exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)
        
        if str(relative_path) not in self.state.generated_files:
            self.state.generated_files.append(str(relative_path))
            StateManager.save_specs(
                self.spec_file, self.state.domain_model, 
                self.state.architecture, self.state.generated_files
            )

    def run_maven_compile(self):
        """
        Ejecuta compilación real para el Auto-Healing.
        Verifica que el código generado sea válido para el compilador Java.
        """
        print("   [System] Verificando integridad del código con Maven...")
        backend_path = self.out_dir / "backend"
        # Si aún no existe el pom.xml (bootstrap), no podemos compilar
        if not (backend_path / "pom.xml").exists(): 
            return None
        
        try:
            # Ejecutamos solo la compilación para ahorrar tiempo
            result = subprocess.run(
                ["mvn", "clean", "compile", "-DskipTests"], 
                cwd=backend_path, 
                capture_output=True, 
                text=True, 
                timeout=180
            )
            if result.returncode != 0:
                # Retornamos el log de error para que la IA lo analice
                return result.stdout + result.stderr
            return None
        except Exception as e:
            return f"Error al ejecutar Maven: {str(e)}"

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee = Tee(log_path)
        sys.stdout = tee 

        try:
            print(f"🚀 --- INICIANDO FACTORÍA INDUSTRIAL: {self.project_name.upper()} ---")
            print(f"📦 PAQUETE BASE: {self.base_package}")

            # ------------------------------------------------------------------
            # FASE 1 & 2: DISEÑO
            # ------------------------------------------------------------------
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 FASE 1: Inferencia de Dominio (DDD)...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)

                print("\n📐 FASE 2: Diseño de Inventario (Architect)...")
                res_arch = self.executor.run_task("create_inventory", context_data=self.state.domain_model, base_package=self.base_package)
                self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Diseño cargado desde caché (Specs).")

            inventory = self.state.architecture.get("file_inventory", [])
            inventory_map = "\n".join([f"- {Path(f['path']).stem}: {f['path']}" for f in inventory])

            # ------------------------------------------------------------------
            # FASE: BOOTSTRAP (Shared Kernel)
            # ------------------------------------------------------------------
            print("\n🏗️  FASE: BOOTSTRAP (Kernel del Sistema)...")
            pkg_path = self.base_package.replace('.', '/')
            bootstrap_tasks = [
                ("backend/pom.xml", f"Pom.xml con Spring Boot 3, JPA, Postgres, Lombok, MapStruct."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/ValueObject.java", "Base ValueObject."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/Entity.java", "Base Entity."),
                (f"backend/src/main/java/{pkg_path}/domain/exception/DomainException.java", "Base Exception.")
            ]
            for path, task_desc in bootstrap_tasks:
                if path not in self.state.generated_files:
                    print(f"   [BOOT] Fabricando: {path}")
                    agent = build_sre_agent() if "pom.xml" in path else build_backend_builder()
                    code = self.executor.run_task("write_code", context_data=self.state.domain_model, path=path, desc=task_desc, base_package=self.base_package)
                    self.save_to_disk(path, self.clean_llm_output(code))

            # ------------------------------------------------------------------
            # FASE 3: CONSTRUCCIÓN + QA + AUTO-HEALING
            # ------------------------------------------------------------------
            print(f"\n🛠️  FASE 3: Fabricando {len(inventory)} archivos de negocio...")
            for file_info in inventory:
                path, desc = file_info['path'], file_info['description']
                if path in self.state.generated_files:
                    print(f"   ⏩ Saltando (Ya existe): {path}"); continue

                print(f"   👉 Generando: {path}")
                agent = build_frontend_builder() if (".js" in path or ".jsx" in path) else build_backend_builder()
                context = f"BASE PACKAGE: {self.base_package}\nMAPA PROYECTO:\n{inventory_map}\n\nDOMAIN KIT:\n{self.state.domain_model}"
                
                # 1. Generación Inicial
                code = self.executor.run_task("write_code", context_data=context, path=path, desc=desc, base_package=self.base_package)
                code = self.clean_llm_output(code)

                # 2. Auditoría QA
                print(f"   🔍 QA Audit...")
                qa_res = self.executor.run_task("audit_code", context_data=code, path=path, base_package=self.base_package)
                if "APROBADO" not in qa_res.upper():
                    print(f"   ⚠️ QA reportó inconsistencias. Corrigiendo...")
                    code = self.executor.run_task("write_code", context_data=f"CORRECCIÓN REQUERIDA: {qa_res}\nCÓDIGO ACTUAL: {code}", path=path, desc=desc, base_package=self.base_package)
                    code = self.clean_llm_output(code)

                self.save_to_disk(path, code)

                # 3. Auto-Healing (Solo para archivos Java)
                if path.endswith(".java"):
                    error_log = self.run_maven_compile() # <--- AQUÍ ESTÁ LA FUNCIÓN
                    if error_log:
                        print(f"   🩹 Error detectado por Maven. Iniciando Auto-Healing para {Path(path).name}...")
                        # Tarea especial de reparación enviada al Builder
                        code = self.executor.run_task("heal_code", context_data=context, path=path, error_log=error_log[:2500])
                        self.save_to_disk(path, self.clean_llm_output(code))

            # ------------------------------------------------------------------
            # FASE 4: INFRAESTRUCTURA, CI/CD & OBSERVABILIDAD
            # ------------------------------------------------------------------
            print("\n🐳 FASE 4: Generando Infraestructura y Pipeline de CI/CD...")
            infra_tasks = [
                (".github/workflows/pipeline.yml", "Genera el workflow de GitHub Actions para compilar con Maven."),
                ("docker-compose.yml", "Docker Compose con Backend, Postgres y stack ELK."),
                ("backend/src/main/resources/logback-spring.xml", "Configuración para envío de logs a ELK."),
                ("README.md", "Documentación técnica y manual de usuario.")
            ]
            for path, task_desc in infra_tasks:
                if path not in self.state.generated_files:
                    print(f"   [SRE] Fabricando: {path}")
                    code = self.executor.run_task("write_code", context_data=self.state.domain_model, path=path, desc=task_desc, base_package=self.base_package, agent_override="sre_agent")
                    self.save_to_disk(path, self.clean_llm_output(code))

            self.state.status = "COMPLETED"
            print(f"\n✨ --- PROYECTO FINALIZADO CON ÉXITO ---")

        except Exception as e:
            self.state.status = "FAILED"
            print(f"❌ Error Crítico: {e}")
            import traceback
            traceback.print_exc()
        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout = sys.__stdout__
            if 'tee' in locals(): tee.close()

if __name__ == "__main__":
    input_idea = " ".join(sys.argv[1:]).strip()
    if not input_idea:
        print("❌ Error: Proporciona una idea.")
        sys.exit(1)
    factory = SoftwareFactory(input_idea)
    factory.run()