from PIL import Image
import sys

img = Image.open(sys.argv[1]).convert("RGB")
px = img.load()
w, h = img.size
y = int(sys.argv[2]) if len(sys.argv) > 2 else h // 2

prev = None
run = 0
for x in range(0, w):
    p = px[x, y]
    key = (p[0] // 32, p[1] // 32, p[2] // 32)
    if key != prev:
        if run:
            print("x=%d..%d key=%s rgb=%s" % (x - run, x - 1, prev, prevrgb))
        prev = key
        prevrgb = p
        run = 1
    else:
        run += 1
if run:
    print("x=%d..%d key=%s rgb=%s" % (w - run, w - 1, prev, prevrgb))
