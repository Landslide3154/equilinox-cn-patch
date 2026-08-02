from PIL import Image, ImageDraw, ImageFont

MSYH = r"C:\Windows\Fonts\msyh.ttc"
size = 39
pad = 8
font = ImageFont.truetype(MSYH, size)

for ch in ("你", "永", "一", "。", "是"):
    bbox = font.getbbox(ch, anchor="ls")
    xmin, ymin, xmax, ymax = bbox
    ink_w = xmax - xmin
    ink_h = ymax - ymin
    cell_w = ink_w + 2 * pad
    cell_h = ink_h + 2 * pad
    img = Image.new("RGBA", (cell_w, cell_h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.text((pad - xmin, pad - ymin), ch, font=font, fill=(255, 255, 255, 255), anchor="ls")
    a = img.split()[3]
    pa = a.load()
    # find actual ink bbox in the cell
    minx = miny = 10 ** 9
    maxx = maxy = -1
    for yy in range(cell_h):
        for xx in range(cell_w):
            if pa[xx, yy] > 10:
                minx = min(minx, xx)
                maxx = max(maxx, xx)
                miny = min(miny, yy)
                maxy = max(maxy, yy)
    print("%s bbox=(%d,%d,%d,%d) inkInCell=(%d,%d)-(%d,%d) expectedLeft=%d expectedTop=%d"
          % (ch, xmin, ymin, xmax, ymax, minx, miny, maxx, maxy, pad, pad))
