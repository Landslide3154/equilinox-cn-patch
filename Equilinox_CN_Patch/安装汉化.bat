@echo off
chcp 65001 >nul 2>&1
title Equilinox 中文汉化补丁

:: 检查Python是否可用
where python >nul 2>&1
if %errorlevel%==0 (
    python "%~dp0apply_patch.py"
    goto :end
)

where python3 >nul 2>&1
if %errorlevel%==0 (
    python3 "%~dp0apply_patch.py"
    goto :end
)

where py >nul 2>&1
if %errorlevel%==0 (
    py "%~dp0apply_patch.py"
    goto :end
)

echo 错误：未找到Python环境。
echo.
echo 请安装Python后重试：https://www.python.org/downloads/
echo 安装时请勾选 "Add Python to PATH" 选项。
echo.
pause

:end
