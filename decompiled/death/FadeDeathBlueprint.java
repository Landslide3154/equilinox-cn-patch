/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.FadeDeath;
import instances.Entity;
import utils.CSVReader;

public class FadeDeathBlueprint
implements DeathAiBlueprint {
    private float fadeTime;

    @Override
    public DeathAi createInstance(Entity entity) {
        return new FadeDeath(this.fadeTime, entity);
    }

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.fadeTime = reader.getNextFloat();
        return this;
    }

    protected float getFadeTime() {
        return this.fadeTime;
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return this.createInstance(entity);
    }
}

