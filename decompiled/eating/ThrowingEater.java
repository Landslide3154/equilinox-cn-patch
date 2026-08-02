/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.StandardEatingAi;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import toolbox.Maths;
import toolbox.Transformation;

public class ThrowingEater
implements EatingAnimation {
    private static final ParticleSystem dustParticles = ThrowingEater.createDustParticleSystem();
    private static final ParticleSystem rockParticles = ThrowingEater.createRockParticleSystem();
    private static final float ROCK_AMOUNT = 3.0f;
    private static final float ROCK_SPEED = 6.0f;
    private static final float ROCK_TIME = 0.5f;
    private final Transformation transform;
    private final StandardEatingAi eater;
    private final MovementComp mover;
    private Transformation carrotTransform;
    private boolean struggling = true;
    private float struggleTime = 0.0f;
    private float timer = 0.4f;

    public ThrowingEater(Transformation transform, StandardEatingAi eater, MovementComp mover) {
        this.transform = transform;
        this.eater = eater;
        this.mover = mover;
    }

    @Override
    public boolean doNomming(boolean targetAvailable) {
        if (!this.mover.normalize()) {
            return !targetAvailable;
        }
        if (this.struggling) {
            this.struggle();
            return !targetAvailable;
        }
        this.updateRabbit();
        return this.checkCarrot();
    }

    private void updateRabbit() {
        Vector3f targetPos = this.eater.getTarget().getTransform().getPosition();
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(this.transform.getPosition().x, this.transform.getPosition().z);
        float height = targetPos.y - terrainHeight;
        float x = 0.2f;
        float rot = (float)Math.atan(height / x);
        float offset = (float)((double)0.05f * Math.sin(rot));
        float heightActual = offset + terrainHeight;
        this.transform.setYPosition(heightActual);
        this.transform.setXRotation((float)(-Math.toDegrees(rot)));
    }

    private void struggle() {
        this.struggleTime += GameManager.getGameSeconds();
        float rot = Maths.fakeSin(-3.0f, 3.0f, this.struggleTime * 6.0f);
        this.transform.setXRotation(rot);
        if (this.struggleTime >= 0.5f) {
            this.launchCarrot();
        }
    }

    private boolean checkCarrot() {
        this.timer -= GameManager.getGameSeconds();
        if (this.timer < 0.0f) {
            float terrainHeight = GameManager.getWorld().getHeightOfTerrain(this.carrotTransform.getPosition().x, this.carrotTransform.getPosition().z);
            return this.carrotTransform.getPosition().y < terrainHeight + 0.1f;
        }
        return false;
    }

    private void launchCarrot() {
        this.struggling = false;
        this.eater.eat();
        this.carrotTransform = this.eater.getTarget().getTransform();
        rockParticles.pulseParticles(this.eater.getTarget().getTransform().getPosition(), 1.0f);
    }

    private static ParticleSystem createDustParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(8);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 1.0f, 0.2f, 0.02f, 1.0f, 0.25f);
        system.setDirection(Maths.UP, 0.3f);
        return system;
    }

    private static ParticleSystem createRockParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 15.0f, 1.0f, 0.3f, 0.5f, 0.021f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.1f);
        system.setLifeError(0.4f);
        system.setFadeValues(1.0f, 0.0f, 0.9f);
        return system;
    }

    @Override
    public void interrupt() {
        this.transform.setXRotation(0.0f);
    }
}

