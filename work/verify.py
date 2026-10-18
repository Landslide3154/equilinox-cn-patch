# -*- coding: utf-8 -*-
"""Pre-release consistency checks for the Equilinox Chinese patch.

Run after the build steps and before packaging/releasing:

    python work/verify.py

It checks the language sheet, placeholders, glyph coverage, the class list,
the committed patch/ tree, and (when present) the release directory and zip.
"""
import hashlib
import io
import os
import re
import struct
import sys
import zipfile

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, os.path.join(BASE, "work"))

import patch_classes
import patch_strings
from version import PATCH_VERSION

CJK = re.compile(r"[\u4e00-\u9fff]")
PLACEHOLDER = re.compile(r"\$\d+\$", re.ASCII)
errors = []


def fail(msg):
    errors.append(msg)


def sha256(path):
    h = hashlib.sha256()
    with open(path, "rb") as f:
        for chunk in iter(lambda: f.read(1 << 20), b""):
            h.update(chunk)
    return h.hexdigest()


def read_rows(path):
    with io.open(path, "r", encoding="utf-8", newline="") as f:
        lines = f.read().splitlines()
    return lines[0], [line.split(";") for line in lines[1:] if line.strip()]


def fnt_chars(path):
    common = {}
    chars = set()
    declared = None
    with io.open(path, "r", encoding="ascii", errors="replace") as f:
        for line in f:
            if line.startswith("common "):
                for key in ("scaleW", "scaleH"):
                    m = re.search(key + r"=(\d+)", line)
                    if m:
                        common[key] = int(m.group(1))
            m = re.match(r"char id=(\d+)", line)
            if m:
                chars.add(int(m.group(1)))
            m = re.match(r"chars count=(\d+)", line)
            if m:
                declared = int(m.group(1))
    return common, chars, declared


def png_size(path):
    with open(path, "rb") as f:
        head = f.read(24)
    if head[:8] != b"\x89PNG\r\n\x1a\n":
        raise ValueError("not a PNG: " + path)
    return struct.unpack(">II", head[16:24])


def check_language():
    orig_header, orig = read_rows(os.path.join(BASE, "jar", "res", "languageSheet.csv"))
    build_header, build = read_rows(os.path.join(BASE, "build", "res", "languageSheet.csv"))
    if orig_header != build_header:
        fail("language sheet header changed: %r != %r" % (orig_header, build_header))
    if len(orig) != len(build):
        fail("language sheet row count %d != %d" % (len(orig), len(build)))
    oids = [int(r[0]) for r in orig]
    bids = [int(r[0]) for r in build]
    if oids != bids:
        fail("language sheet IDs/order differ")
    for row in build:
        if len(row) != 4:
            fail("row %s has %d fields" % (row[0], len(row)))
            continue
        text_id, chinese = row[0], row[2]
        if not chinese.strip():
            fail("row %s has empty translation" % text_id)
        if any(c in chinese for c in (";", "\t", "\r", "\n")):
            fail("row %s contains a CSV delimiter" % text_id)
        if "\ufffd" in chinese:
            fail("row %s contains U+FFFD" % text_id)
        if text_id in {r[0] for r in orig}:
            source = next(r[2] for r in orig if r[0] == text_id)
            if sorted(PLACEHOLDER.findall(source)) != sorted(PLACEHOLDER.findall(chinese)):
                fail("row %s placeholder mismatch: %r vs %r" %
                     (text_id, PLACEHOLDER.findall(source), PLACEHOLDER.findall(chinese)))
    print("language sheet: %d rows, %d issues" % (len(build), len(errors)))


def check_fonts():
    needed = set()
    _, rows = read_rows(os.path.join(BASE, "build", "res", "languageSheet.csv"))
    for row in rows:
        if len(row) >= 3:
            needed |= {ord(c) for c in row[2]}
    for value in patch_strings.STRING_MAP.values():
        needed |= {ord(c) for c in value}
    for mapping in patch_strings.CLASS_MAP.values():
        for value in mapping.values():
            needed |= {ord(c) for c in value}
    for fn in os.listdir(os.path.join(BASE, "work")):
        if fn.endswith(".java"):
            with io.open(os.path.join(BASE, "work", fn), encoding="utf-8") as f:
                needed |= {ord(c) for c in f.read() if ord(c) > 127}

    for name in ("gill3", "segoeUI"):
        fnt = os.path.join(BASE, "build", "res", "guis", "fonts", name + ".fnt")
        png = os.path.join(BASE, "build", "res", "guis", "fonts", name + ".png")
        if not os.path.exists(fnt) or not os.path.exists(png):
            fail("font pair missing: " + name)
            continue
        common, chars, declared = fnt_chars(fnt)
        if declared != len(chars):
            fail("%s chars count %r != %d" % (name, declared, len(chars)))
        missing = sorted(c for c in needed if c not in chars and c != 32)
        if missing:
            fail("%s missing glyphs: %r" % (name, [hex(c) for c in missing[:20]]))
        try:
            size = png_size(png)
            if (common.get("scaleW"), common.get("scaleH")) != size:
                fail("%s png size %r != fnt scale %r" %
                     (name, size, (common.get("scaleW"), common.get("scaleH"))))
        except Exception as exc:
            fail(str(exc))
        print("font %s: %d glyphs, %d missing" % (name, len(chars), len(missing)))


def check_classes():
    patch_classes.validate_maps()
    changed = os.path.join(BASE, "work", "changed_classes.txt")
    with io.open(changed, encoding="utf-8") as f:
        rels = [line.strip() for line in f if line.strip()]
    for rel in rels:
        path = os.path.join(BASE, "build", rel.replace("/", os.sep))
        if not os.path.exists(path):
            fail("changed class missing from build: " + rel)
            continue
        with open(path, "rb") as f:
            head = f.read(8)
        major = struct.unpack(">H", head[6:8])[0]
        if major != 52:
            fail("%s has class major %d" % (rel, major))
    print("class list: %d entries" % len(rels))


def check_tree_and_zip():
    import build_patch_small as builder
    entries = builder.ENTRIES
    for rel in entries:
        build = os.path.join(BASE, "build", rel.replace("/", os.sep))
        patch = os.path.join(BASE, "patch", rel.replace("/", os.sep))
        if not os.path.exists(build):
            fail("shipped entry missing from build: " + rel)
        if not os.path.exists(patch):
            fail("shipped entry missing from patch/: " + rel)
        elif os.path.exists(build) and sha256(build) != sha256(patch):
            fail("patch/ differs from build: " + rel)

    pkg = os.path.join(BASE, "发布", "Equilinox汉化补丁")
    for rel in entries:
        cached = os.path.join(pkg, "patch", rel.replace("/", os.sep))
        build = os.path.join(BASE, "build", rel.replace("/", os.sep))
        if os.path.exists(pkg) and os.path.exists(cached) and os.path.exists(build):
            if sha256(cached) != sha256(build):
                fail("release dir differs from build: " + rel)
    for name in ("安装汉化.bat", "恢复原版.bat", "使用说明.txt", "LICENSE"):
        if os.path.exists(pkg) and not os.path.exists(os.path.join(pkg, name)):
            fail("release dir missing " + name)

    zips = sorted(
        (os.path.join(BASE, "发布", n) for n in os.listdir(os.path.join(BASE, "发布"))
         if n.startswith("Equilinox汉化补丁_") and n.endswith(".zip")),
        key=os.path.getmtime,
    ) if os.path.isdir(os.path.join(BASE, "发布")) else []
    if zips:
        zpath = zips[-1]
        with zipfile.ZipFile(zpath) as zf:
            names = set(zf.namelist())
            if "Equilinox汉化补丁/LICENSE" not in names:
                fail("zip missing LICENSE")
            for rel in entries:
                arc = "Equilinox汉化补丁/patch/" + rel
                if arc not in names:
                    fail("zip missing " + arc)
                else:
                    disk = os.path.join(pkg, "patch", rel.replace("/", os.sep))
                    if os.path.exists(disk) and sha256(disk) != hashlib.sha256(zf.read(arc)).hexdigest():
                        fail("zip differs from release dir: " + rel)
        print("zip:", os.path.basename(zpath), len(names), "entries")
    print("patch tree: %d entries" % len(entries))


def main():
    check_language()
    check_fonts()
    check_classes()
    check_tree_and_zip()
    if errors:
        print("\nFAILED (%d):" % len(errors))
        for e in errors:
            print("  - " + e)
        raise SystemExit(1)
    print("\nall checks passed")


if __name__ == "__main__":
    main()
