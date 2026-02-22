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
        pattern = r"```(?:java|javascript|jsx|yaml|json|xml|text)?\n?(.*?)\n?```"
        match = re.search(pattern, text, re.DOTALL)
        return match.group(1).strip() if match else text.strip()

    def run_command(self, cmd, cwd=None):
        try:
            res = subprocess.run(cmd, cwd=cwd or self.out_dir, capture_output=True, text=True, timeout=300)
            return None if res.returncode == 0 else res.stdout + res.stderr
        except Exception as e: return str(e)

    def sanitize_package_casing(self, content):
        lines = content.split('\n')
        for i, line in enumerate(lines):
            clean_line = line.strip()
            if clean_line.startswith('package '):
                lines[i] = line.lower()
            if clean_line.startswith(f'import {self.base_package.split(".")[0]}'):
                parts = line.split(' ')
                if len(parts) > 1:
                    path_parts = parts[1].split('.')
                    class_name = path_parts[-1].replace(';', '')
                    package_parts = [p.lower() for p in path_parts[:-1]]
                    lines[i] = f"import {'.'.join(package_parts)}.{class_name};"
        return '\n'.join(lines)

    def save_to_disk(self, relative_path, content):
        p = Path(relative_path)
        if "src/main/java" in str(p) or "src/test/java" in str(p):
            filename = p.name
            parent = str(p.parent).lower()
            full_path = self.out_dir / parent / filename
            content = self.sanitize_package_casing(content)
        else:
            full_path = self.out_dir / relative_path

        full_path.parent.mkdir(parents=True, exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f: f.write(content)
        
        if str(relative_path) not in self.state.generated_files:
            self.state.generated_files.append(str(relative_path))
            StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)

    def run_massive_healing(self, error_log):
        print("\n🚨 REPARACIÓN MASIVA: Sincronizando contratos y eliminando duplicados...")
        analysis = self.executor.run_task("project_debug", error_log=error_log[:4000], base_package=self.base_package)
        inventory = self.state.architecture.get("file_inventory", [])
        for f in [fi for fi in inventory if "valueobject" in fi['path'] or "shared" in fi['path']]:
            print(f"   🩹 Re-sincronizando: {Path(f['path']).name}")
            code = self.executor.run_task("write_code", context_data=f"ANALISIS_REPARACION: {analysis}", path=f['path'], desc=f['description'], base_package=self.base_package)
            self.save_to_disk(f['path'], self.clean_llm_output(code))

    def run(self):
        sys.stdout = Tee(self.out_dir / "execution.log")
        try:
            print(f"🚀 --- INICIANDO FACTORÍA INDUSTRIAL: {self.project_name.upper()} ---")
            if not (self.out_dir / ".git").exists(): self.run_command(["git", "init"])

            # FASE 1 & 2: DISEÑO
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 FASE 1: Inferencia de Dominio...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)
                print("\n📐 FASE 2: Diseño de Inventario...")
                res_arch = self.executor.run_task("create_inventory", context_data=self.state.domain_model, base_package=self.base_package, base_package_path=self.base_package_path)
                self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Diseño cargado desde caché.")

            inventory = self.state.architecture.get("file_inventory", [])
            inv_map = "\n".join([f"- {Path(f['path']).name}: {f['path']}" for f in inventory])

            # FASE: BOOTSTRAP (Cimientos inyectados con constructor flexible)
            print("\n🏗️  FASE: BOOTSTRAP (Shared Kernel)...")
            vo_path = f"backend/src/main/java/{self.base_package_path}/domain/shared/ValueObject.java"
            en_path = f"backend/src/main/java/{self.base_package_path}/domain/shared/Entity.java"
            
            if vo_path not in self.state.generated_files:
                self.save_to_disk(vo_path, f"package {self.base_package}.domain.shared;\nimport java.io.Serializable;\npublic interface ValueObject extends Serializable {{}}")
            if en_path not in self.state.generated_files:
                entity_content = f"""package {self.base_package}.domain.shared;
import lombok.Getter;
import java.util.Objects;

@Getter
public abstract class Entity<ID extends ValueObject> {{
    protected ID id;

    protected Entity() {{}} // Requerido para Lombok NoArgsConstructor en hijos

    protected Entity(ID id) {{
        this.id = Objects.requireNonNull(id, "The ID cannot be null");
    }}

    @Override
    public boolean equals(Object o) {{
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }}

    @Override
    public int hashCode() {{
        return Objects.hash(id);
    }}
}}"""
                self.save_to_disk(en_path, entity_content)

            # FASE 3: FABRICACIÓN
            print(f"\n🛠️  FASE 3: Fabricando {len(inventory)} archivos...")
            for file_info in inventory:
                path, desc = file_info['path'], file_info['description']
                if path in self.state.generated_files: continue
                
                if "sharedkernel" in path.lower() or "shared/valueobject" in path.lower(): continue

                print(f"   👉 {path}")
                agent = build_frontend_builder() if (".js" in path or ".jsx" in path) else build_backend_builder()
                ctx = f"BASE: {self.base_package}\nPAQUETE_SHARED: {self.base_package}.domain.shared\nMAP: {inv_map}\nDOMAIN: {self.state.domain_model}"
                
                code = self.clean_llm_output(self.executor.run_task("write_code", context_data=ctx, path=path, desc=desc, base_package=self.base_package))
                self.save_to_disk(path, code)

                if path.endswith(".java") and ("domain" in path or "application" in path):
                    # Generar Test
                    test_p = path.replace("src/main/java", "src/test/java").replace(".java", "Test.java")
                    t_code = self.clean_llm_output(self.executor.run_task("write_tests", context_data=ctx, path=path, base_package=self.base_package))
                    self.save_to_disk(test_p, t_code)

                    # Maven Check
                    err = self.run_command(["mvn", "clean", "compile", "test", "-DskipTests=false"], cwd=self.out_dir / "backend")
                    if err:
                        if err.count("[ERROR]") > 15: 
                            self.run_massive_healing(err)
                            continue
                        print(f"   🩹 Auto-Healing puntual...")
                        fixed = self.clean_llm_output(self.executor.run_task("heal_code", context_data=ctx, path=path, error_log=err[:2500], base_package=self.base_package))
                        self.save_to_disk(path, fixed)
                
                self.run_command(["git", "add", "."])
                self.run_command(["git", "commit", "-m", f"feat: add {Path(path).name}"])

            # FASE 4: INFRAESTRUCTURA
            print("\n🐳 FASE 4: Generando Infraestructura y CI/CD...")
            infra = [("backend/pom.xml", "POM con Spring Boot 3, JaCoCo y ELK."), (".github/workflows/pipeline.yml", "CI Pipeline."), ("docker-compose.yml", "ELK + DB.")]
            for p, d in infra:
                if p not in self.state.generated_files:
                    code = self.executor.run_task("write_code", context_data=self.state.domain_model, path=p, desc=d, base_package=self.base_package, agent_override="sre_agent")
                    self.save_to_disk(p, self.clean_llm_output(code))

            self.state.status = "COMPLETED"
            print(f"\n✨ PROYECTO FINALIZADO CON ÉXITO")

        except Exception as e:
            self.state.status = "FAILED"
            print(f"❌ Error: {e}"); import traceback; traceback.print_exc()
        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout.flush(); sys.stdout = sys.__stdout__
            if 'tee' in locals(): tee.close()

if __name__ == "__main__":
    idea = " ".join(sys.argv[1:]).strip()
    if idea: SoftwareFactory(idea).run()