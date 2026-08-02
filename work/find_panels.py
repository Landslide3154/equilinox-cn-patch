from PIL import Image
import sys

img = Image.open(sys.argv[1] if len(sys.argv) > 1 else r"D:\code\equilinox\work\menu_visual.png").convert("RGB")
w, h = img.size
px = img.load()

# DNA button background is a light beige/green tinted rounded panel.
# Sample a grid and cluster pixels by colour similarity.
from collections import defaultdict
colors = defaultdict(int)
for y in range(0, h, 6):
    for x in range(0, w, 6):
        r, g, b = px[x, y]
        key = (r // 16, g // 16, b // 16)
        colors[key] += 1
top = sorted(colors.items(), key=lambda kv: -kv[1])[:12]
print("top colour buckets (r16,g16,b16): count")
for k, c in top:
    print("  ", k, c)

# find rows where a wide stretch has a common panel colour
# use the most common non-background colours
print("size:", w, h)
