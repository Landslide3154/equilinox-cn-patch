/*
 * Decompiled with CFR 0.152.
 */
package ai;

import baseMovement.MovementComp;
import components.InformationComponent;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class IdlePlay {
    private static final float BOB_AMOUNT = 3.5f;
    private static final float BOB_SPEED = 0.8f;
    private static final float CLOSE_RADIUS = 0.2f;
    private float minStandTime = 4.0f;
    private float maxStandTime = 8.0f;
    private float turnTime = 0.8f;
    private float playAroundArea = 0.5f;
    private float turnAmount = 20.0f;
    private boolean stood = true;
    private float timeTillMove = Maths.randomNumberBetween(this.minStandTime, this.maxStandTime);
    private float timeTillTurn = this.turnTime;
    private Vector3f target = new Vector3f();
    private float startRot;
    private float startXRot;
    private float time = 0.0f;
    private boolean checkSwimming = false;
    private final Transformation transform;
    private final MovementComp mover;
    private final InformationComponent info;

    public IdlePlay(MovementComp mover, Transformation transform, InformationComponent info) {
        this.mover = mover;
        this.transform = transform;
        this.info = info;
    }

    public void setSwimmingCheck(boolean swimCheck) {
        if (swimCheck) {
            this.checkSwimming = true;
            if (this.mover.isSwimming()) {
                this.switchToMove();
            }
        }
    }

    public void setStandTime(float min, float max) {
        this.minStandTime = min;
        this.maxStandTime = max;
    }

    public void setTurnVariables(float turnAngle, float turnTime) {
        this.turnAmount = turnAngle;
        this.turnTime = turnTime;
    }

    public void setPlayAroundArea(float area) {
        this.playAroundArea = area;
    }

    public void reset() {
        if (this.checkSwimming && this.mover.isSwimming()) {
            this.switchToMove();
        } else {
            this.switchToStand();
        }
    }

    public void playAround() {
        if (this.stood) {
            if (this.mover.normalize()) {
                this.doStanding();
            }
        } else {
            this.moveToTarget();
        }
    }

    private void moveToTarget() {
        boolean arrived = this.mover.goToTarget(this.target, false, 0.2f);
        if (arrived && this.mover.normalize()) {
            if (this.checkSwimming && this.mover.isSwimming()) {
                this.switchToMove();
            } else {
                this.switchToStand();
            }
        }
    }

    private void doStanding() {
        this.timeTillMove -= GameManager.getGameSeconds();
        this.timeTillTurn -= GameManager.getGameSeconds();
        this.time += GameManager.getGameSeconds() * 0.8f;
        this.transform.setXRotation(this.startXRot + Maths.fakeSin(-3.5f, 3.5f, this.time));
        if (this.timeTillMove < 0.0f) {
            this.switchToMove();
        } else if (this.timeTillTurn < 0.0f) {
            this.doTurn();
        }
    }

    private void doTurn() {
        this.timeTillTurn = this.turnTime;
        float change = Maths.RANDOM.nextBoolean() ? this.turnAmount : -this.turnAmount;
        this.mover.turn(this.startRot + change);
    }

    private void switchToMove() {
        boolean goodTarget = this.generateTarget();
        if (goodTarget) {
            this.stood = false;
        } else {
            this.timeTillMove = Maths.randomNumberBetween(this.minStandTime, this.maxStandTime);
        }
    }

    private boolean generateTarget() {
        Vector3f position = this.transform.getPosition();
        this.target = Maths.randomPointInSquare(position.x, position.z, this.playAroundArea);
        this.info.clampToHomeArea(this.target);
        return this.checkGoodTarget();
    }

    private void switchToStand() {
        this.stood = true;
        this.time = 0.0f;
        this.startXRot = this.transform.getRotX();
        this.startRot = this.transform.getRotY();
        this.timeTillMove = Maths.randomNumberBetween(this.minStandTime, this.maxStandTime);
        this.timeTillTurn = this.turnTime;
    }

    private boolean checkGoodTarget() {
        float height = GameManager.getWorld().getHeightOfTerrain(this.target.x, this.target.z);
        return height > GameManager.getWorld().getWaterHeight() && GameManager.getWorld().isOnWorld(this.target);
    }
}

