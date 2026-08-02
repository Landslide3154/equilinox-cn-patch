/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.StandardEatingAi;
import gameManaging.GameManager;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import toolbox.Maths;
import toolbox.Transformation;

public class DiggingEater
implements EatingAnimation {
    private static final float[] DIG_FRAMES = new float[]{0.0f, 50.0f, 35.0f, 50.0f, 35.0f, 55.0f, -40.0f, 0.0f};
    private static final float[] DIG_TIMES = new float[]{0.0f, 0.5f, 0.8f, 1.1f, 1.4f, 1.7f, 1.8f, 2.2f};
    private static final float PARTICLE_START = 0.2f;
    private static final float PARTICLE_END = 1.4f;
    private static final float EAT_TIME = 1.75f;
    private final Transformation transform;
    private final MovementComp mover;
    private final StandardEatingAi eater;
    private boolean ready = false;
    private boolean eaten = false;
    private float digTime = 0.0f;
    private int nextFrame = 1;
    private ParticleSystem dustParticles = DiggingEater.createDustParticleSystem();
    private ParticleSystem rockParticles = DiggingEater.createRockParticleSystem();

    public DiggingEater(Transformation transform, MovementComp mover, StandardEatingAi eater) {
        this.transform = transform;
        this.mover = mover;
        this.eater = eater;
    }

    @Override
    public boolean doNomming(boolean targetAvailable) {
        if (!this.checkReady()) {
            return !targetAvailable;
        }
        boolean finished = this.updateFrame();
        if (finished) {
            this.transform.setXRotation(0.0f);
            return true;
        }
        this.updateAnimation();
        this.emitParticles();
        this.checkEating();
        return false;
    }

    private boolean checkReady() {
        if (!this.ready) {
            this.ready = this.mover.normalize();
        }
        return this.ready;
    }

    private void updateAnimation() {
        int lastFrame = this.nextFrame - 1;
        float progress = (this.digTime - DIG_TIMES[lastFrame]) / (DIG_TIMES[this.nextFrame] - DIG_TIMES[lastFrame]);
        float yRot = Maths.smoothInterpolate(DIG_FRAMES[lastFrame], DIG_FRAMES[this.nextFrame], progress);
        this.transform.setXRotation(yRot);
    }

    private boolean updateFrame() {
        this.digTime += GameManager.getGameSeconds();
        int i = this.nextFrame;
        while (i < DIG_TIMES.length) {
            if (DIG_TIMES[i] > this.digTime) {
                this.nextFrame = i;
                return false;
            }
            ++i;
        }
        return true;
    }

    private void checkEating() {
        if (!this.eaten && this.digTime > 1.75f) {
            this.eater.eat();
            this.eaten = true;
            this.rockParticles.pulseParticles(this.eater.getTarget().getTransform().getPosition(), 1.0f);
        }
    }

    private void emitParticles() {
        if (this.digTime >= 0.2f && this.digTime <= 1.4f) {
            this.dustParticles.generateParticles(this.eater.getTarget().getTransform().getPosition(), 1.0f);
        }
    }

    private static ParticleSystem createDustParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(8);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 6.0f, 0.3f, 0.02f, 1.8f, 0.4f);
        system.setDirection(Maths.UP, 0.3f);
        return system;
    }

    private static ParticleSystem createRockParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 16.0f, 1.8f, 0.5f, 0.5f, 0.06f);
        system.setDirection(Maths.UP, 0.4f);
        system.setScaleError(0.5f);
        system.setLifeError(0.4f);
        system.setFadeValues(1.0f, 0.0f, 0.9f);
        return system;
    }

    @Override
    public void interrupt() {
        this.transform.setXRotation(0.0f);
    }
}

