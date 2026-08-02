from PIL import Image

img = Image.open(r"D:\code\equilinox\screenshot_main_menu.png").convert("RGB")
w, h = img.size
px = list(img.getdata())
n = len(px)
mean = tuple(sum(p[i] for p in px) // n for i in range(3))
variance = sum(sum((p[i] - mean[i]) ** 2 for i in range(3)) for p in px) / n
print("size:", w, "x", h)
print("mean RGB:", mean)
print("variance:", int(variance))
# count pixels notably different from mean (edges/text)
diff = sum(1 for p in px if abs(p[0] - mean[0]) + abs(p[1] - mean[1]) + abs(p[2] - mean[2]) > 120)
print("high-contrast pixel ratio: %.2f%%" % (100.0 * diff / n))
# sample a horizontal strip in the middle where menu buttons usually are
strip = img.crop((0, int(h * 0.35), w, int(h * 0.75)))
spx = list(strip.getdata())
light = sum(1 for p in spx if sum(p) > 600)
print("bright pixels in menu strip: %.2f%%" % (100.0 * light / len(spx)))
