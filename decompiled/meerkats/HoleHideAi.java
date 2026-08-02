/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.BaseMovement;
import baseMovement.MovementComp;
import gameManaging.GameManager;
import instances.Entity;
import meerkats.BurrowingAi;
import meerkats.HoleHideComponent;
import movementUtils.JumpToTarget;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class HoleHideAi
implements Ai {
    private static final int RUNNING_STAGE = 0;
    private static final int JUMP_IN_STAGE = 1;
    private static final int HIDING_STAGE = 2;
    private static final int JUMP_OUT_STAGE = 3;
    private static final float JUMP_RADIUS = 0.5f;
    private static final float JUMP_TIME = 0.5f;
    private static final float MAX_SAFE_TIME = 5.0f;
    private static final float OUT_JUMP_TIME = 0.8f;
    private static final float OUT_JUMP_DIS = 0.3f;
    private static final float PRIORITY = 100.0f;
    private static final float MAX_TIME = 50.0f;
    private float hiddenTime = 0.0f;
    private final HoleHideComponent holeHideComp;
    private final MovementComp mover;
    private final Entity meerkat;
    private final Vector3f holePos;
    private Vector3f jumpAimPos = new Vector3f();
    private JumpToTarget jumper;
    private int stage = 0;
    private float safeTimer = 0.0f;

    protected HoleHideAi(Entity meerkat, HoleHideComponent holeHideComp, MovementComp mover, Vector3f holePos) {
        this.holeHideComp = holeHideComp;
        this.mover = mover;
        this.holePos = holePos;
        this.meerkat = meerkat;
    }

    @Override
    public boolean carryOut() {
        if (this.stage == 0) {
            this.runToHole();
        } else if (this.stage == 1) {
            this.jumpInHole();
        } else if (this.stage == 2) {
            this.hide();
        } else if (this.stage == 3) {
            return this.jumpOutOfHole();
        }
        return false;
    }

    private void runToHole() {
        boolean reached = this.mover.goToTargetAndFace(this.holePos, true, 0.5f);
        if (reached) {
            this.switchToJumpInStage();
        }
    }

    private void switchToJumpInStage() {
        this.holeHideComp.setUnderground(true);
        this.stage = 1;
        this.jumpAimPos.set(this.holePos);
        this.jumpAimPos.y -= 0.1f;
        this.jumper = new JumpToTarget(this.mover.getTransform(), 0.5f, this.jumpAimPos, 90.0f);
    }

    private void jumpInHole() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.doJumpParticles(this.holePos);
            this.switchToHideStage();
        }
    }

    private void switchToHideStage() {
        this.stage = 2;
        Vector3f pos = this.mover.getTransform().getPosition();
        this.mover.getTransform().setYPosition(GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z) - 1.0f);
        this.safeTimer = Maths.randomNumberBetween(0.0f, 5.0f);
    }

    private void hide() {
        this.mover.block(true);
        this.hiddenTime += GameManager.getGameSeconds();
        if (this.hiddenTime > 50.0f) {
            this.holeHideComp.clearPredators();
            this.switchToJumpOutStage();
            return;
        }
        this.holeHideComp.updatePredatorList();
        if (this.holeHideComp.isPredatorNearby()) {
            return;
        }
        this.safeTimer -= GameManager.getGameSeconds();
        if (this.safeTimer < 0.0f) {
            this.switchToJumpOutStage();
        }
    }

    private void switchToJumpOutStage() {
        this.stage = 3;
        this.doJumpParticles(this.holePos);
        Vector2f dir = Maths.generateRandom2dVector();
        float rotY = Maths.calculateVectorRotationY(dir);
        this.mover.getTransform().setXRotation(-90.0f);
        ((BaseMovement)this.mover).setActualRotY(rotY);
        this.jumpAimPos.set(this.holePos);
        this.jumpAimPos.x += dir.x * 0.3f;
        this.jumpAimPos.z += dir.y * 0.3f;
        this.jumpAimPos.y = GameManager.getWorld().getHeightOfTerrain(this.jumpAimPos.x, this.jumpAimPos.z);
        this.jumper = new JumpToTarget(this.mover.getTransform(), 0.8f, this.jumpAimPos, 0.0f);
    }

    private boolean jumpOutOfHole() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.mover.block(false);
            this.holeHideComp.setUnderground(false);
            return true;
        }
        return false;
    }

    @Override
    public float getPriority() {
        return 100.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.holeHideComp;
    }

    @Override
    public void interrupt() {
        this.holeHideComp.setUnderground(false);
        this.stage = 0;
        this.mover.block(false);
    }

    @Override
    public String getDescription() {
        return this.stage == 0 ? "Escaping" : "Hiding";
    }

    private void doJumpParticles(Vector3f pos) {
        if (this.meerkat.getCurrentGridSection().getDistanceFromCam() > 55.0f) {
            return;
        }
        BurrowingAi.ROCK_JUMP_PARTICLES.pulseParticles(pos, 1.0f);
        BurrowingAi.DUST_JUMP_PARTICLES.pulseParticles(pos, 1.0f);
    }
}

