from PIL import Image

img = Image.open(r"D:\code\equilinox\work\menu2.png").convert("RGB")
w, h = img.size
px = img.load()

# Menu buttons: big beige rounded panels. Detect via color similarity to
# typical beige (GameMenu uses beige button colour ~ (210,205,180)).
def is_beige(p):
    r, g, b = p
    return 170 < r < 245 and 160 < g < 240 and 130 < b < 225 and r >= g >= b

# Scan for connected components of beige pixels, report bounding boxes with area > 4000.
visited = set()
boxes = []
for y in range(0, h, 2):
    for x in range(0, w, 2):
        if (x, y) in visited or not is_beige(px[x, y]):
            continue
        stack = [(x, y)]
        visited.add((x, y))
        minx = maxx = x
        miny = maxy = y
        cnt = 0
        while stack:
            cx, cy = stack.pop()
            cnt += 1
            minx, maxx = min(minx, cx), max(maxx, cx)
            miny, maxy = min(miny, cy), max(maxy, cy)
            for dx in (-2, 0, 2):
                for dy in (-2, 0, 2):
                    nx, ny = cx + dx, cy + dy
                    if 0 <= nx < w and 0 <= ny < h and (nx, ny) not in visited and is_beige(px[nx, ny]):
                        visited.add((nx, ny))
                        stack.append((nx, ny))
        if cnt > 1500:
            boxes.append((minx, miny, maxx, maxy, cnt))

boxes.sort(key=lambda b: (b[1], b[0]))
for b in boxes:
    print("box x[%d,%d] y[%d,%d] px=%d  center=(%d,%d)" % (b[0], b[2], b[1], b[3], b[4], (b[0] + b[2]) // 2, (b[1] + b[3]) // 2))
