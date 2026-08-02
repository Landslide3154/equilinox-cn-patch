import re
from PIL import Image

def load(path):
    chars = {}
    for line in open(path, encoding="ascii", errors="replace"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
    return chars

new = load(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")
A = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
pa = A.split()[3].load()

for ch in ("开", "始", "游", "戏", "你", "。", "一"):
    v = new[ord(ch)]
    x, y, w, h = v["x"], v["y"], v["width"], v["height"]
    # find actual ink bbox in the cell
    minx = miny = 10 ** 9
    maxx = maxy = -1
    for yy in range(y, y + h):
        for xx in range(x, x + w):
            if pa[xx, yy] > 20:
                minx = min(minx, xx); maxx = max(maxx, xx)
                miny = min(miny, yy); maxy = max(maxy, yy)
    print("%s entry=%s inkInCell=(%d,%d)-(%d,%d) expectedPad=%d" % (ch, v, minx - x, miny - y, maxx - x, maxy - y, 8))
