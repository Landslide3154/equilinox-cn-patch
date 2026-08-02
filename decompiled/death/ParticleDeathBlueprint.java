/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.ParticleDeath;
import instances.Entity;
import particleComponent.ParticleSystemLoader;
import particles.ParticleSystem;
import utils.CSVReader;

public class ParticleDeathBlueprint
implements DeathAiBlueprint {
    private ParticleSystem system;

    @Override
    public DeathAi createInstance(Entity entity) {
        return new ParticleDeath(entity, this.system);
    }

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.system = ParticleSystemLoader.loadParticleSystem(reader);
        return this;
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return this.createInstance(entity);
    }

    protected ParticleSystem getSystem() {
        return this.system;
    }
}

