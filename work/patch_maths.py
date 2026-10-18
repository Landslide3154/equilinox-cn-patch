# -*- coding: utf-8 -*-
"""Patch toolbox/Maths.formatNumber to group digits in fours.

The game groups thousands with `% 3`; changing that one `iconst_3` to
`iconst_4` gives the Chinese four-digit grouping without recompiling the
large Maths class or changing any other method.  The class file is otherwise
copied byte-for-byte, so StackMapTable/debug data stay valid.
"""
import io
import os
import struct
import sys

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(BASE, "jar", "toolbox", "Maths.class")
DST = os.path.join(BASE, "build", "toolbox", "Maths.class")


def fail(msg):
    raise SystemExit(msg)


def u2(data, pos):
    return struct.unpack(">H", data[pos:pos + 2])[0], pos + 2


def u4(data, pos):
    return struct.unpack(">I", data[pos:pos + 4])[0], pos + 4


def read_pool(data):
    if data[:4] != b"\xca\xfe\xba\xbe":
        fail("not a class file: " + SRC)
    count = struct.unpack(">H", data[8:10])[0]
    cp = {}
    pos = 10
    i = 1
    while i < count:
        tag = data[pos]
        if tag == 1:
            ln = struct.unpack(">H", data[pos + 1:pos + 3])[0]
            cp[i] = data[pos + 3:pos + 3 + ln].decode("utf-8", errors="replace")
            pos += 3 + ln
        elif tag in (3, 4):
            pos += 5
        elif tag in (5, 6):
            pos += 9
            i += 1
        elif tag in (7, 8, 16, 19, 20):
            pos += 3
        elif tag in (9, 10, 11, 12, 17, 18):
            pos += 5
        elif tag == 15:
            pos += 4
        else:
            fail("unknown constant-pool tag %d at %d" % (tag, pos))
        i += 1
    return cp, pos


def main():
    with io.open(SRC, "rb") as f:
        data = bytearray(f.read())
    cp, pos = read_pool(data)

    pos += 6  # access_flags, this_class, super_class
    interfaces, pos = u2(data, pos)
    pos += 2 * interfaces

    fields, pos = u2(data, pos)
    for _ in range(fields):
        pos += 6
        attrs, pos = u2(data, pos)
        for _ in range(attrs):
            pos += 2
            ln, pos = u4(data, pos)
            pos += ln

    methods, pos = u2(data, pos)
    target = None
    for _ in range(methods):
        access, pos = u2(data, pos)
        name_idx, pos = u2(data, pos)
        desc_idx, pos = u2(data, pos)
        attrs, pos = u2(data, pos)
        name = cp.get(name_idx, "")
        desc = cp.get(desc_idx, "")
        code = None
        for _ in range(attrs):
            attr_name_idx, pos = u2(data, pos)
            ln, pos = u4(data, pos)
            body = pos
            if cp.get(attr_name_idx) == "Code":
                code = body
            pos += ln
        if name == "formatNumber" and desc == "(J)Ljava/lang/String;":
            if code is None:
                fail("formatNumber has no Code attribute")
            target = code

    if target is None:
        fail("formatNumber not found")

    max_stack, p = u2(data, target)
    max_locals, p = u2(data, p)
    code_len, p = u4(data, p)
    code_start = p
    code = data[code_start:code_start + code_len]
    pattern = b"\x06\x70"  # iconst_3; irem
    hits = [i for i in range(len(code) - 1) if code[i:i + 2] == pattern]
    if len(hits) != 1:
        fail("expected one iconst_3/irem in formatNumber, found %d" % len(hits))
    data[code_start + hits[0]] = 0x07  # iconst_4
    print("patched formatNumber: iconst_3 -> iconst_4 at code offset %d" % hits[0])
    major = struct.unpack(">H", data[6:8])[0]
    if major != 52:
        fail("Maths class major %d, expected 52" % major)

    os.makedirs(os.path.dirname(DST), exist_ok=True)
    with io.open(DST, "wb") as f:
        f.write(data)
    print("wrote", DST)


if __name__ == "__main__":
    main()
