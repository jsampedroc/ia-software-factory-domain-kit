# main.py
import os, sys, json, yaml, re, subprocess, time
from pathlib import Path
from datetime import datetime
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
        self.shared_types = {"enums": [], "value_objects": []}
        self.signatures = {} 
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
        
        self.SHARED_KERNEL_CONTEXT = f"Root Package: {self.base_package}. Flat Hexagonal Architecture enforced."

    def log_step(self, message):
        now = datetime.now().strftime("%H:%M:%S")
        print(f"[{now}] {message}")

    def clean_llm_output(self, text):
        if not text: return ""
        pattern = r"```(?:json|java|xml|text|python)?\n?(.*?)\n?```"
        match = re.search(pattern, text, re.DOTALL | re.IGNORECASE)
        content = match.group(1).strip() if match else text.strip()
        if any(ind in content for ind in ["package ", "public class ", "public record ", "<?xml"]): return content
        start_idx = min((content.find(c) for c in '[{' if content.find(c) != -1), default=-1)
        if start_idx != -1:
            try:
                decoder = json.JSONDecoder()
                obj, _ = decoder.raw_decode(content[start_idx:])
                return json.dumps(obj)
            except: pass
        return content

    def run_command(self, cmd, cwd=None):
        try:
            res = subprocess.run(cmd, cwd=cwd or self.out_dir, capture_output=True, text=True, timeout=300)
            if res.returncode == 0: return None
            return (res.stdout or "") + (res.stderr or "")
        except Exception as e: return f"Execution Error: {str(e)}"

    def find_test_path(self, filename):
        for root, _, files in os.walk(self.out_dir / "backend/src/test"):
            if filename in files:
                return str(Path(root) / filename).replace(str(self.out_dir) + "/", "")
        return None

    def extract_signatures(self, code, filename):
        """Extrae métodos Y CAMPOS para que el Auditor y los Mappers no fallen."""
        class_name = Path(filename).stem
        methods = re.findall(r'(?:public|protected)\s+[\w<>]+\s+(?!' + class_name + r')(\w+)\s*\(', code)
        fields = re.findall(r'private\s+[\w<>]+\s+(\w+)\s*;', code)
        self.state.signatures[class_name] = {
            "methods": list(set(methods)),
            "fields": list(set(fields))
        }

    def sanitize_java_code(self, content, relative_path):
        """Elite Firewall V12: Sincronización, SuperBuilder y Layer Guard Robusto."""
        filename = Path(relative_path).stem
        
        # 1. Sincronizar Clase <-> Archivo
        match = re.search(r'public\s+(?:class|interface|enum|record)\s+(\w+)', content)
        if match:
            ia_name = match.group(1)
            if ia_name != filename: content = re.sub(r'\b' + ia_name + r'\b', filename, content)

        # 2. Fix Herencia y Shadowing
        if "extends Entity" in content:
            content = re.sub(r'(private|protected|public)\s+\w+Id\s+id\s*;', '', content)
            content = content.replace("@Builder", "@SuperBuilder")
            if "experimental.SuperBuilder" not in content:
                content = content.replace("import lombok.Builder;", "import lombok.experimental.SuperBuilder;")

        # 3. Forzado de Contrato ValueObject (Records)
        if "valueobject" in str(relative_path).lower() or filename.endswith("Id"):
            if "implements ValueObject" not in content:
                content = re.sub(r'(\bpublic\s+(?:record|class)\s+\w+.*?)(\{)', r'\1 implements ValueObject \2', content)
            if "domain.shared.ValueObject" not in content:
                import_vo = f"import {self.base_package}.domain.shared.ValueObject;"
                if "package " in content: content = re.sub(r'(package\s+[\w\.]+;)', r'\1\n' + import_vo, content)

        # 4. Jakarta & Naming
        content = content.replace("javax.validation", "jakarta.validation").replace("javax.persistence", "jakarta.persistence")
        content = content.replace("RequestDTO", "Request").replace("ResponseDTO", "Response")
        
        # 5. Aplanar Imports
        content = re.sub(r'import ' + re.escape(self.base_package) + r'\.domain\.(?!model|shared|valueobject|repository|exception)\w+\.(\w+);', r'import ' + self.base_package + r'.domain.model.\1;', content)
        content = re.sub(r'import ' + re.escape(self.base_package) + r'\.application\.dto\.\w+\.(\w+);', r'import ' + self.base_package + r'.application.dto.\1;', content)

        # 6. Layer Guard Dinámico para Packages
        parts = str(relative_path).replace("backend/src/main/java/", "").replace("backend/src/test/java/", "").split('/')
        base_parts = [p for p in parts[:-1] if p]
        
        # Re-inyectar capas si faltan (com.app.[capa].modulo)
        project_slug_idx = -1
        for i, p in enumerate(base_parts):
            if p == self.project_slug: project_slug_idx = i; break
        
        if project_slug_idx != -1:
            target_idx = project_slug_idx + 1
            if "domain" not in base_parts and any(x in base_parts for x in ["model", "valueobject", "repository"]):
                base_parts.insert(target_idx, "domain")
            elif "application" not in base_parts and any(x in base_parts for x in ["service", "dto", "mapper"]):
                base_parts.insert(target_idx, "application")
        
        expected_package = ".".join(base_parts).lower()
        if "package " in content: content = re.sub(r'package\s+[\w\.]+;', f"package {expected_package};", content)
        else: content = f"package {expected_package};\n\n" + content
            
        return content.strip()

    def save_to_disk(self, relative_path, content, is_skeleton=False):
        p = Path(relative_path)
        path_str = str(p)
        skip = ['request', 'response', 'enums', 'patient', 'appointment', 'dentist', 'user', 'billing', 'invoice', 'treatment']
        for s in skip: path_str = path_str.replace(f"/{s}/", "/")
        p = Path(path_str)

        # No sanitizar clases base del Shared Kernel
        is_base_shared = "domain/shared" in str(p).lower()
        if p.suffix == ".java" and not is_skeleton and not is_base_shared:
            content = self.sanitize_java_code(content, p)
        
        full_path = self.out_dir / p
        full_path.parent.mkdir(parents=True, exist_ok=True)
        with open(full_path, "w", encoding="utf-8", newline='\n') as f: f.write(content.strip())
        
        if p.suffix == ".java" and "src/main/java" in str(p): self.extract_signatures(content, p.name)
        if not is_skeleton and str(p) not in self.state.generated_files:
            self.state.generated_files.append(str(p))
            StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)

    def run_maven_test(self, mode="compile"):
        backend_p = self.out_dir / "backend"
        if not (backend_p / "pom.xml").exists(): return "POM_MISSING"
        self.log_step(f"🔨 Running 'mvn {mode}'...")
        return self.run_command(["mvn", mode], cwd=backend_p)

    def run_systemic_repair(self):
        self.log_step("🤖 Entering PHASE 7: Systemic AI Repair Mode")
        for attempt in range(3):
            raw_log = self.run_maven_test(mode="test-compile")
            if not raw_log: self.log_step("✅ No systemic errors found."); return True
            self.log_step(f"❌ Errors detected ({len(raw_log)} chars). Drafting fix script...")
            script_code = self.clean_llm_output(self.executor.run_task("generate_systemic_fix", error_log=raw_log[:8000], base_path=str(self.out_dir / "backend/src/main/java")))
            if "import" in script_code or "os" in script_code:
                fix_path = self.out_dir / "fix_emergency.py"
                with open(fix_path, "w", encoding="utf-8") as f: f.write(script_code)
                self.run_command(["python3", "fix_emergency.py"], cwd=self.out_dir)
        return False

    def run(self):
        log_path = self.out_dir / "execution.log"
        tee_instance = Tee(log_path)
        sys.stdout = tee_instance 
        try:
            print("="*60); self.log_step(f"🚀 FACTORY ACTIVE: {self.project_name.upper()}"); print("="*60)
            if not (self.out_dir / ".git").exists(): self.run_command(["git", "init"])

            # 1. DESIGN & INVENTORY
            if not StateManager.load_specs(self.spec_file, self.state):
                self.log_step("🧠 PHASE 1: Domain Reasoning...")
                self.state.domain_model = self.executor.run_task("model_domain", idea=self.idea, base_package=self.base_package)
                self.log_step("🔍 PHASE 1.5: Shared Types Discovery...")
                raw_s = self.executor.run_task("identify_shared_types", domain_model=self.state.domain_model)
                try: self.state.shared_types = json.loads(self.clean_llm_output(raw_s))
                except: self.state.shared_types = {"enums": [], "value_objects": []}
                self.log_step("📐 PHASE 2: Inventory Design...")
                res_arch = self.executor.run_task("create_inventory", domain_kit=self.state.domain_model, base_package=self.base_package, base_package_path=self.base_package_path)
                try: self.state.architecture = {"file_inventory": json.loads(self.clean_llm_output(res_arch))}
                except: raise ValueError("Architect failed.")
                StateManager.save_specs(self.spec_file, self.state.domain_model, self.state.architecture, self.state.generated_files)
            else:
                self.log_step(f"✅ Design loaded from cache. Projected files: {len(self.state.architecture['file_inventory'])}")

            # 2. BOOTSTRAP
            self.log_step("🏗️  PHASE: BOOTSTRAP (Kernel)")
            pkg_p, base_p = self.base_package_path, self.base_package
            pom_code = self.executor.run_task("write_code", context_data=self.state.domain_model, path="backend/pom.xml", desc="STRICT XML: pom.xml Spring Boot 3.2", base_package=self.base_package, agent_override="sre_agent")
            self.save_to_disk("backend/pom.xml", self.clean_llm_output(pom_code))
            
            # Plantillas multilínea
            vo_code = f"package {base_p}.domain.shared;\n\npublic interface ValueObject extends java.io.Serializable {{\n}}"
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/ValueObject.java", vo_code, is_skeleton=True)
            self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/Entity.java", f"package {base_p}.domain.shared;\nimport lombok.*;\nimport lombok.experimental.SuperBuilder;\n@Getter @SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED) public abstract class Entity<ID extends ValueObject> {{ protected ID id; }}", is_skeleton=True)

            for en in self.state.shared_types.get('enums', []):
                self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/{en['name']}.java", f"package {base_p}.domain.shared;\npublic enum {en['name']} {{ {en['values']} }}")
            for vo in self.state.shared_types.get('value_objects', []):
                f_java = "".join([f"private {f.strip()}; " for f in vo['fields'].split(',')])
                self.save_to_disk(f"backend/src/main/java/{pkg_p}/domain/shared/{vo['name']}.java", f"package {base_p}.domain.shared;\nimport lombok.*;\n@Data @Builder @NoArgsConstructor @AllArgsConstructor\npublic class {vo['name']} {{ {f_java} }}")

            # 3. MANUFACTURING
            inventory = self.state.architecture.get("file_inventory", [])
            for idx, f in enumerate(inventory, 1):
                path = f['path']
                if path in self.state.generated_files and (self.out_dir / path).exists():
                    # RE-CARGAR FIRMAS SI SALTÁRAMOS EL ARCHIVO
                    with open(self.out_dir / path, 'r') as existing_f: self.extract_signatures(existing_f.read(), Path(path).name)
                    continue
                if any(x in path.lower() for x in ["shared", "mapper", "test"]): continue
                self.log_step(f"   [{idx}/{len(inventory)}] | File: {Path(path).name}")
                code = self.clean_llm_output(self.executor.run_task("write_code", context_data=self.state.domain_model, path=path, desc=f['description'], base_package=self.base_package))
                self.save_to_disk(path, code)

            # 4. MAPPERS, 5. TESTS, 6. REPAIR
            for f in [x for x in inventory if "mapper" in x['path'].lower()]:
                if f['path'] in self.state.generated_files and (self.out_dir / f['path']).exists():
                    with open(self.out_dir / f['path'], 'r') as ex_f: self.extract_signatures(ex_f.read(), Path(f['path']).name)
                    continue
                eb = Path(f['path']).stem.replace("Mapper", "")
                code = self.clean_llm_output(self.executor.run_task("write_mapper", path=f['path'], entity_name=eb, dto_name=eb, base_package=self.base_package))
                self.save_to_disk(f['path'], code)

            for f in [x for x in inventory if any(y in x['path'].lower() for y in ["service", "rest"])]:
                tp = f['path'].replace("src/main/java", "src/test/java").replace(".java", "Test.java")
                if tp in self.state.generated_files and (self.out_dir / tp).exists(): continue
                if not (self.out_dir / f['path']).exists(): continue
                with open(self.out_dir / f['path'], 'r') as src_f: src_c = src_f.read()
                t_code = self.clean_llm_output(self.executor.run_task("write_tests", path=tp, base_package=self.base_package, context_data=src_c))
                self.save_to_disk(tp, t_code)

            self.run_systemic_repair()

            # 7. FINAL AUDIT
            self.log_step("🔍 PHASE 6: Final Elite Audit")
            for attempt in range(4):
                full_err = self.run_maven_test(mode="test-compile")
                if not full_err: break
                broken = list(set(re.findall(r"([a-zA-Z0-9]+\.java)", full_err)))
                self.log_step(f"   ⚠️ Auditor fixing {len(broken)} files (Attempt {attempt+1}).")
                for fname in broken[:30]:
                    p = next((f['path'] for f in inventory if Path(f['path']).name == fname), self.find_test_path(fname))
                    if not p: continue
                    with open(self.out_dir / p, 'r') as f: cur = f.read()
                    fix = self.clean_llm_output(self.executor.run_task("audit_code", path=p, content=cur, error_log=full_err, global_context=json.dumps(self.state.signatures), base_package=self.base_package))
                    self.save_to_disk(p, fix)
            self.log_step("✨ PROJECT FINISHED SUCCESSFULY")

        except Exception as e:
            self.log_step(f"❌ CRITICAL ERROR: {e}"); import traceback; traceback.print_exc()
        finally:
            if tee_instance: 
                sys.stdout.flush(); sys.stdout = sys.__stdout__
                time.sleep(0.1); tee_instance.close()

if __name__ == "__main__":
    input_idea = " ".join(sys.argv[1:]).strip()
    if input_idea: SoftwareFactory(input_idea).run()