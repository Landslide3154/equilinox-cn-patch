/*
 * Decompiled with CFR 0.152.
 */
package birds;

import ai.IdlePlay;
import aiBasics.AiRoutine;
import baseMovement.MovementComp;
import components.InformationComponent;
import gameManaging.GameManager;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;
import world.World;

public class BabyBirdAi
implements AiRoutine {
    private static final String DESC = GameText.getText(187);
    private float minIdleTime = 10.0f;
    private float maxIdleTime = 25.0f;
    private final MovementComp mover;
    private final InformationComponent info;
    private boolean idle = true;
    private float timeTillWander = Maths.randomNumberBetween(0.0f, this.maxIdleTime);
    private Vector3f targetPosition;
    private final IdlePlay idlePlayAi;
    private boolean unableToSwim;
    private boolean gettingOutOfWater = false;

    public BabyBirdAi(MovementComp mover, Transformation transform, InformationComponent info, boolean unableToSwim) {
        this.mover = mover;
        this.info = info;
        this.unableToSwim = unableToSwim;
        this.idlePlayAi = new IdlePlay(mover, transform, info);
    }

    public void setIdleTimes(float min, float max) {
        this.minIdleTime = min;
        this.maxIdleTime = max;
    }

    @Override
    public boolean update() {
        this.updateIdle();
        if (this.unableToSwim) {
            this.checkForWater();
        }
        return false;
    }

    @Override
    public void interrupt() {
        this.switchToWander();
        this.gettingOutOfWater = false;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    protected void updateIdle() {
        this.idlePlayAi.playAround();
        this.timeTillWander -= GameManager.getGameSeconds();
        if (this.timeTillWander < 0.0f) {
            this.switchToWander();
        }
    }

    protected Vector3f getTarget() {
        return this.targetPosition;
    }

    protected void switchToWander() {
        this.idle = false;
        this.targetPosition = this.info.getRandomInRangePoint();
        World world = GameManager.getWorld();
        if (world != null) {
            this.targetPosition.y = GameManager.getWorld().getHeightOfTerrain(this.targetPosition.x, this.targetPosition.z);
        }
    }

    protected void switchToIdle() {
        this.idle = true;
        this.idlePlayAi.reset();
        this.timeTillWander = Maths.randomNumberBetween(this.minIdleTime, this.maxIdleTime);
    }

    protected void updateWander() {
        boolean reached = this.mover.goToTarget(this.targetPosition, false, 0.4f);
        if (reached && this.mover.normalize()) {
            this.gettingOutOfWater = false;
            this.switchToIdle();
        }
    }

    private void checkForWater() {
        boolean needNewTarget;
        boolean bl = needNewTarget = this.mover.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight() || this.targetPosition != null && this.targetPosition.y < GameManager.getWorld().getWaterHeight();
        if (!this.gettingOutOfWater && needNewTarget) {
            Vector3f pos = this.info.getRandomInRangePoint();
            if (GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z) > GameManager.getWorld().getWaterHeight()) {
                this.idle = false;
                this.targetPosition = pos;
                this.gettingOutOfWater = true;
            }
        }
    }
}

