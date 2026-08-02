from PIL import Image

img = Image.open(r"D:\code\equilinox\work\menu_orig.png").convert("RGB")
px = img.load()
w, h = img.size

def white(p):
    return p[0] > 245 and p[1] > 245 and p[2] > 245

# find white components in the whole image
visited = set()
comps = []
for y in range(0, h, 2):
    for x in range(0, w, 2):
        if (x, y) in visited or not white(px[x, y]):
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
                    if (nx, ny) not in visited and 0 <= nx < w and 0 <= ny < h and white(px[nx, ny]):
                        visited.add((nx, ny))
                        stack.append((nx, ny))
        if len(cells) > 20:
            xs = [p[0] for p in cells]
            ys = [p[1] for p in cells]
            comps.append((min(xs), max(xs), min(ys), max(ys), len(cells)))

comps.sort(key=lambda c: (c[2], c[0]))
for c in comps:
    x0, x1, y0, y1, n = c
    print("text comp x[%d,%d] y[%d,%d] h=%d w=%d n=%d" % (x0, x1, y0, y1, y1 - y0, x1 - x0, n))
