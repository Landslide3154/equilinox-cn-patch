from PIL import Image

for name in ("gill3", "segoeUI"):
    img = Image.open(r"D:\code\equilinox\build\res\guis\fonts\%s.png" % name).convert("RGBA")
    w, h = img.size
    a = img.split()[3]
    px = a.load()
    # count isolated alpha pixels (neighbors all zero) - speck detector
    specks = 0
    for y in range(1, h - 1, 1):
        for x in range(1, w - 1, 1):
            v = px[x, y]
            if v > 0:
                nb = sum(1 for dx in (-1, 0, 1) for dy in (-1, 0, 1)
                         if (dx or dy) and px[x + dx, y + dy] > 0)
                if nb == 0:
                    specks += 1
    print(name, "atlas", w, "x", h, "isolated specks:", specks)
