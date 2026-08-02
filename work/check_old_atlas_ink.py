from PIL import Image

old = Image.open(r"D:\code\equilinox\Equilinox_CN_Patch\files\fonts\gill3.png").convert("RGBA")
A = old.split()[3].load()

# 开: x=1736, y=335, w=55, h=51 (pad 8)
x, y, w, h = 1736, 335, 55, 51
# print the top-left corner of the cell (rows 0-16, cols 0-20)
print("old atlas 开 cell top-left (x=1736,y=335):")
for yy in range(0, 18):
    print("".join("#" if A[x + xx, y + yy] > 60 else "." for xx in range(0, 24)))

# my atlas 开: x=283, y=790
mine = Image.open(r"D:\code\equilinox\build\res\guis\fonts\gill3.png").convert("RGBA")
M = mine.split()[3].load()
print("my atlas 开 cell top-left (x=283,y=790):")
for yy in range(0, 18):
    print("".join("#" if M[283 + xx, 790 + yy] > 60 else "." for xx in range(0, 24)))
