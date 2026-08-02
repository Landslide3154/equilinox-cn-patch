from PIL import Image
import sys

path = sys.argv[1]
x0, y0, x1, y1 = [int(v) for v in sys.argv[2:6]]
cols = int(sys.argv[6]) if len(sys.argv) > 6 else 120

img = Image.open(path).convert("RGB")
px = img.load()
chars = " .:-=+*#%@"
rows = int(cols * (y1 - y0) / (x1 - x0) * 0.5)
for r in range(rows):
    line = []
    for c in range(cols):
        sx = x0 + c * (x1 - x0) // cols
        sy = y0 + r * (y1 - y0) // rows
        p = px[sx, sy]
        avg = (p[0] + p[1] + p[2]) / 3.0
        idx = int(avg / 256.0 * len(chars))
        line.append(chars[min(idx, len(chars) - 1)])
    print("y=%d %s" % (y0 + r * (y1 - y0) // rows, "".join(line)))
