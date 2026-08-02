from PIL import Image
import sys

def measure(path, btn_x0, btn_x1, text_x0, text_x1, y_start, y_end):
    img = Image.open(path).convert("RGB")
    px = img.load()
    w, h = img.size

    # panel edge rows: low-variance horizontal rows across button width
    edges = []
    for y in range(y_start, y_end):
        vals = [px[x, y] for x in range(btn_x0, btn_x1, 2)]
        avg = sum(v[0] for v in vals) / len(vals)
        var = sum((v[0] - avg) ** 2 for v in vals) / len(vals)
        if var < 25:
            edges.append(y)

    # text rows: bright pixels in text region
    text_rows = []
    for y in range(y_start, y_end):
        cnt = sum(1 for x in range(text_x0, text_x1) if sum(px[x, y]) > 700)
        if cnt > 3:
            text_rows.append(y)
    print(path.split("\\")[-1], "panel edges:", edges[:5], "...", edges[-5:] if edges else "")
    if text_rows:
        print("  text ink y[%d,%d]" % (text_rows[0], text_rows[-1]))
        # nearest edge above/below text
        above = [e for e in edges if e < text_rows[0]]
        below = [e for e in edges if e > text_rows[-1]]
        if above and below:
            top_margin = text_rows[0] - max(above)
            bot_margin = min(below) - text_rows[-1]
            print("  top margin=%dpx bottom margin=%dpx -> %s" % (
                top_margin, bot_margin,
                "CENTERED" if abs(top_margin - bot_margin) <= 3 else
                ("HIGH by %dpx" % (bot_margin - top_margin) if bot_margin > top_margin else "LOW by %dpx" % (top_margin - bot_margin))))

# Original menu (full screen 1603x926 at 160,90). Buttons on the right; first button text ~x1120-1200, y~?
# Use the earlier layout: button panel corner at (960,475); text rows ~ (1100-1500)
print("=== ORIGINAL ===")
measure(r"D:\code\equilinox\work\menu_orig.png", 700, 1500, 1000, 1500, 430, 560)

print("=== USER HANZI MENU (406x889) ===")
# button 1: panel rows ~9-15 of 60-row space -> y ~133-222; text at x 40-130, y 147-199
measure(r"D:\code\equilinox\Snipaste_2026-07-31_18-38-16.png", 5, 300, 30, 280, 130, 230)
