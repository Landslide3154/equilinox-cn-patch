from PIL import Image
import os

base = r"D:\code\equilinox\jar\res\guis"
for name in ("focusLines.png", "dnaButton.png", "line0.png", "line1.png", "line2.png",
             "line3.png", "line4.png", "line5.png", "line6.png", "line7.png",
             "selectorBar.png", "grid.png", "mainMenuDna.png", "background.png"):
    p = os.path.join(base, name)
    if not os.path.exists(p):
        continue
    img = Image.open(p).convert("RGBA")
    print("%-20s size=%s" % (name, img.size))
    a = img.split()[3].load()
    # sample a row through the middle to see pattern periodicity
    w, h = img.size
    y = h // 2
    row = [a[x, y] for x in range(w)]
    nz = [x for x, v in enumerate(row) if v > 40]
    if nz:
        gaps = [nz[i + 1] - nz[i] for i in range(len(nz) - 1)]
        print("   nonzero x at y=%d: %d px, gaps %s" % (y, len(nz), sorted(set(gaps))[:8]))
