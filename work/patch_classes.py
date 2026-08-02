# -*- coding: utf-8 -*-
"""Rewrite class-file constant pools to replace hardcoded strings."""
import io
import os
import struct

import patch_strings

BASE = r"D:\code\equilinox"
SRC = os.path.join(BASE, "jar")
DST = os.path.join(BASE, "build")


def modified_utf8(s):
    out = bytearray()
    for ch in s:
        cp = ord(ch)
        if cp == 0:
            out += b"\xc0\x80"
        elif cp < 0x80:
            out.append(cp)
        elif cp < 0x800:
            out.append(0xC0 | (cp >> 6))
            out.append(0x80 | (cp & 0x3F))
        elif cp < 0x10000:
            out.append(0xE0 | (cp >> 12))
            out.append(0x80 | ((cp >> 6) & 0x3F))
            out.append(0x80 | (cp & 0x3F))
        else:
            cp -= 0x10000
            hi = 0xD800 + (cp >> 10)
            lo = 0xDC00 + (cp & 0x3FF)
            for u in (hi, lo):
                out.append(0xE0 | (u >> 12))
                out.append(0x80 | ((u >> 6) & 0x3F))
                out.append(0x80 | (u & 0x3F))
    return bytes(out)


def parse_pool(data):
    assert data[:4] == b"\xca\xfe\xba\xbe", "not a class file"
    count = struct.unpack(">H", data[8:10])[0]
    entries = []
    pos = 10
    i = 1
    while i < count:
        tag = data[pos]
        if tag == 1:
            ln = struct.unpack(">H", data[pos + 1:pos + 3])[0]
            raw = data[pos:pos + 3 + ln]
            content = raw[3:].decode("utf-8", errors="replace")
            entries.append((tag, raw, content))
            pos += 3 + ln
        elif tag in (3, 4):
            entries.append((tag, data[pos:pos + 5], None))
            pos += 5
        elif tag in (5, 6):
            entries.append((tag, data[pos:pos + 9], None))
            pos += 9
            i += 1  # long/double take two slots
        elif tag in (7, 8, 16, 19, 20):
            entries.append((tag, data[pos:pos + 3], None))
            pos += 3
        elif tag in (9, 10, 11, 12, 17, 18):
            entries.append((tag, data[pos:pos + 5], None))
            pos += 5
        elif tag == 15:
            entries.append((tag, data[pos:pos + 4], None))
            pos += 4
        else:
            raise ValueError("unknown tag %d at %d" % (tag, pos))
        i += 1
    return count, entries, pos


def rebuild(header, count, entries, tail):
    out = bytearray()
    out += header
    out += struct.pack(">H", count)
    for tag, raw, content in entries:
        out += raw
    out += tail
    return bytes(out)


def patch_class(path, replacements):
    with io.open(path, "rb") as f:
        data = f.read()
    header = data[:8]
    count, entries, tail_pos = parse_pool(data)
    tail = data[tail_pos:]
    changed = False
    for idx, (tag, raw, content) in enumerate(entries):
        if tag == 1 and content in replacements:
            new_bytes = modified_utf8(replacements[content])
            new_raw = b"\x01" + struct.pack(">H", len(new_bytes)) + new_bytes
            entries[idx] = (tag, new_raw, content)
            changed = True
    if not changed:
        return None
    return rebuild(header, count, entries, tail)


def main():
    changed_files = []
    replaced_count = 0
    for root, dirs, files in os.walk(SRC):
        for fn in files:
            if not fn.endswith(".class"):
                continue
            full = os.path.join(root, fn)
            rel = os.path.relpath(full, SRC).replace("\\", "/")
            if rel.startswith(patch_strings.EXCLUDE):
                continue
            replacements = dict(patch_strings.STRING_MAP)
            replacements.update(patch_strings.CLASS_MAP.get(rel, {}))
            if not replacements:
                continue
            new_data = patch_class(full, replacements)
            if new_data is None:
                continue
            dst = os.path.join(DST, rel)
            os.makedirs(os.path.dirname(dst), exist_ok=True)
            with io.open(dst, "wb") as f:
                f.write(new_data)
            changed_files.append(rel)
            # count actual replaced strings
            for old, new in replacements.items():
                if old.encode("utf-8") in new_data:
                    replaced_count += 1
    print("patched %d class files" % len(changed_files))
    with io.open(os.path.join(BASE, "work", "changed_classes.txt"), "w", encoding="utf-8") as f:
        f.write("\n".join(sorted(changed_files)))


if __name__ == "__main__":
    main()
