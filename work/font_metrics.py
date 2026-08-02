import io, re

base = r"D:\code\equilinox\build\res\guis\fonts"

def parse(fnt, pad):
    tops, bottoms, heights = [], [], []
    for line in io.open(fnt, encoding="ascii"):
        m = re.search(r"char id=(\d+)", line)
        if not m:
            continue
        vals = dict((k, int(v)) for k, v in re.findall(r"(x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
        cid = int(m.group(1))
        if cid == 32:
            continue
        if cid < 256:
            continue  # only new CJK additions
        ink_top = vals["yoffset"] + pad
        ink_bottom = vals["yoffset"] + vals["height"] - pad
        tops.append(ink_top)
        bottoms.append(ink_bottom)
        heights.append(vals["height"] - 2 * pad)
    return tops, bottoms, heights

for name, pad in (("gill3", 8), ("segoeUI", 10)):
    tops, bottoms, heights = parse(base + "\\" + name + ".fnt", pad)
    tops.sort(); bottoms.sort(); heights.sort()
    n = len(tops)
    print("%s new glyphs: %d" % (name, n))
    print("  inkTop: min=%d p25=%d median=%d p75=%d max=%d" % (tops[0], tops[n//4], tops[n//2], tops[3*n//4], tops[-1]))
    print("  inkBottom: min=%d median=%d max=%d" % (bottoms[0], bottoms[n//2], bottoms[-1]))
    print("  inkHeight: min=%d median=%d max=%d" % (heights[0], heights[n//2], heights[-1]))
