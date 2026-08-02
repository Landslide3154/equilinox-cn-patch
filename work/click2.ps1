param(
    [int]$X = 1300,
    [int]$Y = 480,
    [int]$Delay = 400
)

Add-Type @"
using System;
using System.Runtime.InteropServices;
public class C2 {
    [DllImport("user32.dll")]
    public static extern bool SetForegroundWindow(IntPtr hwnd);
    [DllImport("user32.dll")]
    public static extern bool SetCursorPos(int X, int Y);
    [DllImport("user32.dll")]
    public static extern void mouse_event(uint dwFlags, uint dx, uint dy, uint dwData, UIntPtr dwExtraInfo);
}
"@

$proc = Get-Process | Where-Object { $_.ProcessName -match 'java' -and $_.MainWindowTitle -eq 'Equilinox' } | Select-Object -First 1
if ($proc) {
    [C2]::SetForegroundWindow($proc.MainWindowHandle) | Out-Null
    Start-Sleep -Milliseconds 500
}
[C2]::SetCursorPos($X, $Y) | Out-Null
Start-Sleep -Milliseconds $Delay
[C2]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
Start-Sleep -Milliseconds 120
[C2]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
Write-Output "clicked ($X,$Y)"
