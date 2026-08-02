from PIL import Image

def text_bands(path, x0, x1, y0, y1):
    img = Image.open(path).convert("RGB")
    px = img.load()
    # find dark text pixels: menu text is white, but panels are bright; use contrast:
    # text = pixels notably brighter than local background. Instead, detect rows where
    # there are strong vertical transitions (edges) - simple: pixels with low local contrast.
    rows = []
    for y in range(y0, y1):
        cnt = 0
        for x in range(x0, x1):
            p = px[x, y]
            if sum(p) > 700:
                cnt += 1
        rows.append(cnt)
    return rows

# First button row: from the 80-row ascii, panel corner at (960,475); button spans y ~475-540
for name, path in (("ORIG", r"D:\code\equilinox\work\menu_orig.png"),
                   ("PATCH", r"D:\code\equilinox\work\menu_visual.png")):
    rows = text_bands(path, 700, 1560, 450, 700)
    # find bands with >5 bright pixels
    bands = []
    inb = False
    for i, c in enumerate(rows):
        if c > 5 and not inb:
            s = i
            inb = True
        elif c <= 5 and inb:
            if i - s > 3:
                bands.append((450 + s, 450 + i))
            inb = False
    if inb:
        bands.append((450 + s, 450 + len(rows)))
    print(name, "text bands in y450-700:", bands[:10])
