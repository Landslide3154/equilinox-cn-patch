# Equilinox 汉化补丁 — 开发约定

> 跨项目通用规则见全局 `C:\Users\godis\.dsh\AGENTS.md`（git 推送、shell 编码等不在此重述）；本文件只写这个项目独有的东西。

## 1. 这个项目是什么

Steam 游戏 **Equilinox**（ThinMatrix）的简体中文补丁：**Java 字节码补丁 + 资源补丁**，不重打包、不夹带游戏本体。仓库 `Landslide3154/equilinox-cn-patch`，本地目录 `D:\code\Equilinox`。

只有这几个路径重要：

- `work/` — **主战场**：补丁源 `*.java`、字符串映射 `patch_strings.py`、构建脚本、分片翻译 `zh_1..4.tsv`、类清单 `changed_classes.txt`
- `jar/` — 解包后的原版树（classes + res），所有脚本的输入，只读
- `build/` — 脚本产出的中间树；`patch/` — 已编译的补丁类（入库，勿手改）
- `发布/` — 成品目录与 zip；`decompiled/` — CFR 反编译参考，只读；`original/` — 原版 jar，只读

`.gitignore` 忽略 `build/`、`jar/`、`original/`、`发布/` 与 `*.jar/*.zip/*.png/*.class`，只有 `patch/**` 和 `docs/**/*.png` 被显式反向跟踪——**游戏本体与发布资产不进仓库**。

## 2. 常用命令（可直接复制执行）

| 目的 | 命令 |
| --- | --- |
| 合并分片翻译 → `build/res/languageSheet.csv` | `python D:\code\Equilinox\work\merge_language.py` |
| 生成字体（需 Pillow）→ `build/res/guis/fonts/` | `python D:\code\Equilinox\work\fontgen.py` |
| 改写字节码常量池 → `build/**`，并重写 `changed_classes.txt` | `python D:\code\Equilinox\work\patch_classes.py` |
| 编译要改逻辑的补丁类（`work/*.java` → `build/`） | `& "C:\Program Files\Java\jdk-25\bin\javac.exe" -encoding UTF-8 -cp "D:\code\Equilinox\jar" -d "D:\code\Equilinox\build" "D:\code\Equilinox\work\Word.java"` |
| 打完整 jar（本地实测用，不入库） | `python D:\code\Equilinox\work\repack.py` |
| 产出发布目录与 zip | `python D:\code\Equilinox\work\build_patch_small.py` |
| 发版 | `git -C D:\code\Equilinox tag vN`，再发 GitHub Release 附 zip |

- 仓库**没有封装编译脚本**：`javac` 一行要自己拼，`-cp` 指向 `jar/`，产出统一落到 `build/`。
- 只有改逻辑才写 `.java`；纯文本替换改 `work/patch_strings.py` 的映射表即可。

## 3. 硬约束（必须 / 禁止）

- 必须：`languageSheet.csv` 在游戏里始终是 **GBK**——脚本用 `write_gbk()` 做转换。**禁止**用编辑器按 UTF-8 直存，否则游戏内全是乱码。
- 必须：换字体时 `.fnt` 字符表与 `.png` 图集**两件一起换**（`.fnt` 存码位与图集坐标，错位就出乱码块）。
- 必须：`changed_classes.txt` 与实际补丁类保持一致。`patch_classes.py` 会自动重写它；手工新增的类要自己补进清单，否则不进包。
- 必须：出包前装进 Steam 版本地实测——主菜单 / 选项 / 图鉴与任务文本全中文、字体清晰、长文本换行整齐、存档名与底部时间已汉化；再跑一遍 `恢复原版.bat` 确认能还原；截图放 `docs/screenshots/`。
- 禁止：发布包含游戏本体或任何无关类；README 与 Release 都要写明「需自行购买正版、仅修改本地文件」。
- 禁止：手改 `patch/**` 的 `.class`（由编译产出）；禁止把 `decompiled/` 当自己的代码分发——翻译文本与构建脚本是 MIT，`decompiled/` 与游戏资源版权归 ThinMatrix。

## 4. 架构边界与因果（从代码看不出为什么）

- 发行包之所以是**白名单**（`build_patch_small.py` 的 `ENTRIES` + `changed_classes.txt`）而不是打包整个 `build/`：早期曾把 `toolbar/DppmCounter` 这类残留类混进精简版 zip。
- 语言表之所以必须 GBK：游戏运行时按 GBK 读 `languageSheet.csv`，脚本内部仍是 UTF-8，靠 `write_gbk()` 出包前转码。
- 之所以有两条产物线：`repack.py` 把补丁替换进完整 `EquilinoxWindows.jar`（含游戏本体的本地实测用），`build_patch_small.py` 从 `build/` 产出只含补丁文件的发行 zip——**别把两者搞混**。
- 安装脚本之所以先备份 `EquilinoxWindows.jar.orig.bak`：Steam「验证文件完整性」会把游戏还原成英文，这是预期行为，重跑安装即可。

## 5. 版本与发布

- 发布序号 `vN` **硬编码在 `work/build_patch_small.py` 里**（zip 文件名与安装 bat 标题），发新版要同时改脚本内的 `vN`，没有其它单一来源。
- 产物：`发布/Equilinox汉化补丁/`（含 `安装汉化.bat`、`恢复原版.bat`、`patch/`）+ `发布/Equilinox汉化补丁_vN.zip`。
- Release 正文模板仓库里没有（工作区那份已删），需要时从 git 历史取回：

  ```
  git -C D:\code\Equilinox log --all --oneline -- "work/release_body_*.md"
  git -C D:\code\Equilinox show <该提交>:work/release_body_vN.md > D:\code\Equilinox\work\release_body.md
  ```

- 会漂移的当前值（当前发布序号、目标游戏版本、体积/实测数字）→ 记忆空间「Equilinox 汉化补丁」，本文件不写。

## 6. 已知坑

- **所有 `work/*.py` 把 `BASE` 硬编码成 `D:\code\equilinox`**：换目录、换机器克隆后必须先改脚本里的 `BASE`，否则报路径不存在。
- 症状：游戏内文字乱码 → `languageSheet.csv` 被按 UTF-8 存过 → 重跑 `merge_language.py` 覆盖。
- 症状：新补丁类没进包 → `changed_classes.txt` 与 `build/` 实际内容不一致 → 重跑 `patch_classes.py` 或手工补清单。
- 症状：zip 里混进旧类 → `build/` 没清干净 → 清空 `build/` 重跑整条流程。
- 症状：改完补丁进游戏没变化 → 多半是漏跑某一步（脚本只写 `build/`，不自动串起来）→ 按第 2 节表格顺序重跑，最后重新组装发布包。

## 7. 指针

- 全局规则：`C:\Users\godis\.dsh\AGENTS.md`
- 记忆空间：Equilinox 汉化补丁（当前发布序号、目标游戏版本、实测记录）
- 相关技能：`github-rest-cli`（打 tag / 发 Release / 传附件）
