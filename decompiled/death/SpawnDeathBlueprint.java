/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.SpawnDeath;
import instances.Entity;
import toolbox.Maths;
import utils.CSVReader;

public class SpawnDeathBlueprint
implements DeathAiBlueprint {
    private int entityId;
    private int minCount;
    private int maxCount;
    private boolean onlyFullGrown;

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.entityId = reader.getNextInt();
        this.minCount = reader.getNextInt();
        this.maxCount = reader.getNextInt();
        this.onlyFullGrown = reader.getNextBool();
        return this;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean mustBeFullGrown() {
        return this.onlyFullGrown;
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return new SpawnDeath(entity, this, (Integer)extraData);
    }

    public int getRandomCount() {
        int dif = this.maxCount - this.minCount;
        return this.minCount + Maths.RANDOM.nextInt(dif + 1);
    }

    @Override
    public DeathAi createInstance(Entity entity) {
        return new SpawnDeath(entity, this, this.getRandomCount());
    }
}

