/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import gameManaging.GameManager;
import java.io.IOException;
import languages.GameText;
import session.Session;
import tasks.TaskRequirement;
import utils.BinaryReader;
import utils.BinaryWriter;

public abstract class CountReq
extends TaskRequirement {
    private static final String DONE = GameText.getText(656);
    private static final String NOT_DONE = GameText.getText(657);
    private int target;
    private int count = 0;
    private boolean complete = false;
    private boolean useWords = false;

    protected CountReq(String shortDescription, String description, boolean needsChecking, int target) {
        super(shortDescription, description, needsChecking);
        this.target = target;
    }

    protected CountReq(String shortDescription, String description, boolean needsChecking) {
        super(shortDescription, description, needsChecking);
        this.target = 1;
        this.useWords = true;
    }

    @Override
    public void loadState(BinaryReader reader, Session newSession) throws Exception {
        this.count = super.needsChecking() ? Math.min(this.target, this.checkCount(newSession)) : reader.readInt();
        this.complete = this.count >= this.target;
    }

    @Override
    public void reset(Session newSession) {
        this.count = super.needsChecking() ? Math.min(this.target, this.checkCount(newSession)) : 0;
        this.complete = this.count >= this.target;
        super.statusChange();
    }

    @Override
    public String getProgressText() {
        if (this.target == 1 && this.useWords) {
            if (this.count < 1) {
                return NOT_DONE;
            }
            return DONE;
        }
        if (this.target < 100) {
            return String.valueOf(this.count) + "/" + this.target;
        }
        return String.valueOf((int)((float)this.count / (float)this.target * 100.0f)) + "%";
    }

    @Override
    public boolean isStarted() {
        return this.count > 0;
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public void setCompleted() {
        this.count = this.target;
        this.complete = true;
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        if (!super.needsChecking()) {
            writer.writeInt(this.count);
        }
    }

    @Override
    public float getProgress() {
        return (float)this.count / (float)this.target;
    }

    @Override
    public void check() {
        int newCount = this.checkCount(GameManager.getSession());
        this.setCount(newCount);
    }

    protected abstract int checkCount(Session var1);

    protected void increment() {
        this.setCount(this.count + 1);
    }

    protected void increase(int amount) {
        this.setCount(this.count + amount);
    }

    protected void setCount(int newCount) {
        if (this.complete || newCount == this.count) {
            return;
        }
        this.count = Math.min(newCount, this.target);
        if (this.count == this.target) {
            this.complete = true;
        }
        super.statusChange();
    }
}

