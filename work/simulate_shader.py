from PIL import Image

# Simulate the game's font shader for a menu-size text (edge ~0.46, antialias ~0.1)
def shader(dist, edge_x=0.46, edge_y=0.1):
    def ss(e0, e1, x):
        t = (x - e0) / (e1 - e0) if e1 != e0 else 0.0
        t = max(0.0, min(1.0, t))
        return t * t * (3.0 - 2.0 * t)
    alpha = ss((1 - edge_x) - edge_y, 1 - edge_x, dist)
    outline = ss(1.0, 1.0, dist)  # degenerate when dist == 1.0
    overall = alpha + (1 - alpha) * outline
    return alpha, outline, overall

for name in ("gill3", "segoeUI"):
    img = Image.open(r"D:\code\equilinox\build\res\guis\fonts\%s.png" % name).convert("RGBA")
    a = img.split()[3]
    n = a.width * a.height
    dists = [v / 255.0 for v in a.getdata()]
    degenerate = sum(1 for d in dists if d == 1.0)
    nan_risk = sum(1 for d in dists if d >= 0.999)
    print("%s: pixels=%d, dist==1.0: %d, dist>=0.999: %d" % (name, n, degenerate, nan_risk))

# render a sample glyph through the shader: 你 from gill3
import re
fnt = open(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt", encoding="ascii").read()
m = re.search(r"char id=20320   x=(\d+)     y=(\d+)     width=(\d+)     height=(\d+)", fnt)
cx, cy, cw, chh = [int(v) for v in m.groups()]
img = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
pa = img.split()[3].load()
out = []
for yy in range(cy, cy + chh):
    row = []
    for xx in range(cx, cx + cw):
        d = pa[xx, yy] / 255.0
        alpha, outline, overall = shader(d)
        vis = alpha > 0.05
        row.append("#" if overall > 0.5 else ("+" if alpha > 0.2 else ("-" if alpha > 0 else ".")))
    out.append("".join(row))
print("simulated 你 rendering through game shader:")
print("\n".join(out))
