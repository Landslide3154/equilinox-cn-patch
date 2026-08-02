/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import gameManaging.GameManager;
import guis.GuiComponent;
import taskUi.TaskRewardGui;
import tasks.Reward;
import tasks.Task;
import tasks.TaskManager;
import tasks.TaskRewardOrder;
import toolbox.Maths;

public class CashReward
extends Reward {
    private final int dp;

    protected CashReward(int dp) {
        this.dp = dp;
    }

    @Override
    public void setStateUnlocked() {
    }

    @Override
    public void payOut() {
        GameManager.getSession().getStats().increaseDp(this.dp);
    }

    @Override
    public String getInfo() {
        return String.valueOf(Maths.formatNumber(this.dp)) + " dp";
    }

    @Override
    public void linkTask(Task task, TaskManager manager) {
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
        return TaskRewardOrder.CASH.ordinal();
    }
}

