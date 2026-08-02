/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import gameManaging.GameManager;
import java.io.IOException;
import languages.GameText;
import session.Session;
import tasks.TaskRequirement;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TaskCompletorRequirement
extends TaskRequirement {
    private static final String SHORT_DESC = GameText.getText(109);
    private static final String DESC = GameText.getText(110);
    private int count = 0;

    protected TaskCompletorRequirement() {
        super(SHORT_DESC, DESC, false);
        EventManager.TASK_COMPLETE.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                TaskCompletorRequirement.this.check();
            }
        }, new String[0]);
    }

    @Override
    public void loadState(BinaryReader reader, Session nesSession) throws Exception {
        this.count = reader.readInt();
    }

    @Override
    public void reset(Session newSession) {
        this.count = 0;
        super.statusChange();
    }

    @Override
    public String getProgressText() {
        return String.valueOf(this.count) + "/" + this.getMax();
    }

    @Override
    public boolean isStarted() {
        return this.count > 0;
    }

    @Override
    public boolean isComplete() {
        return this.count >= this.getMax();
    }

    @Override
    public void setCompleted() {
        this.count = this.getMax();
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.count);
    }

    @Override
    public void check() {
        int newCount = Math.min(this.getMax(), GameManager.getTaskManager().calculateCompletedTaskCount());
        if (this.count != newCount) {
            this.count = newCount;
            super.statusChange();
        }
    }

    private int getMax() {
        return GameManager.getTaskManager().getTaskCount() - 1;
    }

    @Override
    public float getProgress() {
        return (float)this.count / (float)this.getMax();
    }
}

