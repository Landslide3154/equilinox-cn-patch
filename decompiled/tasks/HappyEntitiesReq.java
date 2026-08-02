/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import instances.Entity;
import java.io.IOException;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.TaskRequirement;
import utils.BinaryReader;
import utils.BinaryWriter;

public class HappyEntitiesReq
extends TaskRequirement {
    private static final ComplexString TITLE = GameText.getComplexText(99);
    private static final ComplexString DESC = GameText.getComplexText(100);
    private final int target;
    private final float enviroAim;
    private final Classification classification;
    private int count = 0;
    private boolean fullGrown;

    protected HappyEntitiesReq(Classification classification, int number, int enviro, boolean fullGrown) {
        super(TITLE.getString(classification.getName()), DESC.getString(Integer.toString(number), classification.getName(), Integer.toString(enviro)), true);
        this.target = number;
        this.fullGrown = fullGrown;
        this.classification = classification;
        this.enviroAim = (float)enviro / 100.0f;
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
        return String.valueOf(this.count) + "/" + this.target;
    }

    @Override
    public float getProgress() {
        return (float)this.count / (float)this.target;
    }

    @Override
    public boolean isStarted() {
        return this.count > 0;
    }

    @Override
    public boolean isComplete() {
        return this.count >= this.target;
    }

    @Override
    public void setCompleted() {
        this.count = this.target;
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.count);
    }

    @Override
    public void check() {
        int counter = 0;
        EntityBundle entities = GameManager.getWorld().getEntityGrid().getSortedEntities().getEntities(this.classification);
        if (entities == null) {
            return;
        }
        for (Entity entity : entities) {
            LifeComponent lifeComp = (LifeComponent)entity.getComponent(ComponentType.LIFE);
            GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (!(lifeComp.getEnvironmentalSatisfaction() > this.enviroAim) || this.fullGrown && !growth.isFullyGrown()) continue;
            ++counter;
        }
        if (counter != this.count) {
            this.count = counter;
            super.statusChange();
        }
    }
}

