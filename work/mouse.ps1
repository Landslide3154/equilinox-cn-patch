param(
    [int]$X = 0,
    [int]$Y = 0,
    [int]$Click = 0
)
Add-Type @"
using System;
using System.Runtime.InteropServices;
public class Mouse32 {
    [DllImport("user32.dll")]
    public static extern bool SetCursorPos(int X, int Y);
    [DllImport("user32.dll")]
    public static extern void mouse_event(uint dwFlags, uint dx, uint dy, uint dwData, UIntPtr dwExtraInfo);
}
"@
[Mouse32]::SetCursorPos($X, $Y) | Out-Null
if ($Click -gt 0) {
    [Mouse32]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
    Start-Sleep -Milliseconds 80
    [Mouse32]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
}
