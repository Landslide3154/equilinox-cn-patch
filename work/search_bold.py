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
            text = open(p, encoding="utf-8", errors="replace").read()
        except Exception:
            continue
        for i, line in enumerate(text.splitlines(), 1):
            if "makeBold" in line or "setBorder(" in line or "setGlowing(" in line or "solidBorder" in line:
                hits.append("%s:%d: %s" % (os.path.relpath(p, root), i, line.strip()))

for h in hits:
    print(h)
