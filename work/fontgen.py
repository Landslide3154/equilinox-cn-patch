import io
import os
import re
from PIL import Image, ImageDraw, ImageFont

base = r"D:\code\equilinox"
FONTS_SRC = os.path.join(base, "jar", "res", "guis", "fonts")
FONTS_DST = os.path.join(base, "build", "res", "guis", "fonts")
MSYH = r"C:\Windows\Fonts\方正准圆_GBK.ttf"


def parse_fnt(path):
    lines = []
    chars = {}
    header = {}
    with io.open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.rstrip("\r\n")
            lines.append(line)
            m = re.search(r"char id=(\d+)", line)
            if m:
                vals = {}
                for k in ("id", "x", "y", "width", "height", "xoffset", "yoffset", "xadvance"):
                    km = re.search(k + r"=(-?\d+)", line)
                    vals[k] = int(km.group(1))
                chars[vals["id"]] = vals
            if line.startswith("common "):
                for k in ("lineHeight", "base", "scaleW", "scaleH"):
                    km = re.search(k + r"=(\d+)", line)
                    header[k] = int(km.group(1))
    return lines, chars, header


def shift_original_line(line, dy):
    """Shift yoffset of an original (embedded) char entry by dy."""
    def repl(m):
        return m.group(1) + str(int(m.group(2)) + dy)
    return re.sub(r"(yoffset=)(-?\d+)", repl, line)


def render_char(ch, font_path, size, pad, baseline_line_top):
    # Supersample at high resolution and downscale: wider anti-aliasing
    # gradient in the bitmap, so the game's shader renders smooth edges
    # without thinning. This is done at build time, not at runtime.
    scale = 16
    font = ImageFont.truetype(font_path, size * scale)
    bbox = font.getbbox(ch, anchor="ls")
    xminS, yminS, xmaxS, ymaxS = bbox
    advS = font.getlength(ch)
    ink_wS = xmaxS - xminS
    ink_hS = ymaxS - yminS
    if ink_wS <= 0 or ink_hS <= 0:
        return None
    cellS_w = ink_wS + 2 * scale * pad
    cellS_h = ink_hS + 2 * scale * pad
    imgS = Image.new("RGBA", (cellS_w, cellS_h), (0, 0, 0, 0))
    d = ImageDraw.Draw(imgS)
    d.text((scale * pad - xminS, scale * pad - yminS), ch, font=font, fill=(255, 255, 255, 255), anchor="ls")
    img = imgS.resize((cellS_w // scale, cellS_h // scale), Image.LANCZOS)
    # BMFont-style metrics (converted back to the 1x coordinate space)
    xoffset = int(round(xminS / float(scale))) - pad
    yoffset = int(round((scale * baseline_line_top + yminS) / float(scale))) - pad
    xadvance = int(round(advS / float(scale))) + 2 * pad + 2
    return img, dict(x=xminS // scale, y=yminS // scale, width=ink_wS // scale, height=ink_hS // scale,
                     xoffset=xoffset, yoffset=yoffset, xadvance=xadvance)


def generate(font_name, size, pad, baseline_line_top, atlas_w, new_chars, orig_lines, orig_chars):
    atlas = Image.new("RGBA", (atlas_w, atlas_w), (0, 0, 0, 0))
    orig_png = os.path.join(FONTS_SRC, font_name + ".png")
    orig_img = Image.open(orig_png).convert("RGBA")
    atlas.paste(orig_img, (0, 0))

    cursor_x = 0
    cursor_y = 512
    row_h = 0
    placed = []
    for cid in sorted(new_chars):
        ch = chr(cid)
        res = render_char(ch, MSYH, size, pad, baseline_line_top)
        if res is None:
            print("skip char", hex(ord(ch)))
            continue
        img, metrics = res
        cw, chh = img.size
        if cursor_x + cw > atlas_w:
            cursor_x = 0
            cursor_y += row_h
            row_h = 0
        if cursor_y + chh > atlas_w:
            print("ATLAS OVERFLOW at", atlas_w, "char", hex(ord(ch)))
            return False
        atlas.paste(img, (cursor_x, cursor_y))
        # IMPORTANT: record the CELL top-left corner (cursor_x/cursor_y), NOT
        # the ink start. The game's parser treats x/y as the padded cell top-left
        # and samples [x-2, x+width+2]; recording the ink start here made every
        # glyph quad reach 2px into the NEXT glyph's ink (bleeding fragments).
        x = cursor_x
        y = cursor_y
        metrics_out = dict(metrics)
        metrics_out["x"] = x
        metrics_out["y"] = y
        metrics_out["width"] = metrics["width"] + 2 * pad
        metrics_out["height"] = metrics["height"] + 2 * pad
        placed.append((cid, metrics_out))
        cursor_x += cw
        row_h = max(row_h, chh)

    os.makedirs(FONTS_DST, exist_ok=True)
    atlas.save(os.path.join(FONTS_DST, font_name + ".png"))

    # rebuild fnt
    header_new = []
    for line in orig_lines:
        if line.startswith("common "):
            line = re.sub(r"scaleW=\d+", "scaleW=%d" % atlas_w, line)
            line = re.sub(r"scaleH=\d+", "scaleH=%d" % atlas_w, line)
            header_new.append(line)
        elif line.startswith("chars count="):
            header_new.append("chars count=%d" % (len(orig_chars) + len(placed)))
        elif line.startswith("char id="):
            pass
        else:
            header_new.append(line)
    # keep original char lines verbatim
    for line in orig_lines:
        if line.startswith("char id="):
            header_new.append(line)
    for cid, m in placed:
        header_new.append(
            "char id=%d   x=%d     y=%d     width=%d     height=%d     xoffset=%d     yoffset=%d    xadvance=%d     page=0  chnl=0 " % (
                cid, m["x"], m["y"], m["width"], m["height"], m["xoffset"], m["yoffset"], m["xadvance"]))
    with io.open(os.path.join(FONTS_DST, font_name + ".fnt"), "w", encoding="ascii", newline="") as f:
        f.write("\r\n".join(header_new) + "\r\n")
    print("%s: placed %d new glyphs, atlas %dx%d" % (font_name, len(placed), atlas_w, atlas_w))
    return True


def main():
    # collect needed chars: translations + patched strings
    import patch_strings
    texts = []
    with io.open(os.path.join(base, "build", "res", "languageSheet.csv"), "r", encoding="utf-8") as f:
        for line in f:
            parts = line.split(";")
            if len(parts) >= 3 and parts[0].isdigit():
                texts.append(parts[2])
    for old, new in patch_strings.STRING_MAP.items():
        texts.append(new)
    needed = set()
    for t in texts:
        for ch in t:
            needed.add(ord(ch))

    for font_name, size, pad, baseline in (("segoeUI", 52, 10, 60), ("gill3", 39, 8, 38)):
        orig_lines, orig_chars, header = parse_fnt(os.path.join(FONTS_SRC, font_name + ".fnt"))
        new_chars = [c for c in needed if c not in orig_chars and c != 32]
        print("%s needs %d new chars" % (font_name, len(new_chars)))
        ok = False
        for atlas_w in (2048, 4096):
            ok = generate(font_name, size, pad, baseline, atlas_w, new_chars, orig_lines, orig_chars)
            if ok:
                break
        if not ok:
            raise SystemExit("ATLAS TOO SMALL for %s" % font_name)


if __name__ == "__main__":
    main()
