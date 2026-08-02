/*
 * Decompiled with CFR 0.152.
 */
package dolphinMovement;

import baseMovement.BaseMovement;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentBundle;
import dolphinMovement.DolphinMoveBlueprint;
import gameManaging.GameManager;
import interpolation.SmoothFloat;
import interpolation.SteadyFloat;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import toolbox.Colour;
import toolbox.Maths;

public class DolphinMovement
extends BaseMovement {
    private static final ParticleSystem SPLASH = DolphinMovement.createSplashParticleSystem();
    private static final ParticleSystem FOAM = DolphinMovement.createFoamParticleSystem();
    private static final float MAX_ROT_PER_SEC = 200.0f;
    private static final float SPEED_AGIL = 4.0f;
    private final float swimAgility;
    private static final float LIFE_FACTOR = 0.7f;
    private static final float SHALLOW_WATER = 0.9f;
    private static final float TRANS_SPEED = 2.4f;
    private static final float MIN_TRANS_SPEED = 0.05f;
    private static final float HAPPY_FACTOR = 0.7f;
    private static final float WOBBLE_DEPTH = 0.3f;
    private static final float WOBBLE_SPEED = 2.0f;
    private static final float PARTICLE_RANGE = 25.0f;
    private final DolphinMoveBlueprint blueprint;
    private boolean lockedHeight = false;
    private float time = 0.0f;
    private SmoothFloat speed = new SmoothFloat(0.0f, 4.0f);
    private boolean rotating = false;
    private boolean swimming = true;
    private SteadyFloat rotation = new SteadyFloat(0.0f, 200.0f);
    private float upAcceleration = 0.0f;
    private float upVelocity = 0.0f;
    private float lastDiveRotation = 0.0f;
    private float lastDiveHeight = 0.0f;
    private float realRot = 0.0f;
    private boolean inWater = true;

    protected DolphinMovement(DolphinMoveBlueprint blueprint) {
        super(blueprint, blueprint.rotSpeed, blueprint.hasEggStage, 1.4f);
        this.blueprint = blueprint;
        this.swimAgility = 4.0f * blueprint.swimInertia;
    }

    @Override
    public boolean normalize() {
        return !this.rotating && this.rotation.isReached();
    }

    @Override
    public boolean isSwimming() {
        return this.swimming;
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        this.speed.setAgility(this.swimming ? this.swimAgility : 4.0f);
        this.speed.setTarget(0.0f);
        if (super.isMoving()) {
            this.indicateMoving();
        }
        this.updateMoving();
    }

    private void indicateMoving() {
        this.rotating = true;
        float speedValue = ((FloatTrait)super.getTrait(0)).getValue();
        this.speed.setTarget(speedValue * super.getRunFactor());
    }

    private void updateMoving() {
        this.speed.update(GameManager.getGameSeconds());
        this.moveForward();
        if (this.shouldBeDiving()) {
            this.lockedHeight = false;
            this.updateDiving();
        } else if (this.lockedHeight) {
            this.updateSwimming();
        } else {
            this.updateTransition();
        }
        float wobble = this.calculateWobbleRot();
        float wobbleDamper = this.calcWobbleDamper();
        this.getTransform().setXRotation(this.realRot + wobble * wobbleDamper);
    }

    private boolean shouldBeDiving() {
        boolean ableToDive;
        boolean inAir;
        boolean bl = inAir = GameManager.getWorld().getWaterHeight() < this.getTransform().getPosition().y || this.upVelocity > 0.1f;
        if (this.getTransform().getTerrainHeight() > GameManager.getWorld().getWaterHeight()) {
            return false;
        }
        if (inAir) {
            return true;
        }
        float waterDepth = GameManager.getWorld().getWaterHeight() - this.getTransform().getTerrainHeight();
        boolean wantsToDive = super.isMoving() && super.isRunning();
        boolean bl2 = ableToDive = this.getGrowthComp().getGrowthFactor() > 0.5f && this.getLifeComp().getEnvironmentalSatisfaction() > 0.7f;
        return ableToDive && wantsToDive && waterDepth > 0.9f;
    }

    private void updateTransition() {
        float excess;
        this.upAcceleration = 0.0f;
        this.upVelocity = 0.0f;
        float dolphPos = this.getTransform().getPosition().y;
        float swimHeight = this.getSwimHeight();
        float toSwimHeight = swimHeight - dolphPos;
        float change = (toSwimHeight + (excess = Math.signum(toSwimHeight) * 0.05f)) * GameManager.getGameSeconds() * 2.4f;
        if (Math.abs(change) >= Math.abs(toSwimHeight)) {
            this.getTransform().setYPosition(swimHeight);
            this.lockedHeight = true;
            return;
        }
        this.getTransform().setYPosition(dolphPos + change);
        float blend = (dolphPos - this.lastDiveHeight) / (swimHeight - this.lastDiveHeight);
        this.realRot = Maths.interpolate(this.lastDiveRotation, 0.0f, blend);
    }

    private void updateSwimming() {
        this.realRot = 0.0f;
        this.getTransform().setYPosition(this.getSwimHeight());
    }

    private void updateDiving() {
        boolean under;
        this.updateAcceleration();
        this.updateUpwardsMovement();
        this.updateDivingRotation();
        this.lastDiveHeight = this.getTransform().getPosition().y;
        this.lastDiveRotation = this.realRot;
        boolean bl = under = super.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight();
        if (this.inWater != under) {
            this.makeSplash();
            this.inWater = under;
        }
    }

    private void makeSplash() {
        if (super.getEntity().getCurrentGridSection().getDistanceFromCam() > 25.0f) {
            return;
        }
        Vector3f pos = new Vector3f(this.getTransform().getPosition());
        pos.y = GameManager.getWorld().getWaterHeight();
        SPLASH.pulseParticles(pos, 0.6f);
        FOAM.pulseParticles(pos, 0.6f);
    }

    private void updateUpwardsMovement() {
        this.upVelocity += this.upAcceleration * GameManager.getGameSeconds();
        this.upVelocity = Math.min(this.upVelocity, 2.0f);
        super.getTransform().increasePosition(0.0f, this.upVelocity * GameManager.getGameSeconds(), 0.0f);
    }

    private float getSwimHeight() {
        float terrainHeight = this.getTransform().getTerrainHeight();
        float waterDepth = GameManager.getWorld().getWaterHeight() - terrainHeight;
        float halfDepth = terrainHeight + waterDepth * 0.5f;
        if (waterDepth <= 0.0f) {
            return terrainHeight;
        }
        return Math.max(GameManager.getWorld().getWaterHeight() - this.blueprint.swimHeight, halfDepth);
    }

    private void moveForward() {
        float swimFactor = this.swimming ? this.blueprint.swimFactor : 1.0f;
        double rot = Math.toRadians(super.getActualRotY());
        float speed = this.speed.get() * swimFactor * GameManager.getGameSeconds() * super.getLifeSizeFactor();
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        super.getTransform().increasePosition(dx, 0.0f, dz);
    }

    private void updateAcceleration() {
        boolean under = super.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight();
        boolean underDeep = super.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight() - 0.05f;
        float f = this.upAcceleration = under ? 0.5f : -6.0f;
        if (under && (this.upVelocity > 0.0f || underDeep)) {
            this.upAcceleration = 7.0f;
        }
    }

    private void updateDivingRotation() {
        try {
            this.realRot = (float)(-Math.toDegrees(Math.atan(this.upVelocity * 0.7f / this.speed.get())));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private float calcWobbleDamper() {
        float depth = GameManager.getWorld().getWaterHeight() - this.getTransform().getPosition().y;
        return Maths.clamp(depth / 0.3f, 0.0f, 1.0f);
    }

    private float calculateWobbleRot() {
        float rotSpeed = Maths.getFactor(this.getLifeFactor(), 0.7f) * (this.speed.get() * 2.0f) / this.getSizeFactor();
        this.time += GameManager.getGameSeconds() * rotSpeed;
        this.time %= 1.0f;
        return Maths.fakeSin(this.blueprint.minRot, this.blueprint.maxRot, this.time);
    }

    private static ParticleSystem createSplashParticleSystem() {
        ParticleSystem system = new ParticleSystem(new Colour(174.0f, 215.0f, 196.0f, true), false, new PointSpawn(), 45.0f, 2.6f, 0.6f, 0.4f, 0.035f);
        system.setDirection(new Vector3f(0.0f, 1.0f, 0.0f), 0.1f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.5f);
        system.setDirectionLocalSpace();
        system.setOffset(new Vector3f(0.0f, 0.05f, 0.0f));
        system.setLifeError(0.4f);
        system.randomizeRotation();
        system.setXRotation(600.0f);
        system.setFadeValues(1.0f, 0.1f, 0.9f);
        return system;
    }

    private static ParticleSystem createFoamParticleSystem() {
        ParticleSystem system = new ParticleSystem(new Colour(210.0f, 231.0f, 221.0f, true), false, new PointSpawn(), 40.0f, 2.6f, 0.6f, 0.55f, 0.04f);
        system.setDirection(new Vector3f(0.0f, 1.0f, 0.0f), 0.1f);
        system.setScaleError(0.35f);
        system.setDirectionLocalSpace();
        system.setOffset(new Vector3f(0.0f, 0.05f, 0.0f));
        system.setLifeError(0.4f);
        system.setSpeedError(0.5f);
        system.randomizeRotation();
        system.setXRotation(500.0f);
        system.setFadeValues(1.0f, 0.1f, 0.9f);
        return system;
    }
}

