from PIL import Image

a = Image.open(r"D:\code\equilinox\work\menu_orig.png").convert("RGB")
b = Image.open(r"D:\code\equilinox\work\menu_visual.png").convert("RGB")
pa = a.load()
pb = b.load()

# scan for isolated diff points in the text region y 340-610, x 580-1600
points = []
for y in range(350, 600):
    for x in range(600, 1590):
        da = pa[x, y]
        db = pb[x, y]
        d = sum(abs(da[i] - db[i]) for i in range(3))
        if d > 120:
            points.append((x, y, da, db))

print("diff points in region:", len(points))
for x, y, da, db in points[:30]:
    print("(%d,%d) orig=%s patched=%s" % (x, y, da, db))

# classify: how many points have patched bright (text color ~ white) vs both mid
bright_in_patched = sum(1 for _, _, da, db in points if sum(db) > 650)
bright_in_orig = sum(1 for _, _, da, db in points if sum(da) > 650)
print("points bright in patched:", bright_in_patched)
print("points bright in original:", bright_in_orig)

# cluster check: are these points near text glyphs? print a mini-map of diff points
from collections import Counter
cnt = Counter()
for x, y, _, _ in points:
    cnt[(x // 20, y // 20)] += 1
print("cluster heat (20px cells):")
for gy in range(350 // 20, 600 // 20):
    row = "".join(chr(65 + min(cnt.get((gx, gy), 0), 25)) if cnt.get((gx, gy)) else "." for gx in range(600 // 20, 1590 // 20))
    print(row)
