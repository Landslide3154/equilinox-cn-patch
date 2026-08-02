/*
 * Decompiled with CFR 0.152.
 */
package flying;

import baseMovement.BaseMovement;
import baseMovement.MoveUtils;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import flying.FlyBlueprint;
import flying.WormMovement;
import gameManaging.GameManager;
import growth.GrowthComponent;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class FlyMovement
extends BaseMovement {
    private static final float DOWN_ACCEL = 2.5f;
    private static final float LOW_HEIGHT = 0.4f;
    private static final float ACCEL = -3.0f;
    private static final float MAX_UP_VELOCITY = 1.1f;
    private static final float FLOAT_VELOCITY = -0.35f;
    private static final float SPEED = 0.7f;
    private static final float ROCK_SPEED = 5.0f;
    private static final float ROCK_ROT = 30.0f;
    private static final int WORM_STAGE = 0;
    private static final float ROT_DAMPING = 0.4f;
    private static final float LANDING_ALT = 0.15f;
    private GrowthComponent growth;
    private Vector3f velocity = new Vector3f();
    private float acceleration = 0.0f;
    private boolean flyUp = false;
    private boolean inTheAir;
    private float time = 0.0f;
    private float mainRot = 0.0f;
    private WormMovement wormMovement = new WormMovement(this);

    protected FlyMovement(FlyBlueprint blueprint, float rotSpeed) {
        super(blueprint, rotSpeed, 2.0f);
    }

    @Override
    public boolean normalize() {
        return !this.inTheAir;
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        if (this.growth.getStageNumber() == 0) {
            this.wormMovement.update();
            return;
        }
        if (this.inTheAir) {
            this.updateFlight();
        }
        if (super.isMoving() || super.isTurning()) {
            this.inTheAir = true;
            if (this.flyUp) {
                this.flyUpwards();
            }
        } else {
            this.acceleration = 2.5f;
        }
    }

    private void updateFlight() {
        float speed = this.calculateSpeed();
        this.updateVelocity(speed);
        float altitude = this.testAltitude();
        this.updateRotation(speed, altitude);
        MoveUtils.applyVelocity(this.velocity, this.getTransform(), GameManager.getGameSeconds());
    }

    private float calculateSpeed() {
        if (super.isMoving()) {
            return 0.7f;
        }
        return 0.0f;
    }

    private float testAltitude() {
        float altitude = super.getTransform().checkWithTerrainAndWater();
        if (altitude <= 0.4f) {
            this.flyUp = true;
            if (altitude <= 0.0f) {
                this.inTheAir = false;
            }
        }
        return altitude;
    }

    private void updateVelocity(float speed) {
        this.velocity.y -= this.acceleration * GameManager.getGameSeconds();
        this.velocity.y = Math.max(-0.35f, this.velocity.y);
        double rot = Math.toRadians(this.getTransform().getRotY());
        this.velocity.x = speed * (float)Math.sin(rot);
        this.velocity.z = speed * (float)Math.cos(rot);
    }

    private void flyUpwards() {
        this.acceleration = -3.0f;
        if (this.velocity.y > 1.1f) {
            this.flyUp = false;
            this.acceleration = 2.5f;
        }
    }

    private void updateRotation(float speed, float altitude) {
        this.mainRot = super.isMoving() ? (float)Math.atan(this.velocity.y * 0.4f / speed) : (this.mainRot += -this.mainRot * GameManager.getGameSeconds());
        float value = super.isMoving() ? (float)Math.atan(this.velocity.y / speed) : 0.0f;
        this.time += GameManager.getGameSeconds() * 5.0f;
        this.time %= 1.0f;
        float extra = Maths.rock(-30.0f, 30.0f, this.time);
        float damp = Math.min(1.0f, altitude / 0.15f);
        super.getTransform().setXRotation((float)Math.toDegrees(-value) + extra * damp);
    }
}

