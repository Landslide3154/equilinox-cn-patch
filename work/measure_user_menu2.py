from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png").convert("RGB")
w, h = img.size
px = img.load()

# Find rows that look like horizontal panel edges (many pixels with similar, distinct color,
# forming a long horizontal line of near-constant color across the button width).
def row_stats(y, x0, x1):
    vals = [px[x, y] for x in range(x0, x1, 2)]
    r_avg = sum(v[0] for v in vals) / len(vals)
    r_var = sum((v[0] - r_avg) ** 2 for v in vals) / len(vals)
    return r_avg, r_var

# Analyze the left column region x 20-380
edges = []
for y in range(100, 860):
    avg, var = row_stats(y, 30, 380)
    if var < 30:  # low variance = flat color row (edge line)
        edges.append((y, avg, var))

# group consecutive flat rows
groups = []
for e in edges:
    if groups and e[0] - groups[-1][-1][0] <= 3:
        groups[-1].append(e)
    else:
        groups.append([e])
for g in groups:
    if len(g) > 1:
        print("flat band y[%d,%d] avgRGB=%d" % (g[0][0], g[-1][0], g[0][1]))
