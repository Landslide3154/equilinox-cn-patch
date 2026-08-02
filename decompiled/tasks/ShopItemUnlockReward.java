/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import guis.GuiComponent;
import languages.ComplexString;
import languages.GameText;
import shops.ShopItem;
import taskUi.TaskRewardGui;
import tasks.Reward;
import tasks.Task;
import tasks.TaskManager;
import tasks.TaskRewardOrder;
import userInterfaces.TabButtonUi;

public class ShopItemUnlockReward
extends Reward {
    private static final ComplexString UNLOCKS = GameText.getComplexText(105);
    private final ShopItem item;

    public ShopItemUnlockReward(ShopItem item) {
        this.item = item;
    }

    @Override
    public void setStateUnlocked() {
        this.item.setLocked(false);
    }

    @Override
    public void payOut() {
        this.item.unlock();
        TabButtonUi tab = this.item.getShop().getShopButton();
        if (tab != null && !tab.isToggledOn()) {
            tab.flash(true);
        }
    }

    @Override
    public String getInfo() {
        return UNLOCKS.getString(this.item.getName());
    }

    @Override
    public void linkTask(Task task, TaskManager manager) {
        this.item.linkTask(task);
    }

    @Override
    public boolean hasExtraInfo() {
        return false;
    }

    @Override
    public GuiComponent addExtraInfo(TaskRewardGui panel, float xPos, float yPos) {
        return null;
    }

    @Override
    public int getOrderingWeight() {
        return TaskRewardOrder.ITEM.ordinal();
    }
}

