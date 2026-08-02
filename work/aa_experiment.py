from PIL import Image, ImageDraw, ImageFilter, ImageFont

FONT = r"C:\Windows\Fonts\方正准圆_GBK.ttf"
SIZE = 39
PAD = 8

def render_native():
    font = ImageFont.truetype(FONT, SIZE)
    bbox = font.getbbox("开", anchor="ls")
    xmin, ymin, xmax, ymax = bbox
    w, h = xmax - xmin + 2 * PAD, ymax - ymin + 2 * PAD
    img = Image.new("L", (w, h), 0)
    d = ImageDraw.Draw(img)
    d.text((PAD - xmin, PAD - ymin), "开", font=font, fill=255, anchor="ls")
    return img

def render_2x():
    font = ImageFont.truetype(FONT, SIZE * 2)
    bbox = font.getbbox("开", anchor="ls")
    xmin, ymin, xmax, ymax = bbox
    w, h = xmax - xmin + 4 * PAD, ymax - ymin + 4 * PAD
    img = Image.new("L", (w, h), 0)
    d = ImageDraw.Draw(img)
    d.text((2 * PAD - xmin, 2 * PAD - ymin), "开", font=font, fill=255, anchor="ls")
    img = img.resize((w // 2, h // 2), Image.LANCZOS)
    return img

def render_blur():
    img = render_native()
    return img.filter(ImageFilter.GaussianBlur(0.7))

def shader(dist, e0=0.44, e1=0.54):
    t = max(0.0, min(1.0, (dist - e0) / (e1 - e0)))
    return t * t * (3.0 - 2.0 * t)

for name, img in (("native", render_native()), ("2x supersample", render_2x()), ("blur 0.7", render_blur())):
    a = img
    # shader output
    out = a.point(lambda v: int(255 * shader(v / 255.0)))
    # horizontal profile through the top bar of 开 (row with max ink)
    profile_row = None
    best = 0
    for y in range(img.height):
        cnt = sum(1 for x in range(img.width) if out.getpixel((x, y)) > 128)
        if cnt > best:
            best = cnt
            profile_row = y
    row = [out.getpixel((x, profile_row)) for x in range(img.width)]
    # count transition pixels (not 0 and not 255)
    trans = sum(1 for v in row if 0 < v < 255)
    # glyph weight: total opaque pixels in shader output
    weight = sum(1 for y in range(img.height) for x in range(img.width) if out.getpixel((x, y)) > 128)
    print("%s: shader profile row=%d transitions=%d weight(px)=%d" % (name, profile_row, trans, weight))
    print("  profile:", [v for v in row if v > 0])
    print("  ascii:")
    for y in range(img.height):
        print("  " + "".join("#" if out.getpixel((x, y)) > 128 else "." for x in range(img.width)))
