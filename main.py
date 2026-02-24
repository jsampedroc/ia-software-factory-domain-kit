# main.py
import os, sys, json, yaml, re, subprocess, time
from pathlib import Path
from dotenv import load_dotenv

# Helpers & Infrastructure
from ai.pipeline.state_manager import StateManager
from ai.pipeline.task_executor import TaskExecutor
from ai.utils.logging_helper import Tee
from ai.reporting.report_generator import generate_report

# Modular Agents & Tasks
import ai.agents as agents
import ai.tasks as tasks

class PipelineState:
    def __init__(self, idea, project_name):
        self.idea, self.project_name = idea, project_name
        self.domain_model, self.architecture, self.generated_files = None, None, []
        self.status, self.start_time = "IN_PROGRESS", time.time()

class SoftwareFactory:
    def __init__(self, idea):
        load_dotenv()
        self.idea, self.executor = idea, TaskExecutor()
        
        try:
            with open("config/architecture.yaml", "r") as f:
                self.arch_config = yaml.safe_load(f)
        except FileNotFoundError:
            print("❌ Error: config/architecture.yaml not found."); sys.exit(1)
        
        # 1. AGNOSTIC NAMING & PACKAGE PROCESSING
        self.project_name = self.arch_config['project']['name']
        self.project_slug = re.sub(r'[^a-zA-Z0-9]', '', self.project_name.lower())
        self.base_package = f"com.{self.project_slug}"
        self.base_package_path = self.base_package.replace('.', '/')
        
        self.out_dir = Path("outputs") / self.project_slug
        self.spec_file = Path("specs") / f"{self.project_slug}.json"
        
        self.out_dir.mkdir(parents=True, exist_ok=True); Path("specs").mkdir(exist_ok=True)
        self.state = PipelineState(self.idea, self.project_name)
        
        # ELITE INJECTION CONTEXT (English for better AI reasoning)
        self.SHARED_KERNEL_CONTEXT = f"""
        MANDATORY PROJECT STRUCTURE & BASE CLASSES:
        - Root Package: {self.base_package}
        - Shared Kernel Classes (USE THESE FOR IMPORTS):
          1. interface {self.base_package}.domain.shared.ValueObject
          2. abstract class {self.base_package}.domain.shared.Entity<ID extends ValueObject>
          3. interface {self.base_package}.domain.shared.EntityRepository<T, ID extends ValueObject>
          4. class {self.base_package}.domain.exception.DomainException
        """

    def clean_llm_output(self, text):
        pattern = r"```(?:java|javascript|jsx|yaml|json|xml|text)?\n?(.*?)\n?```"
        match = re.search(pattern, text, re.DOTALL)
        return match.group(1).strip() if match else text.strip()

    def sanitize_java_code(self, content):
        """ELITE SYNTAX CORRECTOR: Enforces Java 17 standards and package consistency."""
        lines = content.split('\n')
        for i, line in enumerate(lines):
            clean_l = line.strip()
            if clean_l.startswith('package '): lines[i] = clean_l.lower()
            if clean_l.startswith(f'import {self.base_package.split(".")[0]}'):
                parts = clean_l.split(' ')
                if len(parts) > 1:
                    path_parts = parts[1].split('.')
                    class_name = path_parts[-1].replace(';', '')
                    package_parts = [p.lower() for p in path_parts[:-1]]
                    lines[i] = f"import {'.'.join(package_parts)}.{class_name};"
        
        full_content = '\n'.join(lines)
        # Fix heritage: records/classes must implement interfaces
        full_content = re.sub(r'(class|record)\s+(\w+)\s+extends\s+ValueObject', r'\1 \2 implements ValueObject', full_content, flags=re.IGNORECASE)
        # Restore generics: In Java diamonds <>, 'extends' is mandatory even for interfaces
        full_content = full_content.replace('<ID implements ValueObject>', '<ID extends ValueObject>')
        full_content = full_content.replace('ID implements ValueObject', 'ID extends ValueObject')
        # Force Entity inheritance
        full_content = re.sub(r'implements\s+Entity', 'extends Entity', full_content, flags=re.IGNORECASE)
        return full_content

    def save_to_disk(self, relative_path, content, is_skeleton=False):
        p = Path(relative_path)
        if "src/main/java" in str(p) or "src/test/java" in str(p):
            filename = p.name
            parent = str(p.parent).lower()
            full_path = self.out_dir / parent / filename
            content = self.sanitize_java_code(content)
        else:
            full_path = self.out_dir / relative_path

        full_path.parent.mkdir(parents=True, exist_ok=True)
        with open(full_path, "w", encoding="utf-8") as f: f.write(content)
        
        # Only mark as 'completed' if it's NOT a skeleton pass
        if not is_skeleton and str(relative_path) not in self.state.generated_files:
            self.state.generated_files.append(str(relative_path))
            StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)

    def run_command(self, cmd, cwd=None):
        try:
            res = subprocess.run(cmd, cwd=cwd or self.out_dir, capture_output=True, text=True, timeout=300)
            return None if res.returncode == 0 else res.stdout + res.stderr
        except Exception as e: return str(e)

    def run_maven_test(self):
        backend_path = self.out_dir / "backend"
        if not (backend_path / "pom.xml").exists(): return "POM_MISSING"
        return self.run_command(["mvn", "clean", "compile", "test", "-DskipTests=false"], cwd=backend_path)

    def recalibrate_factory(self):
        print("\n🔍 RECALIBRANDO: Purgando archivos con errores sintácticos...")
        error_log = self.run_maven_test()
        if error_log:
            culprits = list(set(re.findall(r"([a-zA-Z0-9]+\.java)", error_log)))
            if culprits:
                print(f"🛠️  Borrando {len(culprits)} archivos para regeneración limpia.")
                self.state.generated_files = [f for f in self.state.generated_files if not any(c in f for c in culprits)]
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
                return True
        return False

    def run_massive_healing(self, error_log):
        print("\n🚨 REPARACIÓN MASIVA: Sincronizando contratos estructurales...")
        analysis = self.executor.run_task("project_debug", error_log=error_log[:4000], base_package=self.base_package)
        inventory = self.state.architecture.get("file_inventory", [])
        for f in [fi for fi in inventory if "valueobject" in fi['path'] or "shared" in fi['path']]:
            code = self.executor.run_task("write_code", context_data=f"ARCHITECTURAL_REPAIR_PLAN: {analysis}", path=f['path'], desc=f['description'], base_package=self.base_package)
            self.save_to_disk(f['path'], self.clean_llm_output(code))

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee_instance = Tee(log_path)
        sys.stdout = tee_instance 
        try:
            print(f"🚀 --- FACTORÍA ELITE ACTIVA: {self.project_name.upper()} ---")
            if not (self.out_dir / ".git").exists(): self.run_command(["git", "init"])

            # 1. DESIGN PHASES
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 FASE 1: Inferencia de Dominio...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)
                print("\n📐 FASE 2: Diseño de Inventario...")
                res_arch = self.executor.run_task("create_inventory", context_data=self.state.domain_model, base_package=self.base_package, base_package_path=self.base_package_path)
                self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Diseño cargado desde caché."); self.recalibrate_factory()

            inventory = self.state.architecture.get("file_inventory", [])
            inv_map = "\n".join([f"- {Path(f['path']).name}: {f['path']}" for f in inventory])

            # 2. BOOTSTRAP (Maven & Shared Kernel)
            print("\n🏗️  FASE: BOOTSTRAP (Infrastructure & Kernel)...")
            pkg_p, base_p = self.base_package_path, self.base_package
            pom_path = "backend/pom.xml"
            if not (self.out_dir / pom_path).exists():
                code_pom = self.executor.run_task("write_code", context_data=self.state.domain_model, path=pom_path, desc="Maven POM with Lombok and JaCoCo", base_package=self.base_package, agent_override="sre_agent")
                self.save_to_disk(pom_path, self.clean_llm_output(code_pom), is_skeleton=False)

            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/ValueObject.java", f"package {base_p}.domain.shared;\nimport java.io.Serializable;\npublic interface ValueObject extends Serializable {{}}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/Entity.java", f"package {base_p}.domain.shared;\nimport lombok.*;\nimport java.util.Objects;\n@Getter @NoArgsConstructor(access=AccessLevel.PROTECTED)\npublic abstract class Entity<ID extends ValueObject> {{ protected ID id; protected Entity(ID id) {{ this.id = Objects.requireNonNull(id); }} }}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/EntityRepository.java", f"package {base_p}.domain.shared;\nimport java.util.*;\npublic interface EntityRepository<T extends Entity<ID>, ID extends ValueObject> {{ T save(T entity); Optional<T> findById(ID id); List<T> findAll(); void delete(T entity); boolean existsById(ID id); }}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/exception/DomainException.java", f"package {base_p}.domain.exception;\npublic class DomainException extends RuntimeException {{ public DomainException(String m) {{ super(m); }} }}", is_skeleton=False)

            # 3. PASS 1: SCAFFOLDING (Skeletons)
            print(f"\n🦴 PASADA 1: Creando esqueletos físicos de {len(inventory)} archivos...")
            for file_info in inventory:
                path = file_info['path']
                if not path.endswith(".java") or "shared" in path.lower() or "exception" in path.lower(): continue
                if not (self.out_dir / path).exists():
                    sk_code = self.clean_llm_output(self.executor.run_task("create_skeleton", path=path, name=Path(path).stem, base_package=self.base_package))
                    self.save_to_disk(path, sk_code, is_skeleton=True)

            # 4. PASS 2: LOGIC + TESTS + HEALING + GIT
            print(f"\n🧠 PASADA 2: Inyectando lógica en los archivos...")
            for file_info in inventory:
                path, desc = file_info['path'], file_info['description']
                if path in self.state.generated_files or (self.out_dir / path).exists() and "record" in open(self.out_dir / path.lower() if "src/main" in path else self.out_dir / path).read(): 
                    # Pequeña validación: si el archivo ya tiene lógica (no es solo llaves), saltar
                    if path in self.state.generated_files: continue

                print(f"   👉 Generando: {path}")
                agent_role = "frontend_builder" if (".js" in path or ".jsx" in path) else "backend_builder"
                ctx = f"{self.SHARED_KERNEL_CONTEXT}\nPROJECT_FILE_MAP:\n{inv_map}\nBUSINESS_DOMAIN:\n{self.state.domain_model}"
                
                # Logic Injection
                code = self.clean_llm_output(self.executor.run_task("write_code", context_data=ctx, path=path, desc=desc, base_package=self.base_package, agent_override=agent_role))
                self.save_to_disk(path, code, is_skeleton=False)

                # Tests & Auto-Healing
                if path.endswith(".java") and ("domain" in path or "application" in path):
                    test_p = path.replace("src/main/java", "src/test/java").replace(".java", "Test.java")
                    t_ctx = f"{ctx}\n\nREAL_SOURCE_CODE_FOR_TESTING:\n{code}"
                    t_code = self.clean_llm_output(self.executor.run_task("write_tests", context_data=t_ctx, path=path, base_package=self.base_package))
                    self.save_to_disk(test_p, t_code, is_skeleton=False)

                    err = self.run_maven_test()
                    if err:
                        if err.count("[ERROR]") > 15: self.run_massive_healing(err); continue 
                        print(f"   🩹 Auto-Healing..."); fixed = self.clean_llm_output(self.executor.run_task("heal_code", context_data=t_ctx, path=path, error_log=err[:2500], base_package=self.base_package))
                        self.save_to_disk(path, fixed, is_skeleton=False)
                
                self.run_command(["git", "add", "."]); self.run_command(["git", "commit", "-m", f"feat: implemented {Path(path).name}"])

            # 5. SYNC FINAL
            print("\n✨ PROYECTO FINALIZADO CON ÉXITO")
            remote_url = os.getenv("GITHUB_REMOTE_URL")
            if remote_url: 
                self.run_command(["git", "remote", "add", "origin", remote_url])
                self.run_command(["git", "push", "-u", "origin", "main", "--force"])

        except Exception as e:
            print(f"❌ Error: {e}"); import traceback; traceback.print_exc()
        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout.flush(); sys.stdout = sys.__stdout__
            if tee_instance: tee_instance.close()

if __name__ == "__main__":
    idea = " ".join(sys.argv[1:]).strip()
    if idea: SoftwareFactory(idea).run()
    else: print("❌ Error: Please provide a business idea.")