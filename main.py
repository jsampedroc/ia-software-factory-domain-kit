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
        self.base_package_path = self.base_package.replace('.', '/')
        
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
        """Guarda archivos físicamente y actualiza el StateManager"""
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

    def run_maven_test(self):
        """Ejecuta mvn test para validar lógica y cobertura JaCoCo"""
        print(f"   [System] Ejecutando Maven Test en {self.project_name}...")
        backend_path = self.out_dir / "backend"
        if not (backend_path / "pom.xml").exists(): return None
        
        try:
            # Usamos compile primero para detectar errores rápido antes de los tests
            result = subprocess.run(
                ["mvn", "clean", "compile", "test", "-DskipTests=false"], 
                cwd=backend_path, capture_output=True, text=True, timeout=300
            )
            if result.returncode != 0:
                return result.stdout + result.stderr
            return None
        except Exception as e:
            return str(e)

    def run_massive_healing(self, error_log):
        """
        Analiza un log de error masivo y re-sincroniza los contratos del Shared Kernel
        y los Value Objects de Identidad.
        """
        print("\n🚨 REPARACIÓN MASIVA: Detectadas inconsistencias de contrato.")
        
        # 1. El QA analiza la causa raíz (Capa de arquitectura)
        analysis = self.executor.run_task(
            "project_debug", 
            error_log=error_log[:4000], 
            base_package=self.base_package
        )
        print(f"   [QA Analysis]: {analysis[:200]}...")

        # 2. Identificar archivos críticos a re-generar
        inventory = self.state.architecture.get("file_inventory", [])
        critical_files = [f for f in inventory if "shared" in f['path'] or "valueobject" in f['path']]
        
        for file_info in critical_files:
            path = file_info['path']
            print(f"   🩹 Sincronizando: {Path(path).name}")
            
            fix_desc = f"Aplica esta corrección de arquitectura: {analysis}. Asegura que el código sea compatible con el resto del sistema."
            code = self.executor.run_task(
                "write_code", context_data=self.state.domain_model, 
                path=path, desc=fix_desc, base_package=self.base_package
            )
            self.save_to_disk(path, self.clean_llm_output(code))

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee = Tee(log_path)
        sys.stdout = tee 

        try:
            print(f"🚀 --- INICIANDO FACTORÍA INDUSTRIAL: {self.project_name.upper()} ---")
            print(f"📦 PAQUETE BASE: {self.base_package}")

            # FASE 1 & 2: DISEÑO
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 FASE 1: Inferencia de Dominio...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)
                print("\n📐 FASE 2: Diseño de Inventario...")
                res_arch = self.executor.run_task("create_inventory", context_data=self.state.domain_model, base_package=self.base_package, base_package_path=self.base_package_path)
                self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Diseño cargado desde caché (Specs).")

            inventory = self.state.architecture.get("file_inventory", [])
            inventory_map = "\n".join([f"- {Path(f['path']).name}: {f['path']}" for f in inventory])

            # FASE: BOOTSTRAP (Kernel del Sistema)
            print("\n🏗️  FASE: BOOTSTRAP (Shared Kernel)...")
            pkg_path = self.base_package_path
            bootstrap_tasks = [
                ("backend/pom.xml", f"Pom.xml con Spring Boot 3, JaCoCo, Lombok y MapStruct."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/ValueObject.java", "INTERFAZ base para Value Objects."),
                (f"backend/src/main/java/{pkg_path}/domain/shared/Entity.java", "CLASE ABSTRACTA base para entidades: Entity<ID extends ValueObject>."),
                (f"backend/src/main/java/{pkg_path}/domain/exception/DomainException.java", "RuntimeException base.")
            ]
            for path, task_desc in bootstrap_tasks:
                if path not in self.state.generated_files:
                    print(f"   [BOOT] Fabricando: {path}")
                    agent = build_sre_agent() if "pom.xml" in path else build_backend_builder()
                    code = self.executor.run_task("write_code", context_data=self.state.domain_model, path=path, desc=task_desc, base_package=self.base_package)
                    self.save_to_disk(path, self.clean_llm_output(code))

            # FASE 3: FABRICACIÓN + QA + AUTO-HEALING
            print(f"\n🛠️  FASE 3: Fabricando {len(inventory)} archivos con Auditoría Proactiva...")
            for file_info in inventory:
                path, desc = file_info['path'], file_info['description']
                if path in self.state.generated_files: continue

                print(f"   👉 Generando: {path}")
                agent = build_frontend_builder() if (".js" in path or ".jsx" in path) else build_backend_builder()
                context = f"BASE PACKAGE: {self.base_package}\nMAPA PROYECTO:\n{inventory_map}\n\nDOMAIN KIT:\n{self.state.domain_model}"
                
                # 1. Generación de Código
                code = self.executor.run_task("write_code", context_data=context, path=path, desc=desc, base_package=self.base_package)
                code = self.clean_llm_output(code)
                
                # 2. Auditoría QA Previa
                qa_res = self.executor.run_task("audit_code", context_data=code, path=path, base_package=self.base_package)
                if "APROBADO" not in qa_res.upper():
                    print(f"   ⚠️ QA detectó fallos. Reparando...")
                    code = self.executor.run_task("write_code", context_data=f"ERROR QA: {qa_res}\nCODE: {code}", path=path, desc=desc, base_package=self.base_package)
                    code = self.clean_llm_output(code)

                self.save_to_disk(path, code)

                # 3. Generación de Tests
                if path.endswith(".java") and ("domain" in path.lower() or "application" in path.lower()) and "shared" not in path:
                    print(f"   🧪 Fabricando Tests para {Path(path).name}...")
                    test_path = path.replace("src/main/java", "src/test/java").replace(".java", "Test.java")
                    test_code = self.executor.run_task("write_tests", context_data=context, path=path, base_package=self.base_package)
                    self.save_to_disk(test_path, self.clean_llm_output(test_code))

                    # 4. Auto-Healing Real con Maven
                    error_log = self.run_maven_test()
                    if error_log:
                        # Si el error es masivo (>50 errores), disparamos la reparación de arquitectura
                        if error_log.count("[ERROR]") > 15:
                            self.run_massive_healing(error_log)
                        else:
                            print(f"   🩹 Reparando fallo puntual en {Path(path).name}...")
                            fixed_code = self.executor.run_task("heal_code", context_data=context, path=path, error_log=error_log[:2000], base_package=self.base_package)
                            self.save_to_disk(path, self.clean_llm_output(fixed_code))

            # FASE 4: CI/CD & OBSERVABILIDAD
            print("\n🐳 FASE 4: Generando Infraestructura, CI/CD y ELK...")
            # ... (Lógica de infraestructura igual) ...

            self.state.status = "COMPLETED"
            print(f"\n✨ --- PROYECTO FINALIZADO CON ÉXITO ---")

        except Exception as e:
            self.state.status = "FAILED"
            print(f"❌ Error Crítico: {e}")
            import traceback; traceback.print_exc()
        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout = sys.__stdout__
            if 'tee' in locals(): tee.close()

if __name__ == "__main__":
    input_idea = " ".join(sys.argv[1:]).strip()
    if not input_idea: sys.exit(1)
    factory = SoftwareFactory(input_idea)
    factory.run()