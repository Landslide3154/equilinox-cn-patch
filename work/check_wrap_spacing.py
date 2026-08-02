import re

def load(path):
    chars = {}
    for line in open(path, encoding="ascii", errors="replace"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
    return chars

chars = load(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")
PAD = 8
VERT = 0.04 / (64 - 16)
HOR = VERT / (1600.0 / 900.0)

def adv(cid):
    return (chars[cid]["xadvance"] - 2 * PAD) * HOR

# replicate patched createStructure
def layout(s, max_len):
    lines = []
    cur_line = []
    cur_len = 0.0
    cur_word = []
    cur_word_len = 0.0
    space_before = False
    for c in s:
        if c == " ":
            if cur_word:
                wl = cur_word_len
                add = wl if not cur_line or not space_before else wl + 39 * HOR
                if cur_len + add > max_len and cur_line:
                    lines.append((cur_line, cur_len))
                    cur_line = []
                    cur_len = 0.0
                cur_line.append(("".join(cur_word), space_before, cur_word_len))
                cur_len += (cur_word_len if not cur_line[:1] or not space_before else cur_word_len + 39 * HOR)
                cur_word = []
                cur_word_len = 0.0
            space_before = True
        else:
            cid = ord(c)
            if cid not in chars:
                continue
            if cur_word:
                # would adding this char overflow?
                space = 39 * HOR if space_before and cur_line else 0.0
                if cur_len + space + cur_word_len + adv(cid) > max_len:
                    # commit current word
                    if cur_line:
                        lines.append((cur_line, cur_len))
                    cur_line = [("".join(cur_word), space_before, cur_word_len)]
                    cur_len = cur_word_len
                    cur_word = []
                    cur_word_len = 0.0
                    space_before = False
            cur_word.append(c)
            cur_word_len += adv(cid)
    if cur_word:
        add = cur_word_len if not cur_line or not space_before else cur_word_len + 39 * HOR
        if cur_len + add > max_len and cur_line:
            lines.append((cur_line, cur_len))
            cur_line = []
            cur_len = 0.0
        cur_line.append(("".join(cur_word), space_before, cur_word_len))
        cur_len += cur_word_len
    lines.append((cur_line, cur_len))
    return lines

for s in ("植物的所有统计数据", "点击世界中的草来查看它的当前状态和其他选项", "或 W,A,S,D"):
    print("=== %s ===" % s)
    for li, (words, ln) in enumerate(layout(s, 0.12)):
        x = 0.0
        pos = ""
        for w, sb, wl in words:
            if sb:
                x += 39 * HOR
            pos += " |%s@%.2f" % (w, x)
            x += wl
        print("  line%d: %s" % (li, pos))
