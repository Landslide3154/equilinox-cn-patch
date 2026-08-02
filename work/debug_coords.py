import re
from PIL import Image, ImageDraw, ImageFont

MSYH = r"C:\Windows\Fonts\msyh.ttc"
size = 39
pad = 8
font = ImageFont.truetype(MSYH, size)

ATLAS = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
A = ATLAS.split()[3].load()

def parse_fnt(path):
    chars = {}
    for line in open(path, encoding="ascii"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
    return chars

chars = parse_fnt(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")

ch = "开"
cid = ord(ch)
v = chars[cid]
print("fnt entry:", v)

# reference glyph
bbox = font.getbbox(ch, anchor="ls")
xmin, ymin, xmax, ymax = bbox
img = Image.new("L", (xmax - xmin + 2 * pad, ymax - ymin + 2 * pad), 0)
d = ImageDraw.Draw(img)
d.text((pad - xmin, pad - ymin), ch, font=font, fill=255, anchor="ls")
print("reference (cell %dx%d):" % img.size)
for yy in range(img.size[1]):
    print("".join("#" if img.getpixel((xx, yy)) > 60 else "." for xx in range(img.size[0])))

# atlas region per fnt coords
x0, y0 = v["x"], v["y"]
print("atlas region at fnt coords (x=%d y=%d w=%d h=%d):" % (x0, y0, v["width"], v["height"]))
for yy in range(v["height"]):
    print("".join("#" if A[x0 + xx, y0 + yy] > 60 else "." for xx in range(v["width"])))
