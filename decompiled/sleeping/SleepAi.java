/*
 * Decompiled with CFR 0.152.
 */
package sleeping;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.InterFloat;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import sleeping.SleepComponent;
import toolbox.Maths;
import toolbox.Transformation;
import world.GridSection;

public class SleepAi
implements Ai {
    private final ParticleSystem SNORE_PARTICLES = SleepAi.createSnoreParticleSystem();
    private static final float PARTICLE_MIN_DIS = 14.0f;
    private static final String DESC = GameText.getText(188);
    private static final float SLEEP_ROT = -52.0f;
    private static final float BREATH_SPEED = 0.5f;
    private static final float BREATH_ANGLE = 4.0f;
    private static final float SLEEP_ROT_SPEED = 100.0f;
    private static final float PRIORITY = 9.0f;
    private final Transformation transform;
    private final Entity entity;
    private final SleepComponent sleepComponent;
    private final float endTime;
    private boolean waking = false;
    private InterFloat sheepRot = new InterFloat(100.0f);
    private float time = 0.0f;

    public SleepAi(Entity entity, SleepComponent sleepComponent, Transformation transform, float endTime) {
        this.transform = transform;
        this.entity = entity;
        this.sleepComponent = sleepComponent;
        this.endTime = endTime;
        this.sheepRot.setSlide(transform.getRotZ() % 360.0f, -52.0f);
    }

    @Override
    public boolean carryOut() {
        float value = this.sheepRot.update(GameManager.getGameSeconds());
        this.transform.setZRotation(value);
        float currentTime = GameManager.getSession().getStats().getCalendar().getRawTime();
        if (currentTime > this.endTime && (double)currentTime - 0.5 < (double)this.endTime) {
            return this.updateWakingUp();
        }
        this.updateSleep();
        GridSection section = this.entity.getCurrentGridSection();
        if (section != null && section.getDistanceFromCam() < 14.0f) {
            this.SNORE_PARTICLES.generateParticles(this.transform.getModelMatrix(), 1.0f);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private void updateSleep() {
        if (this.sheepRot.isReached()) {
            this.time += GameManager.getGameSeconds() * 0.5f;
            float rot = Maths.fakeSin(-56.0f, -48.0f, this.time);
            this.transform.setZRotation(rot);
        }
    }

    private boolean updateWakingUp() {
        if (!this.waking) {
            this.wakeUp();
        }
        return this.sheepRot.isReached();
    }

    private void wakeUp() {
        this.waking = true;
        this.sheepRot.setSlide(this.transform.getRotZ(), 0.0f);
    }

    @Override
    public float getPriority() {
        return 9.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.sleepComponent;
    }

    @Override
    public void interrupt() {
        this.transform.setZRotation(0.0f);
    }

    private static ParticleSystem createSnoreParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(11);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 1.5f, 0.1f, -0.005f, 2.3f, 0.05f);
        system.setDirection(new Vector3f(0.0f, 1.0f, 0.0f), 0.2f);
        system.setScaleError(0.2f);
        system.setOffset(new Vector3f(0.0f, 1.5f, 1.5f));
        system.setLifeError(0.1f);
        system.setFadeValues(1.0f, 0.0f, 0.9f);
        return system;
    }
}

