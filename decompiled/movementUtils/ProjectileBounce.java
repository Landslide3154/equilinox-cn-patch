/*
 * Decompiled with CFR 0.152.
 */
package movementUtils;

import baseMovement.MoveUtils;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class ProjectileBounce {
    private final float bounciness;
    private final Transformation transform;
    private Vector3f velocity;
    private int bouncesRemaining;

    public ProjectileBounce(Transformation transform, Vector3f velocity, int totalBounces, float bounciness) {
        this.transform = transform;
        this.bouncesRemaining = totalBounces;
        this.bounciness = bounciness;
        this.velocity = velocity;
    }

    public boolean update() {
        boolean landed;
        MoveUtils.applyVelocityWithGravity(this.velocity, this.transform, GameManager.getGameSeconds());
        boolean bl = landed = this.transform.checkWithTerrain() <= 0.0f;
        if (landed && this.velocity.y < 0.0f) {
            this.bounce();
            return this.bouncesRemaining < 0;
        }
        return false;
    }

    private void bounce() {
        --this.bouncesRemaining;
        this.velocity.scale(this.bounciness);
        this.velocity.y = -this.velocity.y;
    }
}

