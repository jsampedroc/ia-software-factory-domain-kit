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
            with open("config/architecture.yaml", "r") as f: self.arch_config = yaml.safe_load(f)
        except FileNotFoundError:
            print("❌ ERROR: config/architecture.yaml not found."); sys.exit(1)
        
        self.project_name = self.arch_config['project']['name']
        self.project_slug = re.sub(r'[^a-zA-Z0-9]', '', self.project_name.lower())
        self.base_package = f"com.{self.project_slug}"
        self.base_package_path = self.base_package.replace('.', '/')
        self.out_dir = Path("outputs") / self.project_slug
        self.spec_file = Path("specs") / f"{self.project_slug}.json"
        self.out_dir.mkdir(parents=True, exist_ok=True); Path("specs").mkdir(exist_ok=True)
        self.state = PipelineState(self.idea, self.project_name)
        
        # MEMORIA DE SESIÓN PARA EVITAR BUCLES
        self.healed_in_this_session = set() 
        
        self.SHARED_KERNEL_CONTEXT = f"""
        CORE ARCHITECTURAL CONTRACTS:
        - Root Package: {self.base_package}
        - Base Classes MUST be imported from: {self.base_package}.domain.shared
        - ValueObjects: Records implementing ValueObject.
        - Entities: Classes extending Entity<ID>.
        """

    def clean_llm_output(self, text):
        pattern = r"```(?:java|javascript|jsx|yaml|json|xml|text)?\n?(.*?)\n?```"
        match = re.search(pattern, text, re.DOTALL)
        return match.group(1).strip() if match else text.strip()

    def sanitize_java_code(self, content):
        """Elite syntax sanitizer using Regex to prevent common AI hallucinations."""
        content = content.replace(f"{self.base_package}.domain.base", f"{self.base_package}.domain.shared")
        content = content.replace(f"{self.base_package}.domain.common", f"{self.base_package}.domain.shared")
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
        full_content = re.sub(r'(class|record)\s+(\w+)\s+extends\s+ValueObject', r'\1 \2 implements ValueObject', full_content, flags=re.IGNORECASE)
        full_content = full_content.replace('<ID implements ValueObject>', '<ID extends ValueObject>')
        full_content = full_content.replace('ID implements ValueObject', 'ID extends ValueObject')
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
        if not is_skeleton and str(relative_path) not in self.state.generated_files:
            self.state.generated_files.append(str(relative_path))
            StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)

    def run_command(self, cmd, cwd=None):
        try:
            res = subprocess.run(cmd, cwd=cwd or self.out_dir, capture_output=True, text=True, timeout=300)
            return None if res.returncode == 0 else res.stdout + res.stderr
        except Exception as e: return str(e)

    def run_maven_test(self):
        """Runs Maven and filters for errors. Optimized to be used in healing loops."""
        backend_path = self.out_dir / "backend"
        if not (backend_path / "pom.xml").exists(): return "POM_MISSING"
        # We use mvn compile to avoid the overhead of full clean every time in loops
        raw_log = self.run_command(["mvn", "compile"], cwd=backend_path)
        if raw_log:
            error_lines = [line for line in raw_log.split('\n') if "[ERROR]" in line]
            return "\n".join(error_lines) if error_lines else None
        return None

    def run_massive_healing(self, error_log):
        """Surgical repair using the Arbiter, avoiding infinite loops."""
        print("\n🚨 MASSIVE HEALING: Principal Architect intervening...")
        culprits = list(set(re.findall(r"([a-zA-Z0-9]+\.java)", error_log)))
        if not culprits: return

        inventory = self.state.architecture.get("file_inventory", [])
        # Only heal files that haven't been healed in this session yet
        to_heal = [f for f in inventory if Path(f['path']).name in culprits 
                   and Path(f['path']).name not in self.healed_in_this_session][:3]
        
        if not to_heal:
            print("   ⚠️ All detected culprits were already processed. Breaking loop to avoid recursion.")
            return

        for f in to_heal:
            fname = Path(f['path']).name
            print(f"   🩹 [Arbiter] Rescuing: {fname}")
            self.healed_in_this_session.add(fname)
            code = self.executor.run_task(
                "arbitration", 
                context_data=f"THIS FILE IS BROKEN. FIX IMPORTS AND PACKAGE: {self.base_package}",
                path=f['path'], 
                error_log=error_log[:2000],
                base_package=self.base_package
            )
            self.save_to_disk(f['path'], self.clean_llm_output(code), is_skeleton=False)

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee_instance = Tee(log_path)
        sys.stdout = tee_instance 
        try:
            print("="*60)
            print(f"🚀 ELITE FACTORY ACTIVE: {self.project_name.upper()}")
            print(f"📦 BASE PACKAGE: {self.base_package}")
            print("="*60)

            if not (self.out_dir / ".git").exists(): self.run_command(["git", "init"])

            # 1. DESIGN & INVENTORY
            if not StateManager.load_specs(self.spec_file, self.state):
                print("\n🧠 PHASE 1: Domain Reasoning...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)
                print("\n📐 PHASE 2: Architecture Inventory...")
                res_arch = self.executor.run_task("create_inventory", context_data=self.state.domain_model, base_package=self.base_package, base_package_path=self.base_package_path)
                self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                print(f"✅ Design loaded from cache.")

            inventory = self.state.architecture.get("file_inventory", [])
            import_map = "\n".join([f"Class: {Path(f['path']).stem} -> Import: {f['path'].replace('backend/src/main/java/', '').replace('/', '.').replace('.java', '')}" for f in inventory])

            # 2. BOOTSTRAP (Shared Kernel)
            print("\n🏗️  PHASE: BOOTSTRAP (Infrastructure)...")
            pkg_p, base_p = self.base_package_path, self.base_package
            pom_path = "backend/pom.xml"
            if not (self.out_dir / pom_path).exists():
                code_pom = self.executor.run_task("write_code", context_data=self.state.domain_model, path=pom_path, desc="Maven POM with JaCoCo", base_package=self.base_package, agent_override="sre_agent")
                self.save_to_disk(pom_path, self.clean_llm_output(code_pom), is_skeleton=False)

            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/ValueObject.java", f"package {base_p}.domain.shared;\nimport java.io.Serializable;\npublic interface ValueObject extends Serializable {{}}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/Entity.java", f"package {base_p}.domain.shared;\nimport lombok.*;\nimport java.util.Objects;\nimport lombok.experimental.SuperBuilder;\n@Getter @SuperBuilder @NoArgsConstructor(access=AccessLevel.PROTECTED)\npublic abstract class Entity<ID extends ValueObject> {{ protected ID id; protected Entity(ID id) {{ this.id = id; }} }}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/EntityRepository.java", f"package {base_p}.domain.shared;\nimport java.util.*;\npublic interface EntityRepository<T extends Entity<ID>, ID extends ValueObject> {{ T save(T entity); Optional<T> findById(ID id); List<T> findAll(); void delete(T entity); boolean existsById(ID id); }}", is_skeleton=False)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/exception/DomainException.java", f"package {base_p}.domain.exception;\npublic class DomainException extends RuntimeException {{ public DomainException(String m) {{ super(m); }} }}", is_skeleton=False)

            # 3. MANUFACTURING PASS 1 (Skeletons)
            print(f"\n🦴 PASS 1: Creating skeletons for {len(inventory)} files...")
            for file_info in inventory:
                path = file_info['path']
                if not path.endswith(".java") or "shared" in path.lower(): continue
                if not (self.out_dir / path).exists():
                    sk_code = self.clean_llm_output(self.executor.run_task("create_skeleton", path=path, name=Path(path).stem, base_package=self.base_package))
                    self.save_to_disk(path, sk_code, is_skeleton=True)

            # 4. MANUFACTURING PASS 2 (Logic + Healing + Git)
            print(f"\n🧠 PASS 2: Business Logic Injection...")
            for index, file_info in enumerate(inventory, 1):
                path, desc = file_info['path'], file_info['description']
                if path in self.state.generated_files or "shared" in path.lower() or "pom.xml" in path: continue

                print(f"   [{index}/{len(inventory)}] 👉 Generating: {path}")
                ctx = f"{self.SHARED_KERNEL_CONTEXT}\nIMPORT_MAP:\n{import_map}\nDOMAIN_KIT:\n{self.state.domain_model}"
                
                # Logic Injection
                code = self.clean_llm_output(self.executor.run_task("write_code", context_data=ctx, path=path, desc=desc, base_package=self.base_package))
                self.save_to_disk(path, code, is_skeleton=False)

                # HEALING LOOP
                err = self.run_maven_test()
                for attempt in range(2): # Solo 2 intentos de cura individual
                    if not err: break
                    print(f"      🩹 Individual Healing attempt {attempt + 1}...")
                    code = self.clean_llm_output(self.executor.run_task("heal_code", context_data=code, error_log=err, path=path, base_package=self.base_package))
                    self.save_to_disk(path, code, is_skeleton=False)
                    err = self.run_maven_test()
                
                # REPARACIÓN MASIVA SI HAY CAOS
                if err and err.count("[ERROR]") > 10:
                    self.run_massive_healing(err)
                
                self.run_command(["git", "add", "."]); self.run_command(["git", "commit", "-m", f"feat: add {Path(path).name}"])

            # 5. SYNC FINAL
            print("\n✨ PROJECT FINISHED SUCCESSFULY")
            remote_url = os.getenv("GITHUB_REMOTE_URL")
            if remote_url: self.run_command(["git", "remote", "add", "origin", remote_url]); self.run_command(["git", "push", "-u", "origin", "main", "--force"])

        except Exception as e:
            self.state.status = "FAILED"
            print(f"❌ Error: {e}"); import traceback; traceback.print_exc()
        finally:
            generate_report(self.state, self.out_dir)
            sys.stdout.flush(); sys.stdout = sys.__stdout__
            if tee_instance: tee_instance.close()

if __name__ == "__main__":
    input_idea = " ".join(sys.argv[1:]).strip()
    if input_idea: SoftwareFactory(input_idea).run()
    else: print("❌ Error: Please provide a business idea.")