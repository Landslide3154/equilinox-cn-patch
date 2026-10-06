# AGENTS.md — Equilinox 简体中文汉化补丁

给 Steam 游戏 **Equilinox 1.7.2**（ThinMatrix）做的简体中文补丁：**Java 字节码 + 资源补丁**，不重打包游戏本体。
仓库 `Landslide3154/equilinox-cn-patch`（公开）；本地目录 `D:\code\Equilinox`；发布包命名 `equilinox-cn-patch-vN.zip`（当前序号以 `发布/` 与 Releases 为准）。

## 目录职责（不要混用）

| 目录 | 是什么 | 能不能改 |
|---|---|---|
| `original/` | 原版 `EquilinoxWindows_original.jar`（**不入库**） | 只读，别动 |
| `decompiled/` | CFR 反编译结果（`tools/cfr.jar` 产出） | 只读参考。**已入库但版权归 ThinMatrix**，仅供学习，别当自己的代码 |
| `patch/` | 编译好的 `.class` 补丁（按包结构，**入库**） | 由编译产出，不要手改 |
| `build/` | 组装好的补丁树（资源 + 类），打包的输入（**不入库**） | 可重建 |
| `work/` | 全部脚本与补丁源：`*.java` 补丁源、`patch_strings.py`、`patch_classes.py`、`repack.py`、`fontgen.py`、`merge_language.py`、`build_patch_small.py`、`changed_classes.txt`、`zh_1..4.tsv`、`release_body_vN.md` | **主战场** |
| `发布/` | 成品：`Equilinox汉化补丁/`（含 `安装汉化.bat`、`恢复原版.bat`、`patch/`）+ zip（**不入库**） | 由脚本产出 |

`original/`、`jar/`、`build/`、`发布/` 及 `*.jar/*.zip/*.class` 都在 `.gitignore` 里——**游戏本体与发布资产不进仓库**，只有 `patch/**` 被显式反向跟踪。

## 构建与发布流程

1. 改文本：`work/zh_*.tsv`（分片翻译）→ `work/merge_language.py` 合并
2. 改代码：改 `work/*.java` 补丁源 → `work/patch_classes.py` 编译
3. 组装：`work/build_patch_small.py` 把资源与类装进 `build/`，再产出 `发布/Equilinox汉化补丁/`
   - 固定随包文件：`res/languageSheet.csv`、`res/guis/fonts/{gill3,segoeUI}.{fnt,png}`、`utils/MyFile.class`、`fontRendering/{Word,Line,TextLoader,GillCalculator,SegoeCalculator}.class`、`bottomBar/TimeDisplay.class`
   - 其余按 `work/changed_classes.txt` 逐行追加——**改了新类一定要同步这个清单**，否则新补丁不会进包
4. 字体：`work/fontgen.py` 生成 `gill3.*` / `segoeUI.*`（`.fnt` 字符表与 `.png` 图集必须配套，换字体两件一起换）
5. 打 zip：`发布/Equilinox汉化补丁/` → `equilinox-cn-patch-vN.zip`；Release 正文模板复制 `work/release_body_vN.md` 改序号
6. 发布：`git tag vN` + GitHub Release 附 zip

## 必须记住的坑

- **`languageSheet.csv` 是 GBK 编码**：脚本里有专门的 `write_gbk()`。按 UTF-8 写会让游戏内全是乱码——改这个文件时务必走脚本，别用编辑器直接存
- **发布包不含游戏本体**：只发 `patch/` 与两个 bat、说明文件；README 与 Release 都要写明"需自行购买正版、仅修改本地文件"
- **安装脚本会备份** `EquilinoxWindows.jar.orig.bak`；Steam"验证文件完整性"会把游戏还原成英文，属预期行为，重跑安装即可
- 反编译源码的版权归原作者，**不要**把 `decompiled/` 当成可自由分发的产物

## 验证

- 装进 Steam 版 1.7.2 实测：主菜单 → 选项 → 图鉴/任务文本是否全中文、字体是否清晰、长文本换行是否整齐、存档名与底部时间是否汉化
- 跑一遍 `恢复原版.bat` 确认能还原
- 截图放 `docs/screenshots/`（README 会引用）

## 其它

- 翻译文本与构建脚本 MIT；`decompiled/` 与游戏资源版权归 ThinMatrix
- 全仓推送到 GitHub：改完立即 `git push`，遵循全局 `~/.dsh/AGENTS.md` 第 2 节的推送原则
- 会漂移的状态（当前发布序号、目标游戏版本）见 DSH 记忆空间「本机环境与工具」；本文件只放规则
