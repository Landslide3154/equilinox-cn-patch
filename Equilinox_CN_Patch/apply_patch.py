# -*- coding: utf-8 -*-
"""
Equilinox 中文汉化补丁
======================
功能：将 Equilinox 游戏汉化为中文界面。
使用方法：双击运行，或将整个文件夹放到任意位置后运行。

补丁内容：
- 翻译所有界面文本为中文
- 替换字体为微软雅黑（支持中文显示）
- 修复字体渲染崩溃问题
- 修复中文编码读取问题
"""
import os
import sys
import shutil
import zipfile
import tempfile
import winreg

def find_steam_path():
    """通过注册表查找Steam安装路径"""
    try:
        key = winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE, r"SOFTWARE\WOW6432Node\Valve\Steam")
        path, _ = winreg.QueryValueEx(key, "InstallPath")
        winreg.CloseKey(key)
        return path
    except:
        pass
    try:
        key = winreg.OpenKey(winreg.HKEY_CURRENT_USER, r"Software\Valve\Steam")
        path, _ = winreg.QueryValueEx(key, "SteamPath")
        winreg.CloseKey(key)
        return path
    except:
        pass
    return None

def find_game_path():
    """查找Equilinox游戏安装路径"""
    candidates = []
    
    # 1. 从注册表获取Steam路径
    steam = find_steam_path()
    if steam:
        candidates.append(os.path.join(steam, "steamapps", "common", "Equilinox"))
    
    # 2. 常见Steam安装路径
    common_paths = [
        r"C:\Program Files (x86)\Steam\steamapps\common\Equilinox",
        r"C:\Program Files\Steam\steamapps\common\Equilinox",
        r"D:\Steam\steamapps\common\Equilinox",
        r"D:\SteamLibrary\steamapps\common\Equilinox",
        r"E:\Steam\steamapps\common\Equilinox",
        r"E:\SteamLibrary\steamapps\common\Equilinox",
    ]
    for p in common_paths:
        if p not in candidates:
            candidates.append(p)
    
    # 3. 检查每个候选路径
    for path in candidates:
        jar = os.path.join(path, "EquilinoxWindows.jar")
        if os.path.isfile(jar):
            return path
    
    return None

def apply_patch(game_dir, patch_dir):
    """将补丁应用到游戏目录"""
    jar_path = os.path.join(game_dir, "EquilinoxWindows.jar")
    
    if not os.path.isfile(jar_path):
        print(f"错误：找不到游戏文件 {jar_path}")
        return False
    
    # 备份原始文件
    backup_path = jar_path + ".bak"
    if not os.path.isfile(backup_path):
        print("备份原始文件...")
        shutil.copy2(jar_path, backup_path)
        print(f"  已备份到: {backup_path}")
    else:
        print(f"  备份已存在: {backup_path}")
    
    # 补丁文件映射: jar内路径 -> 本地文件路径
    files_dir = os.path.join(patch_dir, "files")
    patch_map = {
        "res/languageSheet.csv": os.path.join(files_dir, "languageSheet.csv"),
    }
    # class文件（从classes/目录递归加载，保留目录结构）
    classes_dir = os.path.join(files_dir, "classes")
    if os.path.isdir(classes_dir):
        for root, dirs, filenames in os.walk(classes_dir):
            for fname in filenames:
                if fname.endswith(".class"):
                    full_path = os.path.join(root, fname)
                    # 计算相对于classes_dir的路径作为jar内路径
                    rel = os.path.relpath(full_path, classes_dir).replace("\\", "/")
                    patch_map[rel] = full_path
    # 兼容旧版：根目录下的Loader.class和MyFile.class
    for legacy in ["Loader.class", "MyFile.class"]:
        legacy_path = os.path.join(files_dir, legacy)
        if os.path.isfile(legacy_path):
            if legacy == "Loader.class":
                patch_map["basics/Loader.class"] = legacy_path
            elif legacy == "MyFile.class":
                patch_map["utils/MyFile.class"] = legacy_path
    # 字体文件
    fonts_dir = os.path.join(files_dir, "fonts")
    if os.path.isdir(fonts_dir):
        for f in os.listdir(fonts_dir):
            if f.endswith(".fnt") or f.endswith(".png"):
                patch_map["res/guis/fonts/" + f] = os.path.join(fonts_dir, f)
    
    # 验证所有补丁文件存在
    for jar_name, local_path in patch_map.items():
        if not os.path.isfile(local_path):
            print(f"错误：缺少补丁文件 {local_path}")
            return False
    
    # 创建临时文件进行jar修改
    print("正在应用汉化补丁...")
    tmp_fd, tmp_path = tempfile.mkstemp(suffix=".jar", dir=game_dir)
    os.close(tmp_fd)
    
    try:
        replaced = 0
        total = 0
        
        with zipfile.ZipFile(jar_path, 'r') as zin:
            with zipfile.ZipFile(tmp_path, 'w', zipfile.ZIP_DEFLATED) as zout:
                total = len(zin.infolist())
                for i, item in enumerate(zin.infolist()):
                    name = item.filename
                    if name in patch_map:
                        with open(patch_map[name], 'rb') as f:
                            data = f.read()
                        zout.writestr(item, data)
                        replaced += 1
                    else:
                        data = zin.read(name)
                        zout.writestr(item, data)
                    
                    # 进度显示
                    pct = (i + 1) * 100 // total
                    if (i + 1) % 200 == 0 or (i + 1) == total:
                        print(f"\r  进度: {pct}% ({i+1}/{total})", end="", flush=True)
        
        print()
        print(f"  已替换 {replaced} 个文件")
        
        # 替换原始jar
        os.remove(jar_path)
        shutil.move(tmp_path, jar_path)
        
        return True
        
    except Exception as e:
        print(f"\n错误：{e}")
        # 清理临时文件
        if os.path.exists(tmp_path):
            os.remove(tmp_path)
        return False

def restore_original(game_dir):
    """恢复原始文件"""
    jar_path = os.path.join(game_dir, "EquilinoxWindows.jar")
    backup_path = jar_path + ".bak"
    
    if os.path.isfile(backup_path):
        shutil.copy2(backup_path, jar_path)
        print("已恢复原始英文版本。")
        return True
    else:
        print("错误：找不到备份文件，无法恢复。")
        return False

def main():
    print("=" * 50)
    print("    Equilinox 中文汉化补丁 v1.1")
    print("=" * 50)
    print()
    
    # 获取补丁文件所在目录
    if getattr(sys, 'frozen', False):
        patch_dir = os.path.dirname(sys.executable)
    else:
        patch_dir = os.path.dirname(os.path.abspath(__file__))
    
    # 检查补丁文件
    files_dir = os.path.join(patch_dir, "files")
    if not os.path.isdir(files_dir):
        print("错误：找不到补丁文件目录 'files/'")
        print("请确保本程序与 files 文件夹在同一目录下。")
        input("\n按回车键退出...")
        return
    
    print("[1] 安装汉化补丁")
    print("[2] 恢复英文原版")
    print("[3] 退出")
    print()
    
    choice = input("请选择操作 (1/2/3): ").strip()
    
    if choice == "3":
        return
    
    if choice == "2":
        game_dir = find_game_path()
        if not game_dir:
            game_dir = input("请输入游戏安装路径: ").strip().strip('"')
        if os.path.isdir(game_dir):
            restore_original(game_dir)
        else:
            print("错误：路径无效。")
        input("\n按回车键退出...")
        return
    
    if choice != "1":
        print("无效选择。")
        input("\n按回车键退出...")
        return
    
    # 查找游戏路径
    print("\n正在查找游戏安装路径...")
    game_dir = find_game_path()
    
    if game_dir:
        print(f"  找到: {game_dir}")
        confirm = input("  确认使用此路径？(Y/n): ").strip().lower()
        if confirm == 'n':
            game_dir = None
    
    if not game_dir:
        game_dir = input("请输入Equilinox游戏安装路径: ").strip().strip('"')
    
    if not os.path.isdir(game_dir):
        print(f"错误：路径不存在: {game_dir}")
        input("\n按回车键退出...")
        return
    
    jar_path = os.path.join(game_dir, "EquilinoxWindows.jar")
    if not os.path.isfile(jar_path):
        print(f"错误：在该路径下找不到 EquilinoxWindows.jar")
        input("\n按回车键退出...")
        return
    
    print()
    success = apply_patch(game_dir, patch_dir)
    
    if success:
        print()
        print("=" * 50)
        print("  汉化补丁安装成功！")
        print("  现在可以启动游戏了。")
        print()
        print("  提示：原始文件已备份为")
        print("  EquilinoxWindows.jar.bak")
        print("  如需恢复英文，重新运行本程序选择[2]。")
        print("=" * 50)
    else:
        print("\n补丁安装失败，请检查错误信息。")
    
    input("\n按回车键退出...")

if __name__ == "__main__":
    main()
