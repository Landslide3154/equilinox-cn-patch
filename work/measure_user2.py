from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png").convert("RGB")
w, h = img.size
px = img.load()

# Panel = beige (warm light), background = dark. For each row, count beige and white pixels in x 0-360.
def beige(p):
    r, g, b = p
    return 90 < r < 125 and 85 < g < 115 and 60 < b < 95

def white(p):
    return p[0] > 240 and p[1] > 240 and p[2] > 240

rows = []
for y in range(0, h):
    be = sum(1 for x in range(10, 380, 2) if beige(px[x, y]))
    wh = sum(1 for x in range(0, 360, 2) if white(px[x, y]))
    rows.append((y, be, wh))

# button panel bands: >= 60 beige samples per row
bands = []
inb = False
for y, be, wh in rows:
    if be >= 80 and not inb:
        s = y
        inb = True
    elif be < 60 and inb:
        if y - s > 8:
            bands.append((s, y))
        inb = False
if inb:
    bands.append((s, h - 1))
print("panel bands (y0,y1):", bands)

# for each panel band, find the text (white) rows
for b0, b1 in bands:
    wrows = [y for y in range(b0, b1) if rows[y][2] >= 2]
    if wrows:
        t0, t1 = wrows[0], wrows[-1]
        panel_h = b1 - b0
        text_h = t1 - t0
        top_m = t0 - b0
        bot_m = b1 - t1
        print("panel y[%d,%d] h=%d | text y[%d,%d] h=%d | top=%d bottom=%d -> %s" % (
            b0, b1, panel_h, t0, t1, text_h, top_m, bot_m,
            "CENTERED" if abs(top_m - bot_m) <= 4 else ("HIGH by %dpx" % (bot_m - top_m) if bot_m > top_m else "LOW by %dpx" % (top_m - bot_m))))
