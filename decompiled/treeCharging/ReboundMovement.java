/*
 * Decompiled with CFR 0.152.
 */
package treeCharging;

import baseMovement.MoveUtils;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.InterFloat;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class ReboundMovement {
    private static final int BOUNCE_NUM = 3;
    private static final float DAMPING = 0.7f;
    private static final float BOUNCE_POWER = 1.0f;
    private static final float BOUNCE_Y_VEL = 1.7f;
    private static final float GRAVITY_FACTOR = 1.0f;
    private final InterFloat ROTATION_Z = new InterFloat(150.0f);
    private final InterFloat ROTATION_X = new InterFloat(150.0f);
    private final Transformation transform;
    private final Entity chargingEntity;
    private int bounceCount = 0;
    private Vector3f velocity = new Vector3f();

    protected ReboundMovement(Entity chargingEntity, Transformation transform) {
        this.transform = transform;
        this.chargingEntity = chargingEntity;
    }

    protected void initBounce(Vector3f treePos) {
        this.bounceCount = 0;
        Vector3f.sub(this.transform.getPosition(), treePos, this.velocity);
        this.velocity.y = 0.0f;
        this.velocity.normalise();
        this.velocity.scale(1.0f);
        this.velocity.y = 1.7f;
        this.ROTATION_Z.setSlide(this.transform.getRotZ() % 360.0f, 0.0f);
        this.ROTATION_X.setSlide(this.transform.getRotX() % 360.0f, 0.0f);
    }

    protected boolean updateBounce() {
        MoveUtils.applyVelocityWithGravity(this.velocity, this.transform, 1.0f, GameManager.getGameSeconds());
        if (this.transform.checkWithTerrain() < 0.0f && this.velocity.y <= 0.0f) {
            if (this.bounceCount == 3) {
                return true;
            }
            this.velocity.y = -this.velocity.y;
            this.velocity.scale(0.7f);
            ++this.bounceCount;
        }
        GameManager.getWorld().getEntityGrid().updateInGrid(this.chargingEntity);
        this.updateRotation();
        return false;
    }

    private void updateRotation() {
        this.transform.setXRotation(this.ROTATION_X.update(GameManager.getGameSeconds()));
        this.transform.setZRotation(this.ROTATION_Z.update(GameManager.getGameSeconds()));
    }
}

