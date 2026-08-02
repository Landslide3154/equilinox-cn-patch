from PIL import Image

img = Image.open(r"D:\code\equilinox\work\menu_visual.png").convert("RGB")
px = img.load()
w, h = img.size

# find a big bright cluster (text glyph) in button area x>700
def bright(p):
    return sum(p) > 660

best = None
for y in range(300, 700):
    for x in range(700, 1560):
        if bright(px[x, y]):
            # collect component
            stack = [(x, y)]
            seen = set()
            while stack:
                cx, cy = stack.pop()
                if (cx, cy) in seen:
                    continue
                seen.add((cx, cy))
                for dx in (-1, 0, 1):
                    for dy in (-1, 0, 1):
                        nx, ny = cx + dx, cy + dy
                        if (nx, ny) not in seen and 0 <= nx < w and 0 <= ny < h and bright(px[nx, ny]):
                            stack.append((nx, ny))
            if best is None or len(seen) > len(best):
                best = seen
            if len(seen) > 200:
                break
    if best and len(best) > 200:
        break

xs = [p[0] for p in best]
ys = [p[1] for p in best]
x0, x1, y0, y1 = min(xs), max(xs), min(ys), max(ys)
print("glyph component at x[%d,%d] y[%d,%d] size=%d" % (x0, x1, y0, y1, len(best)))

# render magnified ascii (1 char per pixel) with brightness map, including surroundings
pad = 3
for y in range(y0 - pad, y1 + pad + 1):
    row = []
    for x in range(x0 - pad, x1 + pad + 1):
        p = px[x, y]
        s = sum(p)
        if s > 660:
            row.append("#")
        elif s > 560:
            row.append("+")
        elif s > 450:
            row.append("-")
        else:
            row.append(".")
    print("".join(row))
