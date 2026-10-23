# Equilinox 汉化补丁 — 开发约定

> 跨机器通用规则（git、推送、shell、编码）见全局 `AGENTS.md`，本文件不重复；本机路径、游戏版本、tag、产物实测值见 `AGENTS.local.md`。

## 1. 这个项目是什么

Steam 游戏 **Equilinox**（ThinMatrix）的简体中文补丁：**Java 字节码补丁 + 资源补丁**，不重打包、不夹带游戏本体。仓库 `Landslide3154/equilinox-cn-patch`。

链路：`jar/`（原版解包树，只读输入）→ `build/`（脚本产出）→ `patch/`（已编译补丁类，入库、勿手改）→ `发布/`（zip）。`work/` 是主战场：补丁源 `*.java`、翻译分片 `zh_1..4.tsv`、字符串映射 `patch_strings.py`、类清单 `changed_classes.txt` 与各构建脚本；`decompiled/`、`original/` 只读参考。

游戏本体与发布资产不进仓库（`.gitignore` 忽略 `build/`、`jar/`、`original/`、`发布/`，只反向跟踪 `patch/**` 与 `docs/*.png`）。

## 2. 常用命令（仓库根目录执行）

| 目的 | 命令 |
| --- | --- |
| 合并分片翻译 → `build/res/languageSheet.csv` | `python work/merge_language.py` |
| 生成字体（需 Pillow）→ `build/res/guis/fonts/` | `python work/fontgen.py` |
| 改写字节码常量池 → `build/**`，并重写 `work/changed_classes.txt` | `python work/patch_classes.py` |
| 把数字分组改成中文四位 → `build/toolbox/Maths.class` | `python work/patch_maths.py` |
| 编译要改逻辑的补丁类 | `python work/compile_patched.py`（固定 `javac --release 8`，产出 major 52） |
| 打完整 jar（本地实测用，不入库） | `python work/repack.py` |
| 产出发布目录与 zip | `python work/build_patch_small.py` |
| 发布前一致性校验 | `python work/verify.py` |

- 编译要求（必须走 `compile_patched.py`）、改逻辑 vs 改文本、脚本固定顺序与漏跑排查 → [docs/build-pipeline.md](docs/build-pipeline.md)

## 3. 硬约束（必须 / 禁止）

- 必须：`languageSheet.csv` **全流程按 UTF-8 处理**（合并、字体生成、入库与进包的那几份都是；GBK 解码实测报错）。**只有发出去的** `安装汉化.bat` / `恢复原版.bat` / `使用说明.txt` 要 GBK（`write_gbk()`）。
- 必须：换字体时 `.fnt` 字符表与 `.png` 图集**两件一起换**（`.fnt` 存码位与图集坐标，错位就出乱码块）。
- 必须：`changed_classes.txt` 与实际补丁类保持一致——`patch_classes.py` 会自动重写，手工新增的类要自己补进清单，否则不进包。
- 必须：出包前装进 Steam 版本地实测——主菜单 / 选项 / 图鉴与任务文本全中文、字体清晰、长文本换行整齐、存档名与底部时间已汉化；再跑一遍 `恢复原版.bat` 确认能还原。
- 禁止：发布包含游戏本体或任何无关类；README 与 Release 写明「需自行购买正版、仅修改本地文件」。
- 禁止：手改 `patch/**` 的 `.class`；禁止把 `decompiled/` 当自己的代码分发——翻译文本与构建脚本是 MIT，`decompiled/` 与游戏资源版权归 ThinMatrix。

## 4. 架构边界与因果

- 发行包是**白名单**（`build_patch_small.py` 的 `ENTRIES` + `changed_classes.txt`）而非打包整个 `build/`：早期曾把 `toolbar/DppmCounter` 之类的残留类混进 zip。
- 有两条产物线，别搞混：`repack.py` 把补丁替换进完整 `EquilinoxWindows.jar`（含游戏本体，本地实测用）；`build_patch_small.py` 从 `build/` 产出只含补丁的发行 zip。
- 安装脚本先备份 `EquilinoxWindows.jar.orig.bak`：Steam「验证文件完整性」会把游戏还原成英文，属预期，重跑安装即可。

## 5. 版本与发布

- 发布序号集中在 `work/version.py`（`PATCH_VERSION` / `GAME_VERSION`）；`build_patch_small.py` 与 `patch_strings.py` 都从这里取。README 的下载文件名和 Release 资产名仍需手写同步。
- 产物结构：`发布/Equilinox汉化补丁/`（`安装汉化.bat`、`恢复原版.bat`、`使用说明.txt`、`LICENSE`、`patch/`）+ 同名 `_vN.zip`；实际文件名与体积以该目录当场内容为准。
- 打 tag 与发 Release 等用户明确口令（见全局 `AGENTS.md` 的「Git 与 GitHub」节）。

## 6. 已知坑

- **字体源**：换机器没装「方正准圆_GBK」时用 `EQUILINOX_FONT` 指向任一中文字体（本机路径见 `AGENTS.local.md`），否则 `fontgen.py` 直接退出。构建脚本路径按 `__file__` 推导。
- 症状：游戏内文字乱码 → 先核对 `build/res/languageSheet.csv` 与发布包里那份（`发布/Equilinox汉化补丁/patch/res/languageSheet.csv`）的编码是否被编辑器改过 → 重新生成覆盖后实机确认。
- 症状：新补丁类没进包 → `changed_classes.txt` 与 `build/` 实际内容不一致 → 重跑 `patch_classes.py` 或手工补清单。
- 症状：zip 里混进旧类 → `build/` 没清干净 → 清空 `build/` 重跑整条流程。

## 7. 指针

- 截图：`docs/screenshots/`
- 相关技能：`github-rest-cli`（打 tag / 发 Release / 传附件）
