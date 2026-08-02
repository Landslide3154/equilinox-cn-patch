from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-39.png").convert("RGB")
w, h = img.size
px = img.load()
print("size:", w, h)

# The crop shows text; find all bright pixels and component analysis
def bright(p):
    return sum(p) > 400

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
print("components:", len(comps))
for c in comps[:30]:
    xs = [p[0] for p in c]
    ys = [p[1] for p in c]
    print("comp x[%d,%d] y[%d,%d] size=%d" % (min(xs), max(xs), min(ys), max(ys), len(c)))

# small components (dots) NOT connected to big text
big = [c for c in comps if len(c) > 30]
small = [c for c in comps if len(c) <= 12]
print("small components:", len(small))
for c in small[:40]:
    xs = [p[0] for p in c]
    ys = [p[1] for p in c]
    print("dot x[%d,%d] y[%d,%d] size=%d rgb=%s" % (min(xs), max(xs), min(ys), max(ys), len(c), px[xs[0], ys[0]]))
