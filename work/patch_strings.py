# -*- coding: utf-8 -*-
"""String replacements for hardcoded UI strings in Equilinox classes."""

# Safe to replace globally across all game classes (exact constant match).
STRING_MAP = {
    "Ability": "能力",
    "Abiltity": "能力",
    "Wandering": "四处游荡",
    "Looking Cute": "卖萌中",
    "Hunts": "捕食",
    "Attack Power": "攻击力",
    "Tier": "等级",
    "Status": "状态",
    "Diseased": "已患病",
    "Everything": "全部",
    "Harvest": "收获",
    "Duplicate": "复制",
    "Game Mode": "游戏模式",
    "Scare Prey": "惊吓猎物",
    "Force": "蛙跳",
    "Flare": "开屏",
    "Flaring": "开屏中",
    "Panicking": "惊慌中",
    "Hauling": "搬运",
    "Escaping": "逃跑中",
    "Hiding": "躲藏中",
    "Chasing butterflies": "追逐蝴蝶",
    "Cutting Time": "砍伐时间",
    "Bark Percentage": "树皮百分比",
    " seconds": " 秒",
    "Total Build Points": "建造点数总量",
    " points": " 点",
    "Building Speed": "建造速度",
    " points per visit": " 点/次拜访",
    "Catches and consumes nearby insects.": "捕捉并吞食附近的昆虫。",
    "Hunts small animals and carries them back to the nest area.": "捕猎小动物并将其带回巢穴区域。",
    "No stats to show.": "没有可显示的统计数据。",
    "Hold M to place multiple": "按住 M 放置多个",
    "Hold M to place many": "按住 M 大量放置",
    "Drop All Fruit": "掉落全部果实",
    "Productivity": "生产力",
    "Breeding boost": "繁殖增益",
    "Creates a network of burrows and tunnels in its territory.": "在领地内建造洞穴和隧道网络。",
    "Can hide in burrows to escape from predators.": "能躲进洞穴以逃避掠食者。",
    "Going to burrow": "前往洞穴",
    "Entering Burrow": "进入洞穴",
    "Burrowing": "挖洞中",
    "Exiting Burrow": "离开洞穴",
    "Turns to face the sun.": "会转向太阳。",
    "The beaver is unable to build a den here due to the fact that there is not enough water nearby. Move the beavers to an area with more water presetn.":
        "海狸无法在这里建造巢穴，因为附近的水不够。请把海狸移到水更多的地方。",
    "Hares Caught": "捕获的野兔",
    "Have an Eagle catch a Desert Hare": "让老鹰捕获一只沙漠野兔",
    "Holes Dug": "挖出的洞穴",
    "Have Meerkats dig 5 holes": "让猫鼬挖出 5 个洞穴",
    "Full-Screen": "全屏",
    "Locked!": "未解锁！",
    "Click for more info...": "点击查看更多信息...",
    "Colour:": "颜色：",
    "Cheat!": "作弊！",
    "All species are now unlocked.": "所有物种现已解锁。",
    "ERROR - NAME": "错误 - 名称",
    "No Name": "未命名",
    "Version 1.7.2": "汉化 v6",
    "English": "汉语",
    "SAVING ERROR!": "保存错误！",
    "The autosave failed. Try saving manually, but if the problem continues contact thinmatrix@gmail.com":
        "自动保存失败。请尝试手动保存，如果问题持续存在请联系 thinmatrix@gmail.com",
    "Error!": "错误！",
    "An error has occurred!": "发生错误！",
    "No Audio Device Found!": "未找到音频设备！",
    "The game was unable to launch which was likely because no audio devices were found (headphones, speakers, etc.) Try plugging in headphones or turning on speakers to see if that fixes the problem. For more help, contact the dev at thinmatrix@gmail.com and copy-paste the following error message to the email:":
        "游戏无法启动，很可能是因为未找到音频设备（耳机、扬声器等）。请尝试插入耳机或打开扬声器，看看是否能解决问题。如需更多帮助，请联系开发者 thinmatrix@gmail.com，并将以下错误信息复制粘贴到邮件中：",
    "Failed to Launch!": "启动失败！",
    "Equilinox has unfortunately failed to create a display. This usually happens when the computer does not support the required version of OpenGL (3.3+) so please check to see if that is the case. You can contact the developer at thinmatrix@gmail.com for more help.":
        "Equilinox 不幸未能创建显示窗口。这通常是因为电脑不支持所需的 OpenGL 版本（3.3+），请检查是否为这种情况。你可以联系开发者 thinmatrix@gmail.com 获取更多帮助。",
    "The game was unable to create a saves folder. THis is usually a permissions error. You could try installing the game in a different location on your computer and try again, or try running as an administrator. Contact the dev at thinmatrix@gmail.com if you need more help.":
        "游戏无法创建存档文件夹。这通常是权限错误。你可以尝试把游戏安装到电脑的其他位置再试一次，或者以管理员身份运行。如需更多帮助，请联系开发者 thinmatrix@gmail.com。",
    "No error log available.": "没有可用的错误日志。",
    "Shader Error": "着色器错误",
    "Could not read shader file: ": "无法读取着色器文件：",
    "Could not compile shader ": "无法编译着色器 ",
    "Shader failed to compile": "着色器编译失败",
    "An error has caused the program to crash, sorry for the inconvenience! Please email the dev at thinmatrix@gmail.com and copy-paste the error message below:":
        "发生错误导致程序崩溃，非常抱歉给您带来不便！请发送邮件给开发者 thinmatrix@gmail.com，并将下面的错误信息复制粘贴到邮件中：",
    "In Simulation Mode you have unlimited DP and access to any species that you've previously unlocked in normal mode. Great for trying things out with your species.":
        "在模拟模式中，你拥有无限的 DP，可以使用任何你在普通模式中解锁过的物种。非常适合尝试各种实验。",
    "Unlimited DP and all species are unlocked. Most simulation aspects are turned off (there's no health, growth, hunger, breeding, or environmental requirements). Good for creating nice looking low-poly scenes.":
        "无限的 DP，所有物种全部解锁。大部分模拟机制被关闭（没有健康、生长、饥饿、繁殖或环境需求）。适合打造漂亮的多边形场景。",
}

# Per-class replacements for ambiguous constants.
CLASS_MAP = {
    "environment/EnvironmentComponentLoader.class": {
        "Not ": "非",
        "m ": "米",
        "m": "米",
    },
    "saves/SaveSlot.class": {
        "Save ": "存档 ",
    },
    "materials/NaturalColoursGui.class": {
        "to": "至",
    },
}

# Classes excluded from global string replacement.
EXCLUDE = ("org/lwjgl", "de/matthiasmann", "com/jcraft", "org/ninjacave")
