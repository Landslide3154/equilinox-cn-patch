/*
 * Decompiled with CFR 0.152.
 */
package floatyMovement;

import baseMovement.BaseMovement;
import basics.DisplayManager;
import breedingTraits.FloatTrait;
import floatyMovement.FloatyMoveBlueprint;
import gameManaging.GameManager;
import interpolation.SmoothFloat;
import toolbox.Maths;

public class FloatyMovement
extends BaseMovement {
    private static final float SPEED_AGIL = 4.0f;
    private static final float RUN_FACTOR = 1.5f;
    private static final float SHALLOW_SWIM_DEPTH = 0.5f;
    private static final float SHALLOW_SWIM_INV = 2.0f;
    private static final float SWIM_HEIGHT = 4.0f;
    private static final float MIN_BOB_SPEED = 0.02f;
    private static final float MAX_BOB_SPEED = 0.12f;
    private static final float BOB_AMOUNT = 0.07f;
    private static final float MIN_DEPTH = 0.5f;
    private static final float MAX_DEPTH = 1.5f;
    private SmoothFloat speed = new SmoothFloat(0.0f, 4.0f);
    private boolean swimming = false;
    private float time = 0.0f;
    private float timeSpeed = Maths.randomNumberBetween(0.02f, 0.12f);

    protected FloatyMovement(FloatyMoveBlueprint blueprint) {
        super(blueprint, blueprint.rotSpeed, 1.5f);
    }

    @Override
    public boolean normalize() {
        return true;
    }

    @Override
    public boolean isSwimming() {
        return this.swimming;
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        this.speed.setAgility(4.0f);
        this.speed.setTarget(0.0f);
        if (super.isMoving()) {
            this.indicateMoving();
        }
        this.updateMoving();
    }

    private float getOffset(float waterDepth) {
        this.time += GameManager.getGameSeconds() * this.timeSpeed;
        this.time %= 1.0f;
        float offset = Maths.fakeSin(-1.0f, 1.0f, this.time);
        float blend = Maths.quickStep(0.5f, 1.5f, waterDepth);
        return (offset *= 0.07f) * blend;
    }

    private void indicateMoving() {
        float speedValue = ((FloatTrait)super.getTrait(0)).getValue();
        this.speed.setTarget(speedValue * super.getRunFactor());
    }

    private void updateMoving() {
        this.speed.update(GameManager.getGameSeconds());
        this.moveForward();
        this.swimming = this.setHeight(this.getTransform().getTerrainHeight());
    }

    private void moveForward() {
        double rot = Math.toRadians(super.getActualRotY());
        float speed = this.speed.get() * GameManager.getGameSeconds() * super.getLifeSizeFactor();
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        super.getTransform().increasePosition(dx, 0.0f, dz);
    }

    protected boolean setHeight(float height) {
        float waterDepth = GameManager.getWorld().getWaterHeight() - height;
        float desiredHeight = 0.0f;
        if (waterDepth <= 0.0f) {
            this.getTransform().setYPosition(height);
            return false;
        }
        desiredHeight = waterDepth < 8.0f ? GameManager.getWorld().getWaterHeight() - waterDepth * 0.5f : GameManager.getWorld().getWaterHeight() - 4.0f;
        float yPos = this.getTransform().getPosition().y;
        float toDesiredHeight = (desiredHeight += this.getOffset(waterDepth)) - yPos;
        if (toDesiredHeight >= 0.0f) {
            this.getTransform().setYPosition(desiredHeight);
        } else {
            this.getTransform().setYPosition(yPos + (desiredHeight - yPos) * DisplayManager.getDeltaSeconds() * 5.0f);
        }
        return true;
    }
}

