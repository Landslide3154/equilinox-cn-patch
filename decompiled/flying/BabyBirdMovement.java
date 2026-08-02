/*
 * Decompiled with CFR 0.152.
 */
package flying;

import baseMovement.BaseMovement;
import gameManaging.GameManager;
import toolbox.Maths;

public class BabyBirdMovement {
    private static final float SPEED = 0.2f;
    private static final float ROCK_AMOUNT = 20.0f;
    private static final float ROCK_SPEED = 3.0f;
    private static final float INIT_Y_SPEED = 1.0f;
    private static final float FLOAT_SPEED = -0.8f;
    private static final float GRAVITY_FACTOR = 0.4f;
    private final BaseMovement mover;
    private float time = 0.0f;
    private boolean inNest = true;
    private float ySpeed = 0.0f;

    protected BabyBirdMovement(BaseMovement mover) {
        this.mover = mover;
    }

    protected void update() {
        if (this.mover.isMoving()) {
            if (this.inNest) {
                this.leaveNest();
            }
            double rot = Math.toRadians(this.mover.getTransform().getRotY());
            float speed = 0.2f * GameManager.getGameSeconds();
            float dx = speed * (float)Math.sin(rot);
            float dz = speed * (float)Math.cos(rot);
            this.mover.getTransform().increasePosition(dx, 0.0f, dz);
            this.time += GameManager.getGameSeconds() * 3.0f;
            this.time %= 1.0f;
            this.mover.getTransform().setXRotation(Maths.rock(-20.0f, 20.0f, this.time));
        }
        if (!this.inNest) {
            this.ySpeed -= 4.0f * GameManager.getGameSeconds();
            this.ySpeed = Math.max(-0.8f, this.ySpeed);
            this.mover.getTransform().increasePosition(0.0f, this.ySpeed * GameManager.getGameSeconds(), 0.0f);
        }
        this.mover.getTransform().checkWithTerrain();
    }

    private void leaveNest() {
        this.inNest = false;
        this.ySpeed = 1.0f;
    }
}

