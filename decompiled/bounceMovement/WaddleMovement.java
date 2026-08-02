/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import bounceMovement.BounceBaseMovement;
import bounceMovement.WaddleMoveBlueprint;
import gameManaging.GameManager;

public class WaddleMovement
extends BounceBaseMovement {
    private static final float MAX_ROT = 32.0f;
    private static final float ROT_SPEED = 5.0f;
    private static final float RETURN_SPEED = 25.0f;
    private boolean right = false;

    protected WaddleMovement(WaddleMoveBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public boolean normalize() {
        return super.normalize() && Math.abs(super.getTransform().getRotZ()) <= 2.0f;
    }

    @Override
    protected void updateInFlight(float height) {
        float rotZ = super.getTransform().getRotZ();
        rotZ = this.right ? (rotZ += (32.0f - rotZ) * Math.min(GameManager.getGameSeconds() * 5.0f, 1.0f)) : (rotZ += (-32.0f - rotZ) * Math.min(GameManager.getGameSeconds() * 5.0f, 1.0f));
        super.getTransform().setZRotation(rotZ);
    }

    @Override
    protected void updateStandingOnGround() {
        float rotZ = super.getTransform().getRotZ();
        if (rotZ != 0.0f) {
            rotZ += -rotZ * Math.min(GameManager.getGameSeconds() * 25.0f, 1.0f);
            super.getTransform().setZRotation(rotZ);
        }
    }

    @Override
    protected void startBounce() {
        super.getTransform().setXRotation(0.0f);
    }

    @Override
    protected void endBounce() {
        this.right = !this.right;
    }
}

