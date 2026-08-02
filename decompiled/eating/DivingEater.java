/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.StandardEatingAi;
import gameManaging.GameManager;
import interpolation.InterFloat;
import toolbox.Transformation;

public class DivingEater
implements EatingAnimation {
    private static final float DIVE_SPEED = 0.5f;
    private static final float EAT_HEIGHT = 0.15f;
    private static final float ACCEL_TIME = 1.0f;
    private static final float DIVE_ROT = 90.0f;
    private static final float ROT_SPEED_DOWN = 120.0f;
    private static final float ROT_SPEED_UP = 150.0f;
    private final MovementComp mover;
    private final Transformation transform;
    private final StandardEatingAi eater;
    private boolean ready = false;
    private InterFloat downSpeed = new InterFloat();
    private InterFloat rotation = new InterFloat();
    private boolean swimmingDown = true;
    private float startHeight = -100.0f;

    protected DivingEater(MovementComp mover, Transformation transform, StandardEatingAi eater) {
        this.mover = mover;
        this.eater = eater;
        this.transform = transform;
        this.downSpeed.setSlideWithSetTime(0.0f, 0.5f, 1.0f);
        this.rotation.setSlideWithChange(0.0f, 90.0f, 120.0f);
    }

    @Override
    public boolean doNomming(boolean targetAvailable) {
        if (!this.checkReady()) {
            return false;
        }
        this.startHeight = Math.max(this.startHeight, this.transform.getPosition().y);
        this.mover.block(true);
        if (!this.swimmingDown) {
            return this.swimUp();
        }
        this.swimDown(targetAvailable);
        return false;
    }

    private boolean swimUp() {
        float value = this.downSpeed.update(GameManager.getGameSeconds());
        this.transform.increasePosition(0.0f, value * GameManager.getGameSeconds(), 0.0f);
        this.transform.setXRotation(this.rotation.update(GameManager.getGameSeconds()));
        if (this.transform.getPosition().y >= this.startHeight) {
            this.mover.block(false);
            this.transform.setXRotation(0.0f);
            this.transform.setYPosition(this.startHeight);
            return true;
        }
        return false;
    }

    private void swimDown(boolean targetAvailable) {
        float value = this.downSpeed.update(GameManager.getGameSeconds());
        this.transform.increasePosition(0.0f, -value * GameManager.getGameSeconds(), 0.0f);
        this.transform.setXRotation(this.rotation.update(GameManager.getGameSeconds()));
        if (this.transform.getPosition().y - this.eater.getTarget().getTransform().getPosition().y < 0.15f || !targetAvailable) {
            this.swimmingDown = false;
            this.rotation.setSlideWithChange(this.transform.getRotX(), 0.0f, 150.0f);
            this.downSpeed.setSlideWithSetTime(0.0f, 0.5f, 1.0f);
            if (targetAvailable) {
                this.eater.eat();
            }
        }
    }

    private boolean checkReady() {
        if (!this.ready) {
            this.ready = this.mover.normalize();
        }
        return this.ready;
    }

    @Override
    public void interrupt() {
        this.transform.setXRotation(0.0f);
        this.mover.block(false);
    }
}

