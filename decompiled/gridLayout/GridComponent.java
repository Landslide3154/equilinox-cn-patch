/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.CurrentFilterSettings;
import gridLayout.ItemPageGui;
import guis.GuiComponent;

public interface GridComponent
extends Comparable<GridComponent> {
    public GuiComponent getComponentGui(ItemPageGui var1);

    public boolean isInFilterGroup(CurrentFilterSettings var1);

    public boolean matchesSearch(String var1);
}

