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

def reference_glyph(ch):
    bbox = font.getbbox(ch, anchor="ls")
    xmin, ymin, xmax, ymax = bbox
    ink_w = xmax - xmin
    ink_h = ymax - ymin
    img = Image.new("L", (ink_w + 2 * pad, ink_h + 2 * pad), 0)
    d = ImageDraw.Draw(img)
    d.text((pad - xmin, pad - ymin), ch, font=font, fill=255, anchor="ls")
    return img

def atlas_glyph(cid):
    v = chars[cid]
    # parser samples [x-2, x+width+2] -- take the ink region [x+pad, x+pad+ink]
    ink_w = v["width"] - 2 * pad
    ink_h = v["height"] - 2 * pad
    x0 = v["x"] + pad
    y0 = v["y"] + pad
    img = Image.new("L", (ink_w + 2 * pad, ink_h + 2 * pad), 0)
    p = img.load()
    for yy in range(ink_h + 2 * pad):
        for xx in range(ink_w + 2 * pad):
            p[xx, yy] = A[min(4095, x0 - pad + xx), min(4095, y0 - pad + yy)]
    return img

def compare(a, b):
    # normalize sizes and compare alpha similarity
    w = max(a.width, b.width)
    h = max(a.height, b.height)
    diff = 0
    n = 0
    for yy in range(h):
        for xx in range(w):
            va = a.getpixel((xx, yy)) if xx < a.width and yy < a.height else 0
            vb = b.getpixel((xx, yy)) if xx < b.width and yy < b.height else 0
            diff += abs(va - vb)
            n += 1
    return diff / n

test_chars = "开始游戏任务完成退出加载世界操作选项保存"
mismatch = 0
for ch in test_chars:
    cid = ord(ch)
    if cid not in chars:
        print(ch, "MISSING from fnt!")
        continue
    ref = reference_glyph(ch)
    atl = atlas_glyph(cid)
    d = compare(ref, atl)
    status = "OK" if d < 30 else "MISMATCH"
    if d >= 30:
        mismatch += 1
    print("%s (U+%04X) avgDiff=%.1f %s" % (ch, cid, d, status))
print("mismatches:", mismatch)
