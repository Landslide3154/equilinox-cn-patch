/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.UpDownDeath;
import instances.Entity;
import particleComponent.ParticleSystemLoader;
import particles.ParticleSystem;
import utils.CSVReader;

public class UpDownDeathBlueprint
implements DeathAiBlueprint {
    private ParticleSystem particles;
    private float speed;

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.speed = reader.getNextLabelFloat();
        this.particles = ParticleSystemLoader.loadParticleSystem(reader);
        return this;
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return this.createInstance(entity);
    }

    protected float getSpeed() {
        return this.speed;
    }

    protected ParticleSystem getParticleSystem() {
        return this.particles;
    }

    @Override
    public DeathAi createInstance(Entity entity) {
        return new UpDownDeath(this, entity, entity.getTransform());
    }
}

