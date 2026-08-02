/*
 * Decompiled with CFR 0.152.
 */
package movementUtils;

import baseMovement.MoveUtils;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class JumpToTarget {
    private final Transformation transform;
    private final float duration;
    private final float aimRotX;
    private final float startRotX;
    private float time = 0.0f;
    private Vector3f startTarget;
    private Vector3f velocity;

    public JumpToTarget(Transformation transform, float duration, Vector3f targetPos, float aimRotX) {
        this.transform = transform;
        this.aimRotX = aimRotX;
        this.startRotX = transform.getRotX();
        this.startTarget = new Vector3f(targetPos);
        this.duration = duration;
        this.launchJump();
    }

    public boolean update(Vector3f target) {
        Vector3f difference = Vector3f.sub(target, this.startTarget, null);
        this.startTarget.set(target);
        this.time += GameManager.getGameSeconds();
        MoveUtils.applyVelocityWithGravity(this.velocity, this.transform, GameManager.getGameSeconds());
        this.transform.increasePosition(difference);
        this.transform.setXRotation(this.time / this.duration * (this.aimRotX - this.startRotX) + this.startRotX);
        return this.time >= this.duration;
    }

    public float getTime() {
        return this.time;
    }

    private void launchJump() {
        Vector3f entityPos = this.transform.getPosition();
        float upVelocity = (this.startTarget.y + 5.0f * this.duration * this.duration - entityPos.y) / this.duration;
        Vector2f toTarget2d = new Vector2f(this.startTarget.x - entityPos.x, this.startTarget.z - entityPos.z);
        toTarget2d.scale(1.0f / this.duration);
        this.velocity = new Vector3f(toTarget2d.x, upVelocity, toTarget2d.y);
    }
}

