/*
 * Decompiled with CFR 0.152.
 */
package flying;

import baseMovement.BaseMovement;
import gameManaging.GameManager;
import toolbox.Maths;
import toolbox.Transformation;

public class WormMovement {
    private static final float SPEED = 0.2f;
    private static final float WOBBLE_SPEED = 2.0f;
    private final BaseMovement mover;
    private float time = 0.0f;

    protected WormMovement(BaseMovement mover) {
        this.mover = mover;
    }

    public void update() {
        if (this.mover.isMoving()) {
            Transformation transform = this.mover.getTransform();
            double rot = Math.toRadians(transform.getRotY());
            float speed = 0.2f * GameManager.getGameSeconds();
            float dx = speed * (float)Math.sin(rot);
            float dz = speed * (float)Math.cos(rot);
            transform.increasePosition(dx, 0.0f, dz);
            transform.clampToTerrain(0.0f);
            this.time += GameManager.getGameSeconds() * 2.0f;
            this.time %= 1.0f;
            transform.setXRotation(Maths.rock(-20.0f, 20.0f, this.time));
        }
    }
}

