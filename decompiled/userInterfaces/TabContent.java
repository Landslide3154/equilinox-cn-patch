/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guis.GuiComponent;

public class TabContent {
    protected final String name;
    protected final GuiComponent content;

    public TabContent(String name, GuiComponent content) {
        this.name = name;
        this.content = content;
    }
}

