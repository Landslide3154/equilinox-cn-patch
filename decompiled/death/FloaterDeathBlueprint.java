/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.FloaterDeath;
import instances.Entity;
import utils.CSVReader;

public class FloaterDeathBlueprint
implements DeathAiBlueprint {
    private float deadRot;

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.deadRot = reader.getNextLabelFloat();
        return this;
    }

    @Override
    public DeathAi createInstance(Entity entity) {
        return new FloaterDeath(entity, this.deadRot);
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return this.createInstance(entity);
    }
}

