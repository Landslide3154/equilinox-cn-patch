/*
 * Decompiled with CFR 0.152.
 */
package shops;

import gridLayout.GridComponent;
import guis.GuiComponent;
import shopping.Shop;
import tasks.Task;
import textures.Texture;

public interface ShopItem
extends GridComponent {
    public int getPrice();

    public String getName();

    public Texture getIcon();

    public boolean flipIcon();

    public boolean isLocked();

    public void buy();

    public void unlock();

    public void linkTask(Task var1);

    public Task getRequiredTask();

    public boolean hasLinkedTask();

    public GuiComponent getLockedMouseover();

    public void reactToLockedClick();

    public void setLocked(boolean var1);

    public void displayInfo();

    public boolean isNew();

    public void setNotNew();

    public int getTier();

    public Shop getShop();

    public boolean isSpecial();
}

