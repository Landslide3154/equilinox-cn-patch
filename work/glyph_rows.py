from PIL import Image
import sys

def text_rows(path, x0=700, x1=1560, y0=330, y1=700):
    img = Image.open(path).convert("RGB")
    px = img.load()
    # collect candidate text pixels: white-ish AND notably brighter than surroundings
    pts = []
    for y in range(y0, y1, 1):
        for x in range(x0, x1, 1):
            r, g, b = px[x, y]
            if r > 235 and g > 235 and b > 235 and abs(r - g) < 12 and abs(g - b) < 12:
                pts.append((x, y))
    # group by connected rows (y bands where density high)
    from collections import Counter
    ys = Counter(y for x, y in pts)
    bands = []
    inb = False
    for y in range(y0, y1):
        c = ys.get(y, 0)
        if c > 10 and not inb:
            s = y
            inb = True
        elif c <= 10 and inb:
            if y - s > 5:
                bands.append((s, y))
            inb = False
    if inb:
        bands.append((s, y1))
    return bands

for name, path in (("ORIG ", r"D:\code\equilinox\work\menu_orig.png"),
                   ("PATCH", r"D:\code\equilinox\work\menu_visual.png"),
                   ("FIXED", r"D:\code\equilinox\work\menu_fixed.png")):
    bands = text_rows(path)
    print(name, "dense white rows:", bands)
