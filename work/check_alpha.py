from PIL import Image

for name, cell in (("gill3", (90, 335, 31, 44)), ("segoeUI", (296, 314, 53, 58))):
    img = Image.open(r"D:\code\equilinox\jar\res\guis\fonts\%s.png" % name).convert("RGBA")
    x, y, w, h = cell
    pa = img.split()[3].load()
    # sample the glyph interior (ink area) alphas
    vals = []
    for yy in range(y + 10, y + h - 8, 2):
        for xx in range(x + 10, x + w - 8, 2):
            vals.append(pa[xx, yy])
    print("%s interior alpha: max=%d min=%d count=%d values255=%d"
          % (name, max(vals), min(vals), len(vals), sum(1 for v in vals if v == 255)))
    # also count exact 255 pixels in the whole atlas
    total255 = sum(1 for v in img.split()[3].getdata() if v == 255)
    print("  atlas pixels exactly 255:", total255)
