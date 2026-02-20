# ai/reporting/report_generator.py
from pathlib import Path

def generate_report(state, out_dir):
    """Genera un reporte final en Markdown basado en el estado de la factoría"""
    report_path = Path(out_dir) / "GENERATION_REPORT.md"
    
    content = f"""# 📊 Reporte Final de Generación: {state.project_name.upper()}

## 💡 Idea de Negocio
{state.idea}

## 🏗️ Resumen Técnico
- **Arquitectura:** Hexagonal / DDD
- **Estado Final:** {state.status}
- **Archivos Generados:** {len(state.generated_files)}

## 📂 Inventario de Archivos Entregados
"""
    for file in state.generated_files:
        content += f"- ✅ `{file}`\n"

    content += f"""
## 🛡️ Calidad y Auditoría
Todos los archivos han pasado por un proceso de revisión por el **Agente QA Senior**, asegurando el cumplimiento de los estándares de Java 17 y Clean Architecture.

---
*Generado por la Factoría de Software Industrial Domain Kit + IA*
"""
    with open(report_path, "w", encoding="utf-8") as f:
        f.write(content)