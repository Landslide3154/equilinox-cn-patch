from PIL import Image

def pure_white_rows(path, x0=700, x1=1560, y0=330, y1=700):
    img = Image.open(path).convert("RGB")
    px = img.load()
    rows = []
    for y in range(y0, y1):
        cnt = 0
        for x in range(x0, x1):
            r, g, b = px[x, y]
            if r == 255 and g == 255 and b == 255:
                cnt += 1
        rows.append((y, cnt))
    return rows

for name, path in (("ORIG ", r"D:\code\equilinox\work\menu_orig.png"),
                   ("PATCH", r"D:\code\equilinox\work\menu_visual.png"),
                   ("FIXED", r"D:\code\equilinox\work\menu_fixed2.png")):
    rows = pure_white_rows(path)
    dense = [(y, c) for y, c in rows if c >= 3]
    if not dense:
        print(name, "no dense pure-white rows")
        continue
    # group into bands
    bands = []
    s = dense[0][0]
    prev = dense[0][0]
    for y, c in dense[1:]:
        if y - prev > 3:
            bands.append((s, prev))
            s = y
        prev = y
    bands.append((s, prev))
    print(name, "pure-white text bands (top,bottom):", bands[:8])
