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
        low = text.lower()
        if "marker" in low or "highlight" in low or ("colour" in low and "split" in low) or "textcolour" in low:
            hits.append(p)

for p in sorted(set(hits)):
    print(os.path.relpath(p, root))
