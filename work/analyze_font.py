from PIL import Image

img = Image.open(r"D:\code\equilinox\jar\res\guis\fonts\segoeUI.png").convert("RGBA")
# char 'A': x=296 y=314 width=53 height=58 (includes padding 10)
x, y, w, h = 296, 314, 53, 58

print("corner pixels (alpha):", [img.getpixel((x + dx, y + dy))[3] for dx in (0, 5, 9) for dy in (0, 5, 9)])
print("center pixel:", img.getpixel((x + 26, y + 29)))

print("8x8 grid of alphas within cell (cell 53x58, pad 10):")
for gy in range(8):
    row = []
    for gx in range(8):
        cx = x + 3 + gx * 6
        cy = y + 3 + gy * 7
        row.append(img.getpixel((cx, cy))[3])
    print(row)

# check overall texture color style: max rgb where alpha>0
maxrgb = (0, 0, 0)
nz = 0
for py in range(0, 512, 4):
    for px in range(0, 512, 4):
        r, g, b, a = img.getpixel((px, py))
        if a > 0:
            nz += 1
            maxrgb = tuple(max(maxrgb[i], (r, g, b)[i]) for i in range(3))
print("sample: nonzero pixels (4px grid):", nz, "max rgb:", maxrgb)
