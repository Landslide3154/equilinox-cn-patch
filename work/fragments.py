from PIL import Image
import sys

path = sys.argv[1] if len(sys.argv) > 1 else r"D:\code\equilinox\Snipaste_2026-07-31_18-38-55.png"
img = Image.open(path).convert("RGB")
w, h = img.size
px = img.load()

# Detect bright fragments (text-colored) that are small relative to glyphs.
# Text glyphs at this size are ~10-30 px; fragments are 1-8 px.
def bright(p):
    return sum(p) > 480

visited = set()
comps = []
for y in range(h):
    for x in range(w):
        if (x, y) in visited or not bright(px[x, y]):
            continue
        stack = [(x, y)]
        visited.add((x, y))
        cells = []
        while stack:
            cx, cy = stack.pop()
            cells.append((cx, cy))
            for dx in (-1, 0, 1):
                for dy in (-1, 0, 1):
                    nx, ny = cx + dx, cy + dy
                    if (nx, ny) not in visited and 0 <= nx < w and 0 <= ny < h and bright(px[nx, ny]):
                        visited.add((nx, ny))
                        stack.append((nx, ny))
        comps.append(cells)

comps.sort(key=len, reverse=True)
print("total bright components:", len(comps))
big = [c for c in comps if len(c) > 25]
small = [c for c in comps if len(c) <= 10]
print("glyph components (>25px):", len(big), " small fragments (<=10px):", len(small))

# fragments: print their positions and colors
for c in small[:60]:
    xs = [p[0] for p in c]
    ys = [p[1] for p in c]
    print("frag x[%d,%d] y[%d,%d] n=%d rgb=%s" % (min(xs), max(xs), min(ys), max(ys), len(c), px[xs[0], ys[0]]))
