# Equilinox 补丁构建链路：编译、改动方式与脚本顺序

> 自仓库根 `AGENTS.md` §2「常用命令」外移（2026-10-24），搬移的正文逐字保留；命令表与七节骨架仍在 `../AGENTS.md`。
>
> 本文件不注入每轮上下文——改补丁源码、或补丁进游戏没生效时来这里。

## 1. 编译必须走 compile_patched.py

- 编译必须走 `compile_patched.py`：它把 `*_patched.java` 复制成真实类名，并固定 `javac --release 8`；直接 `javac work/Line_patched.java` 会因 public 类名与文件名不一致而失败，用默认 `javac` 还会产出 Java 8 加载不了的 major 69。

## 2. 改动方式：改逻辑 vs 改文本

- 只有改逻辑才写 `.java`；纯文本替换改 `work/patch_strings.py` 的映射表即可。

## 3. 脚本顺序固定（不串联，但顺序不能换）

- 脚本之间**不互相串联**，但顺序固定：`merge_language` → `patch_classes` → `patch_maths` → `compile_patched` → `fontgen` → `repack` / `build_patch_small` → `verify`。改完补丁进游戏没变化多半是漏跑某步。
