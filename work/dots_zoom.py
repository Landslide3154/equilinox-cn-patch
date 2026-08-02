from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-39.png").convert("RGB")
px = img.load()

for y in range(4, 40):
    row = []
    for x in range(140, 200):
        r, g, b = px[x, y]
        s = sum(px[x, y])
        if s > 700:
            row.append("#")
        elif s > 520:
            row.append("+")
        elif s > 380:
            row.append("-")
        elif s > 240:
            row.append(".")
        else:
            row.append(" ")
    print("%2d %s" % (y, "".join(row)))
