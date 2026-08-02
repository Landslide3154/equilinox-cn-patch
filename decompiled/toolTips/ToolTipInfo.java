/*
 * Decompiled with CFR 0.152.
 */
package toolTips;

import toolTips.ToolTip;

public class ToolTipInfo {
    protected final String title;
    protected final String description;
    protected final boolean prefersBottom;
    protected final boolean prefersRight;

    private ToolTipInfo(String title, String desc, boolean prefersBottom, boolean prefersRight) {
        this.title = title;
        this.description = desc;
        this.prefersBottom = prefersBottom;
        this.prefersRight = prefersRight;
    }

    public ToolTip createToolTip() {
        return new ToolTip(this);
    }

    public static ToolTipInfo newInfo(String title, String desc) {
        return new ToolTipInfo(title, desc, true, true);
    }

    public static ToolTipInfo newInfo(String title, String desc, boolean prefersBottom, boolean prefersRight) {
        return new ToolTipInfo(title, desc, prefersBottom, prefersRight);
    }
}

