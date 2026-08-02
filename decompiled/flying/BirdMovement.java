/*
 * Decompiled with CFR 0.152.
 */
package flying;

import baseMovement.BaseMovement;
import baseMovement.MoveUtils;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import flying.BabyBirdMovement;
import flying.BirdMoveBlueprint;
import gameManaging.GameManager;
import growth.GrowthComponent;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class BirdMovement
extends BaseMovement {
    private static final float DOWN_ACCEL = -1.3f;
    private static final float TARGET_HEIGHT = 1.35f;
    private static final float LANDING_RANGE = 5.0f;
    private static final float LANDING_RANGE_SQU = 25.0f;
    private static final float CRITIAL_RANGE = 1.0f;
    private static final float CRITIAL_RANGE_SQU = 1.0f;
    private static final float RESET_RANGE = 5.0f;
    private static final float RESET_RANGE_SQU = 25.0f;
    private static final float ERROR_MARGIN = 5.0E-4f;
    private static final float ROT_CORRECT_DIS = 0.7f;
    private static final float SLOW_LAND_DIS = 0.22f;
    private static final float MIN_LAND_SPEED = 0.03f;
    private static final float MIN_LAND_SPEED_HUNT = 0.75f;
    private static final float LAND_ALTITUDE = 0.5f;
    private static final float BUFFER_HEIGHT = 0.35f;
    private static final float APPROACH_BUFFER_HEIGHT = 0.175f;
    private static final float UP_ACCEL = 3.0f;
    private static final float MAX_UP_VELOCITY = 1.0f;
    private static final float SPEED = 1.5f;
    private static final float ROCK_SPEED_FAST = 4.0f;
    private static final float ROCK_ROT = 35.0f;
    private GrowthComponent growth;
    private float wobbleSpeed = 0.0f;
    private Vector3f velocity = new Vector3f();
    private float acceleration = 0.0f;
    private boolean underCruiseHeight = false;
    private boolean inTheAir;
    private float time = 0.0f;
    private BabyBirdMovement babyMovement;
    private final BirdMoveBlueprint blueprint;
    private Vector3f target = null;
    private boolean inLandingRange = false;
    private boolean landing = false;
    private float distance;
    private boolean leaving = false;
    private boolean gliding = false;
    private boolean hunting = false;

    protected BirdMovement(BirdMoveBlueprint blueprint, float rotSpeed) {
        super(blueprint, rotSpeed, 2.0f);
        this.blueprint = blueprint;
        this.babyMovement = new BabyBirdMovement(this);
    }

    @Override
    public boolean normalize() {
        return true;
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }

    @Override
    public boolean land(Vector3f target) {
        Vector2f vectorToTarget = MoveUtils.getVectorToTarget(this, target.x, target.z);
        float disSquared = vectorToTarget.lengthSquared();
        if (disSquared == 0.0f) {
            this.finishLanding();
            return true;
        }
        this.target = target;
        this.testLandingRange(disSquared);
        this.moveToOrFromTarget(vectorToTarget, disSquared);
        return false;
    }

    public void indicateHunting() {
        this.hunting = true;
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        if (this.growth.getGrowthFactor() < 0.5f) {
            this.babyMovement.update();
            return;
        }
        if (this.target == null) {
            this.landing = false;
            this.leaving = false;
        }
        if (this.inTheAir) {
            this.updateFlight();
            this.testAltitude();
        }
        this.determineMovementValues();
        this.updateLanding(rotTargetReached);
        this.hunting = false;
    }

    private void testLandingRange(float distanceSquared) {
        boolean bl = this.inLandingRange = distanceSquared < 25.0f;
        if (!(this.landing && this.gliding || !(distanceSquared < 1.0f))) {
            this.leaving = true;
        } else if (this.leaving && distanceSquared > 25.0f) {
            this.leaving = false;
        }
    }

    private void moveToOrFromTarget(Vector2f vectorToTarget, float disSquared) {
        if (this.leaving) {
            MoveUtils.goFromTarget(this, this.target, false);
            this.inLandingRange = false;
            this.distance = disSquared;
        } else {
            MoveUtils.goInDirection(this, vectorToTarget);
            this.distance = (float)Math.sqrt(disSquared);
        }
    }

    private void updateLanding(boolean rotTargetReached) {
        if (this.inLandingRange && rotTargetReached) {
            if (!this.underCruiseHeight) {
                this.landing = true;
            }
            if (this.landing) {
                this.land();
            }
        } else {
            this.landing = false;
        }
        this.target = null;
        this.inLandingRange = false;
    }

    private void land() {
        this.acceleration = -1.3f;
        this.wobbleSpeed = 0.0f;
        if (this.getTransform().getPosition().y - this.target.y <= 5.0E-4f && Maths.getComparitableDistance(this.target, this.getTransform().getPosition()) < 0.0025000002f) {
            this.finishLanding();
            super.getTransform().setPosition(this.target);
        }
    }

    private void determineMovementValues() {
        if (super.isMoving()) {
            if (this.underCruiseHeight) {
                this.acceleration = 3.0f;
                this.wobbleSpeed = 4.0f;
            } else {
                this.acceleration = -1.3f;
                this.wobbleSpeed = 0.0f;
            }
            this.inTheAir = true;
        } else {
            this.acceleration = -1.3f;
            this.wobbleSpeed = 0.0f;
        }
    }

    private void updateFlight() {
        float speed = this.calculateSpeed();
        float glide = this.blueprint.glideDown;
        if (this.landing) {
            glide = -(super.getTransform().getPosition().y - this.target.y) / (this.distance / speed);
        }
        this.updateVelocity(speed, glide);
        Vector3f actualVel = new Vector3f(this.velocity);
        if (this.landing) {
            float minSpeed = this.hunting ? 0.75f : 0.03f;
            actualVel.scale(minSpeed + Math.min(1.0f - minSpeed, this.distance / 0.22f));
        }
        this.updateRotation(speed);
        MoveUtils.applyVelocity(actualVel, this.getTransform(), GameManager.getGameSeconds());
    }

    private float calculateSpeed() {
        return 1.5f;
    }

    private void testAltitude() {
        float altitude = super.getTransform().checkWithTerrainAndWater();
        float heightAboveTarget = this.getHeightAboveTarget(altitude);
        if (this.underCruiseHeight) {
            if (heightAboveTarget >= 0.0f) {
                this.underCruiseHeight = false;
            }
        } else {
            float bufferHeight;
            float f = bufferHeight = this.target == null ? 0.35f : 0.175f;
            if (heightAboveTarget <= -bufferHeight) {
                this.underCruiseHeight = true;
            }
        }
        if (altitude <= 0.0f) {
            this.inTheAir = false;
        }
    }

    private float getHeightAboveTarget(float altitude) {
        float height = altitude - 1.35f;
        if (this.target != null) {
            float aboveTarget = super.getTransform().getPosition().y - (this.target.y + 0.5f);
            height = Math.min(height, aboveTarget);
        }
        return height;
    }

    private void updateVelocity(float speed, float glideVelocity) {
        this.velocity.y += this.acceleration * GameManager.getGameSeconds();
        this.velocity.y = Math.min(1.0f, this.velocity.y);
        if (this.velocity.y < glideVelocity) {
            this.velocity.y = glideVelocity;
            this.gliding = true;
        } else {
            this.gliding = false;
        }
        double rot = Math.toRadians(this.getTransform().getRotY());
        this.velocity.x = speed * (float)Math.sin(rot);
        this.velocity.z = speed * (float)Math.cos(rot);
    }

    private void updateRotation(float speed) {
        float value = (float)Math.atan(this.velocity.y / speed);
        this.time += GameManager.getGameSeconds() * this.wobbleSpeed;
        this.time %= 1.0f;
        float extra = Maths.rock(0.0f, 35.0f, this.time);
        float flyingRotation = (float)Math.toDegrees(-value) + extra + 15.0f;
        float distanceFactor = this.landing ? Math.min(1.0f, this.distance / 0.7f) : 1.0f;
        float targetRot = flyingRotation * distanceFactor;
        float change = targetRot - this.getTransform().getRotX();
        float maxChange = 350.0f * GameManager.getGameSeconds();
        if (Math.abs(change) > maxChange) {
            change = change > 0.0f ? maxChange : -maxChange;
        }
        super.getTransform().increaseRotation(change, 0.0f, 0.0f);
    }

    private void finishLanding() {
        this.landing = false;
        this.inTheAir = false;
        this.leaving = false;
    }
}

