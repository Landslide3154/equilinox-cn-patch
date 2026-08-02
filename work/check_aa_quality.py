import re
from PIL import Image

def load(path):
    chars = {}
    for line in open(path, encoding="ascii", errors="replace"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
    return chars

chars = load(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")
A = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
pa = A.split()[3].load()

def edge_stats(cid):
    v = chars[cid]
    x, y, w, h = v["x"], v["y"], v["width"], v["height"]
    vals = []
    for yy in range(y, y + h):
        for xx in range(x, x + w):
            a = pa[xx, yy]
            if a > 0:
                vals.append(a)
    if not vals:
        return 0, 0, 0
    partial = sum(1 for a in vals if 0 < a < 255)
    solid = sum(1 for a in vals if a == 255)
    return len(vals), partial, solid

for ch in ("开", "始", "游", "你", "一"):
    total, partial, solid = edge_stats(ord(ch))
    print("%s: nonzero=%d antialiased(partial)=%d solid=%.0f%%" % (ch, total, partial, 100.0 * solid / max(total, 1)))
