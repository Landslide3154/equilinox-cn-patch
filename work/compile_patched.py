# -*- coding: utf-8 -*-
"""Compile the hand-written patch classes for the game's Java 8 runtime.

Several sources are named *_patched.java but declare a public class with the
original name, so they are copied to a temp directory under their real file
name before javac runs.  The build deliberately targets --release 8: the game
classes are major version 52, and compiling with a modern default (major 69)
produces classes the game cannot load.
"""
import os
import shutil
import struct
import subprocess
import sys
import tempfile

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WORK = os.path.join(BASE, "work")
JAR = os.path.join(BASE, "jar")
OUT = os.path.join(BASE, "build")

SOURCES = {
    "Word.java": "Word.java",
    "Line_patched.java": "Line.java",
    "MyFile_patched.java": "MyFile.java",
    "TextLoader_patched.java": "TextLoader.java",
    "TimeDisplay.java": "TimeDisplay.java",
    "GillCalculator.java": "GillCalculator.java",
    "SegoeCalculator.java": "SegoeCalculator.java",
}

EXPECTED = [
    "fontRendering/Word.class",
    "fontRendering/Line.class",
    "fontRendering/TextLoader.class",
    "fontRendering/GillCalculator.class",
    "fontRendering/SegoeCalculator.class",
    "utils/MyFile.class",
    "bottomBar/TimeDisplay.class",
]


def class_major(path):
    with open(path, "rb") as f:
        head = f.read(8)
    return struct.unpack(">H", head[6:8])[0]


def main():
    javac = shutil.which("javac")
    if not javac:
        raise SystemExit("javac not found; install a JDK 8+ and put it on PATH")
    os.makedirs(OUT, exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="equilinox_javac_") as tmp:
        src_dir = os.path.join(tmp, "src")
        os.makedirs(src_dir)
        for src_name, real_name in SOURCES.items():
            src = os.path.join(WORK, src_name)
            if not os.path.exists(src):
                raise SystemExit("missing source: " + src)
            shutil.copy2(src, os.path.join(src_dir, real_name))
        cmd = [javac, "--release", "8", "-encoding", "UTF-8",
               "-cp", JAR, "-d", OUT] + [os.path.join(src_dir, n) for n in SOURCES.values()]
        print(" ".join('"%s"' % c if " " in c else c for c in cmd))
        subprocess.check_call(cmd)

    for rel in EXPECTED:
        path = os.path.join(OUT, rel.replace("/", os.sep))
        if not os.path.exists(path):
            raise SystemExit("javac did not produce " + rel)
        major = class_major(path)
        if major != 52:
            raise SystemExit("%s has class major %d, expected 52" % (rel, major))
    print("compiled %d classes, all major 52" % len(EXPECTED))


if __name__ == "__main__":
    main()
