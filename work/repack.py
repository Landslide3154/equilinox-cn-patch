import io
import os
import shutil
import zipfile

base = r"D:\code\equilinox"
original = os.path.join(base, "original", "EquilinoxWindows_original.jar")
dest = os.path.join(base, "build", "EquilinoxWindows.jar")

changed = {}


def add(name, path):
    changed[name] = path


# language sheet
add("res/languageSheet.csv", os.path.join(base, "build", "res", "languageSheet.csv"))
# fonts
for n in ("segoeUI.fnt", "segoeUI.png", "gill3.fnt", "gill3.png"):
    add("res/guis/fonts/" + n, os.path.join(base, "build", "res", "guis", "fonts", n))
# patched MyFile
add("utils/MyFile.class", os.path.join(base, "build", "utils", "MyFile.class"))
# recompiled font-layout classes (CJK line wrapping fix)
add("fontRendering/Word.class", os.path.join(base, "build", "fontRendering", "Word.class"))
add("fontRendering/Line.class", os.path.join(base, "build", "fontRendering", "Line.class"))
add("fontRendering/TextLoader.class", os.path.join(base, "build", "fontRendering", "TextLoader.class"))
add("fontRendering/GillCalculator.class", os.path.join(base, "build", "fontRendering", "GillCalculator.class"))
add("fontRendering/SegoeCalculator.class", os.path.join(base, "build", "fontRendering", "SegoeCalculator.class"))
# recompiled time display (format: 第N年,第N天 - HH:MM)
add("bottomBar/TimeDisplay.class", os.path.join(base, "build", "bottomBar", "TimeDisplay.class"))
# recompiled toolbar Dp-per-minute counter (+692 与 dp/分 间距修复)
add("toolbar/DppmCounter.class", os.path.join(base, "build", "toolbar", "DppmCounter.class"))
# patched classes
with io.open(os.path.join(base, "work", "changed_classes.txt"), encoding="utf-8") as f:
    for rel in f.read().splitlines():
        p = os.path.join(base, "build", rel.replace("/", os.sep))
        if os.path.exists(p):
            add(rel, p)
        else:
            print("MISSING build file:", rel)

zin = zipfile.ZipFile(original, "r")
zout = zipfile.ZipFile(dest, "w", allowZip64=True)
replaced = 0
for item in zin.infolist():
    name = item.filename
    if name in changed:
        with io.open(changed[name], "rb") as f:
            data = f.read()
        zout.writestr(zipfile.ZipInfo(name, date_time=(2020, 1, 1, 0, 0, 0)), data,
                      compress_type=zipfile.ZIP_DEFLATED)
        replaced += 1
    else:
        zout.writestr(item, zin.read(name))
zin.close()
zout.close()
print("replaced %d entries; output size: %.1f MB" % (replaced, os.path.getsize(dest) / 1048576))

# sanity: open and verify entries
with zipfile.ZipFile(dest) as z:
    for n in ("res/languageSheet.csv", "res/guis/fonts/gill3.fnt", "res/guis/fonts/segoeUI.png",
              "utils/MyFile.class", "main/MainApp.class"):
        print("check", n, "->", z.getinfo(n).file_size, "bytes")
