/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import death.DeathAiBlueprint;
import death.FallDeath;
import instances.Entity;
import java.util.HashSet;
import java.util.Set;
import particleComponent.ParticleSystemLoader;
import particles.ParticleSystem;
import utils.CSVReader;

public class FallDeathBlueprint
implements DeathAiBlueprint {
    private float fallTime;
    private float totalTime;
    private float fallenAngle;
    private boolean hasParticleEffect;
    private float explodeTime;
    private boolean useEntityColour;
    private ParticleSystem system;
    private Set<Integer> particleModelStages = new HashSet<Integer>();

    @Override
    public DeathAi createInstance(Entity entity) {
        return new FallDeath(this, entity);
    }

    @Override
    public DeathAiBlueprint loadInfo(CSVReader reader) {
        this.fallTime = reader.getNextLabelFloat();
        this.totalTime = reader.getNextLabelFloat();
        this.fallenAngle = reader.getNextLabelFloat();
        this.hasParticleEffect = reader.getNextLabelBool();
        if (this.hasParticleEffect) {
            this.explodeTime = reader.getNextLabelFloat();
            this.useEntityColour = reader.getNextLabelBool();
            this.system = ParticleSystemLoader.loadParticleSystem(reader);
            this.loadModelStages(reader);
        }
        return this;
    }

    @Override
    public DeathAi createInstance(Entity entity, Object extraData) {
        return this.createInstance(entity);
    }

    protected boolean isParticleModelStage(int stage) {
        return this.particleModelStages.contains(stage);
    }

    protected boolean hasParticleEffect() {
        return this.hasParticleEffect;
    }

    protected boolean useEntityColour() {
        return this.useEntityColour;
    }

    protected float getFallTime() {
        return this.fallTime;
    }

    protected float getTotalTime() {
        return this.totalTime;
    }

    protected float getFallenAngle() {
        return this.fallenAngle;
    }

    protected float getExplodeTime() {
        return this.explodeTime;
    }

    protected ParticleSystem getSystem() {
        return this.system;
    }

    private void loadModelStages(CSVReader reader) {
        int count = reader.getNextLabelInt();
        int i = 0;
        while (i < count) {
            this.particleModelStages.add(reader.getNextInt());
            ++i;
        }
    }
}

