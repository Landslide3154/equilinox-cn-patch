/*
 * Decompiled with CFR 0.152.
 */
package ai;

import aiBasics.AiRoutine;
import baseMovement.MovementComp;
import components.InformationComponent;
import gameManaging.GameManager;
import movementUtils.JumpToTarget;
import objectPools.Vec4Pool;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;
import toolbox.Transformation;
import world.World;

public class StopStandAi
implements AiRoutine {
    private static final float BOB_AMOUNT = 2.5f;
    private static final float BOB_SPEED = 0.5f;
    private float minIdleTime = 10.0f;
    private float maxIdleTime = 25.0f;
    private static final int WANDER_STAGE = 0;
    private static final int JUMP_UP_STAGE = 1;
    private static final int STAND_STAGE = 2;
    private static final int JUMP_DOWN_STAGE = 3;
    private static final float JUMP_TIME = 0.3f;
    public static final float STAND_ROT = -75.0f;
    private final MovementComp mover;
    private final InformationComponent info;
    private float timeTillWander = Maths.randomNumberBetween(0.0f, this.maxIdleTime);
    private Vector3f targetPosition;
    private boolean gettingOutOfWater = false;
    private Vector3f jumpAimPos = new Vector3f();
    private JumpToTarget jumper;
    private int stage = 0;
    private static final Vector4f BACK = new Vector4f(0.0f, 0.7825f, -0.912f, 1.0f);
    private float time = 0.0f;

    public StopStandAi(MovementComp mover, Transformation transform, InformationComponent info) {
        this.mover = mover;
        this.info = info;
        this.switchToWanderStage();
    }

    public void setIdleTimes(float min, float max) {
        this.minIdleTime = min;
        this.maxIdleTime = max;
    }

    @Override
    public boolean update() {
        if (this.stage == 0) {
            this.updateWander();
        } else if (this.stage == 1) {
            this.updateJumpUp();
        } else if (this.stage == 2) {
            this.updateStanding();
        } else if (this.stage == 3) {
            this.updateJumpDown();
        }
        return false;
    }

    private void updateWander() {
        boolean reached = this.mover.goToTarget(this.targetPosition, false, 0.4f);
        this.checkForWater();
        if (reached && this.mover.normalize()) {
            this.gettingOutOfWater = false;
            this.switchToJumpUpStage();
        }
    }

    private void switchToJumpUpStage() {
        this.stage = 1;
        Vector4f res = Matrix4f.transform(this.mover.getTransform().getModelMatrix(), BACK, Vec4Pool.get());
        this.jumpAimPos.set(this.mover.getTransform().getPosition());
        float height = GameManager.getWorld().getHeightOfTerrain(res.x, res.z);
        Vec4Pool.release(res);
        this.jumpAimPos.y = height + this.mover.getTransform().getScale() * 0.7f;
        this.jumper = new JumpToTarget(this.mover.getTransform(), 0.3f, this.jumpAimPos, -75.0f);
    }

    private void updateJumpUp() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.switchToStandStage();
        }
    }

    private void switchToStandStage() {
        this.mover.block(true);
        this.mover.getTransform().setPosition(this.jumpAimPos);
        this.time = 0.0f;
        this.timeTillWander = Maths.randomNumberBetween(this.minIdleTime, this.maxIdleTime);
        this.stage = 2;
    }

    private void updateStanding() {
        this.time += GameManager.getGameSeconds() * 0.5f;
        this.mover.getTransform().setXRotation(-75.0f + Maths.fakeSin(-2.5f, 2.5f, this.time));
        this.mover.block(true);
        this.timeTillWander -= GameManager.getGameSeconds();
        if (this.timeTillWander < 0.0f) {
            this.switchToJumpDownStage();
        }
    }

    private void switchToJumpDownStage() {
        this.stage = 3;
        this.jumpAimPos.set(this.mover.getTransform().getPosition());
        this.jumpAimPos.y = this.mover.getTransform().getTerrainHeight();
        this.jumper = new JumpToTarget(this.mover.getTransform(), 0.3f, this.jumpAimPos, 0.0f);
    }

    private void updateJumpDown() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.switchToWanderStage();
        }
    }

    private void switchToWanderStage() {
        this.stage = 0;
        this.mover.block(false);
        this.targetPosition = this.info.getRandomInRangePoint();
        World world = GameManager.getWorld();
        if (world != null) {
            this.targetPosition.y = GameManager.getWorld().getHeightOfTerrain(this.targetPosition.x, this.targetPosition.z);
        }
    }

    @Override
    public void interrupt() {
        this.switchToWanderStage();
        this.mover.block(false);
        this.gettingOutOfWater = false;
    }

    @Override
    public String getDescription() {
        return this.stage == 0 ? "Wandering" : "Looking Cute";
    }

    protected Vector3f getTarget() {
        return this.targetPosition;
    }

    private void checkForWater() {
        boolean needNewTarget;
        boolean bl = needNewTarget = this.mover.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight() || this.targetPosition != null && this.targetPosition.y < GameManager.getWorld().getWaterHeight();
        if (this.gettingOutOfWater || !needNewTarget) {
            return;
        }
        Vector3f pos = this.info.getRandomInRangePoint();
        if (GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z) > GameManager.getWorld().getWaterHeight()) {
            this.targetPosition = pos;
            this.gettingOutOfWater = true;
        }
    }
}

