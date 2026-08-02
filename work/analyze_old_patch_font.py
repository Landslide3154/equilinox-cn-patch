from PIL import Image
import re

old = r"D:\code\equilinox\Equilinox_CN_Patch\files\fonts"

# fnt header
for name in ("gill3.fnt", "segoeUI.fnt"):
    lines = open(old + "\\" + name, encoding="ascii", errors="replace").read().splitlines()
    print("=== %s ===" % name)
    for l in lines[:5]:
        print(l)
    print("char count:", sum(1 for l in lines if l.startswith("char id=")))

# atlas stats
for name in ("gill3.png", "segoeUI.png"):
    img = Image.open(old + "\\" + name).convert("RGBA")
    a = img.split()[3]
    hist = a.histogram()
    nz = sum(hist) - hist[0]
    maxa = max(i for i, c in enumerate(hist) if c > 0)
    print("%s: size=%s max_alpha=%d nonzero=%d" % (name, img.size, maxa, nz))
    for lo in (100, 120, 140, 170, 200, 250):
        print("  alpha>=%d: %d (%.1f%%)" % (lo, sum(hist[lo:]), 100.0 * sum(hist[lo:]) / max(nz, 1)))
