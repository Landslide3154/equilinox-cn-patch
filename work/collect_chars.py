import io, re, os

base = r"D:\code\equilinox"

def load_original_ids(fnt):
    ids = set()
    with io.open(fnt, "r", encoding="utf-8") as f:
        for line in f:
            m = re.search(r"char id=(\d+)", line)
            if m:
                ids.add(int(m.group(1)))
    return ids

segoe_ids = load_original_ids(os.path.join(base, "jar", "res", "guis", "fonts", "segoeUI.fnt"))
gill_ids = load_original_ids(os.path.join(base, "jar", "res", "guis", "fonts", "gill3.fnt"))
print("segoeUI chars:", len(segoe_ids), "gill3 chars:", len(gill_ids))

# all text that will be rendered: translations + hardcoded strings (kept ASCII + patched Chinese)
texts = []
with io.open(os.path.join(base, "build", "res", "languageSheet.csv"), "r", encoding="utf-8") as f:
    for line in f:
        parts = line.split(";")
        if len(parts) >= 3 and parts[0].isdigit():
            texts.append(parts[2])
texts.append("Equilinox")
texts.append("dp")
texts.append("W,A,S,D")

all_chars = set()
for t in texts:
    for ch in t:
        all_chars.add(ord(ch))

missing_segoe = sorted(c for c in all_chars if c not in segoe_ids and c != 32)
missing_gill = sorted(c for c in all_chars if c not in gill_ids and c != 32)
print("chars used in translations:", len(all_chars))
print("missing from segoeUI:", len(missing_segoe))
print("missing from gill3:", len(missing_gill))

# chars that are NOT CJK but missing (should be few)
def is_cjk(c):
    return 0x4E00 <= c <= 0x9FFF or 0x3000 <= c <= 0x303F or 0xFF00 <= c <= 0xFFEF
non_cjk_missing = [hex(c) for c in missing_segoe if not is_cjk(c)]
print("non-CJK missing from segoe:", non_cjk_missing)
print("non-CJK missing from gill:", [hex(c) for c in missing_gill if not is_cjk(c)])

with io.open(os.path.join(base, "work", "missing_segoe.txt"), "w", encoding="utf-8") as f:
    f.write("".join(chr(c) for c in missing_segoe))
with io.open(os.path.join(base, "work", "missing_gill.txt"), "w", encoding="utf-8") as f:
    f.write("".join(chr(c) for c in missing_gill))
