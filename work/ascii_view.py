from PIL import Image
import sys

path = sys.argv[1] if len(sys.argv) > 1 else r"D:\code\equilinox\work\menu_visual.png"
cols = int(sys.argv[2]) if len(sys.argv) > 2 else 140
rows = int(sys.argv[3]) if len(sys.argv) > 3 else 48

img = Image.open(path).convert("RGB")
w, h = img.size
px = img.load()

chars = " .:-=+*#%@"
out = []
for r in range(rows):
    line = []
    for c in range(cols):
        x0 = c * w // cols
        x1 = max(x0 + 1, (c + 1) * w // cols)
        y0 = r * h // rows
        y1 = max(y0 + 1, (r + 1) * h // rows)
        # average
        rs = gs = bs = 0
        n = 0
        for yy in range(y0, y1, 2):
            for xx in range(x0, x1, 2):
                p = px[xx, yy]
                rs += p[0]; gs += p[1]; bs += p[2]
                n += 1
        avg = (rs + gs + bs) / (3.0 * n)
        idx = int(avg / 256.0 * len(chars))
        line.append(chars[min(idx, len(chars) - 1)])
    out.append("".join(line))
print("\n".join(out))
