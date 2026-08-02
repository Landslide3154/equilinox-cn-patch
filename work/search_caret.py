import os

root = r"D:\code\equilinox\decompiled"
hits = []
for dirpath, dirs, files in os.walk(root):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        p = os.path.join(dirpath, fn)
        if any(x in p for x in (r"de\matthiasmann", r"org\lwjgl", r"com\jcraft")):
            continue
        try:
            lines = open(p, encoding="utf-8", errors="replace").read().splitlines()
        except Exception:
            continue
        for i, line in enumerate(lines, 1):
            if "94" in line or "caret" in line.lower() or "^" in line:
                hits.append("%s:%d: %s" % (os.path.relpath(p, root), i, line.strip()))

for h in hits:
    if h.count("^") >= 1 or "CARET" in h or "QUOTE" in h:
        print(h)
