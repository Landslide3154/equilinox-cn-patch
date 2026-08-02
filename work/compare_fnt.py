import re

def load(path):
    chars = {}
    for line in open(path, encoding="ascii", errors="replace"):
        m = re.search(r"char id=(\d+)", line)
        if m:
            vals = dict((k, int(v)) for k, v in re.findall(r"(id|x|y|width|height|xoffset|yoffset|xadvance)=(-?\d+)", line))
            chars[vals["id"]] = vals
    return chars

old = load(r"D:\code\equilinox\Equilinox_CN_Patch\files\fonts\gill3.fnt")
mine = load(r"D:\code\equilinox\build\res\guis\fonts\gill3.fnt")

test_chars = "开 始 游 戏 任 务 完 成 退 出 加 载 世 界 操 作 选 项 保 存 你 好 ！ 。 ， 、 一 是 不"
for ch in test_chars.split():
    cid = ord(ch)
    o = old.get(cid)
    m = mine.get(cid)
    print("%s U+%04X" % (ch, cid))
    print("  OLD :", o)
    print("  MINE:", m)
