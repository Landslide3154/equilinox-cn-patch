/*
 * Decompiled with CFR 0.152.
 */
package rockingMovement;

import baseMovement.BaseMovement;
import breedingTraits.FloatTrait;
import gameManaging.GameManager;
import interpolation.SmoothFloat;
import interpolation.SteadyFloat;
import rockingMovement.RockingBlueprint;
import toolbox.Maths;

public class RockingMovement
extends BaseMovement {
    private static final float MAX_ROT_PER_SEC = 200.0f;
    private static final float SMOOTH_RETURN = 100.0f;
    private static final float SPEED_AGIL = 4.0f;
    private final float swimAgility;
    private static final float SHALLOW_SWIM_DEPTH = 0.7f;
    private static final float SHALLOW_SWIM_INV = 1.4285715f;
    private static final float LIFE_FACTOR = 0.8f;
    private final RockingBlueprint blueprint;
    private float time = 0.0f;
    private SmoothFloat speed = new SmoothFloat(0.0f, 4.0f);
    private boolean rotating = false;
    private boolean swimming = false;
    private SteadyFloat rotation = new SteadyFloat(0.0f, 200.0f);

    protected RockingMovement(RockingBlueprint blueprint) {
        super(blueprint, blueprint.rotSpeed, blueprint.hasEggStage, blueprint.getRunFactor());
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
        this.swimming = this.setHeight(this.getTransform().getTerrainHeight());
        if (this.rotating) {
            this.updateRotation(!super.isMoving());
        }
        if (!this.rotation.isReached()) {
            float aimRot = this.rotation.update(GameManager.getGameSeconds());
            this.setRotation(aimRot);
        }
    }

    private void moveForward() {
        float swimFactor = this.swimming ? this.blueprint.swimFactor : 1.0f;
        double rot = Math.toRadians(super.getActualRotY());
        float speed = this.speed.get() * swimFactor * GameManager.getGameSeconds() * super.getLifeSizeFactor();
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        super.getTransform().increasePosition(dx, 0.0f, dz);
    }

    private boolean setHeight(float height) {
        float yPos;
        float waterDepth = GameManager.getWorld().getWaterHeight() - height;
        float desiredHeight = 0.0f;
        if (waterDepth <= 0.0f) {
            this.getTransform().setYPosition(height);
            return false;
        }
        desiredHeight = waterDepth < this.blueprint.swimHeight * 1.4285715f ? GameManager.getWorld().getWaterHeight() - waterDepth * 0.7f : GameManager.getWorld().getWaterHeight() - this.blueprint.swimHeight;
        float toDesiredHeight = desiredHeight - (yPos = this.getTransform().getPosition().y);
        if (toDesiredHeight >= 0.0f) {
            this.getTransform().setYPosition(desiredHeight);
        } else {
            this.getTransform().setYPosition(yPos + (desiredHeight - yPos) * GameManager.getGameSeconds() * 5.0f);
        }
        return true;
    }

    private void updateRotation(boolean stop) {
        if (!this.isMoving()) {
            this.endRotation();
            return;
        }
        float rot = this.calculateRot();
        this.rotation.setTarget(rot);
    }

    private float calculateRot() {
        float swimFactor = this.swimming ? this.blueprint.swimFactor : 1.0f;
        float rotSpeed = this.blueprint.rockSpeed * swimFactor * Maths.getFactor(this.getLifeFactor(), 0.8f) * this.getRunFactor() / this.getSizeFactor();
        this.time += GameManager.getGameSeconds() * rotSpeed;
        this.time %= 1.0f;
        return Maths.fakeSin(this.blueprint.minRot, this.blueprint.maxRot, this.time);
    }

    private void setRotation(float rot) {
        if (this.blueprint.rotType == 1) {
            super.getTransform().setXRotation(rot);
        } else if (this.blueprint.rotType == 0) {
            super.getTransform().setZRotation(rot);
            super.getTransform().returnXRotToZero(100.0f);
        } else {
            super.setExtraRotY(rot);
            super.getTransform().returnXRotToZero(100.0f);
        }
    }

    private void endRotation() {
        this.time = 0.0f;
        this.rotating = false;
        this.rotation.setTarget(0.0f);
    }
}

