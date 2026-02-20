# ai/utils/logging_helper.py
import sys

class Tee:
    def __init__(self, file_path):
        # Aseguramos que el directorio existe antes de abrir el archivo
        file_path.parent.mkdir(parents=True, exist_ok=True)
        self.file = open(file_path, "w", encoding="utf-8")
        self.stdout = sys.stdout

    def write(self, data):
        self.file.write(data)
        self.stdout.write(data)
        # FORZAR ESCRITURA EN DISCO EN TIEMPO REAL
        self.file.flush() 
        self.stdout.flush()

    def flush(self):
        self.file.flush()
        self.stdout.flush()

    def close(self):
        self.file.close()