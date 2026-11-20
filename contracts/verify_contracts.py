from pathlib import Path
import hashlib
import json

root = Path(__file__).resolve().parent
manifest = json.loads((root / "manifest.json").read_text(encoding="utf-8"))
problems = []
for relative, expected in manifest["sha256"].items():
    target = root / relative
    if not target.is_file():
        problems.append("MISSING " + relative)
    elif hashlib.sha256(target.read_bytes().replace(b"\r\n", b"\n")).hexdigest() != expected:
        problems.append("CHANGED " + relative)
if problems:
    raise SystemExit("\n".join(problems))
print("Contract " + manifest["contractVersion"] + ": OK")
