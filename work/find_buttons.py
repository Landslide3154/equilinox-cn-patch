from PIL import Image

img = Image.open(r"D:\code\equilinox\work\menu_repro.png").convert("RGB")
w, h = img.size
px = img.load()

# Main menu buttons are beige rounded panels on the left-center of the screen.
# Scan columns for regions of beige-ish color that differ from background.
def is_beige(p):
    r, g, b = p
    return 150 < r < 235 and 140 < g < 225 and 110 < b < 200 and abs(r - g) < 45 and abs(g - b) < 60

cols = {}
for x in range(0, w, 4):
    cnt = 0
    for y in range(0, h, 4):
        if is_beige(px[x, y]):
            cnt += 1
    if cnt > 5:
        cols[x] = cnt
print("beige column x-ranges:", sorted(cols.keys())[:5] if cols else "none", "...", sorted(cols.keys())[-3:] if cols else "")

# vertical profile at x = center of detected column band
if cols:
    xs = sorted(cols.keys())
    band_start = xs[0]
    rows = []
    for y in range(h):
        cnt = sum(1 for x in range(band_start, band_start + 120, 4) if is_beige(px[x, y]))
        rows.append(cnt)
    # find contiguous runs of beige rows
    runs = []
    in_run = False
    for y, c in enumerate(rows):
        if c > 2 and not in_run:
            start = y
            in_run = True
        elif c <= 2 and in_run:
            runs.append((start, y))
            in_run = False
    if in_run:
        runs.append((start, h - 1))
    print("button runs (y0,y1):", runs[:8])
