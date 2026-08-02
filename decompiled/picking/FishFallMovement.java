/*
 * Decompiled with CFR 0.152.
 */
package picking;

import basics.DisplayManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class FishFallMovement {
    private static final float FALL_GRAVITY = 40.0f;
    private final Transformation transform;
    private final float waterHeight;
    private final float aimDepth;
    private Vector3f velocity = new Vector3f();
    private boolean inWater = false;
    private float deceleration;

    public FishFallMovement(Transformation transform, Vector3f velocity, float waterHeight, float aimDepth) {
        this.transform = transform;
        this.waterHeight = waterHeight;
        this.aimDepth = aimDepth;
        this.velocity = velocity;
    }

    public boolean update() {
        System.out.println(this.transform.getPosition() + ", " + this.velocity);
        if (this.inWater) {
            return this.decelerate();
        }
        this.fall();
        return false;
    }

    private boolean decelerate() {
        this.transform.increasePosition(0.0f, this.velocity.y * DisplayManager.getDeltaSeconds(), 0.0f);
        this.velocity.y += DisplayManager.getDeltaSeconds() * this.deceleration;
        return this.velocity.y >= 0.0f;
    }

    private void fall() {
        this.velocity.y -= DisplayManager.getDeltaSeconds() * 40.0f;
        this.transform.increasePosition(0.0f, this.velocity.y * DisplayManager.getDeltaSeconds(), 0.0f);
        this.inWater = this.transform.getPosition().y < this.waterHeight;
        System.out.println("Pos: " + this.transform.getPosition().y + ", " + this.waterHeight);
        if (this.inWater) {
            this.deceleration = this.velocity.y * this.velocity.y / (2.0f * this.aimDepth);
            System.out.println("******************************" + this.deceleration + ", " + this.velocity.y);
        }
    }
}

