from PIL import Image

for name in ("gill3", "segoeUI"):
    img = Image.open(r"D:\code\equilinox\jar\res\guis\fonts\%s.png" % name).convert("RGBA")
    a = img.split()[3]
    hist = a.histogram()
    total = sum(hist)
    nonzero = total - hist[0]
    # max alpha
    maxa = max(i for i, c in enumerate(hist) if c > 0)
    print("%s: max alpha = %d, nonzero px = %d" % (name, maxa, nonzero))
    # distribution of high alphas
    for lo in (100, 120, 130, 140, 150, 200, 250):
        cnt = sum(hist[lo:])
        print("  alpha >= %d: %d px (%.1f%% of nonzero)" % (lo, cnt, 100.0 * cnt / nonzero))
