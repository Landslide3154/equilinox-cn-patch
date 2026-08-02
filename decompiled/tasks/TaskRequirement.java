/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import java.io.IOException;
import session.Session;
import tasks.Task;
import utils.BinaryReader;
import utils.BinaryWriter;

public abstract class TaskRequirement {
    private final String shortDescription;
    private final String description;
    private final boolean needsChecking;
    private Task task;
    private boolean overviewUpdateNeeded = false;
    private boolean infoUpdateNeeded = false;

    protected TaskRequirement(String shortDescription, String description, boolean needsChecking) {
        this.needsChecking = needsChecking;
        this.description = description;
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void notifyOverviewUpdated() {
        this.overviewUpdateNeeded = false;
    }

    public void notifyInfoUpdated() {
        this.infoUpdateNeeded = false;
    }

    public boolean needsChecking() {
        return this.needsChecking;
    }

    public boolean isOverviewUpdateRequired() {
        return this.overviewUpdateNeeded;
    }

    public boolean isInfoUpdateRequired() {
        return this.infoUpdateNeeded;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean alreadyCompleted() {
        return this.task.alreadyCompleted();
    }

    protected void statusChange() {
        this.infoUpdateNeeded = true;
        this.overviewUpdateNeeded = true;
        this.task.updateState();
    }

    public abstract void loadState(BinaryReader var1, Session var2) throws Exception;

    public abstract void reset(Session var1);

    public abstract String getProgressText();

    public abstract float getProgress();

    public abstract boolean isStarted();

    public abstract boolean isComplete();

    public abstract void setCompleted();

    public abstract void export(BinaryWriter var1) throws IOException;

    public abstract void check();
}

