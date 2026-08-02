from PIL import Image
import sys

def white_text_bands(path, x0=700, x1=1560, y0=330, y1=700):
    img = Image.open(path).convert("RGB")
    px = img.load()
    rows = []
    for y in range(y0, y1):
        cnt = 0
        for x in range(x0, x1):
            r, g, b = px[x, y]
            if r > 235 and g > 235 and b > 235:
                cnt += 1
        rows.append(cnt)
    bands = []
    inb = False
    for i, c in enumerate(rows):
        if c >= 2 and not inb:
            s = i
            inb = True
        elif c < 2 and inb:
            if i - s >= 2:
                bands.append((y0 + s, y0 + i))
            inb = False
    if inb:
        bands.append((y0 + s, y0 + len(rows)))
    return bands

for name, path in (("ORIG ", r"D:\code\equilinox\work\menu_orig.png"),
                   ("PATCH", r"D:\code\equilinox\work\menu_visual.png"),
                   ("FIXED", r"D:\code\equilinox\work\menu_fixed.png")):
    bands = white_text_bands(path)
    print(name, "white text bands:", bands[:10])
