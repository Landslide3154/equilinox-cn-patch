from PIL import Image

def text_band(path, x0, x1, y0, y1):
    img = Image.open(path).convert("RGB")
    px = img.load()
    rows = []
    for y in range(y0, y1):
        cnt = sum(1 for x in range(x0, x1) if px[x, y] == (255, 255, 255))
        rows.append(cnt)
    # pure-white rows with content
    ys = [y0 + i for i, c in enumerate(rows) if c > 0]
    if not ys:
        return None
    return (ys[0], ys[-1], max(rows))

# ORIGINAL menu: window at (160,90) 1603x926; buttons stacked at x center ~0.29*1603+160=625?
# But our earlier scans showed button text on the right side (x 1000-1560).
# Scan a few x ranges and report pure-white text bands.
for name, path in (("ORIG", r"D:\code\equilinox\work\menu_orig.png"),):
    img = Image.open(path).convert("RGB")
    print("=== %s ===" % name)
    for x0, x1 in ((620, 760), (1000, 1150), (1200, 1500)):
        band = text_band(path, x0, x1, 210, 310)
        print("x[%d,%d] text band y=%s" % (x0, x1, band))

# USER screenshot: button 1 text region
print("=== USER ===")
print("button1 text:", text_band(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png", 30, 200, 110, 230))
