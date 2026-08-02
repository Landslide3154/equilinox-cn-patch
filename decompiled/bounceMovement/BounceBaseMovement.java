/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import baseMovement.BaseMovement;
import baseMovement.MoveUtils;
import bounceMovement.BounceBaseBlueprint;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentBundle;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class BounceBaseMovement
extends BaseMovement {
    private static final int SPEED_ID = 0;
    private static final int BOUNCE_ID = 1;
    private static final float HEALTH_INFLUENCE = 0.5f;
    private Vector3f velocity = new Vector3f();
    private boolean inAir = true;

    protected BounceBaseMovement(BounceBaseBlueprint blueprint) {
        super(blueprint, 180.0f, blueprint.getRunFactor());
    }

    @Override
    public boolean normalize() {
        return !this.inAir;
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
    }

    @Override
    public void block(boolean blocked) {
        super.block(blocked);
        this.inAir = false;
    }

    @Override
    protected void updateMovement(boolean rotTargetReached) {
        if (this.inAir) {
            this.updateBounce();
        } else if (super.isMoving()) {
            this.initBounce();
            this.updateBounce();
        } else {
            this.updateStandingOnGround();
        }
    }

    protected void updateInFlight(float height) {
        this.getTransform().setXRotation(0.0f);
    }

    protected void updateStandingOnGround() {
    }

    protected void startBounce() {
    }

    protected void endBounce() {
    }

    private void updateBounce() {
        MoveUtils.applyVelocityWithGravity(this.velocity, this.getTransform(), GameManager.getGameSeconds());
        float entityHeight = this.getTransform().checkWithTerrain();
        if (entityHeight <= 0.0f) {
            this.inAir = false;
            this.endBounce();
        } else {
            this.updateInFlight(entityHeight);
        }
    }

    private void initBounce() {
        this.inAir = true;
        this.initVelocity();
        this.startBounce();
    }

    private void initVelocity() {
        double rot = Math.toRadians(this.getTransform().getRotY());
        float runFactor = super.getRunFactor();
        float lifeFactor = Maths.getFactor(this.getLifeFactor(), 0.5f);
        float lifeSizeFactor = lifeFactor * super.getSizeFactor();
        float speed = ((FloatTrait)super.getTrait(0)).getValue() * runFactor * lifeSizeFactor;
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        this.velocity.set(dx, this.getBounce(), dz);
    }

    private float getBounce() {
        float traitBounce = ((FloatTrait)super.getTrait(1)).getValue();
        float lifeFactor = Maths.getFactor(this.getLifeFactor(), 0.5f);
        return traitBounce * lifeFactor * super.getSizeFactor();
    }
}

