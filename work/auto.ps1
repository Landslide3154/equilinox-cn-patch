param(
    [int]$X = 641,
    [int]$Y = 250,
    [string]$Action = "click"
)

Add-Type -AssemblyName System.Windows.Forms
Add-Type @"
using System;
using System.Runtime.InteropServices;
public class Auto32 {
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
    [Auto32]::SetForegroundWindow($proc.MainWindowHandle) | Out-Null
    Start-Sleep -Milliseconds 400
}

[Auto32]::SetCursorPos($X, $Y) | Out-Null
Start-Sleep -Milliseconds 200

switch ($Action) {
    "click" {
        [Auto32]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
        Start-Sleep -Milliseconds 80
        [Auto32]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero) | Out-Null
    }
    "hover" {
        Start-Sleep -Milliseconds 1500
    }
    "esc" {
        [System.Windows.Forms.SendKeys]::SendWait("{ESC}")
    }
}
Write-Output "done: $Action at ($X,$Y)"
