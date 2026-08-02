/*
 * Decompiled with CFR 0.152.
 */
package frogMovement;

import baseMovement.BaseMovement;
import breedingTraits.FloatTrait;
import componentArchitecture.ControlBehaviour;
import frogMovement.FrogMovementBlueprint;
import gameManaging.GameManager;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class FrogMovement
extends BaseMovement {
    private static final float STD = 0.1f;
    private static final int BOUNCE_TRAIT = 1;
    private static final int BOUNCE_COUNT = 3;
    private final FrogMovementBlueprint blueprint;
    private boolean jumping = false;
    private float timeTillJump = 0.0f;
    private int bouncesRemaining;
    private Vector3f velocity = new Vector3f();
    private Vector3f vector = new Vector3f();
    private Vector3f forcedJump = null;

    protected FrogMovement(FrogMovementBlueprint blueprint) {
        super(blueprint, 180.0f, blueprint.getRunFactor());
        this.blueprint = blueprint;
    }

    @Override
    protected void updateMovement(boolean targetRotReached) {
        if (this.jumping) {
            this.updateJump();
        } else if (this.forcedJump != null) {
            this.startForcedJump();
        } else if (super.isMoving()) {
            this.updateWait();
        }
    }

    @Override
    public void getControlableBehaviour(List<ControlBehaviour> behaviours) {
        super.getControlableBehaviour(behaviours);
        behaviours.add(new ControlBehaviour("Force", 33, false){

            @Override
            public void doAction() {
                FrogMovement.this.forceJump(new Vector3f(2.0f, 4.0f, 0.0f));
            }
        });
    }

    public void forceJump(Vector3f startVelocity) {
        this.forcedJump = startVelocity;
    }

    private void updateJump() {
        boolean landed;
        this.velocity.y -= 10.0f * GameManager.getGameSeconds();
        this.vector.set(this.velocity);
        this.vector.scale(GameManager.getGameSeconds());
        super.getTransform().increasePosition(this.vector);
        boolean bl = landed = super.getTransform().checkWithTerrain() <= 0.0f;
        if (landed && this.velocity.y < 0.0f) {
            this.bounce();
            if (this.bouncesRemaining == 0) {
                this.stopJump();
            }
        }
    }

    private void updateWait() {
        this.timeTillJump -= GameManager.getGameSeconds();
        if (this.timeTillJump <= 0.0f) {
            this.startJump();
        }
    }

    private void stopJump() {
        this.timeTillJump = (float)(Maths.RANDOM.nextGaussian() * (double)0.1f + (double)this.blueprint.waitTime);
        this.jumping = false;
    }

    private void bounce() {
        --this.bouncesRemaining;
        this.velocity.scale(this.blueprint.bounciness);
        this.velocity.y = -this.velocity.y;
    }

    private void startForcedJump() {
        this.velocity.set(this.forcedJump);
        this.bouncesRemaining = 3;
        this.jumping = true;
        this.forcedJump = null;
    }

    private void startJump() {
        float speed = super.getBaseSpeed() * super.getTotalSpeedFactor();
        double rot = Math.toRadians(super.getTransform().getRotY());
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        float pow = ((FloatTrait)super.getTrait(1)).getValue() * super.getLifeSizeFactor();
        this.velocity.set(dx, pow, dz);
        this.bouncesRemaining = 3;
        this.jumping = true;
    }

    @Override
    public boolean normalize() {
        return !this.jumping;
    }
}

