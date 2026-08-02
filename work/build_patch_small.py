# -*- coding: utf-8 -*-
"""Build the small (patch-files only) distributable package."""
import os
import shutil
import zipfile

BASE = r"D:\code\equilinox"
PATCH_SRC = os.path.join(BASE, "build")
PACKAGE = os.path.join(BASE, "发布", "Equilinox汉化补丁_精简版")

# files to ship in the patch folder (relative to build/)
ENTRIES = [
    "res/languageSheet.csv",
    "res/guis/fonts/gill3.fnt",
    "res/guis/fonts/gill3.png",
    "res/guis/fonts/segoeUI.fnt",
    "res/guis/fonts/segoeUI.png",
    "utils/MyFile.class",
    "fontRendering/Word.class",
    "fontRendering/Line.class",
    "fontRendering/TextLoader.class",
    "fontRendering/GillCalculator.class",
    "fontRendering/SegoeCalculator.class",
    "bottomBar/TimeDisplay.class",
    "toolbar/DppmCounter.class",
]
with open(os.path.join(BASE, "work", "changed_classes.txt"), encoding="utf-8") as f:
    ENTRIES += f.read().splitlines()


def write_gbk(path, text):
    text = text.replace("\r\n", "\n").replace("\n", "\r\n")
    with open(path, "wb") as f:
        f.write(text.encode("gbk", errors="replace"))


INSTALL_BAT = r"""@echo off
chcp 936 >nul
setlocal enabledelayedexpansion
title Equilinox 简体中文汉化补丁 v6 - 安装（精简版）

echo ==================================================
echo    Equilinox 简体中文汉化补丁 v6（精简版）
echo    适用版本：Steam 版 Equilinox 1.7.2
echo ==================================================
echo.

set "GAME_DIR="
for /f "skip=2 tokens=2,*" %%A in ('reg query "HKCU\Software\Valve\Steam" /v SteamPath 2^>nul') do set "STEAM_PATH=%%B"
if defined STEAM_PATH if exist "%STEAM_PATH%\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=%STEAM_PATH%\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "C:\Program Files (x86)\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=C:\Program Files (x86)\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "C:\Program Files\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=C:\Program Files\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "D:\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=D:\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "D:\SteamLibrary\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=D:\SteamLibrary\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "E:\SteamLibrary\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=E:\SteamLibrary\steamapps\common\Equilinox"

if not defined GAME_DIR (
    echo.
    echo 未自动找到游戏目录，请手动输入游戏安装目录。
    set /p GAME_DIR=请输入游戏目录:
)
if not defined GAME_DIR (
    echo 未输入目录，安装取消。
    pause
    exit /b 1
)
if not exist "%GAME_DIR%\EquilinoxWindows.jar" (
    echo.
    echo 错误：在 "%GAME_DIR%" 下未找到 EquilinoxWindows.jar
    pause
    exit /b 1
)

echo 游戏目录：%GAME_DIR%
echo.

if not exist "%GAME_DIR%\EquilinoxWindows.jar.orig.bak" (
    copy /y "%GAME_DIR%\EquilinoxWindows.jar" "%GAME_DIR%\EquilinoxWindows.jar.orig.bak" >nul
    echo [OK] 已备份原版：EquilinoxWindows.jar.orig.bak
) else (
    echo [..] 已存在备份文件，跳过备份
)

echo [..] 正在合成汉化 jar（请稍候）...
powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $g = '%GAME_DIR%'; $p = '%~dp0patch'; Add-Type -AssemblyName System.IO.Compression.FileSystem; $orig = Join-Path $g 'EquilinoxWindows.jar'; $tmp = Join-Path $env:TEMP ('EquilinoxCN_' + [guid]::NewGuid().ToString('N') + '.jar'); Copy-Item $orig $tmp -Force; $zip = [System.IO.Compression.ZipFile]::Open($tmp, 'Update'); Get-ChildItem -Path $p -Recurse -File | ForEach-Object { $rel = $_.FullName.Substring($p.Length + 1).Replace('\','/'); $e = $zip.GetEntry($rel); if ($e) { $e.Delete() }; $ne = $zip.CreateEntry($rel, [System.IO.Compression.CompressionLevel]::Optimal); $in = [System.IO.File]::OpenRead($_.FullName); $out = $ne.Open(); $in.CopyTo($out); $out.Close(); $in.Close() }; $zip.Dispose(); Copy-Item $tmp $orig -Force; Remove-Item $tmp -Force }"
if errorlevel 1 (
    echo.
    echo 错误：合成汉化 jar 失败，请确认系统为 Windows 10/11 且已关闭游戏。
    pause
    exit /b 1
)
echo [OK] 汉化文件已安装

set /a RENAMED=0
if exist "%GAME_DIR%\Equilinox_0_Saves\" (
    for %%f in ("%GAME_DIR%\Equilinox_0_Saves\Equilinox_0-*-Save_*.dat") do (
        set "fn=%%~nf"
        set "newname=!fn:Save_=存档_!"
        if not "!fn!"=="!newname!" (
            if not exist "%GAME_DIR%\Equilinox_0_Saves\!newname!.dat" (
                ren "%%f" "!newname!.dat" >nul 2>nul
                if not errorlevel 1 set /a RENAMED+=1
            )
        )
    )
)
if !RENAMED! gtr 0 echo [OK] 已将 !RENAMED! 个默认存档名改为"存档 N"

echo.
echo ==================================================
echo    汉化安装完成！
echo    启动游戏即可看到中文界面（左下角显示"汉化 v6"）。
echo    如需恢复英文原版，运行"恢复原版.bat"。
echo ==================================================
echo.
pause
exit /b 0
"""


RESTORE_BAT = r"""@echo off
chcp 936 >nul
setlocal enabledelayedexpansion
title Equilinox 汉化补丁 - 恢复英文原版

echo ==================================================
echo    Equilinox 恢复英文原版
echo ==================================================
echo.

set "GAME_DIR="
for /f "skip=2 tokens=2,*" %%A in ('reg query "HKCU\Software\Valve\Steam" /v SteamPath 2^>nul') do set "STEAM_PATH=%%B"
if defined STEAM_PATH if exist "%STEAM_PATH%\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=%STEAM_PATH%\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "C:\Program Files (x86)\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=C:\Program Files (x86)\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "C:\Program Files\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=C:\Program Files\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "D:\Steam\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=D:\Steam\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "D:\SteamLibrary\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=D:\SteamLibrary\steamapps\common\Equilinox"
if not defined GAME_DIR if exist "E:\SteamLibrary\steamapps\common\Equilinox\EquilinoxWindows.jar" set "GAME_DIR=E:\SteamLibrary\steamapps\common\Equilinox"

if not defined GAME_DIR (
    echo 未自动找到游戏目录，请手动输入游戏安装目录。
    set /p GAME_DIR=请输入游戏目录:
)
if not defined GAME_DIR (
    echo 未输入目录，操作取消。
    pause
    exit /b 1
)
if not exist "%GAME_DIR%\EquilinoxWindows.jar.orig.bak" (
    echo 未找到备份文件 EquilinoxWindows.jar.orig.bak，无法恢复。
    pause
    exit /b 1
)

copy /y "%GAME_DIR%\EquilinoxWindows.jar.orig.bak" "%GAME_DIR%\EquilinoxWindows.jar" >nul
if errorlevel 1 (
    echo 错误：无法写入游戏文件，请确认已关闭游戏。
    pause
    exit /b 1
)

if exist "%GAME_DIR%\Equilinox_0_Saves\" (
    for %%f in ("%GAME_DIR%\Equilinox_0_Saves\Equilinox_0-*-存档_*.dat") do (
        set "fn=%%~nf"
        set "newname=!fn:存档_=Save_!"
        if not "!fn!"=="!newname!" (
            if not exist "%GAME_DIR%\Equilinox_0_Saves\!newname!.dat" (
                ren "%%f" "!newname!.dat" >nul 2>nul
            )
        )
    )
)

echo.
echo 已恢复英文原版。
echo 注意：Steam 验证文件完整性时也会自动还原为原版文件。
pause
exit /b 0
"""


README_TXT = """Equilinox 简体中文汉化补丁 v6（精简版）
========================================

【补丁内容】
  · 全部界面文本、任务、帮助、物种名称与描述汉化
  · 内置中文字体：方正准圆（圆体，与英文 Gill Sans 风格协调）
  · 字体渲染优化：16 倍超采样 + mipmap，有效消除锯齿，
    文字不发虚不变细，小字清晰可读
  · 修复长中文文本崩溃、修复文字换行多余空格
  · 修复文字边缘杂点/细线
  · 底部时间显示格式：第1年,第1天 - 09:45
  · 语言选项显示"汉语"，默认存档名显示"存档 N"

【系统要求】
  · Windows 10 / 11（需自带 PowerShell）
  · Steam 版 Equilinox（游戏版本 1.7.2）

【安装方法】
  1. 解压本压缩包到任意位置（路径建议不要有中文）
  2. 确保游戏已完全关闭
  3. 双击运行「安装汉化.bat」
  4. 程序会自动查找游戏目录；如未找到，按提示手动输入
  5. 等待提示"汉化安装完成"，然后启动游戏
  6. 主菜单左下角显示"汉化 v6"即表示安装成功

【恢复英文原版】
  运行「恢复原版.bat」，或把游戏目录下的
  EquilinoxWindows.jar.orig.bak 改名为 EquilinoxWindows.jar

【常见问题】
  Q: 提示找不到游戏目录？
  A: 手动输入游戏安装目录（包含 EquilinoxWindows.jar 的文件夹）。

  Q: 安装后没有变化 / 仍显示英文？
  A: 确认主菜单左下角是否有"汉化 v6"。没有则说明未安装成功，
     请重新运行安装脚本。

  Q: Steam 更新或验证文件完整性后变回英文？
  A: Steam 会把文件还原为原版，重新运行「安装汉化.bat」即可。

  Q: 杀毒软件报毒？
  A: 补丁会修改游戏 jar 文件，可能被误报，请添加信任。

  Q: 安装前存档里的"Save 1"会被改成"存档 1"吗？
  A: 会。默认存档文件会被重命名为"存档 1"（仅默认命名
     Save_数字 的存档），恢复原版时会改回。

【备份说明】
  安装时自动把原版文件备份为：
  游戏目录\EquilinoxWindows.jar.orig.bak

祝你玩得愉快！
"""


def main():
    os.makedirs(PACKAGE, exist_ok=True)
    patch_dir = os.path.join(PACKAGE, "patch")
    os.makedirs(patch_dir, exist_ok=True)
    copied = 0
    for rel in ENTRIES:
        src = os.path.join(PATCH_SRC, rel.replace("/", os.sep))
        dst = os.path.join(patch_dir, rel.replace("/", os.sep))
        if not os.path.exists(src):
            print("MISSING:", rel)
            continue
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        shutil.copy2(src, dst)
        copied += 1
    write_gbk(os.path.join(PACKAGE, "安装汉化.bat"), INSTALL_BAT)
    write_gbk(os.path.join(PACKAGE, "恢复原版.bat"), RESTORE_BAT)
    write_gbk(os.path.join(PACKAGE, "使用说明.txt"), README_TXT)

    zip_path = os.path.join(BASE, "发布", "Equilinox汉化补丁_精简版_v6.zip")
    with zipfile.ZipFile(zip_path, "w", zipfile.ZIP_DEFLATED) as zf:
        for root, dirs, files in os.walk(PACKAGE):
            for fn in files:
                full = os.path.join(root, fn)
                rel = os.path.join("Equilinox汉化补丁_精简版", os.path.relpath(full, PACKAGE))
                zf.write(full, rel)
    print("patch files copied:", copied)
    print("zip:", zip_path, round(os.path.getsize(zip_path) / 1048576, 2), "MB")


if __name__ == "__main__":
    main()
