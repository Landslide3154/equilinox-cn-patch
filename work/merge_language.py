# -*- coding: utf-8 -*-
"""Merge the four translation shards into build/res/languageSheet.csv.

The original language sheet stays the authority for row order, IDs and
placeholders.  Any missing, duplicated or extra translation is a build error:
shipping a half-merged sheet is much worse than stopping here.
"""
import io
import os
import re

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ORIG = os.path.join(BASE, "jar", "res", "languageSheet.csv")
OUT = os.path.join(BASE, "build", "res", "languageSheet.csv")
CHUNKS = ["zh_1.tsv", "zh_2.tsv", "zh_3.tsv", "zh_4.tsv"]
PLACEHOLDER = re.compile(r"\$\d+\$", re.ASCII)

translations = {}
for chunk in CHUNKS:
    path = os.path.join(BASE, "work", chunk)
    with io.open(path, "r", encoding="utf-8") as f:
        for lineno, line in enumerate(f, 1):
            line = line.rstrip("\r\n")
            if not line:
                continue
            parts = line.split("\t", 1)
            if len(parts) != 2:
                raise SystemExit("%s:%d: expected ID<TAB>text" % (chunk, lineno))
            try:
                text_id = int(parts[0])
            except ValueError:
                raise SystemExit("%s:%d: bad ID %r" % (chunk, lineno, parts[0]))
            if text_id in translations:
                raise SystemExit("%s:%d: duplicate ID %d" % (chunk, lineno, text_id))
            text = re.sub(r"\^([^^]*)\^", r"\1", parts[1])
            if not text.strip():
                raise SystemExit("%s:%d: empty translation" % (chunk, lineno))
            if any(c in text for c in (";", "\t", "\r", "\n")):
                raise SystemExit("%s:%d: translation contains a CSV delimiter: %r"
                                 % (chunk, lineno, text))
            translations[text_id] = text

with io.open(ORIG, "r", encoding="utf-8") as f:
    lines = f.read().splitlines()

header = lines[0]
rows = []
ids = []
missing = []
bad_placeholders = []
for line in lines[1:]:
    if not line.strip():
        continue
    parts = line.split(";")
    text_id = int(parts[0])
    ids.append(text_id)
    desc = parts[1]
    english = parts[2] if len(parts) > 2 else ""
    testval = parts[3] if len(parts) > 3 and parts[3] else "TestVal%d" % text_id
    if text_id not in translations:
        missing.append(text_id)
        continue
    chinese = translations[text_id]
    if sorted(PLACEHOLDER.findall(english)) != sorted(PLACEHOLDER.findall(chinese)):
        bad_placeholders.append((text_id, PLACEHOLDER.findall(english),
                                 PLACEHOLDER.findall(chinese)))
    rows.append("%d;%s;%s;%s" % (text_id, desc, chinese, testval))

if missing:
    raise SystemExit("MISSING TRANSLATIONS: %r" % missing)
if bad_placeholders:
    raise SystemExit("PLACEHOLDER MISMATCH: %r" % bad_placeholders)
extra = sorted(set(translations) - set(ids))
if extra:
    raise SystemExit("EXTRA TRANSLATIONS NOT IN ORIGINAL: %r" % extra)

os.makedirs(os.path.dirname(OUT), exist_ok=True)
with io.open(OUT, "w", encoding="utf-8", newline="") as f:
    f.write(header + "\r\n")
    for row in rows:
        f.write(row + "\r\n")

print("Wrote %d rows -> %s" % (len(rows), OUT))
