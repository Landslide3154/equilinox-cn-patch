from PIL import Image, ImageChops

a = Image.open(r"D:\code\equilinox\work\menu_orig.png").convert("RGB")
b = Image.open(r"D:\code\equilinox\work\menu_visual.png").convert("RGB")

diff = ImageChops.difference(a, b)
px = diff.load()
w, h = diff.size

# Find rows with significant differences
from collections import Counter
rows = Counter()
for y in range(h):
    c = sum(1 for x in range(0, w, 3) if sum(px[x, y]) > 60)
    if c > 5:
        rows[y] = c

if not rows:
    print("NO SIGNIFICANT DIFFERENCES - menus look identical")
else:
    ys = sorted(rows.keys())
    bands = []
    start = ys[0]
    prev = ys[0]
    for y in ys[1:]:
        if y - prev > 4:
            bands.append((start, prev))
            start = y
        prev = y
    bands.append((start, prev))
    print("diff bands (y0,y1):", bands)
    for b0, b1 in bands[:12]:
        xs = [x for x in range(0, w, 3) if any(sum(px[x, y]) > 60 for y in range(b0, b1 + 1))]
        print("  band y[%d,%d] x[%d,%d]" % (b0, b1, min(xs), max(xs)))

# Save a diff image with non-zero areas highlighted
out = diff.point(lambda v: 255 if v > 40 else 0)
out.save(r"D:\code\equilinox\work\menu_diff.png")
