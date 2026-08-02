from PIL import Image

img = Image.open(r"D:\code\equilinox\work\menu_visual.png").convert("RGB")
w, h = img.size
px = img.load()
print("size:", w, h)

# Menu buttons: DnaButton background (dnaButton.png) is a rounded rect in beige/green.
# Find bright (white) text pixels: sum(RGB) high.
def bright(p):
    return sum(p) > 560

# Collect white pixel positions
white = []
for y in range(0, h, 2):
    for x in range(0, w, 2):
        if bright(px[x, y]):
            white.append((x, y))
print("white pixel count:", len(white))

# Cluster white pixels into rows (histogram by y), find text bands
from collections import Counter
ys = Counter(y for x, y in white)
bands = []
in_band = False
for y in range(0, h):
    c = ys.get(y, 0)
    if c > 4 and not in_band:
        start = y
        in_band = True
    elif c <= 4 and in_band:
        if y - start > 6:
            bands.append((start, y))
        in_band = False
if in_band:
    bands.append((start, h - 1))
print("white text bands (y0,y1):", bands[:20])

# For each band, x-range
for b0, b1 in bands[:8]:
    xs_in = [x for x, y in white if b0 <= y <= b1]
    if xs_in:
        print("band y[%d,%d] x[%d,%d] center_y=%d" % (b0, b1, min(xs_in), max(xs_in), (b0 + b1) // 2))

# Detect small isolated clusters of bright pixels around text (dots).
# Use connected components on bright pixels; report components with tiny area far from any big component.
visited = set()
comps = []
for x, y in white:
    if (x, y) in visited:
        continue
    stack = [(x, y)]
    visited.add((x, y))
    cells = []
    while stack:
        cx, cy = stack.pop()
        cells.append((cx, cy))
        for dx in (-2, 0, 2):
            for dy in (-2, 0, 2):
                nx, ny = cx + dx, cy + dy
                if (nx, ny) not in visited and 0 <= nx < w and 0 <= ny < h and bright(px[nx, ny]):
                    visited.add((nx, ny))
                    stack.append((nx, ny))
    comps.append(cells)

small = [c for c in comps if 1 <= len(c) <= 8]
print("total components:", len(comps), "small components (<=8 px):", len(small))
for c in small[:25]:
    xs = [p[0] for p in c]; ys2 = [p[1] for p in c]
    print("small dot at x[%d,%d] y[%d,%d] size=%d sampleRGB=%s" % (min(xs), max(xs), min(ys2), max(ys2), len(c), px[xs[0], ys2[0]]))
