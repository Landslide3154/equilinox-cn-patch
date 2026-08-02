/*
 * Decompiled with CFR 0.152.
 */
package flying;

import baseMovement.BaseMovement;
import componentArchitecture.ComponentBundle;
import flying.BeeMovementBlueprint;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class BeeMovement
extends BaseMovement {
    private static final float ROT_SPEED = 200.0f;
    private static final float VARY = 0.1f;
    private static final float WAVE_PER_SEC = 1.0f;
    private static final float SPEED = 1.0f;
    private static final float WOBBLE_PER_SEC = 10.0f;
    private static final float WOBBLE_ANGLE = 25.0f;
    private static final float MID_WAVE = 0.75f;
    private static final float TAKE_OFF_SPEED = 0.45f;
    private static final float LANDING_SPEED = 0.3f;
    private Transformation transform;
    private BeeMovementBlueprint blueprint;
    private boolean inAir = false;
    private boolean atCruiseHeight = false;
    private float waveTime = 0.75f;
    private float wobbleTime = 0.0f;

    protected BeeMovement(BeeMovementBlueprint blueprint) {
        super(blueprint, 200.0f, 2.0f);
        this.blueprint = blueprint;
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        if (this.inAir) {
            this.wobble();
        }
        if (super.isMoving() || super.isTurning()) {
            this.inAir = true;
            if (this.atCruiseHeight) {
                this.cruise();
            } else {
                this.goToCruiseHeight();
            }
        } else {
            this.atCruiseHeight = false;
            this.waveTime = 0.75f;
            if (this.inAir) {
                this.floatDown();
            }
        }
    }

    @Override
    public boolean land(Vector3f target) {
        return super.goToTarget(target, false, 0.1f);
    }

    private void goToCruiseHeight() {
        float toTarget;
        float aimHeight = this.blueprint.cruiseHeight + this.transform.getTerrainOrWaterHeight();
        float distance = 0.45f * GameManager.getGameSeconds();
        if (distance > Math.abs(toTarget = aimHeight - this.transform.getPosition().y)) {
            this.transform.setYPosition(aimHeight);
            this.atCruiseHeight = true;
        } else {
            this.transform.increasePosition(0.0f, distance * Math.signum(toTarget), 0.0f);
        }
    }

    private void cruise() {
        if (super.isMoving()) {
            this.moveForward();
        }
        this.waveTime += GameManager.getGameSeconds() * 1.0f;
        this.waveTime %= 1.0f;
        float offset = Maths.rock(-0.1f, 0.1f, this.waveTime);
        this.transform.setYPosition(this.blueprint.cruiseHeight + offset + this.transform.getTerrainOrWaterHeight());
    }

    private void moveForward() {
        double rot = Math.toRadians(this.getTransform().getRotY());
        float speed = 1.0f * GameManager.getGameSeconds();
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        this.getTransform().increasePosition(dx, 0.0f, dz);
    }

    private void wobble() {
        this.wobbleTime += GameManager.getGameSeconds() * 10.0f;
        this.wobbleTime %= 1.0f;
        float rock = Maths.rock(-25.0f, 25.0f, this.wobbleTime);
        this.transform.setZRotation(rock);
    }

    private void floatDown() {
        float distance = 0.3f * GameManager.getGameSeconds();
        this.transform.increasePosition(0.0f, -distance, 0.0f);
        if (this.transform.checkWithTerrain() <= 0.0f) {
            this.inAir = false;
        }
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.transform = super.getTransform();
    }

    @Override
    public boolean normalize() {
        return true;
    }
}

