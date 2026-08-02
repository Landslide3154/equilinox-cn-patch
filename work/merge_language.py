import os, sys, io
import re

base = r"D:\code\equilinox"
orig = os.path.join(base, "jar", "res", "languageSheet.csv")
out = os.path.join(base, "build", "res", "languageSheet.csv")

translations = {}
for chunk in ["zh_1.tsv", "zh_2.tsv", "zh_3.tsv", "zh_4.tsv"]:
    with io.open(os.path.join(base, "work", chunk), "r", encoding="utf-8") as f:
        for line in f:
            line = line.rstrip("\r\n")
            if not line:
                continue
            parts = line.split("\t", 1)
            text = re.sub(r"\^([^^]*)\^", r"\1", parts[1])
            translations[int(parts[0])] = text

with io.open(orig, "r", encoding="utf-8", errors="replace") as f:
    lines = f.read().split("\r\n")
    if len(lines) == 1:
        lines = f.read().split("\n")
if len(lines) == 1:
    # re-read properly
    with io.open(orig, "r", encoding="utf-8", errors="replace", newline="") as f:
        text = f.read()
    lines = text.splitlines()

header = lines[0]
rows = []
missing = []
for line in lines[1:]:
    if not line.strip():
        continue
    parts = line.split(";")
    idv = int(parts[0])
    desc = parts[1]
    testval = parts[3] if len(parts) > 3 and parts[3] else "TestVal%d" % idv
    if idv not in translations:
        missing.append(idv)
        continue
    rows.append("%d;%s;%s;%s" % (idv, desc, translations[idv], testval))

if missing:
    print("MISSING TRANSLATIONS:", missing)
    sys.exit(1)

extra = set(translations.keys()) - set(int(r.split(";")[0]) for r in rows)
if extra:
    print("EXTRA TRANSLATIONS NOT IN ORIGINAL:", sorted(extra))

os.makedirs(os.path.dirname(out), exist_ok=True)
with io.open(out, "w", encoding="utf-8", newline="") as f:
    f.write(header + "\r\n")
    for r in rows:
        f.write(r + "\r\n")

print("Wrote %d rows -> %s" % (len(rows), out))
