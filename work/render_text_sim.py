"""Render a Chinese string exactly like the game does (MetaFile math +
GL_LINEAR sampling + shader threshold), to check for fragments."""
import re
from PIL import Image

ATLAS = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
A = ATLAS.split()[3].load()
SCALE_W = 4096

def parse_fnt(path):
    chars = {}
    header = {}
    for line in open(path, encoding="ascii"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
        if line.startswith("common "):
            header = dict((k, int(v)) for k, v in re.findall(r"(lineHeight|base|scaleW)=(\d+)", line))
    return chars, header

chars, header = parse_fnt(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")
PAD = 8
lineHeight = header["lineHeight"]
VERT = 0.04 / (lineHeight - 2 * PAD)
HOR = VERT / (1600.0 / 900.0)  # aspect

def meta_char(cid):
    v = chars[cid]
    xtex = (v["x"] + PAD - 10) / SCALE_W
    ytex = (v["y"] + PAD - 10) / SCALE_W
    w = v["width"] - (2 * PAD - 20)
    h = v["height"] - (2 * PAD - 20)
    xoff = (v["xoffset"] + PAD - 10) * HOR
    yoff = (v["yoffset"] + PAD - 10) * VERT
    xadv = (v["xadvance"] - 2 * PAD) * HOR
    return xtex, ytex, w, h, xoff, yoff, xadv

def smoothstep(e0, e1, x):
    t = (x - e0) / (e1 - e0)
    t = max(0.0, min(1.0, t))
    return t * t * (3.0 - 2.0 * t)

def shader(dist, edge_x=0.46, edge_y=0.1):
    alpha = smoothstep((1 - edge_x) - edge_y, 1 - edge_x, dist)
    outline = 0.0  # smoothstep(1,1,dist) = 0 for dist < 1; dist never == 1 in our atlas
    overall = alpha + (1 - alpha) * outline
    return overall

def render_text(s, font_size=1.0, max_line=1.0, W=1600, H=900):
    img = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    px = img.load()
    quads = []
    cx = 0.0
    cy = 0.0
    for ch in s:
        if ch == " ":
            cx += (chars[32]["xadvance"] - 2 * PAD) * HOR * font_size
            continue
        v = chars.get(ord(ch))
        if v is None:
            continue
        xtex, ytex, w, h, xoff, yoff, xadv = meta_char(ord(ch))
        x = cx + xoff * font_size
        y = cy + yoff * font_size
        qw = w * HOR * font_size
        qh = h * VERT * font_size
        quads.append((x, y, qw, qh, xtex, ytex, w / SCALE_W, h / SCALE_W))
        cx += xadv * font_size
    # rasterize
    for py in range(H):
        ny = py / H  # screen y (top = 0)
        for px_x in range(W):
            nx = px_x / W
            best = None
            for (qx, qy, qw, qh, tx, ty, tw, th) in quads:
                # quad in normalized screen coords: x 0..1, y 0..1 (y down)
                if qx <= nx <= qx + qw and qy <= ny <= qy + qh:
                    u = (nx - qx) / qw
                    vv = (ny - qy) / qh
                    # bilinear sample
                    fx = (tx + u * tw) * SCALE_W - 0.5
                    fy = (ty + vv * th) * SCALE_W - 0.5
                    x0 = int(fx); y0 = int(fy)
                    dx = fx - x0; dy = fy - y0
                    def sm(xx, yy):
                        xx = max(0, min(SCALE_W - 1, xx))
                        yy = max(0, min(SCALE_W - 1, yy))
                        return A[xx, yy] / 255.0
                    a00 = sm(x0, y0); a10 = sm(x0 + 1, y0)
                    a01 = sm(x0, y0 + 1); a11 = sm(x0 + 1, y0 + 1)
                    dist = (a00 * (1 - dx) + a10 * dx) * (1 - dy) + (a01 * (1 - dx) + a11 * dx) * dy
                    a = shader(dist)
                    if a > 0.02:
                        px[px_x, py] = (255, 255, 255, int(a * 255))
                    break
    return img

img = render_text("开始游戏", font_size=1.0)
quads = []
# quick manual layout to print positions
cx = 0.0
for ch in "开始游戏":
    if ch == " ":
        continue
    xtex, ytex, w, h, xoff, yoff, xadv = meta_char(ord(ch))
    quads.append((cx + xoff, yoff, w * HOR, h * VERT))
    cx += xadv
print("quad positions (norm):", quads)
import math
# debug sample: center of first quad
xtex, ytex, w, h, xoff, yoff, xadv = meta_char(ord("开"))
print("开 meta: tx=%.4f ty=%.4f w=%d h=%d" % (xtex, ytex, w, h))
fx = (xtex + 0.5 * (w / SCALE_W)) * SCALE_W - 0.5
fy = (ytex + 0.5 * (h / SCALE_W)) * SCALE_W - 0.5
print("sample atlas at", fx, fy, "alpha=", A[int(fx), int(fy)])
print("atlas ink region alpha sample:", A[310, 815])

# debug fragment at (67, 13): which quads cover it and what do they sample
import math
debug_quads = []
cx = 0.0
for ch in "开始游戏":
    if ch == " ":
        continue
    xtex, ytex, w, h, xoff, yoff, xadv = meta_char(ord(ch))
    debug_quads.append((ch, cx + xoff, yoff, w * HOR, h * VERT, xtex, ytex, w / SCALE_W, h / SCALE_W))
    cx += xadv
for ch, qx, qy, qw, qh, tx, ty, tw, th in debug_quads:
    nx = 67 / 1600.0
    ny = 13 / 900.0
    if qx <= nx <= qx + qw and qy <= ny <= qy + qh:
        u = (nx - qx) / qw
        vv = (ny - qy) / qh
        fx = (tx + u * tw) * SCALE_W - 0.5
        fy = (ty + vv * th) * SCALE_W - 0.5
        x0 = int(fx); y0 = int(fy)
        print("pixel(67,13) in quad '%s' u=%.3f v=%.3f samples atlas (%d,%d) a=%d,%d,%d,%d" %
              (ch, u, vv, x0, y0, A[max(0,x0),max(0,y0)], A[min(4095,x0+1),max(0,y0)], A[max(0,x0),min(4095,y0+1)], A[min(4095,x0+1),min(4095,y0+1)]))
for ch in "开始游戏":
    print("char", ch, hex(ord(ch)), "entry:", chars.get(ord(ch)))
img.save(r"D:\code\equilinox\work\sim_text.png")
# print a coarse ascii of the result
small = img.resize((120, 30))
spx = small.load()
for y in range(30):
    row = []
    for x in range(120):
        a = spx[x, y][3]
        row.append("#" if a > 200 else ("+" if a > 100 else ("-" if a > 30 else ".")))
    print("".join(row))
