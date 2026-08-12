# Equilinox 简体中文汉化补丁

[Equilinox](https://store.steampowered.com/app/621880/Equilinox/)（Steam 生态模拟游戏，1.7.2 版）的完整简体中文汉化项目。已翻译全部 1174 条界面文本、重制中文字体、修复中文渲染与换行崩溃，并解决编码、布局等 16 项问题。

> 左侧为原版英文，右侧为汉化后效果（方正准圆字体，与 Gill Sans 圆润风格协调）

## 汉化内容

1. **界面文本**：`res/languageSheet.csv` 全部 1174 条文本译为简体中文（主菜单、选项、任务、帮助、全部物种名称与描述）
2. **中文字体**：重制 `gill3` 与 `segoeUI` 两个位图字体（4096×4096 图集），补齐全部 1179 个所需中文字形，保留原版拉丁字形；16 倍超采样抗锯齿 + mipmap
3. **硬编码字符串**：修改 67 个类文件中残留的英文界面文字（"能力 / 攻击力 / 收获 / 状态 / 捕食"等）
4. **编码修复**：`MyFile.getReader()` 强制 UTF-8 读取文本资源，任意系统区域（GBK/UTF-8）下不乱码
5. **换行修复**：文字引擎按字符换行（CJK 标准排版），修复长中文文本导致的 `ArithmeticException: / by zero` 崩溃
6. **字体渲染修复**：字体纹理关闭 mipmap、启用边缘钳制，消除缩小采样混色
7. **杂点修复**：修正 `.fnt` 坐标语义（格子左上角），消除字形边缘同色杂点/细线
8. **其他**：清除无效 `^...^` 高亮标记、字形垂直居中、语言选项显示"汉语"、存档名汉化、底部时间"第1年,第1天 - 09:45"

完整改动细节见 [docs/汉化说明.md](docs/汉化说明.md)，踩坑方法论见 [docs/AI汉化经验总结.md](docs/AI汉化经验总结.md)。

## 快速开始（推荐）

1. 从 [Releases](https://github.com/Landslide3154/equilinox-cn-patch/releases) 下载 `Equilinox汉化补丁_精简版_v6.zip`（约 4MB，只含改动文件）
2. 解压 → 关闭游戏 → 双击 **安装汉化.bat**（自动查找 Steam 游戏目录，也可手动输入）
3. 启动游戏即中文界面。恢复英文原版运行 **恢复原版.bat**

安装前自动备份原版为 `EquilinoxWindows.jar.orig.bak`，并把默认存档 `Save_N` 重命名为 `存档_N`。若 Steam"验证文件完整性"还原了 jar，重新运行安装脚本即可。

> 需要 Python 3 + Pillow 才能运行脚本；合成补丁 jar 需要原版 `EquilinoxWindows.jar`（仓库不含游戏本体）。

## 从源码构建

```
# 1. 准备原版 jar（Steam 目录）
#    C:\Program Files (x86)\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar

# 2. 解包并反编译（可选，仅改代码时需要）
java -jar tools/cfr.jar <原版jar> --outputdir decompiled

# 3. 生成中文字体 + 合并翻译
python work/fontgen.py            # 生成 gill3/segoeUI 字体（build/res/guis/fonts/）
python work/merge_language.py     # 合并 zh_*.tsv 翻译 → build/res/languageSheet.csv

# 4. 修补 class（硬编码字符串 + 重编译类）
python work/patch_classes.py      # 常量池字符串替换（work/changed_classes.txt）
python work/patch_strings.py      # 硬编码字符串替换表

# 5. 重编译修改过的类（javac --release 8），放入 build/ 对应包路径：
#    utils/MyFile.class  fontRendering/{Word,Line,TextLoader,GillCalculator,SegoeCalculator}.class  bottomBar/TimeDisplay.class

# 6. 合成汉化 jar
python work/repack.py             # → build/EquilinoxWindows.jar

# 7. 打包发布物（可选）
python work/build_patch_small.py  # 精简版补丁 → 发布/Equilinox汉化补丁_精简版/
python work/build_package.py      # 完整版补丁 → 发布/Equilinox汉化补丁/
```

所有脚本内的路径硬编码为 `D:\code\equilinox`，换机器使用需修改各脚本顶部的 `BASE` 常量。

## 目录结构

```
├── patch/            # 补丁文件（发布核心）：翻译 CSV、重制字体、81 个修改/重编译 class
├── work/             # 构建与维护脚本、翻译对照表、重编译源码
│   ├── zh_1~4.tsv    # 翻译对照表（ID → 简体中文）
│   ├── fontgen.py    # 位图字体生成（方正准圆 + 拉丁字形，16 倍超采样）
│   ├── merge_language.py   # 翻译合并 + 移除 ^ 标记
│   ├── patch_classes.py    # class 常量池字符串替换
│   ├── patch_strings.py    # 硬编码字符串替换表
│   ├── repack.py           # 用 patch/ 合成汉化 jar
│   └── *.java        # 重编译类源码（MyFile/TextLoader/Line/Word/TimeDisplay/GillCalculator/SegoeCalculator）
├── decompiled/       # 原版 jar 反编译源码（CFR，仅供学习研究）
├── docs/             # 汉化说明、经验总结
└── tools/cfr.jar     # CFR 反编译器
```

## 免责声明

- 本仓库**不含游戏本体**，`decompiled/` 下的反编译源码仅用于学习与研究，版权归原作者 ThinMatrix 及发行方所有。
- 汉化补丁只修改本地游戏文件，请通过 Steam 购买正版游戏后使用。
- 使用本补丁产生的任何问题（存档、封禁风险等）由使用者自行承担。

## 许可证

补丁中的翻译文本、构建脚本、重编译源码以 [MIT](LICENSE) 许可发布；反编译源码与游戏资源版权归原作者所有。
