/*
 * Decompiled with CFR 0.152.
 */
package iconSystem;

import particles.Particle;
import particles.ParticleTexture;
import toolbox.Transformation;

public class StatusIcon {
    private static final float ENTITY_PAD = 0.4f;
    private static final float ICON_PAD = 0.1f;
    private final ParticleTexture texture;
    private final boolean hasStages;
    private final float size;
    private int stage;
    private Particle iconParticle;

    protected StatusIcon(ParticleTexture texture, float size) {
        this.texture = texture;
        this.size = size;
        this.hasStages = false;
    }

    protected StatusIcon(ParticleTexture texture, float size, boolean manual, int initialStage) {
        this.texture = texture;
        this.size = size;
        this.hasStages = manual;
        this.stage = initialStage;
    }

    public void setStage(int stage) {
        this.stage = stage;
        if (this.iconParticle != null) {
            this.iconParticle.setStage(stage);
        }
    }

    protected float getSize() {
        return this.size;
    }

    protected void kill() {
        if (this.iconParticle != null) {
            this.iconParticle.kill();
        }
    }

    public void setHeight(float height) {
        this.iconParticle.setHeightOffset(height);
    }

    protected ParticleTexture getTexture() {
        return this.texture;
    }

    protected void initIcon(Transformation transform, float height) {
        this.iconParticle = this.createParticle(transform, height);
        if (this.hasStages) {
            this.iconParticle.setManualStages(true);
            this.iconParticle.setStage(this.stage);
        }
    }

    private Particle createParticle(Transformation transform, float height) {
        return new Particle(this.texture, this.size, 0.02f, transform, height);
    }
}

