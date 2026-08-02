from PIL import Image, ImageDraw, ImageFont

candidates = [
    ("微软雅黑（当前，黑体）", r"C:\Windows\Fonts\msyh.ttc", 39),
    ("方正准圆（圆体）", r"C:\Windows\Fonts\方正准圆_GBK.ttf", 39),
    ("霞鹜文楷（楷体/手写）", r"C:\Windows\Fonts\LXGWWenKai-Regular.ttf", 39),
    ("思源宋体 Heavy（宋体）", r"C:\Windows\Fonts\Source Han Serif SC Heavy (TrueType).ttf", 39),
    ("霞鹜新致宋（宋体）", r"C:\Windows\Fonts\LXGWNeoZhiSong.ttf", 39),
    ("方正喵呜（趣味手写）", r"C:\Windows\Fonts\方正喵呜_GBK.ttf", 39),
    ("方正少儿（趣味）", r"C:\Windows\Fonts\方正少儿_GBK.ttf", 39),
]

sample = "开始游戏 第1年,第1天 - 09:45"
samples = ["开始游戏", "第1年,第1天 - 09:45", "植物商店 动物商店 任务", "橡树 绵羊 海狸 向日葵"]

blocks = []
for name, path, size in candidates:
    try:
        font = ImageFont.truetype(path, size)
    except Exception as e:
        print(name, "FAILED", e)
        continue
    lines = samples
    w = 1000
    h = 40 * len(lines) + 60
    img = Image.new("RGB", (w, h), (255, 255, 255))
    d = ImageDraw.Draw(img)
    d.text((10, 10), name, font=ImageFont.truetype(r"C:\Windows\Fonts\msyh.ttc", 22), fill=(0, 0, 0))
    y = 45
    for s in lines:
        d.text((10, y), s, font=font, fill=(0, 0, 0))
        y += 40
    blocks.append((name, img))

gap = 24
total_w = max(b[1].width for b in blocks)
total_h = sum(b[1].height for b in blocks) + gap * (len(blocks) - 1)
canvas = Image.new("RGB", (total_w, total_h), (240, 240, 240))
y = 0
for name, img in blocks:
    canvas.paste(img, (0, y))
    y += img.height + gap
canvas.save(r"D:\code\equilinox\发布\字体风格对比.png")
print("saved comparison:", canvas.size)
