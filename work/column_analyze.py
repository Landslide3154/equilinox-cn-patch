from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_19-34-45.png").convert("RGB")
px = img.load()
w, h = img.size

for col in (187, 188, 129, 246, 304):
    print("=== column x=%d ===" % col)
    prev = None
    run = 0
    for y in range(0, h, 2):
        p = px[col, y]
        key = sum(p) // 100
        if key != prev:
            if run:
                print("  y=%d..%d brightness=%d rgb=%s" % (y - run * 2, y - 2, prev * 100, lastrgb))
            prev = key
            lastrgb = p
            run = 1
        else:
            run += 1
    if run:
        print("  y=%d..%d brightness=%d rgb=%s" % (h - run * 2, h - 2, prev * 100, lastrgb))
