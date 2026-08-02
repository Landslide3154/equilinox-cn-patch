import io
import os
from PIL import Image

base = r"D:\code\equilinox"
dst = os.path.join(base, "build", "res", "guis", "fonts")

for name, size, pad in (("segoeUI", 52, 10), ("gill3", 39, 8)):
    fnt = os.path.join(dst, name + ".fnt")
    lines = io.open(fnt, encoding="ascii").read().splitlines()
    print(name, "fnt lines:", len(lines))
    print("  header:", lines[0])
    print("  common:", lines[1])
    print("  count:", lines[3])
    print("  first CJK:", [l for l in lines if "id=20320" in l])
    print("  last line:", lines[-1][:80])

    img = Image.open(os.path.join(dst, name + ".png")).convert("RGBA")
    print("  atlas:", img.size, "mode:", img.mode)
    # count non-empty pixels in the new region (beyond original 512x512)
    region = img.crop((0, 512, 4096, 4096))
    a = region.split()[3]
    nz = sum(1 for p in a.getdata() if p > 0)
    print("  non-empty pixels in new region:", nz)

    # verify original Latin region is intact: sample the 'A' ink pixel from original
    # original 'A' cell at (296,314) pad 10; ink center ~ (306+16, 324+30)
    px = img.getpixel((296 + 10 + 16, 314 + 10 + 29))
    print("  original 'A' ink sample:", px)
