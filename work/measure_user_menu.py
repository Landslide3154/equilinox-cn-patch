from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png").convert("RGB")
w, h = img.size
px = img.load()
print("image size:", w, h)

# The menu buttons: find horizontal panel edges. Panel background is light beige;
# find rows where a long horizontal run has consistent color.
# First, characterize: sample colors in button 1 area (left side, ~1/3 down)

# Detect text: pure white or near-white pixels
white = []
for y in range(h):
    for x in range(w):
        r, g, b = px[x, y]
        if r > 240 and g > 240 and b > 240:
            white.append((x, y))
print("white px:", len(white))

# text rows histogram
from collections import Counter
ys = Counter(y for x, y in white)
bands = []
inb = False
for y in range(h):
    c = ys.get(y, 0)
    if c > 15 and not inb:
        s = y
        inb = True
    elif c <= 15 and inb:
        if y - s > 4:
            bands.append((s, y))
        inb = False
if inb:
    bands.append((s, h - 1))
print("text bands:", bands)

# button panel edges: rows where many pixels differ from background — use saturation/color:
# panels are beige: r>200, 190<g<230, 150<b<210 roughly
def beige(p):
    r, g, b = p
    return 190 < r < 250 and 175 < g < 240 and 140 < b < 220 and r > g > b

row_beige = []
for y in range(h):
    cnt = sum(1 for x in range(0, w, 4) if beige(px[x, y]))
    row_beige.append(cnt)
# find long beige runs (panel rows)
panels = []
inb = False
for y in range(h):
    if row_beige[y] > w // 8 and not inb:
        s = y
        inb = True
    elif row_beige[y] <= w // 8 and inb:
        if y - s > 10:
            panels.append((s, y))
        inb = False
if inb:
    panels.append((s, h - 1))
print("beige panel bands:", panels)
