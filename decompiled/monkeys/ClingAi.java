/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.InterFloat;
import languages.GameText;
import monkeys.ClingCompBlueprint;
import movementUtils.JumpToTarget;
import movementUtils.ProjectileBounce;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ClingAi
implements Ai {
    private static final float JUMP_RADIUS = 0.35f;
    private static final float JUMP_DUR = 0.4f;
    private static final float PRIORITY = 5.0f;
    private static final String WALKING = GameText.getText(421);
    private static final String CLINGING = GameText.getText(420);
    private final AiProvidingComponent clingComp;
    private final MovementComp mover;
    private final GrowthComponent growth;
    private final Entity parent;
    private final Entity child;
    private final ClingCompBlueprint clingBlueprint;
    private ProjectileBounce bounceMove;
    private JumpToTarget mountingMove;
    private InterFloat rotXDriver = new InterFloat();
    private boolean clinging = false;
    private boolean mounting = false;
    private boolean dismounting = false;

    protected ClingAi(AiProvidingComponent clingComp, Entity child, Entity parent, GrowthComponent growth, MovementComp mover, ClingCompBlueprint clingBlueprint) {
        this.clingComp = clingComp;
        this.parent = parent;
        this.child = child;
        this.growth = growth;
        this.clingBlueprint = clingBlueprint;
        this.mover = mover;
    }

    @Override
    public boolean carryOut() {
        if (!this.dismounting && (this.growth.getGrowthFactor() >= 0.5f || this.parent.isDead() || this.parent.isGrabbed())) {
            if (this.clinging || this.mounting) {
                this.startDismount();
            } else {
                return true;
            }
        }
        if (this.dismounting) {
            return this.updateDismount();
        }
        if (this.mounting) {
            this.updateMounting();
        } else if (this.clinging) {
            this.updateCling();
        } else {
            this.goToParent();
        }
        return false;
    }

    @Override
    public float getPriority() {
        return 5.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.clingComp;
    }

    @Override
    public void interrupt() {
        this.clinging = false;
        this.mounting = false;
        this.dismounting = false;
        this.mover.block(false);
    }

    @Override
    public String getDescription() {
        return this.clinging ? CLINGING : WALKING;
    }

    private void startDismount() {
        this.bounceMove = new ProjectileBounce(this.mover.getTransform(), new Vector3f(0.0f, 1.4f, 0.0f), 1, 0.4f);
        this.rotXDriver.setSlideWithSetTime(this.mover.getTransform().getRotX(), 0.0f, 0.2f);
        this.dismounting = true;
    }

    private void updateMounting() {
        this.mover.block(true);
        boolean finished = this.mountingMove.update(this.calculateTargetPos());
        if (finished) {
            this.mounting = false;
            this.clinging = true;
        }
    }

    private void updateCling() {
        Matrix4f clingMatrix = this.clingBlueprint.getOffsetMatrix(this.parent.getTransform().getScale(), this.mover.getTransform().getScale());
        this.mover.block(true);
        Matrix4f parentMatrix = this.parent.getTransform().getModelMatrix();
        Matrix4f childMatrix = Matrix4f.mul(parentMatrix, clingMatrix, null);
        this.mover.getTransform().setPosition(childMatrix.m30, childMatrix.m31, childMatrix.m32);
        this.mover.getTransform().setModelMatrix(childMatrix);
        GameManager.getWorld().getEntityGrid().updateInGrid(this.child);
    }

    private void goToParent() {
        Vector3f targetPos = this.parent.getTransform().getPosition();
        boolean reached = this.mover.goToTarget(targetPos, true, 0.35f);
        if (reached && this.mover.normalize()) {
            this.startMount();
        }
    }

    private boolean updateDismount() {
        this.mover.block(true);
        float value = this.rotXDriver.update(GameManager.getGameSeconds());
        this.mover.getTransform().setXRotation(value);
        boolean finished = this.bounceMove.update();
        if (finished) {
            this.mover.block(false);
        }
        return finished;
    }

    private void startMount() {
        this.mounting = true;
        this.mountingMove = new JumpToTarget(this.mover.getTransform(), 0.4f, this.calculateTargetPos(), this.clingBlueprint.rotX);
    }

    private Vector3f calculateTargetPos() {
        Vector4f offset = new Vector4f(this.clingBlueprint.offset.x, this.clingBlueprint.offset.y, this.clingBlueprint.offset.z, 1.0f);
        Matrix4f.transform(this.parent.getTransform().getModelMatrix(), offset, offset);
        return new Vector3f(offset);
    }
}

