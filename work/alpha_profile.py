from PIL import Image

# Original gill3 'A' cell (x=90,y=335,31x44), scan alpha along a horizontal line
# through the middle of the glyph
img = Image.open(r"D:\code\equilinox\jar\res\guis\fonts\gill3.png").convert("RGBA")
pa = img.split()[3].load()
x, y, w, h = 90, 335, 31, 44
print("original 'A' alpha profile at mid-height (y=%d):" % (y + 22))
print([pa[xx, y + 22] for xx in range(x, x + w)])

# vertical profile through a stroke
print("vertical at x=%d:" % (x + 8))
print([pa[x + 8, yy] for yy in range(y, y + h)])

# my generated glyph '你' in gill3: find its cell
fnt = open(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt", encoding="ascii").read()
import re
m = re.search(r"char id=20320   x=(\d+)     y=(\d+)     width=(\d+)     height=(\d+)", fnt)
cx, cy, cw, chh = [int(v) for v in m.groups()]
img2 = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
pa2 = img2.split()[3].load()
print("my '你' cell:", cx, cy, cw, chh)
print("my '你' alpha profile at mid-height (y=%d):" % (cy + 26))
print([pa2[xx, cy + 26] for xx in range(cx, cx + cw)])
