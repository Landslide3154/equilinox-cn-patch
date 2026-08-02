/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import guis.GuiComponent;
import taskUi.TaskRewardGui;
import tasks.Task;
import tasks.TaskManager;

public abstract class Reward
implements Comparable<Reward> {
    public abstract void setStateUnlocked();

    public abstract void payOut();

    public abstract String getInfo();

    public abstract GuiComponent addExtraInfo(TaskRewardGui var1, float var2, float var3);

    public abstract boolean hasExtraInfo();

    public abstract void linkTask(Task var1, TaskManager var2);

    public abstract int getOrderingWeight();

    @Override
    public int compareTo(Reward other) {
        return this.getOrderingWeight() - other.getOrderingWeight();
    }
}

