# Equilinox 补丁已知坑：字体源与三类症状排查

> 自仓库根 `AGENTS.md` §6「已知坑」外移（2026-10-24），搬移的正文逐字保留；七节骨架仍在 `../AGENTS.md`。
>
> 本文件不注入每轮上下文——换机器构建报错、或补丁进游戏后不对时来这里。

- **字体源**：换机器没装「方正准圆_GBK」时用 `EQUILINOX_FONT` 指向任一中文字体（本机路径见 `AGENTS.local.md`），否则 `fontgen.py` 直接退出。构建脚本路径按 `__file__` 推导。
- 症状：游戏内文字乱码 → 先核对 `build/res/languageSheet.csv` 与发布包里那份（`发布/Equilinox汉化补丁/patch/res/languageSheet.csv`）的编码是否被编辑器改过 → 重新生成覆盖后实机确认。
- 症状：新补丁类没进包 → `changed_classes.txt` 与 `build/` 实际内容不一致 → 重跑 `patch_classes.py` 或手工补清单。
- 症状：zip 里混进旧类 → `build/` 没清干净 → 清空 `build/` 重跑整条流程。
