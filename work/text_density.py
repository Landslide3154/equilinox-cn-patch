from PIL import Image

img = Image.open(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png").convert("RGB")
px = img.load()
w, h = img.size

def white(p):
    return p[0] > 240 and p[1] > 240 and p[2] > 240

# button 1 panel y[141,219]; print white density per row
for y in range(135, 225):
    cnt = sum(1 for x in range(10, 380) if white(px[x, y]))
    print("%3d: %s" % (y, "#" * (cnt // 4)))
